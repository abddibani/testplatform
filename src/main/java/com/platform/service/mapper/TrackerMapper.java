package com.platform.service.mapper;

import com.platform.domain.*;
import com.platform.service.dto.TrackerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tracker} and its DTO {@link TrackerDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface TrackerMapper extends EntityMapper<TrackerDTO, Tracker> {
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    TrackerDTO toDto(Tracker s);
}
