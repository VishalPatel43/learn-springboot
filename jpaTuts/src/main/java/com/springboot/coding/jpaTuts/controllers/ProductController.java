package com.springboot.coding.jpaTuts.controllers;

import com.springboot.coding.jpaTuts.entities.ProductEntity;
import com.springboot.coding.jpaTuts.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final ProductRepository productRepository;

    // It should be the Hard coded value and don't give flexibility to front end developer to change it
    private static final int PAGE_SIZE = 5;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//    @GetMapping
//    public List<ProductEntity> getAllProducts() {
//        return productRepository.findByTitleOrderByPrice("Mazza");
//        return productRepository.findByOrderByPrice();

    @GetMapping(path = "/method1")
    public List<ProductEntity> getAllProducts(@RequestParam(defaultValue = "productId") String sortBy) {
        // default sort by id and Url: http://localhost:8080/products
//        return productRepository.findBy(Sort.by(sortBy)); // ascending order
//                return productRepository.findBy(Sort.by(Sort.Order.asc(sortBy))); // ascending order
//        return productRepository.findBy(Sort.by(Sort.Direction.ASC, sortBy)); // Ascending order


        // using Sort and pass data in URL
        // http://localhost:8080/products?sortBy=productId --> sort by productId
        // http://localhost:8080/products?sortBy=title --> sort by title

        // http://localhost:8080/products?sortBy=price --> sort by price

        // Descending order
//        return productRepository.findBy(Sort.by(Sort.Order.desc(sortBy))); // descending order

//        return productRepository.findBy(Sort.by(Sort.Direction.DESC, sortBy)); // descending order

//        http://localhost:8080/products?sortBy=quantity, here sortBy=quantity
//        return productRepository.findBy(Sort.by(Sort.Direction.DESC, sortBy, "price", "title")); // descending order


//        return productRepository.findBy(
//                Sort.by(
//                        Sort.Order.desc(sortBy),
//                        Sort.Order.asc("price")
//                )
//        ); // descending order
//        return productRepository.findBy(
//                Sort.by(sortBy).ascending() // ascending order
//        );

        return productRepository.findBy(
                Sort.by(sortBy).descending() // descending order
        );
    }

    @GetMapping(path = "/method2")
    public List<ProductEntity> getAllProducts1(@RequestParam(defaultValue = "productId") String sortBy) {
        return productRepository.findAll();
    }

    @GetMapping(path = "/method3")
    public Page<ProductEntity> getAllProducts2( // give all the data in the form of pages
                                                @RequestParam(defaultValue = "productId") String sortBy,
                                                @RequestParam(defaultValue = "0") Integer pageNumber) {

        Pageable pageable = PageRequest.of(
                pageNumber,
                PAGE_SIZE
        );

        return productRepository.findAll(pageable);
    }

    @GetMapping(path = "/method4")
    public List<ProductEntity> getAllProducts3(
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        // http://localhost:8080/products/method4?pageNumber=0 (default sortBy=productId)
        // http://localhost:8080/products/method4?sortBy=productId&pageNumber=0
        // http://localhost:8080/products/method4 (all 3 are same)
        // here we don't pass Sort, so it will default sort by productId
//        Pageable pageable = PageRequest.of(
//                pageNumber,
//                PAGE_SIZE);

        // http://localhost:8080/products/method4?sortBy=quantity&pageNumber=0
        // http://localhost:8080/products/method4?sortBy=quantity
        Pageable pageable = PageRequest.of(
                pageNumber,
                PAGE_SIZE,
                Sort.by(sortBy).descending());

        return productRepository.findAll(pageable).getContent(); // return List
    }

    @GetMapping(path = "/method5")
    public List<ProductEntity> getAllProducts4(
            @RequestParam(defaultValue = "title") String title,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

//        Pageable pageable = PageRequest.of(
//                pageNumber,
//                PAGE_SIZE,
//                Sort.by(sortBy));

//        http://localhost:8080/products/method4?sortBy=quantity&pageNumber=0&title=pep
        return productRepository.findByTitleContainingIgnoreCase(
                title, // find by title
                PageRequest.of(
                        pageNumber,
                        PAGE_SIZE,
//                        Sort.by(sortBy).descending()// sort method
//                        Sort.by(Sort.Direction.DESC, sortBy)
                        Sort.by(Sort.Order.desc(sortBy))
                )
        );

    }
}
