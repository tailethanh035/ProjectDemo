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
    @Query("select i from Item i where i.name like %?1%")
    Page<Item> findALlByKeyword(String keyword, Pageable pageable);

//    @Query("select  from Item i where i.type = ?1")
//    List<Item> findAllByGender(String gender);
    List<Item> findByGender(Item.Gender gender);

    List<Item> findByTypeAndGender(Item.Type type, Item.Gender gender);

}
