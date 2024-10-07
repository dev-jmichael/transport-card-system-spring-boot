package com.project.TransportCardSystem.infrastructure.repositories;

import com.project.TransportCardSystem.domain.entities.TransportCard;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportCardRepository extends ListCrudRepository<TransportCard, Long> {
}
