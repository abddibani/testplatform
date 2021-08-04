package com.platform.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.platform.IntegrationTest;
import com.platform.domain.Tracker;
import com.platform.repository.TrackerRepository;
import com.platform.service.dto.TrackerDTO;
import com.platform.service.mapper.TrackerMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link TrackerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackerResourceIT {

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTIVATION_BEGIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVATION_BEGIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTIVATION_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVATION_END = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/trackers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    private TrackerMapper trackerMapper;

    @Autowired
    private MockMvc restTrackerMockMvc;

    private Tracker tracker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tracker createEntity() {
        Tracker tracker = new Tracker()
            .activated(DEFAULT_ACTIVATED)
            .createAt(DEFAULT_CREATE_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .activationBegin(DEFAULT_ACTIVATION_BEGIN)
            .activationEnd(DEFAULT_ACTIVATION_END);
        return tracker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tracker createUpdatedEntity() {
        Tracker tracker = new Tracker()
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .activationBegin(UPDATED_ACTIVATION_BEGIN)
            .activationEnd(UPDATED_ACTIVATION_END);
        return tracker;
    }

    @BeforeEach
    public void initTest() {
        trackerRepository.deleteAll();
        tracker = createEntity();
    }

    @Test
    void createTracker() throws Exception {
        int databaseSizeBeforeCreate = trackerRepository.findAll().size();
        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);
        restTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackerDTO)))
            .andExpect(status().isCreated());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate + 1);
        Tracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testTracker.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testTracker.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testTracker.getActivationBegin()).isEqualTo(DEFAULT_ACTIVATION_BEGIN);
        assertThat(testTracker.getActivationEnd()).isEqualTo(DEFAULT_ACTIVATION_END);
    }

    @Test
    void createTrackerWithExistingId() throws Exception {
        // Create the Tracker with an existing ID
        tracker.setId("existing_id");
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        int databaseSizeBeforeCreate = trackerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTrackers() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        // Get all the trackerList
        restTrackerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tracker.getId())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].activationBegin").value(hasItem(DEFAULT_ACTIVATION_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].activationEnd").value(hasItem(DEFAULT_ACTIVATION_END.toString())));
    }

    @Test
    void getTracker() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        // Get the tracker
        restTrackerMockMvc
            .perform(get(ENTITY_API_URL_ID, tracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tracker.getId()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()))
            .andExpect(jsonPath("$.activationBegin").value(DEFAULT_ACTIVATION_BEGIN.toString()))
            .andExpect(jsonPath("$.activationEnd").value(DEFAULT_ACTIVATION_END.toString()));
    }

    @Test
    void getNonExistingTracker() throws Exception {
        // Get the tracker
        restTrackerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewTracker() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();

        // Update the tracker
        Tracker updatedTracker = trackerRepository.findById(tracker.getId()).get();
        updatedTracker
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .activationBegin(UPDATED_ACTIVATION_BEGIN)
            .activationEnd(UPDATED_ACTIVATION_END);
        TrackerDTO trackerDTO = trackerMapper.toDto(updatedTracker);

        restTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
        Tracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testTracker.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testTracker.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testTracker.getActivationBegin()).isEqualTo(UPDATED_ACTIVATION_BEGIN);
        assertThat(testTracker.getActivationEnd()).isEqualTo(UPDATED_ACTIVATION_END);
    }

    @Test
    void putNonExistingTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTrackerWithPatch() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();

        // Update the tracker using partial update
        Tracker partialUpdatedTracker = new Tracker();
        partialUpdatedTracker.setId(tracker.getId());

        partialUpdatedTracker.activated(UPDATED_ACTIVATED);

        restTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTracker))
            )
            .andExpect(status().isOk());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
        Tracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testTracker.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testTracker.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testTracker.getActivationBegin()).isEqualTo(DEFAULT_ACTIVATION_BEGIN);
        assertThat(testTracker.getActivationEnd()).isEqualTo(DEFAULT_ACTIVATION_END);
    }

    @Test
    void fullUpdateTrackerWithPatch() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();

        // Update the tracker using partial update
        Tracker partialUpdatedTracker = new Tracker();
        partialUpdatedTracker.setId(tracker.getId());

        partialUpdatedTracker
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .activationBegin(UPDATED_ACTIVATION_BEGIN)
            .activationEnd(UPDATED_ACTIVATION_END);

        restTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTracker))
            )
            .andExpect(status().isOk());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
        Tracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testTracker.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testTracker.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testTracker.getActivationBegin()).isEqualTo(UPDATED_ACTIVATION_BEGIN);
        assertThat(testTracker.getActivationEnd()).isEqualTo(UPDATED_ACTIVATION_END);
    }

    @Test
    void patchNonExistingTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTracker() throws Exception {
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();
        tracker.setId(UUID.randomUUID().toString());

        // Create the Tracker
        TrackerDTO trackerDTO = trackerMapper.toDto(tracker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trackerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tracker in the database
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTracker() throws Exception {
        // Initialize the database
        tracker.setId(UUID.randomUUID().toString());
        trackerRepository.save(tracker);

        int databaseSizeBeforeDelete = trackerRepository.findAll().size();

        // Delete the tracker
        restTrackerMockMvc
            .perform(delete(ENTITY_API_URL_ID, tracker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tracker> trackerList = trackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
