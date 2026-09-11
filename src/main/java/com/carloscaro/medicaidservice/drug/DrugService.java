package com.carloscaro.medicaidservice.drug;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrugService {
    private final DrugRepository repo;
    private final EntityManager em;

    @Transactional
    public Drug create(final String id, final String name) {
        Drug d = new Drug(id, name);
        em.persist(d);
        return d;
    }
    public Optional<Drug> find(final String id) { return repo.findById(id); }
}

