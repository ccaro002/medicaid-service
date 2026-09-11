package com.carloscaro.medicaidservice.drug;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, String> {
}
