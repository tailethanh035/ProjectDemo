package com.example.projectdemo.Repository;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Model.Orders;
import com.example.projectdemo.Model.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("select distinct i from Orders i, OrderDetail j where i.address like %?1% or i.customer.firstName like %?1%  or i.customer.lastName like %?1% or i.customer.email like %?1% or j.item.name like %?1% ")
    Page<Orders> findALlByKeyword(String keyword, Pageable pageable);

    Orders findByOrderID(Integer id);

    List<Orders> findAllByUsernameOrderByOrderIDDesc(String username);
}
