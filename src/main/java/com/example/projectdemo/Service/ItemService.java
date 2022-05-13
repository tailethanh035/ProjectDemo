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
import java.util.List;

@Service
public class ItemService {
    public static final int ITEMS_PER_PAGE = 2;
    public static final int PRODUCTS_PER_PAGE = 10;
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "des";


    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return (List<Item>)itemRepository.findAll();
    }

    public  void addItem(Item item) {
        itemRepository.save(item);
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

    public List<Item> listProducts(Item.Gender gender, Item.Type type) {
        if(type == null)
            return itemRepository.findByGender(gender);
        else
            return itemRepository.findByTypeAndGender(type, gender);
    }

    public Page<Item> listProducts(int num, String sortType, String sortOrder, String gender) {
        Sort sort = Sort.by(sortType);

        if (sortType == null || sortOrder == null) {
            Pageable pageable = PageRequest.of(num-1, PRODUCTS_PER_PAGE);
//            return itemRepository.findAllByGender(gender, pageable);
        }
        if (sortOrder.equals(ASCENDING))
            sort.ascending();
        else
            sort.descending();

        Pageable pageable = PageRequest.of(num-1, PRODUCTS_PER_PAGE, sort);
        return itemRepository.findAll(pageable);
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
