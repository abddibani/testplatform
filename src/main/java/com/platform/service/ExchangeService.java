package com.platform.service;

import com.platform.domain.Exchange;
import com.platform.repository.ExchangeRepository;
import com.platform.service.dto.ExchangeDTO;
import com.platform.service.mapper.ExchangeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Exchange}.
 */
@Service
public class ExchangeService {

    private final Logger log = LoggerFactory.getLogger(ExchangeService.class);

    private final ExchangeRepository exchangeRepository;

    private final ExchangeMapper exchangeMapper;

    public ExchangeService(ExchangeRepository exchangeRepository, ExchangeMapper exchangeMapper) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeMapper = exchangeMapper;
    }

    /**
     * Save a exchange.
     *
     * @param exchangeDTO the entity to save.
     * @return the persisted entity.
     */
    public ExchangeDTO save(ExchangeDTO exchangeDTO) {
        log.debug("Request to save Exchange : {}", exchangeDTO);
        Exchange exchange = exchangeMapper.toEntity(exchangeDTO);
        exchange = exchangeRepository.save(exchange);
        return exchangeMapper.toDto(exchange);
    }

    /**
     * Partially update a exchange.
     *
     * @param exchangeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExchangeDTO> partialUpdate(ExchangeDTO exchangeDTO) {
        log.debug("Request to partially update Exchange : {}", exchangeDTO);

        return exchangeRepository
            .findById(exchangeDTO.getId())
            .map(
                existingExchange -> {
                    exchangeMapper.partialUpdate(existingExchange, exchangeDTO);

                    return existingExchange;
                }
            )
            .map(exchangeRepository::save)
            .map(exchangeMapper::toDto);
    }

    /**
     * Get all the exchanges.
     *
     * @return the list of entities.
     */
    public List<ExchangeDTO> findAll() {
        log.debug("Request to get all Exchanges");
        return exchangeRepository.findAll().stream().map(exchangeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one exchange by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ExchangeDTO> findOne(String id) {
        log.debug("Request to get Exchange : {}", id);
        return exchangeRepository.findById(id).map(exchangeMapper::toDto);
    }

    /**
     * Delete the exchange by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Exchange : {}", id);
        exchangeRepository.deleteById(id);
    }
}
