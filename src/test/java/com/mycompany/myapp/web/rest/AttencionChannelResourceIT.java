package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AttencionChannel;
import com.mycompany.myapp.domain.enumeration.AttencionChannelType;
import com.mycompany.myapp.repository.AttencionChannelRepository;
import com.mycompany.myapp.service.dto.AttencionChannelDTO;
import com.mycompany.myapp.service.mapper.AttencionChannelMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AttencionChannelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttencionChannelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AttencionChannelType DEFAULT_TYPE = AttencionChannelType.VIRTUAL;
    private static final AttencionChannelType UPDATED_TYPE = AttencionChannelType.PRESENTIAL;

    private static final String ENTITY_API_URL = "/api/attencion-channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttencionChannelRepository attencionChannelRepository;

    @Autowired
    private AttencionChannelMapper attencionChannelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttencionChannelMockMvc;

    private AttencionChannel attencionChannel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttencionChannel createEntity(EntityManager em) {
        AttencionChannel attencionChannel = new AttencionChannel().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return attencionChannel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttencionChannel createUpdatedEntity(EntityManager em) {
        AttencionChannel attencionChannel = new AttencionChannel().name(UPDATED_NAME).type(UPDATED_TYPE);
        return attencionChannel;
    }

    @BeforeEach
    public void initTest() {
        attencionChannel = createEntity(em);
    }

    @Test
    @Transactional
    void createAttencionChannel() throws Exception {
        int databaseSizeBeforeCreate = attencionChannelRepository.findAll().size();
        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);
        restAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeCreate + 1);
        AttencionChannel testAttencionChannel = attencionChannelList.get(attencionChannelList.size() - 1);
        assertThat(testAttencionChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttencionChannel.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createAttencionChannelWithExistingId() throws Exception {
        // Create the AttencionChannel with an existing ID
        attencionChannel.setId(1L);
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        int databaseSizeBeforeCreate = attencionChannelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attencionChannelRepository.findAll().size();
        // set the field null
        attencionChannel.setName(null);

        // Create the AttencionChannel, which fails.
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        restAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attencionChannelRepository.findAll().size();
        // set the field null
        attencionChannel.setType(null);

        // Create the AttencionChannel, which fails.
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        restAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttencionChannels() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        // Get all the attencionChannelList
        restAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attencionChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAttencionChannel() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        // Get the attencionChannel
        restAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, attencionChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attencionChannel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAttencionChannel() throws Exception {
        // Get the attencionChannel
        restAttencionChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttencionChannel() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();

        // Update the attencionChannel
        AttencionChannel updatedAttencionChannel = attencionChannelRepository.findById(attencionChannel.getId()).get();
        // Disconnect from session so that the updates on updatedAttencionChannel are not directly saved in db
        em.detach(updatedAttencionChannel);
        updatedAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(updatedAttencionChannel);

        restAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
        AttencionChannel testAttencionChannel = attencionChannelList.get(attencionChannelList.size() - 1);
        assertThat(testAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();

        // Update the attencionChannel using partial update
        AttencionChannel partialUpdatedAttencionChannel = new AttencionChannel();
        partialUpdatedAttencionChannel.setId(attencionChannel.getId());

        partialUpdatedAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
        AttencionChannel testAttencionChannel = attencionChannelList.get(attencionChannelList.size() - 1);
        assertThat(testAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();

        // Update the attencionChannel using partial update
        AttencionChannel partialUpdatedAttencionChannel = new AttencionChannel();
        partialUpdatedAttencionChannel.setId(attencionChannel.getId());

        partialUpdatedAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
        AttencionChannel testAttencionChannel = attencionChannelList.get(attencionChannelList.size() - 1);
        assertThat(testAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attencionChannelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = attencionChannelRepository.findAll().size();
        attencionChannel.setId(count.incrementAndGet());

        // Create the AttencionChannel
        AttencionChannelDTO attencionChannelDTO = attencionChannelMapper.toDto(attencionChannel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttencionChannel in the database
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttencionChannel() throws Exception {
        // Initialize the database
        attencionChannelRepository.saveAndFlush(attencionChannel);

        int databaseSizeBeforeDelete = attencionChannelRepository.findAll().size();

        // Delete the attencionChannel
        restAttencionChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, attencionChannel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttencionChannel> attencionChannelList = attencionChannelRepository.findAll();
        assertThat(attencionChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
