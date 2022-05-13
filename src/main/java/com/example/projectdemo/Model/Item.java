package com.example.projectdemo.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table
public class Item {
    public enum Gender {
        Men, Women, Boy, Girl;
    }

    public enum Type {
        Shirt, TShirt, Jacket, Pants, Shorts, Dress, Shoes;
    }

    public Item(){
        long millis=System.currentTimeMillis();
        date = new java.sql.Date(millis);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long itemID;

    @Column(length = 40, nullable = false)
    private String name;

    @Column
    private String description;

    @Column(columnDefinition="LONGTEXT")
    private String photo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Integer XS;

    @Column(nullable = false)
    private Integer S;

    @Column(nullable = false)
    private Integer M;

    @Column(nullable = false)
    private Integer L;

    @Column(nullable = false)
    private Integer XL;

    @Column(nullable = false)
    private double retailPrice;

    @Column double discountedPrice;

    @Basic
    private java.sql.Date date;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "item")
    public Set<Image> images;


    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString(){
        return "Name: " + name + ", description: " + description + ", gender: " + gender + ",type: " + type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Type getType() {
        return type;
    }

    public void addImage(Image image) {
        image.setItem(this);
        images.add(image);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getXS() {
        return XS;
    }

    public void setXS(Integer XS) {
        this.XS = XS;
    }

    public Integer getS() {
        return S;
    }

    public void setS(Integer s) {
        S = s;
    }

    public Integer getM() {
        return M;
    }

    public void setM(Integer m) {
        M = m;
    }

    public Integer getL() {
        return L;
    }

    public void setL(Integer l) {
        L = l;
    }

    public Integer getXL() {
        return XL;
    }

    public void setXL(Integer XL) {
        this.XL = XL;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
