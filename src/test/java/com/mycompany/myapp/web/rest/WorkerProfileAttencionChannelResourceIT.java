package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import com.mycompany.myapp.domain.enumeration.WorkerProfileAttencionChannelType;
import com.mycompany.myapp.repository.WorkerProfileAttencionChannelRepository;
import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileAttencionChannelMapper;
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
 * Integration tests for the {@link WorkerProfileAttencionChannelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkerProfileAttencionChannelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WorkerProfileAttencionChannelType DEFAULT_TYPE = WorkerProfileAttencionChannelType.VIRTUAL;
    private static final WorkerProfileAttencionChannelType UPDATED_TYPE = WorkerProfileAttencionChannelType.PRESENTIAL;

    private static final String ENTITY_API_URL = "/api/worker-profile-attencion-channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkerProfileAttencionChannelRepository workerProfileAttencionChannelRepository;

    @Autowired
    private WorkerProfileAttencionChannelMapper workerProfileAttencionChannelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkerProfileAttencionChannelMockMvc;

    private WorkerProfileAttencionChannel workerProfileAttencionChannel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfileAttencionChannel createEntity(EntityManager em) {
        WorkerProfileAttencionChannel workerProfileAttencionChannel = new WorkerProfileAttencionChannel()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return workerProfileAttencionChannel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfileAttencionChannel createUpdatedEntity(EntityManager em) {
        WorkerProfileAttencionChannel workerProfileAttencionChannel = new WorkerProfileAttencionChannel()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        return workerProfileAttencionChannel;
    }

    @BeforeEach
    public void initTest() {
        workerProfileAttencionChannel = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeCreate = workerProfileAttencionChannelRepository.findAll().size();
        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeCreate + 1);
        WorkerProfileAttencionChannel testWorkerProfileAttencionChannel = workerProfileAttencionChannelList.get(
            workerProfileAttencionChannelList.size() - 1
        );
        assertThat(testWorkerProfileAttencionChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfileAttencionChannel.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWorkerProfileAttencionChannelWithExistingId() throws Exception {
        // Create the WorkerProfileAttencionChannel with an existing ID
        workerProfileAttencionChannel.setId(1L);
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        int databaseSizeBeforeCreate = workerProfileAttencionChannelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileAttencionChannelRepository.findAll().size();
        // set the field null
        workerProfileAttencionChannel.setName(null);

        // Create the WorkerProfileAttencionChannel, which fails.
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        restWorkerProfileAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileAttencionChannelRepository.findAll().size();
        // set the field null
        workerProfileAttencionChannel.setType(null);

        // Create the WorkerProfileAttencionChannel, which fails.
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        restWorkerProfileAttencionChannelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkerProfileAttencionChannels() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        // Get all the workerProfileAttencionChannelList
        restWorkerProfileAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workerProfileAttencionChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getWorkerProfileAttencionChannel() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        // Get the workerProfileAttencionChannel
        restWorkerProfileAttencionChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, workerProfileAttencionChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workerProfileAttencionChannel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkerProfileAttencionChannel() throws Exception {
        // Get the workerProfileAttencionChannel
        restWorkerProfileAttencionChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkerProfileAttencionChannel() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();

        // Update the workerProfileAttencionChannel
        WorkerProfileAttencionChannel updatedWorkerProfileAttencionChannel = workerProfileAttencionChannelRepository
            .findById(workerProfileAttencionChannel.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkerProfileAttencionChannel are not directly saved in db
        em.detach(updatedWorkerProfileAttencionChannel);
        updatedWorkerProfileAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            updatedWorkerProfileAttencionChannel
        );

        restWorkerProfileAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileAttencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfileAttencionChannel testWorkerProfileAttencionChannel = workerProfileAttencionChannelList.get(
            workerProfileAttencionChannelList.size() - 1
        );
        assertThat(testWorkerProfileAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfileAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileAttencionChannelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkerProfileAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();

        // Update the workerProfileAttencionChannel using partial update
        WorkerProfileAttencionChannel partialUpdatedWorkerProfileAttencionChannel = new WorkerProfileAttencionChannel();
        partialUpdatedWorkerProfileAttencionChannel.setId(workerProfileAttencionChannel.getId());

        partialUpdatedWorkerProfileAttencionChannel.type(UPDATED_TYPE);

        restWorkerProfileAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfileAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfileAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfileAttencionChannel testWorkerProfileAttencionChannel = workerProfileAttencionChannelList.get(
            workerProfileAttencionChannelList.size() - 1
        );
        assertThat(testWorkerProfileAttencionChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfileAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkerProfileAttencionChannelWithPatch() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();

        // Update the workerProfileAttencionChannel using partial update
        WorkerProfileAttencionChannel partialUpdatedWorkerProfileAttencionChannel = new WorkerProfileAttencionChannel();
        partialUpdatedWorkerProfileAttencionChannel.setId(workerProfileAttencionChannel.getId());

        partialUpdatedWorkerProfileAttencionChannel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restWorkerProfileAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfileAttencionChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfileAttencionChannel))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfileAttencionChannel testWorkerProfileAttencionChannel = workerProfileAttencionChannelList.get(
            workerProfileAttencionChannelList.size() - 1
        );
        assertThat(testWorkerProfileAttencionChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfileAttencionChannel.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workerProfileAttencionChannelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkerProfileAttencionChannel() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileAttencionChannelRepository.findAll().size();
        workerProfileAttencionChannel.setId(count.incrementAndGet());

        // Create the WorkerProfileAttencionChannel
        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = workerProfileAttencionChannelMapper.toDto(
            workerProfileAttencionChannel
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileAttencionChannelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileAttencionChannelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfileAttencionChannel in the database
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkerProfileAttencionChannel() throws Exception {
        // Initialize the database
        workerProfileAttencionChannelRepository.saveAndFlush(workerProfileAttencionChannel);

        int databaseSizeBeforeDelete = workerProfileAttencionChannelRepository.findAll().size();

        // Delete the workerProfileAttencionChannel
        restWorkerProfileAttencionChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, workerProfileAttencionChannel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkerProfileAttencionChannel> workerProfileAttencionChannelList = workerProfileAttencionChannelRepository.findAll();
        assertThat(workerProfileAttencionChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
