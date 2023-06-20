package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import com.mycompany.myapp.domain.enumeration.WaitingRoomAttencionChannelType;
import com.mycompany.myapp.repository.WaitingRoomAttencionChannelRepository;
import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomAttencionChannelMapper;
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
 * Integration tests for the {@link WaitingRoomAttencionChannelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaitingRoomAttencionChannelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WaitingRoomAttencionChannelType DEFAULT_TYPE = WaitingRoomAttencionChannelType.VIRTUAL;
    private static final WaitingRoomAttencionChannelType UPDATED_TYPE = WaitingRoomAttencionChannelType.PRESENTIAL;

    private static final String ENTITY_API_URL = "/api/waiting-room-attencion-channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WaitingRoomAttencionChannelRepository waitingRoomAttencionChannelRepository;

    @Autowired
    private WaitingRoomAttencionChannelMapper waitingRoomAttencionChannelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaitingRoomAttencionChannelMockMvc;

    private WaitingRoomAttencionChannel waitingRoomAttencionChannel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoomAttencionChannel createEntity(EntityManager em) {
        WaitingRoomAttencionChannel waitingRoomAttencionChannel = new WaitingRoomAttencionChannel().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return waitingRoomAttencionChannel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoomAttencionChannel createUpdatedEntity(EntityManager em) {
        WaitingRoomAttencionChannel waitingRoomAttencionChannel = new WaitingRoomAttencionChannel().name(UPDATED_NAME).type(UPDATED_TYPE);
        return waitingRoomAttencionChannel;
    }

    @BeforeEach
    public void initTest() {
        waitingRoomAttencionChannel = createEntity(em);
    }

    @Test
    @Transactional
    void createWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeCreate = waitingRoomAttencionChannelRepository.findAll().size();
        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeCreate + 1);
        WaitingRoomAttencionChannel testWaitingRoomAttencionChannel = waitingRoomAttencionChannelList.get(
            waitingRoomAttencionChannelList.size() - 1
        );
        assertThat(testWaitingRoomAttencionChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaitingRoomAttencionChannel.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWaitingRoomAttencionChannelWithExistingId() throws Exception {
        // Create the WaitingRoomAttencionChannel with an existing ID
        waitingRoomAttencionChannel.setId(1L);
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        int databaseSizeBeforeCreate = waitingRoomAttencionChannelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomAttencionChannelRepository.findAll().size();
        // set the field null
        waitingRoomAttencionChannel.setName(null);

        // Create the WaitingRoomAttencionChannel, which fails.
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        restWaitingRoomAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomAttencionChannelRepository.findAll().size();
        // set the field null
        waitingRoomAttencionChannel.setType(null);

        // Create the WaitingRoomAttencionChannel, which fails.
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        restWaitingRoomAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaitingRoomAttencionChannels() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        // Get all the waitingRoomAttencionChannelList
        restWaitingRoomAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waitingRoomAttencionChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getWaitingRoomAttencionChannel() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        // Get the waitingRoomAttencionChannel
        restWaitingRoomAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, waitingRoomAttencionChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waitingRoomAttencionChannel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWaitingRoomAttencionChannel() throws Exception {
        // Get the waitingRoomAttencionChannel
        restWaitingRoomAttencionChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaitingRoomAttencionChannel() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();

        // Update the waitingRoomAttencionChannel
        WaitingRoomAttencionChannel updatedWaitingRoomAttencionChannel = waitingRoomAttencionChannelRepository
            .findById(waitingRoomAttencionChannel.getId())
            .get();
        // Disconnect from session so that the updates on updatedWaitingRoomAttencionChannel are not directly saved in db
        em.detach(updatedWaitingRoomAttencionChannel);
        updatedWaitingRoomAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            updatedWaitingRoomAttencionChannel
        );

        restWaitingRoomAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomAttencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoomAttencionChannel testWaitingRoomAttencionChannel = waitingRoomAttencionChannelList.get(
            waitingRoomAttencionChannelList.size() - 1
        );
        assertThat(testWaitingRoomAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoomAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomAttencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaitingRoomAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();

        // Update the waitingRoomAttencionChannel using partial update
        WaitingRoomAttencionChannel partialUpdatedWaitingRoomAttencionChannel = new WaitingRoomAttencionChannel();
        partialUpdatedWaitingRoomAttencionChannel.setId(waitingRoomAttencionChannel.getId());

        partialUpdatedWaitingRoomAttencionChannel.type(UPDATED_TYPE);

        restWaitingRoomAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoomAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoomAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoomAttencionChannel testWaitingRoomAttencionChannel = waitingRoomAttencionChannelList.get(
            waitingRoomAttencionChannelList.size() - 1
        );
        assertThat(testWaitingRoomAttencionChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaitingRoomAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWaitingRoomAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();

        // Update the waitingRoomAttencionChannel using partial update
        WaitingRoomAttencionChannel partialUpdatedWaitingRoomAttencionChannel = new WaitingRoomAttencionChannel();
        partialUpdatedWaitingRoomAttencionChannel.setId(waitingRoomAttencionChannel.getId());

        partialUpdatedWaitingRoomAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restWaitingRoomAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoomAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoomAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoomAttencionChannel testWaitingRoomAttencionChannel = waitingRoomAttencionChannelList.get(
            waitingRoomAttencionChannelList.size() - 1
        );
        assertThat(testWaitingRoomAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoomAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waitingRoomAttencionChannelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaitingRoomAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomAttencionChannelRepository.findAll().size();
        waitingRoomAttencionChannel.setId(count.incrementAndGet());

        // Create the WaitingRoomAttencionChannel
        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelMapper.toDto(
            waitingRoomAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomAttencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoomAttencionChannel in the database
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaitingRoomAttencionChannel() throws Exception {
        // Initialize the database
        waitingRoomAttencionChannelRepository.saveAndFlush(waitingRoomAttencionChannel);

        int databaseSizeBeforeDelete = waitingRoomAttencionChannelRepository.findAll().size();

        // Delete the waitingRoomAttencionChannel
        restWaitingRoomAttencionChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, waitingRoomAttencionChannel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WaitingRoomAttencionChannel> waitingRoomAttencionChannelList = waitingRoomAttencionChannelRepository.findAll();
        assertThat(waitingRoomAttencionChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
