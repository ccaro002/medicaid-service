package com.carloscaro.medicaidservice.claim;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "claim")
public class Claim {
    @Id
    private String id;

    @Column(name = "prescription_id", nullable = false, unique = true)
    private String prescriptionId;

    @Column(nullable = false)
    private String status;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;
}
