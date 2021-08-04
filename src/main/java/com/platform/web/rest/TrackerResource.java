package com.platform.web.rest;

import com.platform.repository.TrackerRepository;
import com.platform.service.TrackerService;
import com.platform.service.dto.TrackerDTO;
import com.platform.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.platform.domain.Tracker}.
 */
@RestController
@RequestMapping("/api")
public class TrackerResource {

    private final Logger log = LoggerFactory.getLogger(TrackerResource.class);

    private static final String ENTITY_NAME = "tracker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackerService trackerService;

    private final TrackerRepository trackerRepository;

    public TrackerResource(TrackerService trackerService, TrackerRepository trackerRepository) {
        this.trackerService = trackerService;
        this.trackerRepository = trackerRepository;
    }

    /**
     * {@code POST  /trackers} : Create a new tracker.
     *
     * @param trackerDTO the trackerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackerDTO, or with status {@code 400 (Bad Request)} if the tracker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trackers")
    public ResponseEntity<TrackerDTO> createTracker(@RequestBody TrackerDTO trackerDTO) throws URISyntaxException {
        log.debug("REST request to save Tracker : {}", trackerDTO);
        if (trackerDTO.getId() != null) {
            throw new BadRequestAlertException("A new tracker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackerDTO result = trackerService.save(trackerDTO);
        return ResponseEntity
            .created(new URI("/api/trackers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /trackers/:id} : Updates an existing tracker.
     *
     * @param id the id of the trackerDTO to save.
     * @param trackerDTO the trackerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackerDTO,
     * or with status {@code 400 (Bad Request)} if the trackerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trackers/{id}")
    public ResponseEntity<TrackerDTO> updateTracker(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TrackerDTO trackerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tracker : {}, {}", id, trackerDTO);
        if (trackerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackerDTO result = trackerService.save(trackerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackerDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /trackers/:id} : Partial updates given fields of an existing tracker, field will ignore if it is null
     *
     * @param id the id of the trackerDTO to save.
     * @param trackerDTO the trackerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackerDTO,
     * or with status {@code 400 (Bad Request)} if the trackerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trackerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trackers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TrackerDTO> partialUpdateTracker(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TrackerDTO trackerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tracker partially : {}, {}", id, trackerDTO);
        if (trackerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackerDTO> result = trackerService.partialUpdate(trackerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackerDTO.getId())
        );
    }

    /**
     * {@code GET  /trackers} : get all the trackers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackers in body.
     */
    @GetMapping("/trackers")
    public List<TrackerDTO> getAllTrackers() {
        log.debug("REST request to get all Trackers");
        return trackerService.findAll();
    }

    /**
     * {@code GET  /trackers/:id} : get the "id" tracker.
     *
     * @param id the id of the trackerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trackers/{id}")
    public ResponseEntity<TrackerDTO> getTracker(@PathVariable String id) {
        log.debug("REST request to get Tracker : {}", id);
        Optional<TrackerDTO> trackerDTO = trackerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trackerDTO);
    }

    /**
     * {@code DELETE  /trackers/:id} : delete the "id" tracker.
     *
     * @param id the id of the trackerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trackers/{id}")
    public ResponseEntity<Void> deleteTracker(@PathVariable String id) {
        log.debug("REST request to delete Tracker : {}", id);
        trackerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
