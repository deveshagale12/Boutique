package com.boutique;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/boutique/admin/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductImageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository imageRepository;

    @PostMapping("/{id}/upload-images")
    public ResponseEntity<?> uploadMultipleImages(
            @PathVariable Long id, 
            @RequestParam("files") MultipartFile[] files) {
        
        return productRepository.findById(id).map(product -> {
            if (files.length > 4) {
                return ResponseEntity.badRequest().body("Maximum 4 images allowed.");
            }

            try {
                // Clear existing images for a clean "Edit/Update"
                product.getImages().clear();

                for (MultipartFile file : files) {
                    ProductImage img = new ProductImage();
                    img.setData(file.getBytes());
                    img.setContentType(file.getContentType());
                    img.setProduct(product);
                    product.getImages().add(img);
                }

                productRepository.save(product);
                return ResponseEntity.ok("Successfully uploaded " + files.length + " images to NeonDB.");
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error processing files.");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // API to fetch a specific image by index (0 to 3)
    @GetMapping("/{id}/image/{index}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id, @PathVariable int index) {
        Product product = productRepository.findById(id).orElseThrow();
        if (index >= product.getImages().size()) return ResponseEntity.notFound().build();
        
        ProductImage img = product.getImages().get(index);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getContentType()))
                .body(img.getData());
    }
    
 // --- EDIT/UPDATE ALL IMAGES ---
    @PutMapping("/{id}/images")
    public ResponseEntity<?> updateProductImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        return productRepository.findById(id).map(product -> {
            if (files.length > 4) return ResponseEntity.badRequest().body("Max 4 images allowed.");

            try {
                // Clear old images from NeonDB (OrphanRemoval handles the DB cleanup)
                product.getImages().clear();

                for (MultipartFile file : files) {
                    ProductImage img = new ProductImage();
                    img.setData(file.getBytes());
                    img.setContentType(file.getContentType());
                    img.setProduct(product);
                    product.getImages().add(img);
                }

                productRepository.save(product);
                return ResponseEntity.ok("Images updated successfully.");
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Failed to process images.");
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE SINGLE IMAGE ---
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
            return ResponseEntity.ok("Image deleted from NeonDB.");
        }
        return ResponseEntity.notFound().build();
    }
}