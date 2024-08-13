package com.springboot.coding.jpaTuts.entities.repositories;

import com.springboot.coding.jpaTuts.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByTitle(String title);

    List<ProductEntity> findByTitleContaining(String title);

    List<ProductEntity> findByTitleContainingIgnoreCase(String title);

    List<ProductEntity> findByCreatedAtAfter(LocalDateTime after);

    List<ProductEntity> findByQuantityAndPrice(int quantity, BigDecimal price);

    List<ProductEntity> findByQuantityGreaterThanAndPriceLessThan(int quantity, BigDecimal price);

    List<ProductEntity> findByQuantityGreaterThanOrPriceLessThan(int quantity, BigDecimal price);

    List<ProductEntity> findByTitleLike(String title);

    // This give unique row coz (title, price) have unique constraint
//    List<ProductEntity> findByTitleAndPrice(String title, BigDecimal price);
//    Optional<ProductEntity> findByTitleAndPrice(String title, BigDecimal price);

    // title is in ProductEntity
//    @Query(
//            "SELECT e " +
//                    "FROM ProductEntity e " +
//                    "WHERE e.title=?1 AND e.price=?2"
//    )

    @Query(
            "SELECT e " +
                    "FROM ProductEntity e " +
                    "WHERE e.title=:title AND e.price=:price"
    )
    Optional<ProductEntity> findByTitleAndPrice(String title, BigDecimal price);


    // This is for sorting
    List<ProductEntity> findByTitleOrderByPrice(String title);

}