package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.OrderQueue;
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import com.mycompany.myapp.domain.enumeration.WorkerProfileStatus;
import com.mycompany.myapp.repository.WorkerProfileRepository;
import com.mycompany.myapp.service.WorkerProfileService;
import com.mycompany.myapp.service.criteria.WorkerProfileCriteria;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkerProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkerProfileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WorkerProfileStatus DEFAULT_STATUS = WorkerProfileStatus.ACTIVE;
    private static final WorkerProfileStatus UPDATED_STATUS = WorkerProfileStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/worker-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkerProfileRepository workerProfileRepository;

    @Mock
    private WorkerProfileRepository workerProfileRepositoryMock;

    @Autowired
    private WorkerProfileMapper workerProfileMapper;

    @Mock
    private WorkerProfileService workerProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkerProfileMockMvc;

    private WorkerProfile workerProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfile createEntity(EntityManager em) {
        WorkerProfile workerProfile = new WorkerProfile().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return workerProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfile createUpdatedEntity(EntityManager em) {
        WorkerProfile workerProfile = new WorkerProfile().name(UPDATED_NAME).status(UPDATED_STATUS);
        return workerProfile;
    }

    @BeforeEach
    public void initTest() {
        workerProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkerProfile() throws Exception {
        int databaseSizeBeforeCreate = workerProfileRepository.findAll().size();
        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);
        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWorkerProfileWithExistingId() throws Exception {
        // Create the WorkerProfile with an existing ID
        workerProfile.setId(1L);
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        int databaseSizeBeforeCreate = workerProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileRepository.findAll().size();
        // set the field null
        workerProfile.setName(null);

        // Create the WorkerProfile, which fails.
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileRepository.findAll().size();
        // set the field null
        workerProfile.setStatus(null);

        // Create the WorkerProfile, which fails.
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkerProfiles() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkerProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(workerProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workerProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkerProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workerProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workerProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get the workerProfile
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, workerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workerProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getWorkerProfilesByIdFiltering() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        Long id = workerProfile.getId();

        defaultWorkerProfileShouldBeFound("id.equals=" + id);
        defaultWorkerProfileShouldNotBeFound("id.notEquals=" + id);

        defaultWorkerProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkerProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkerProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkerProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where name equals to DEFAULT_NAME
        defaultWorkerProfileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the workerProfileList where name equals to UPDATED_NAME
        defaultWorkerProfileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWorkerProfileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the workerProfileList where name equals to UPDATED_NAME
        defaultWorkerProfileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where name is not null
        defaultWorkerProfileShouldBeFound("name.specified=true");

        // Get all the workerProfileList where name is null
        defaultWorkerProfileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByNameContainsSomething() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where name contains DEFAULT_NAME
        defaultWorkerProfileShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the workerProfileList where name contains UPDATED_NAME
        defaultWorkerProfileShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where name does not contain DEFAULT_NAME
        defaultWorkerProfileShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the workerProfileList where name does not contain UPDATED_NAME
        defaultWorkerProfileShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where status equals to DEFAULT_STATUS
        defaultWorkerProfileShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the workerProfileList where status equals to UPDATED_STATUS
        defaultWorkerProfileShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWorkerProfileShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the workerProfileList where status equals to UPDATED_STATUS
        defaultWorkerProfileShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        // Get all the workerProfileList where status is not null
        defaultWorkerProfileShouldBeFound("status.specified=true");

        // Get all the workerProfileList where status is null
        defaultWorkerProfileShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByAttencionChannelIsEqualToSomething() throws Exception {
        WorkerProfileAttencionChannel attencionChannel;
        if (TestUtil.findAll(em, WorkerProfileAttencionChannel.class).isEmpty()) {
            workerProfileRepository.saveAndFlush(workerProfile);
            attencionChannel = WorkerProfileAttencionChannelResourceIT.createEntity(em);
        } else {
            attencionChannel = TestUtil.findAll(em, WorkerProfileAttencionChannel.class).get(0);
        }
        em.persist(attencionChannel);
        em.flush();
        workerProfile.setAttencionChannel(attencionChannel);
        workerProfileRepository.saveAndFlush(workerProfile);
        Long attencionChannelId = attencionChannel.getId();
        // Get all the workerProfileList where attencionChannel equals to attencionChannelId
        defaultWorkerProfileShouldBeFound("attencionChannelId.equals=" + attencionChannelId);

        // Get all the workerProfileList where attencionChannel equals to (attencionChannelId + 1)
        defaultWorkerProfileShouldNotBeFound("attencionChannelId.equals=" + (attencionChannelId + 1));
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByCallableQueueIsEqualToSomething() throws Exception {
        OrderQueue callableQueue;
        if (TestUtil.findAll(em, OrderQueue.class).isEmpty()) {
            workerProfileRepository.saveAndFlush(workerProfile);
            callableQueue = OrderQueueResourceIT.createEntity(em);
        } else {
            callableQueue = TestUtil.findAll(em, OrderQueue.class).get(0);
        }
        em.persist(callableQueue);
        em.flush();
        workerProfile.addCallableQueue(callableQueue);
        workerProfileRepository.saveAndFlush(workerProfile);
        Long callableQueueId = callableQueue.getId();
        // Get all the workerProfileList where callableQueue equals to callableQueueId
        defaultWorkerProfileShouldBeFound("callableQueueId.equals=" + callableQueueId);

        // Get all the workerProfileList where callableQueue equals to (callableQueueId + 1)
        defaultWorkerProfileShouldNotBeFound("callableQueueId.equals=" + (callableQueueId + 1));
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            workerProfileRepository.saveAndFlush(workerProfile);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        workerProfile.setCompany(company);
        workerProfileRepository.saveAndFlush(workerProfile);
        Long companyId = company.getId();
        // Get all the workerProfileList where company equals to companyId
        defaultWorkerProfileShouldBeFound("companyId.equals=" + companyId);

        // Get all the workerProfileList where company equals to (companyId + 1)
        defaultWorkerProfileShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllWorkerProfilesByBranchesIsEqualToSomething() throws Exception {
        Branch branches;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            workerProfileRepository.saveAndFlush(workerProfile);
            branches = BranchResourceIT.createEntity(em);
        } else {
            branches = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branches);
        em.flush();
        workerProfile.addBranches(branches);
        workerProfileRepository.saveAndFlush(workerProfile);
        Long branchesId = branches.getId();
        // Get all the workerProfileList where branches equals to branchesId
        defaultWorkerProfileShouldBeFound("branchesId.equals=" + branchesId);

        // Get all the workerProfileList where branches equals to (branchesId + 1)
        defaultWorkerProfileShouldNotBeFound("branchesId.equals=" + (branchesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkerProfileShouldBeFound(String filter) throws Exception {
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkerProfileShouldNotBeFound(String filter) throws Exception {
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkerProfile() throws Exception {
        // Get the workerProfile
        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile
        WorkerProfile updatedWorkerProfile = workerProfileRepository.findById(workerProfile.getId()).get();
        // Disconnect from session so that the updates on updatedWorkerProfile are not directly saved in db
        em.detach(updatedWorkerProfile);
        updatedWorkerProfile.name(UPDATED_NAME).status(UPDATED_STATUS);
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(updatedWorkerProfile);

        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkerProfileWithPatch() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile using partial update
        WorkerProfile partialUpdatedWorkerProfile = new WorkerProfile();
        partialUpdatedWorkerProfile.setId(workerProfile.getId());

        partialUpdatedWorkerProfile.status(UPDATED_STATUS);

        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfile))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateWorkerProfileWithPatch() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile using partial update
        WorkerProfile partialUpdatedWorkerProfile = new WorkerProfile();
        partialUpdatedWorkerProfile.setId(workerProfile.getId());

        partialUpdatedWorkerProfile.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfile))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.saveAndFlush(workerProfile);

        int databaseSizeBeforeDelete = workerProfileRepository.findAll().size();

        // Delete the workerProfile
        restWorkerProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, workerProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
