package com.example.projectdemo.Repository;

import com.example.projectdemo.Model.Customer;
import com.example.projectdemo.Model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.username= :username")
    Customer getCustomerByUsername(@Param("username") String username);

    @Query("select i from Customer i where i.firstName like %?1% or i.address like %?1% or i.lastName like %?1% or i.email like %?1% or i.phoneNumber like %?1%")
    Page<Customer> findALlByKeyword(String keyword, Pageable pageable);
}

