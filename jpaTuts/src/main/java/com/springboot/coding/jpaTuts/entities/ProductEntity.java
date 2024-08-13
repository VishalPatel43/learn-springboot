package com.springboot.coding.jpaTuts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "product_table",
        uniqueConstraints = {
//                @UniqueConstraint( // put columns unique
//                        name = "sku_unique",
//                        columnNames = {"sku"}
//                ),
                @UniqueConstraint( // put columns unique, also create the index
                        name = "title_price_unique",
                        columnNames = {"product_title", "price"} // when we see the both column it should be unique
                )
        },
        indexes = {
                @Index(
                        name = "sku_index",
                        columnList = "sku"
                )
        }
)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;

    // This can't be null and the length is 20
    @Column(nullable = false, length = 20)
    private String sku; // sku --> stock keeping unit

    @Column(name = "product_title") // this name in the database column
    private String title;
    private BigDecimal price;
    private Integer quantity;

    // HH:mm:ss dd-MM-yyyy
    @CreationTimestamp
//    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createdAt;

    @UpdateTimestamp
//    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime updatedAt;

}
