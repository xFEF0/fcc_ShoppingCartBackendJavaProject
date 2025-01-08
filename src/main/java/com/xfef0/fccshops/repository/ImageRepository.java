package com.xfef0.fccshops.repository;

import com.xfef0.fccshops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByType(String type);

    Image findByName(String name);
}
