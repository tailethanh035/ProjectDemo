package com.example.projectdemo.Service;

import com.example.projectdemo.Model.Image;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PageRanges;
import org.springframework.data.domain.Pageable;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@Service
public class ItemService {
    public static final int ITEMS_PER_PAGE = 2;
    public static final String ASCENDING = "asc";



    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return (List<Item>)itemRepository.findAll();
    }

    public  void addItem(Item item) {
        item.setDiscountedPrice((item.getRetailPrice() * ((100 - item.getDiscountedBy())/ 100)));

        Item buffer = itemRepository.findItemByItemID(item.getItemID());
        NumberFormat formatter = new DecimalFormat("#0.0000");
        item.setDiscountedPrice(Double.parseDouble(formatter.format(item.getDiscountedPrice())));
        if (buffer != null && item.getPhoto().equals("")) {
            item.setPhoto(buffer.getPhoto());
        }
        itemRepository.save(item);
    }

    public Item findByID(Long id) {
        return itemRepository.findItemByItemID(id);
    }

    public void deleteItem(Long id) throws Exception {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        } else {
            throw new Exception("Cannot find any items with id: "+id);
        }
    }

    public Item getItem(Long id) throws Exception {
        if (itemRepository.existsById(id)) {
            return itemRepository.getById(id);
        } else {
            throw new Exception("Cannot find any items with id: "+id);
        }
    }

    public Page<Item> listByPage(int num, String sortType, String sortOrder, String keyword) {
        Sort sort = Sort.by(sortType);
        if (sortOrder.equals(ASCENDING))
            sort.ascending();
        else
            sort.descending();

        Pageable pageable = PageRequest.of(num-1, ITEMS_PER_PAGE, sort);

        if (keyword == null) {
            return itemRepository.findAll(pageable);
        } else {
            return itemRepository.findALlByKeyword(keyword, pageable);
        }
    }

    public List<Item> findByKeyword(String keyword) {return itemRepository.findByKeyword(keyword);}

    public List<Item> listProducts(Item.Gender gender, Item.Type type, String sortOrder, String sortType) {
        if(type == null) {
            if (sortType.equals("discountedPrice") && sortOrder.equals("asc"))
                return itemRepository.findByGenderOrderByDiscountedPriceAsc(gender);
            else if (sortType.equals("discountedPrice") && sortOrder.equals("des"))
                return itemRepository.findByGenderOrderByDiscountedPriceDesc(gender);
            else
                return itemRepository.findByGenderOrderByItemIDDesc(gender);
        }
        else {
            if (sortType.equals("discountedPrice") && sortOrder.equals("asc"))
                return itemRepository.findByTypeAndGenderOrderByDiscountedPriceAsc(type, gender);
            else if (sortType.equals("discountedPrice") && sortOrder.equals("des"))
                return itemRepository.findByTypeAndGenderOrderByDiscountedPriceDesc(type, gender);
            else
                return itemRepository.findByTypeAndGenderOrderByItemIDDesc(type, gender);
        }
    }


    public Long getPage () {
        Long page = itemRepository.count() / ITEMS_PER_PAGE;
        if(itemRepository.count() %2!=0) {
            page = page +1 ;
        }
        if (page < 1)
            page = page + 1;
        return page;
    }

    public boolean itemExist(Long id) {
        return itemRepository.existsById(id);
    }

}
