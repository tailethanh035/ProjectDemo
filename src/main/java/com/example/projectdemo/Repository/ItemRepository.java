package com.example.projectdemo.Repository;

import com.example.projectdemo.Model.Image;
import com.example.projectdemo.Model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemByItemID(Long id);

    @Query("select i from Item i where i.name like %?1%")
    Page<Item> findALlByKeyword(String keyword, Pageable pageable);

    @Query("select i from Item i where i.name like %?1%")
    List<Item> findByKeyword(String keyword);

    List<Item> findByGenderOrderByItemIDDesc(Item.Gender gender);

    List<Item> findByGenderOrderByDiscountedPriceAsc(Item.Gender gender);

    List<Item> findByGenderOrderByDiscountedPriceDesc(Item.Gender gender);

    List<Item> findByTypeAndGender(Item.Type type, Item.Gender gender);

    List<Item> findByTypeAndGenderOrderByDiscountedPriceAsc(Item.Type type, Item.Gender gender);

    List<Item> findByTypeAndGenderOrderByDiscountedPriceDesc(Item.Type type, Item.Gender gender);

    List<Item> findByTypeAndGenderOrderByItemIDDesc(Item.Type type, Item.Gender gender);



}
