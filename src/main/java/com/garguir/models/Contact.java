package com.garguir.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Contact {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private boolean dataProtection;

}
