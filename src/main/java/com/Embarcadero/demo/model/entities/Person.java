package com.Embarcadero.demo.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , unique = true)
    private String dni;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    private String phone;
    private String emergencyPhone;
    private String address;
    private String notes;

}


