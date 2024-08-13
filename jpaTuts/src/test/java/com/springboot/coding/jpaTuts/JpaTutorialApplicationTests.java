package com.springboot.coding.jpaTuts;

import com.springboot.coding.jpaTuts.entities.ProductEntity;
import com.springboot.coding.jpaTuts.entities.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class JpaTutorialApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testRepository() {
        // Create the Entity using the Builder pattern
        ProductEntity productEntity = ProductEntity.builder()
                .sku("nestle234")
                .title("Nestle Chocolate")
                .price(BigDecimal.valueOf(23.45))
                .quantity(4)
                .build();

        // Save the Entity to the database
        ProductEntity savedProductEntity = productRepository.save(productEntity);

        System.out.println("Saved Product: " + savedProductEntity);
    }

    @Test
    void getRepository() {
//        List<ProductEntity> entities = productRepository.findAll();
//        List<ProductEntity> entities = productRepository.findByTitle("Pepsi");
//        List<ProductEntity> entities = productRepository.findByTitleContaining("Pep");
//        List<ProductEntity> entities = productRepository.findByTitleContaining("Pepsi");
//        List<ProductEntity> entities = productRepository.findByTitleContainingIgnoreCase("pepsi");

//        List<ProductEntity> entities = productRepository
//                .findByCreatedAtAfter(
//                        LocalDateTime.of(2024, 1, 1, 0, 0, 0)
//                );

//        List<ProductEntity> entities = productRepository
//                .findByQuantityAndPrice(4, BigDecimal.valueOf(23.45));

//        List<ProductEntity> entities = productRepository
//                .findByQuantityGreaterThanAndPriceLessThan(4, BigDecimal.valueOf(23.45));

//        List<ProductEntity> entities = productRepository
//                .findByQuantityGreaterThanOrPriceLessThan(4, BigDecimal.valueOf(23.45));

        // Start with "Pep" and any other characters
//        List<ProductEntity> entities = productRepository.findByTitleLike("Pep%");

        // End with "Pepsi" and any other characters
//        List<ProductEntity> entities = productRepository.findByTitleLike("%Pepsi");

        // Contain Pepsi anywhere in the title
        // If we use Containing then we don't have to use %word%
        List<ProductEntity> entities = productRepository.findByTitleLike("%Pepsi%");
        System.out.println("Entities: " + entities);

        for (ProductEntity entity : entities)
            System.out.println("Entity: " + entity);
    }

    @Test
    void getSingleFromRepository() {
        Optional<ProductEntity> productEntity = productRepository
                .findByTitleAndPrice("Pepsi", BigDecimal.valueOf(14.4));
//        if (productEntity.isPresent())
//            System.out.println("Product: " + productEntity.get());
//        else
//            System.out.println("Product not found");

//            productEntity.ifPresentOrElse(
//                    System.out::println,
//                    () -> System.out.println("Product not found")
//            );

        productEntity.ifPresent(System.out::println);
    }
}
