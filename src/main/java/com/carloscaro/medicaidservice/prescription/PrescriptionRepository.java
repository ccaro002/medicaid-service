package com.carloscaro.medicaidservice.prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository  extends JpaRepository<Prescription, String> {
}
