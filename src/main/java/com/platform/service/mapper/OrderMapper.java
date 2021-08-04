package com.platform.service.mapper;

import com.platform.domain.*;
import com.platform.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    OrderDTO toDto(Order s);
}
