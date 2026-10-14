package com.carloscaro.medicaidservice.prescription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "prescription")
public class Prescription {
    @Id
    private String id;

    @Column(name = "drug_id", nullable = false)
    private String drugId;

    private int quantity;
}
