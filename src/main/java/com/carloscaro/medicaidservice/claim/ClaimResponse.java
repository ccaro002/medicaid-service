package com.carloscaro.medicaidservice.claim;

import java.time.LocalDate;

public record ClaimResponse(String id, String prescriptionId, String status, LocalDate serviceDate) {

    public ClaimResponse(Claim claim) {
        this(
                claim.getId(),
                claim.getPrescriptionId(),
                claim.getStatus(),
                claim.getServiceDate()
        );
    }
}
