package com.xfef0.fccshops.service.image;

import com.xfef0.fccshops.dto.ImageDTO;
import com.xfef0.fccshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    List<ImageDTO> addImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
    void deleteImageById(Long imageId);
    Image getImageById(Long imageId);
    List<Image> getImageByProductId(Long productId);
}
