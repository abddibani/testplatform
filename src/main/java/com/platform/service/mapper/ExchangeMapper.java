package com.platform.service.mapper;

import com.platform.domain.*;
import com.platform.service.dto.ExchangeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exchange} and its DTO {@link ExchangeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface ExchangeMapper extends EntityMapper<ExchangeDTO, Exchange> {
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    ExchangeDTO toDto(Exchange s);
}
