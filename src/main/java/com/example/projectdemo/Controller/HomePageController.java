package com.example.projectdemo.Controller;

import com.example.projectdemo.Model.*;
import com.example.projectdemo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomePageController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/products/*")
    public String showDefault() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String getHomePage(HttpSession session, Model model) {

        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));

        return "index";
    }

    @GetMapping("my_orders")
    public String getUserOrders(HttpSession session, Model model,  Authentication authentication) {
        if (authentication == null)
            return "redirect:/";
        if (authentication.getName().equals("admin"))
            return "redirect:/";
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));

        List<Orders> orders = orderService.findByUser(authentication.getName());
        model.addAttribute("orderList", orders);
        return "MyOrders";
    }

    @GetMapping("update_account")
    public String updateAccount(HttpSession session, Model model,  Authentication authentication) {
        if (authentication == null)
            return "redirect:/";
        if (authentication.getName().equals("admin"))
            return "redirect:/";
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));

        Customer customer =      customerService.findByUsername(authentication.getName());
        model.addAttribute("customer", customer);
        return "update_account";
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(Customer customer, RedirectAttributes redirectAttributes) {
        customerService.addCustomer(customer);
        redirectAttributes.addFlashAttribute("message", "Your account has successfully updated");
        return "redirect:/";
    }

    @GetMapping("/search_item")
    public String searchItemPage(Model model, HttpSession session) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));
        return "search_item";
    }

    @PostMapping("/search_item")
    public String searchItem(@RequestParam(value = "keyword", required = false) String keyword , Model model, HttpSession session) {
        List<Item> items = itemService.findByKeyword(keyword);
        model.addAttribute("itemlist",items);
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));
        return "search_item";
    }

    @GetMapping("/products")
    public String listByPageProducts(@Param("gender") String gender,
                                 @Param("sortOrder") String sortOrder,
                                 @Param("type") String type,
                                 @Param("sortType") String sortType,
                                     HttpSession session, Authentication authentication,
                                 Model model) {
        List<Item> items;
        if(type == null) {
            items = itemService.listProducts(Item.Gender.valueOf(gender), null, sortOrder, sortType);
        } else
            items = itemService.listProducts(Item.Gender.valueOf(gender), Item.Type.valueOf(type), sortOrder, sortType);

        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));


        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortType", sortType);
        model.addAttribute("itemlist", items);
        model.addAttribute("type", type);
        model.addAttribute("gender",gender);

        return "products";
    }

    @GetMapping("/item_detail/{itemID}")
    public String viewItemDetail(@PathVariable(name = "itemID") Long itemID,
                                 HttpSession session,
                                 Model model, Authentication authentication) {
        Item item = itemService.findByID(itemID);
        List<Image> imageList = imageService.findImagebyItemId(itemID);

        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        model.addAttribute("cart", getQuantity(cartItems));

        model.addAttribute("item", item);
        model.addAttribute("imageList", imageList);

        return "item_detail";
    }

    @PostMapping("/add_to_cart")
    public String addToCart(@RequestParam("quantity") Integer quantity,
                            @RequestParam("itemID") Long itemID,
                            @RequestParam("size") String size,
                            HttpSession session) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        List<Cart> cartList = cartItems.get(itemID);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        Item item = itemService.findByID(itemID);
        if (cartItems.containsKey(itemID)) {
            boolean hasSize = false;
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getItem().getItemID() == itemID && cartList.get(i).getSize().equals(size)) {
                    cartList.get(i).setQuantity(cartList.get(i).getQuantity() + quantity);
                    hasSize = true;
                }
            }
            if (!hasSize) {
                Cart buf = new Cart(item, quantity, size);
                cartList.add(buf);
            }
        } else {
            Cart buf = new Cart(item, quantity, size);
            cartList.add(buf);
        }
        cartItems.put(itemID, cartList);
        session.setAttribute("totalCart", cartList.size());

        session.setAttribute("cartList", cartItems);
        return "redirect:/shopping_cart";
    }

    @GetMapping("/shopping_cart")
    public String viewShoppingCart(HttpSession session, Model model, Authentication authentication) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");

        List<Cart> cartList = new ArrayList<>();
        if (cartItems == null) {
            cartItems = new HashMap<>();
        } else {
            for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
                for (int i = 0; i < list.getValue().size(); i ++ ) {
                    Cart buf = list.getValue().get(i);
                    buf.setItem(itemService.findByID(list.getValue().get(i).getItem().getItemID()));
                    if (buf.getQuantity() > buf.getInStock()) {
                        list.getValue().get(i).setQuantity(buf.getInStock());
                        buf.setQuantity(buf.getInStock());
                    }
                    cartList.add(buf);
                }
            }
        }
        model.addAttribute("totalCart",totalPrice(cartItems));
        session.setAttribute("cartList", cartItems);


        model.addAttribute("cart", getQuantity(cartItems));
        model.addAttribute("cartList", cartList);
        return "shopping_cart";
    }

    public double totalPrice(HashMap<Long, List<Cart>> cartItems) {
        double count = 0;
        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            for (int i = 0; i < list.getValue().size(); i ++ ) {
                count += list.getValue().get(i).getPrice() * list.getValue().get(i).getQuantity();
            }
        }
        NumberFormat formatter = new DecimalFormat("#0.0000");

        return Double.parseDouble((formatter.format(count)));
    }

    @GetMapping("/up_quantity")
    public String increaseQuantity(@RequestParam("num") Integer num,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");

        int count = 0;
        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            for (int i = 0; i < list.getValue().size(); i ++ ) {
                if (count == num) {
                    if (list.getValue().get(i).getInStock() <= list.getValue().get(i).getQuantity()) {
                        redirectAttributes.addAttribute("message", "The number of items cannot be higher in stock");
                        return "redirect:/shopping_cart";
                    } else {
                        list.getValue().get(i).setQuantity(list.getValue().get(i).getQuantity() + 1);
                    }
                }
                count ++;
            }
        }
        session.setAttribute("cartList", cartItems);
        return "redirect:/shopping_cart";
    }

    @GetMapping("/down_quantity")
    public String decreaseQuantity(@RequestParam("num") Integer num,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");

        int count = 0;
        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            for (int i = 0; i < list.getValue().size(); i ++ ) {
                if (count == num) {
                    if (list.getValue().get(i).getQuantity() <= 1) {
                        redirectAttributes.addAttribute("message", "The number of items cannot be lower than 1. Please remove instead");
                        return "redirect:/shopping_cart";
                    } else {
                        list.getValue().get(i).setQuantity(list.getValue().get(i).getQuantity() - 1);
                    }
                }
                count ++;
            }
        }
        session.setAttribute("cartList", cartItems);
        return "redirect:/shopping_cart";
    }

    @GetMapping("/remove/{itemID}")
    public String removeCart(@PathVariable("itemID") Long itemID, HttpSession session, RedirectAttributes redirectAttributes) {
        HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");

        int count = 0;
        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            List<Cart> cartList = new ArrayList<>();
                for (int i = 0; i < list.getValue().size(); i ++ ) {
                    if (count == itemID) {
                        if (list.getValue().size() == 1) {
                            cartItems.remove(list.getKey());
                            return "redirect:/shopping_cart";
                        } else {
                            list.getValue().remove(i);
                            return "redirect:/shopping_cart";
                        }
                    }
                    cartList.add(list.getValue().get(i));
                    if (count > itemID) {
                        cartItems.put(list.getKey(), cartList);
                        break;
                    }
                    count ++;
            }
        }
        session.setAttribute("cartList", cartItems);
        return "redirect:/shopping_cart";
    }

    @GetMapping("/check_out")
    public String checkOutPage(Model model,Authentication authentication, HttpSession session) {
        if (authentication.getName().equals("admin")) {
            return "redirect:/shopping_cart";
        } else {
            HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
            List<Cart> cartList = new ArrayList<>();
            if (cartItems == null) {
                cartItems = new HashMap<>();
            } else {
                for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
                    for (int i = 0; i < list.getValue().size(); i ++ ) {
                        Cart buf = list.getValue().get(i);
                        buf.setItem(itemService.findByID(list.getValue().get(i).getItem().getItemID()));
                        if (buf.getQuantity() > buf.getInStock()) {
                            list.getValue().get(i).setQuantity(buf.getInStock());
                            buf.setQuantity(buf.getInStock());
                        }
                        cartList.add(buf);
                    }
                }
            }

            session.setAttribute("cartList", cartItems);

            model.addAttribute("cart", getQuantity(cartItems));
            model.addAttribute("cartList", cartList);
            model.addAttribute("totalCart",totalPrice(cartItems));
            Customer customer = customerService.findByUsername(authentication.getName());
            model.addAttribute("customer", customer);
        }
        return "check_out";
    }

    @PostMapping("/complete_order")
    public String proceedPurchase(Authentication authentication,
                                  HttpSession session,
                                  @RequestParam("address") String address,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  @RequestParam("total") double total,
                                  RedirectAttributes redirectAttributes) {
        try {
            HashMap<Long, List<Cart>> cartItems = (HashMap<Long, List<Cart>>) session.getAttribute("cartList");
            if (cartItems == null) {
                cartItems = new HashMap<>();
            }
            orderService.addOrder(customerService.findByUsername(authentication.getName()), address, phoneNumber, cartItems, total);
            session.setAttribute("cartList", new HashMap<>());
            redirectAttributes.addFlashAttribute("message", "Thank you for purchasing");
        }catch (org.hibernate.exception.ConstraintViolationException e) {
            redirectAttributes.addFlashAttribute("message", "Username or email already exists");
            return "redirect:/update_account";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("message", "Username or email already exists");
            return "redirect:/update_account";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Unknown error occurred while executing the registration");
            return "redirect:/update_account";
        }
        return "redirect:/";
    }

    public Integer getQuantity(HashMap<Long, List<Cart>> cartItems) {
        int count = 0;
        for (Map.Entry<Long, List<Cart>>  list : cartItems.entrySet()) {
            for (int i = 0; i < list.getValue().size(); i ++ ) {
                count ++;
            }
        }
        return count;
    }


}
