package com.carloscaro.medicaidservice.claim;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClaimRequest(
        @NotBlank String prescriptionId,
        @NotNull LocalDate serviceDate
        ) {}
