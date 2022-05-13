package com.example.projectdemo.Controller;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Image;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Service.CustomerService;
import com.example.projectdemo.Service.ImageService;
import com.example.projectdemo.Service.ItemService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/admin/*")
    public String showDefault() {
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @GetMapping("/admin/items")
    public String getItemsControlPanel(Model model) {
        List<Item> itemList = itemService.getAllItems();
        model.addAttribute("itemlist", itemList);
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @GetMapping("/admin/accounts")
    public String getCustomerControlPanels(Model model) {
        return "redirect:/admin/accounts/page/1?sortType=customerID&sortOrder=asc";
    }

    @GetMapping("/admin/accounts/page/{pageNum}")
    public String listByPageAccounts(@PathVariable(name="pageNum")
                                         int pageNum,
                                 @Param("sortType") String sortType,
                                 @Param("sortOrder") String sortOrder,
                                 @Param("keyword") String keyword,
                                 @Param("message") String message,
                                 Model model) {

        Page<Customer> page = customerService.listCustomersByPage(pageNum, sortType, sortOrder, keyword);
        List<Customer> customerList = page.getContent();

        long start = (pageNum - 1) * customerService.ACCOUNTS_PER_PAGE + 1;
        long end = start + customerService.ACCOUNTS_PER_PAGE - 1;

        if (end>page.getTotalElements()) {
            end = page.getTotalElements();
        }

        String sortReversed = "";
        if (sortOrder.equals("asc"))
            sortReversed = "des";
        else
            sortReversed = "asc";

        model.addAttribute("sortReversed", sortReversed);
        model.addAttribute("message", message);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("customerList", customerList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", customerService.getPage());

        return "admin/accounts";
    }

    @GetMapping("/admin/accounts/changeStatus/{id}")
    public String changeCustomerStatus(@PathVariable(name="id")
                                     Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            customerService.changeStatus(id);
            redirectAttributes.addAttribute("message", "User status has been changed ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/accounts/page/1?sortType=customerID&sortOrder=asc";
    }

    @GetMapping("/admin/items/page/{pageNum}")
    public String listByPageItem(@PathVariable(name="pageNum")
                                         int pageNum,
                                 @Param("sortType") String sortType,
                                 @Param("sortOrder") String sortOrder,
                                 @Param("keyword") String keyword,
                                 @Param("message") String message,
                                 Model model) {

        Page<Item> page = itemService.listByPage(pageNum, sortType, sortOrder, keyword);
        List<Item> items = page.getContent();

        long start = (pageNum - 1) * itemService.ITEMS_PER_PAGE + 1;
        long end = start + itemService.ITEMS_PER_PAGE - 1;

        if (end>page.getTotalElements()) {
            end = page.getTotalElements();
        }

        String sortReversed = "";
        if (sortOrder.equals("asc"))
            sortReversed = "des";
        else
            sortReversed = "asc";

        model.addAttribute("sortReversed", sortReversed);
        model.addAttribute("message", message);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("itemlist", items);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", itemService.getPage());

        return "admin/items";
    }

    @GetMapping("/admin/items/additem")
    public String addNewItem(Model model) {
        Item item = new Item();
        model.addAttribute("item", item);
        return "admin/add_item";
    }

    @PostMapping("/admin/items/save")
    public String saveItem(Item item,
                           @RequestParam("image") MultipartFile multipartFile,
                           RedirectAttributes redirectAttributes) {
        try {
            byte[] buffer = Base64.encodeBase64(multipartFile.getBytes());
            item.setPhoto(new String(buffer));
            itemService.addItem(item);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        redirectAttributes.addAttribute("message", "New item saved successfully");
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }


    @GetMapping("/admin/items/delete/{id}")
    public String deleteItem(@PathVariable(name="id")
                                         Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            redirectAttributes.addAttribute("message", "The item has been deleted");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @GetMapping("/admin/items/update/{id}")
    public String updateItem(@PathVariable(name="id")
                                     Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            Item item = itemService.getItem(id);
            model.addAttribute("item", item);
            model.addAttribute("updatedItem", "Updating Item ID: " + id);
            return "admin/update_item";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }



    @GetMapping("/admin/items/add_images/{id}")
    public String addImages(@PathVariable(name="id")
                                     Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            List<Image> imageList = imageService.findImagebyItemId(id);
            model.addAttribute("item_id", id);
            model.addAttribute("imageList", imageList);
            model.addAttribute("updatedItem", "Add Images Item ID: " + id);
            return "admin/add_images";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @PostMapping("/admin/items/saveImage")
    public String saveImage(@RequestParam("id") Long item_id,
                            @RequestParam("image") MultipartFile[] multipartFiles,
                           RedirectAttributes redirectAttributes) {
        try {
            Item item = itemService.getItem(item_id);
            for (int i = 0; i < multipartFiles.length; i++) {
                byte[] buffer = Base64.encodeBase64(multipartFiles[i].getBytes());
                imageService.addImage(new Image(), item, new String(buffer));
            }
            redirectAttributes.addFlashAttribute("message", "The images has been updated");
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
        redirectAttributes.addAttribute("message", "New item saved successfully");
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

    @GetMapping("/admin/items/delete_image/{id}")
    public String deleteImage(@PathVariable(name="id")
                                     Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            imageService.deleteImage(id);
            redirectAttributes.addFlashAttribute("message", "The images has been deleted");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/items/page/1?sortType=itemID&sortOrder=asc";
    }

}
