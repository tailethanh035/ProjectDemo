package com.example.projectdemo.Service;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Image;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Model.ShoppingCart;
import com.example.projectdemo.Repository.CustomerRepository;
import com.example.projectdemo.Repository.ImageRepository;
import com.example.projectdemo.Repository.ItemRepository;
import com.example.projectdemo.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public void addCart(String size, Integer quantity, Long itemID, String username) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItem(itemRepository.findItemByItemID(itemID));
        shoppingCart.setNum(quantity);
        shoppingCart.setSize(size);
        shoppingCart.setCustomer(customerRepository.getCustomerByUsername(username));
        shoppingCart.setInStock();
        repository.save(shoppingCart);
    }

    public ShoppingCart getAlreadyCart(String size, Long itemID) {
        return repository.getAlreadyCart(size, itemID);
    }

    public List<ShoppingCart> getByItemID(Long itemID) {
        return repository.getByItemID(itemID);
    }

    public List<ShoppingCart> findByCustomer(String username) {return repository.findByCustomer(customerRepository.getCustomerByUsername(username));}

}
