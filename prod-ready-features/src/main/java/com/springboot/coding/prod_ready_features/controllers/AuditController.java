package com.springboot.coding.prod_ready_features.controllers;

import com.springboot.coding.prod_ready_features.entities.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


// This is only for admin, so we can return an Entity object
@RestController
@RequestMapping(path = "/audit")
@RequiredArgsConstructor
public class AuditController {

//    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @GetMapping(path = "/posts/{postId}")
    List<PostEntity> getPostRevisions(@PathVariable Long postId) {
//        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditReader reader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());

        List<Number> revisions = reader.getRevisions(PostEntity.class, postId);
//        System.out.println(revisions);
        return revisions
                .stream()
                .map(revisionNumber -> reader.find(PostEntity.class, postId, revisionNumber))
                .collect(Collectors.toList());

    }
}
