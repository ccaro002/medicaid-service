package com.carloscaro.medicaidservice.claim;

import com.carloscaro.medicaidservice.common.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ClaimController {
    private final ClaimService claimService;

    @GetMapping("/claims/{id}")
    public ResponseEntity<ClaimResponse> getClaim(@PathVariable String id) {
        Claim claim = claimService.find(id).orElseThrow(() -> new NotFoundException("Claim not found"));
        ClaimResponse response = new ClaimResponse(claim);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/claims")
    public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody ClaimRequest request) {
        Claim claim = claimService.create(request.prescriptionId(), request.serviceDate());
        ClaimResponse response = new ClaimResponse(claim);
        URI location = URI.create("/claims/" + response.id());
        return ResponseEntity.created(location).body(response);
    }
}
