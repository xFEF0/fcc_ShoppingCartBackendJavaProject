package com.xfef0.fccshops.service.image;

import com.xfef0.fccshops.dto.ImageDTO;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Image;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.ImageRepository;
import com.xfef0.fccshops.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    public static final String NOT_FOUND_IMAGE_WITH = "Not found image with ";
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<ImageDTO> addImages(List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        List<ImageDTO> imageDTOs = new ArrayList<>();

        files.forEach(file -> {
            saveImages(file, product, imageDTOs);
        });

        return imageDTOs;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image existingImage = getImageById(imageId);
        try {
            existingImage.setName(file.getName());
            existingImage.setType(file.getContentType());
            existingImage.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(existingImage);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete,
                        () -> {throw new ResourceNotFoundException(NOT_FOUND_IMAGE_WITH + " id: " + imageId);});
    }

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_IMAGE_WITH + " id: "  + imageId));
    }

    @Override
    public List<Image> getImageByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }

    private void saveImages(MultipartFile file, Product product, List<ImageDTO> imageDTOs) {
        try {
            Image image = new Image();
            image.setName(file.getName());
            image.setType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setProduct(product);

            Image savedImage = imageRepository.save(image);
            savedImage.setDownloadURL("/api/v1/image/download/" + savedImage.getId());
            imageRepository.save(savedImage);

            ImageDTO imageDto = new ImageDTO();
            imageDto.setId(savedImage.getId());
            imageDto.setName(savedImage.getName());
            imageDto.setDownloadURL(savedImage.getDownloadURL());

            imageDTOs.add(imageDto);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
