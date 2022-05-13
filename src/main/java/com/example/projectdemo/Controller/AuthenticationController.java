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
public class AuthenticationController {


    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String getLogIn() {
        return "log_in";
    }

    @GetMapping("/login-error")
    public String logInErrorHandler(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "error");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerAccount(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "register";
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.addCustomer(customer);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            redirectAttributes.addFlashAttribute("message", "Username or email already exists");
            return "redirect:/register";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("message", "Username or email already exists");
            return "redirect:/register";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Unknown error occurred while executing the registration");
            return "redirect:/register";
        }
        return "redirect:/login";
    }

}
