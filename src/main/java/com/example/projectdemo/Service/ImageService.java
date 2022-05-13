package com.example.projectdemo.Service;

import com.example.projectdemo.Model.Image;
import com.example.projectdemo.Model.Item;
import com.example.projectdemo.Repository.ImageRepository;
import com.example.projectdemo.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void addImage(Image image, Item item, String imageBase64) {
        image.setItem(item);
        image.setPhoto(imageBase64);
        imageRepository.save(image);
    }

    public List<Image> findImagebyItemId(Long id) {
        return imageRepository.findImageByItemId(id);
    }

    public void deleteImage(Long id) throws Exception {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        } else {
            throw new Exception("Cannot find any items with id: "+id);
        }
    }
}
