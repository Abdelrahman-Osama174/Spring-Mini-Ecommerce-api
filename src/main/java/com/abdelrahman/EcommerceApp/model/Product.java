package com.abdelrahman.EcommerceApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String brand;
    private String category;
    private String description;

    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;

    private boolean productAvailable;
    private int stockQuantity;

    // Image info
    private String imageName;
    private String imageType;

    @Lob // Used for large data (image)
    private byte[] imageData;

    // Used when product is not found (to avoid null)
    public Product(int id) {
        this.id = id;
    }
}
