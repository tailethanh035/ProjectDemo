package com.example.projectdemo.Repository;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByCustomer(Customer customer);

    ShoppingCart findByCustomerAndItem(Customer customer, Item item);

    @Modifying
    @Query("UPDATE ShoppingCart s SET s.num = ?1 WHERE s.customer.customerID = ?2 and s.item.itemID = ?3")
    void updateCart(Integer num, Long customerID, Long itemID);

    @Query("select s FROM ShoppingCart s where s.size=?1 and s.item.itemID = ?2")
    ShoppingCart getAlreadyCart(String size, Long itemID);

    @Query("select s from ShoppingCart s where s.item.itemID = ?1")
    List<ShoppingCart> getByItemID(Long itemID);
}
