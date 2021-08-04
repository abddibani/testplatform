package com.platform.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.platform.IntegrationTest;
import com.platform.domain.Exchange;
import com.platform.repository.ExchangeRepository;
import com.platform.service.dto.ExchangeDTO;
import com.platform.service.mapper.ExchangeMapper;
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
 * Integration tests for the {@link ExchangeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExchangeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APIKEY = "AAAAAAAAAA";
    private static final String UPDATED_APIKEY = "BBBBBBBBBB";

    private static final String DEFAULT_SECRIT = "AAAAAAAAAA";
    private static final String UPDATED_SECRIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/exchanges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private MockMvc restExchangeMockMvc;

    private Exchange exchange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exchange createEntity() {
        Exchange exchange = new Exchange()
            .name(DEFAULT_NAME)
            .apikey(DEFAULT_APIKEY)
            .secrit(DEFAULT_SECRIT)
            .activated(DEFAULT_ACTIVATED)
            .createAt(DEFAULT_CREATE_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT);
        return exchange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exchange createUpdatedEntity() {
        Exchange exchange = new Exchange()
            .name(UPDATED_NAME)
            .apikey(UPDATED_APIKEY)
            .secrit(UPDATED_SECRIT)
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);
        return exchange;
    }

    @BeforeEach
    public void initTest() {
        exchangeRepository.deleteAll();
        exchange = createEntity();
    }

    @Test
    void createExchange() throws Exception {
        int databaseSizeBeforeCreate = exchangeRepository.findAll().size();
        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);
        restExchangeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isCreated());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate + 1);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExchange.getApikey()).isEqualTo(DEFAULT_APIKEY);
        assertThat(testExchange.getSecrit()).isEqualTo(DEFAULT_SECRIT);
        assertThat(testExchange.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testExchange.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testExchange.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    void createExchangeWithExistingId() throws Exception {
        // Create the Exchange with an existing ID
        exchange.setId("existing_id");
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        int databaseSizeBeforeCreate = exchangeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExchangeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExchanges() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        // Get all the exchangeList
        restExchangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchange.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].apikey").value(hasItem(DEFAULT_APIKEY)))
            .andExpect(jsonPath("$.[*].secrit").value(hasItem(DEFAULT_SECRIT)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())));
    }

    @Test
    void getExchange() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        // Get the exchange
        restExchangeMockMvc
            .perform(get(ENTITY_API_URL_ID, exchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exchange.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.apikey").value(DEFAULT_APIKEY))
            .andExpect(jsonPath("$.secrit").value(DEFAULT_SECRIT))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()));
    }

    @Test
    void getNonExistingExchange() throws Exception {
        // Get the exchange
        restExchangeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewExchange() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();

        // Update the exchange
        Exchange updatedExchange = exchangeRepository.findById(exchange.getId()).get();
        updatedExchange
            .name(UPDATED_NAME)
            .apikey(UPDATED_APIKEY)
            .secrit(UPDATED_SECRIT)
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(updatedExchange);

        restExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExchange.getApikey()).isEqualTo(UPDATED_APIKEY);
        assertThat(testExchange.getSecrit()).isEqualTo(UPDATED_SECRIT);
        assertThat(testExchange.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testExchange.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testExchange.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    void putNonExistingExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExchangeWithPatch() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();

        // Update the exchange using partial update
        Exchange partialUpdatedExchange = new Exchange();
        partialUpdatedExchange.setId(exchange.getId());

        partialUpdatedExchange.apikey(UPDATED_APIKEY).activated(UPDATED_ACTIVATED).modifiedAt(UPDATED_MODIFIED_AT);

        restExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchange))
            )
            .andExpect(status().isOk());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExchange.getApikey()).isEqualTo(UPDATED_APIKEY);
        assertThat(testExchange.getSecrit()).isEqualTo(DEFAULT_SECRIT);
        assertThat(testExchange.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testExchange.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testExchange.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    void fullUpdateExchangeWithPatch() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();

        // Update the exchange using partial update
        Exchange partialUpdatedExchange = new Exchange();
        partialUpdatedExchange.setId(exchange.getId());

        partialUpdatedExchange
            .name(UPDATED_NAME)
            .apikey(UPDATED_APIKEY)
            .secrit(UPDATED_SECRIT)
            .activated(UPDATED_ACTIVATED)
            .createAt(UPDATED_CREATE_AT)
            .modifiedAt(UPDATED_MODIFIED_AT);

        restExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchange))
            )
            .andExpect(status().isOk());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
        Exchange testExchange = exchangeList.get(exchangeList.size() - 1);
        assertThat(testExchange.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExchange.getApikey()).isEqualTo(UPDATED_APIKEY);
        assertThat(testExchange.getSecrit()).isEqualTo(UPDATED_SECRIT);
        assertThat(testExchange.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testExchange.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testExchange.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    void patchNonExistingExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exchangeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExchange() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRepository.findAll().size();
        exchange.setId(UUID.randomUUID().toString());

        // Create the Exchange
        ExchangeDTO exchangeDTO = exchangeMapper.toDto(exchange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exchangeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exchange in the database
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExchange() throws Exception {
        // Initialize the database
        exchange.setId(UUID.randomUUID().toString());
        exchangeRepository.save(exchange);

        int databaseSizeBeforeDelete = exchangeRepository.findAll().size();

        // Delete the exchange
        restExchangeMockMvc
            .perform(delete(ENTITY_API_URL_ID, exchange.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exchange> exchangeList = exchangeRepository.findAll();
        assertThat(exchangeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
