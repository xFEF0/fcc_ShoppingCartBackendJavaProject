package com.xfef0.fccshops.service.image;

import com.xfef0.fccshops.dto.ImageDto;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Image;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.ImageRepository;
import com.xfef0.fccshops.service.product.ProductService;
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
    private final ProductService productService;
    private final ImageRepository imageRepository;


    @Override
    public List<ImageDto> addImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        files.forEach(file -> {
            saveImages(file, product, imageDtos);
        });

        return imageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image existingImage = getImageById(imageId);
        try {
            existingImage.setFileName(file.getName());
            existingImage.setFileType(file.getContentType());
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

    private void saveImages(MultipartFile file, Product product, List<ImageDto> imageDtos) {
        try {
            Image image = new Image();
            image.setFileName(file.getName());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setProduct(product);

            Image savedImage = imageRepository.save(image);
            savedImage.setDownloadURL("/api/v1/image/download/" + savedImage.getId());
            imageRepository.save(savedImage);

            ImageDto imageDto = new ImageDto();
            imageDto.setImageId(savedImage.getId());
            imageDto.setImageName(savedImage.getFileName());
            imageDto.setDownloadURL(savedImage.getDownloadURL());

            imageDtos.add(imageDto);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
