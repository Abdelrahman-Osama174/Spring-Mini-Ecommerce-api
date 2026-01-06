package com.abdelrahman.EcommerceApp.controller;

import com.abdelrahman.EcommerceApp.model.Product;
import com.abdelrahman.EcommerceApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin  // Allow requests from any domain (CORS)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() > 0)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageByProductById(@PathVariable("id") int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() > 0)
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("imageFile") MultipartFile image) {

        /**
         * This solves the problem where Postman can't send JSON directly
         * with Content-Type multipart/form-data.
         *
         * - Instead of receiving the product as a Product object, we get it as a String (`productJson`).
         * - We then use ObjectMapper to convert this String into a Product object.
         *
         * This way, we can send both the product data and the image in one multipart/form-data request
         * without any Content-Type issues.
         */
        Product product = new ObjectMapper().readValue(productJson, Product.class);
        try {
            Product savedProduct = productService.addProduct(product, image);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") int productId,
            @RequestPart("product") String productJson,
            @RequestPart("imageFile") MultipartFile image) {

        Product product = new ObjectMapper().readValue(productJson, Product.class);
        try {
            Product UpdatedProduct = productService.updateProduct(productId, product, image);
            return new ResponseEntity<>(UpdatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() > 0) {
            productService.deleteProduct(product);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword) {
        return new ResponseEntity<>(productService.searchProducts(keyword), HttpStatus.OK);
    }
}
