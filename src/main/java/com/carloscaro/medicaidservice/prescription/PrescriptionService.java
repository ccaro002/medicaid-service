package com.carloscaro.medicaidservice.prescription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository repo;

    public Optional<Prescription> find(final String id) { return repo.findById(id); }
}
