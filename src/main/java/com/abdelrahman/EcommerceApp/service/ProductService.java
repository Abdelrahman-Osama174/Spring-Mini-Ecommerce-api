package com.abdelrahman.EcommerceApp.service;

import com.abdelrahman.EcommerceApp.model.Product;
import com.abdelrahman.EcommerceApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(new Product(-1));
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        if (image != null) {
            product.setImageName(image.getOriginalFilename());
            product.setImageType(image.getContentType());
            product.setImageData(image.getBytes());
        }

        return productRepository.save(product);
    }

    public Product updateProduct(int productId, Product product, MultipartFile image) throws IOException {
        Product updatedProduct = productRepository.findById(productId).orElse(new Product(-1));
        if (image != null) {
            updatedProduct.setImageName(image.getOriginalFilename());
            updatedProduct.setImageType(image.getContentType());
            updatedProduct.setImageData(image.getBytes());
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setBrand(product.getBrand());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setCategory(product.getCategory());
        updatedProduct.setStockQuantity(product.getStockQuantity());
        updatedProduct.setReleaseDate(product.getReleaseDate());
        updatedProduct.setProductAvailable(product.isProductAvailable());

        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }


}
