package com.example.projectdemo.Model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table
public class ShoppingCart {
    @ManyToOne
    @JoinColumn(name = "itemID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    protected Integer cartId;

    @Column(nullable = false)
    private String size;

    @Column
    private Integer num;

    @Column
    private Integer inStock;

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock() {
        if (size.equals("XS"))
            inStock = item.getXS();
        else if (size.equals("S"))
            inStock = item.getS();
        else if (size.equals("M"))
            inStock = item.getM();
        else if (size.equals("L"))
            inStock = item.getL();
        else if (size.equals("XL"))
            inStock = item.getXL();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
