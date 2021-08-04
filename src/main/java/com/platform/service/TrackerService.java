package com.platform.service;

import com.platform.domain.Tracker;
import com.platform.repository.TrackerRepository;
import com.platform.service.dto.TrackerDTO;
import com.platform.service.mapper.TrackerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Tracker}.
 */
@Service
public class TrackerService {

    private final Logger log = LoggerFactory.getLogger(TrackerService.class);

    private final TrackerRepository trackerRepository;

    private final TrackerMapper trackerMapper;

    public TrackerService(TrackerRepository trackerRepository, TrackerMapper trackerMapper) {
        this.trackerRepository = trackerRepository;
        this.trackerMapper = trackerMapper;
    }

    /**
     * Save a tracker.
     *
     * @param trackerDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackerDTO save(TrackerDTO trackerDTO) {
        log.debug("Request to save Tracker : {}", trackerDTO);
        Tracker tracker = trackerMapper.toEntity(trackerDTO);
        tracker = trackerRepository.save(tracker);
        return trackerMapper.toDto(tracker);
    }

    /**
     * Partially update a tracker.
     *
     * @param trackerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrackerDTO> partialUpdate(TrackerDTO trackerDTO) {
        log.debug("Request to partially update Tracker : {}", trackerDTO);

        return trackerRepository
            .findById(trackerDTO.getId())
            .map(
                existingTracker -> {
                    trackerMapper.partialUpdate(existingTracker, trackerDTO);

                    return existingTracker;
                }
            )
            .map(trackerRepository::save)
            .map(trackerMapper::toDto);
    }

    /**
     * Get all the trackers.
     *
     * @return the list of entities.
     */
    public List<TrackerDTO> findAll() {
        log.debug("Request to get all Trackers");
        return trackerRepository.findAll().stream().map(trackerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tracker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TrackerDTO> findOne(String id) {
        log.debug("Request to get Tracker : {}", id);
        return trackerRepository.findById(id).map(trackerMapper::toDto);
    }

    /**
     * Delete the tracker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Tracker : {}", id);
        trackerRepository.deleteById(id);
    }
}
