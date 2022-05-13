package com.example.projectdemo.Controller;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Service.CustomerService;
import com.example.projectdemo.Service.ImageService;
import com.example.projectdemo.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/products/*")
    public String showDefault() {
        return "redirect:/";
    }

    @GetMapping("/products/"    )
    public String getItemsControlPanel(Model model) {
        List<Item> itemList = itemService.getAllItems();
        model.addAttribute("itemlist", itemList);
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPageProducts(@PathVariable(name="pageNum")
                                         int pageNum,
                                 @Param("gender") String gender,
                                 @Param("sortOrder") String sortOrder,
                                 @Param("type") String type,
                                 Model model) {

//        Page<Item> page = itemService.listByPage(pageNum, "asc", "gender", "");

        List<Item> items;
        if(type == null) {
            items = itemService.listProducts(Item.Gender.valueOf(gender), null);
        } else
            items = itemService.listProducts(Item.Gender.valueOf(gender), Item.Type.valueOf(type));

//        long start = (pageNum - 1) * itemService.PRODUCTS_PER_PAGE + 1;
//        long end = start + itemService.PRODUCTS_PER_PAGE - 1;
//
//        if (end>page.getTotalElements()) {
//            end = page.getTotalElements();
//        }
//
//        String sortReversed = "";
//        if (sortOrder.equals("asc"))
//            sortReversed = "des";
//        else
//            sortReversed = "asc";

//        model.addAttribute("sortType", sortType);
//        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("itemlist", items);
        model.addAttribute("type", type);
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("startPage", start);
//        model.addAttribute("endPage", end);
        model.addAttribute("gender",gender);
//        model.addAttribute("totalPage", itemService.getPage());

        return "products";
    }
}
