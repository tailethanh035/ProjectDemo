package com.example.projectdemo.Model;

public class Cart {

    private Item item;
    private String size;
    private int quantity;
    private int inStock;
    private String name;
    private String type;
    private double price;
    private double discountedBy;
    private String photo;
    private Long itemID;

    public Cart() {
    }

    public Cart(Item item, int quantity, String size) {
        this.item = item;
        this.quantity = quantity;
        this.size = size;
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
        this.name = item.getName();
        this.type = item.getType().name();
        this.price = item.getDiscountedPrice();
        this.discountedBy = item.getDiscountedBy();
        this.photo = item.getPhoto();
        this.itemID = item.getItemID();
    }

    public Item getProduct() {
        return item;
    }

    public void setProduct(Item product) {
        this.item = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item itemUpdate) {
        if (size.equals("XS"))
            inStock = itemUpdate.getXS();
        else if (size.equals("S"))
            inStock = itemUpdate.getS();
        else if (size.equals("M"))
            inStock = itemUpdate.getM();
        else if (size.equals("L"))
            inStock = itemUpdate.getL();
        else if (size.equals("XL"))
            inStock = itemUpdate.getXL();
        this.name = itemUpdate.getName();
        this.type = itemUpdate.getType().name();
        this.item = itemUpdate;
        this.price = itemUpdate.getDiscountedPrice();
        this.discountedBy = itemUpdate.getDiscountedBy();
        this.photo = itemUpdate.getPhoto();
        this.itemID = itemUpdate.getItemID();
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountedBy() {
        return discountedBy;
    }

    public void setDiscountedBy(double discountedBy) {
        this.discountedBy = discountedBy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }
}