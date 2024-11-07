package com.springboot.coding.testingApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uc_email", columnNames = "email"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(unique = true)
    private String email;

    private String name;

    private Long salary;

}
