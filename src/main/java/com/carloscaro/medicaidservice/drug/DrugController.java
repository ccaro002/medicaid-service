package com.carloscaro.medicaidservice.drug;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @GetMapping("/drugs/{id}")
    public DrugResponse getDrug(@PathVariable String id) {
        Drug drug = drugService.find(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Drug not found"));
        return new DrugResponse(drug.getId(), drug.getName());
    }
}
