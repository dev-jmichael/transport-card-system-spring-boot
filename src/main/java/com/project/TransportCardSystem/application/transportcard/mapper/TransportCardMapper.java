package com.project.TransportCardSystem.application.transportcard.mapper;

import com.project.TransportCardSystem.application.transportcard.dto.TransportCardResponse;
import com.project.TransportCardSystem.domain.entities.TransportCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransportCardMapper {
    TransportCardResponse toDTO(TransportCard entity);
    TransportCard toEntity(TransportCardResponse dto);
}

