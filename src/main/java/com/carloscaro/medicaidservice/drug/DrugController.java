package com.carloscaro.medicaidservice.drug;

import com.carloscaro.medicaidservice.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DrugController {
    private final DrugService drugService;

    @GetMapping("/drugs/{id}")
    public ResponseEntity<DrugResponse> getDrug(@PathVariable String id) {
        Drug drug = drugService.find(id).orElseThrow(() -> new NotFoundException("Drug not found"));
        DrugResponse response = new DrugResponse(drug.getId(), drug.getName());
        return ResponseEntity.ok(response);
    }
}
