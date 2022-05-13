package com.example.projectdemo.Repository;

import com.example.projectdemo.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select img from Image img where img.item.itemID = :id")
    List<Image> findImageByItemId(@Param("id") Long id);
}
