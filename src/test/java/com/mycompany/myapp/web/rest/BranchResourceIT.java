package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.domain.Worker;
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.domain.enumeration.BranchStatus;
import com.mycompany.myapp.domain.enumeration.Language;
import com.mycompany.myapp.repository.BranchRepository;
import com.mycompany.myapp.service.criteria.BranchCriteria;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.mapper.BranchMapper;
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
 * Integration tests for the {@link BranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BranchStatus DEFAULT_STATUS = BranchStatus.ACTIVE;
    private static final BranchStatus UPDATED_STATUS = BranchStatus.DISABLED;

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchMockMvc;

    private Branch branch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createEntity(EntityManager em) {
        Branch branch = new Branch().name(DEFAULT_NAME).status(DEFAULT_STATUS).language(DEFAULT_LANGUAGE);
        return branch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity(EntityManager em) {
        Branch branch = new Branch().name(UPDATED_NAME).status(UPDATED_STATUS).language(UPDATED_LANGUAGE);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branch = createEntity(em);
    }

    @Test
    @Transactional
    void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();
        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBranch.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBranch.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void createBranchWithExistingId() throws Exception {
        // Create the Branch with an existing ID
        branch.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branch);

        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setName(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branch);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setStatus(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branch);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setLanguage(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branch);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        Long id = branch.getId();

        defaultBranchShouldBeFound("id.equals=" + id);
        defaultBranchShouldNotBeFound("id.notEquals=" + id);

        defaultBranchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where name equals to DEFAULT_NAME
        defaultBranchShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the branchList where name equals to UPDATED_NAME
        defaultBranchShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBranchShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the branchList where name equals to UPDATED_NAME
        defaultBranchShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where name is not null
        defaultBranchShouldBeFound("name.specified=true");

        // Get all the branchList where name is null
        defaultBranchShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByNameContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where name contains DEFAULT_NAME
        defaultBranchShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the branchList where name contains UPDATED_NAME
        defaultBranchShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where name does not contain DEFAULT_NAME
        defaultBranchShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the branchList where name does not contain UPDATED_NAME
        defaultBranchShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status equals to DEFAULT_STATUS
        defaultBranchShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the branchList where status equals to UPDATED_STATUS
        defaultBranchShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBranchShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the branchList where status equals to UPDATED_STATUS
        defaultBranchShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where status is not null
        defaultBranchShouldBeFound("status.specified=true");

        // Get all the branchList where status is null
        defaultBranchShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where language equals to DEFAULT_LANGUAGE
        defaultBranchShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the branchList where language equals to UPDATED_LANGUAGE
        defaultBranchShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBranchesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultBranchShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the branchList where language equals to UPDATED_LANGUAGE
        defaultBranchShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBranchesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where language is not null
        defaultBranchShouldBeFound("language.specified=true");

        // Get all the branchList where language is null
        defaultBranchShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            branchRepository.saveAndFlush(branch);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        branch.setCompany(company);
        branchRepository.saveAndFlush(branch);
        Long companyId = company.getId();
        // Get all the branchList where company equals to companyId
        defaultBranchShouldBeFound("companyId.equals=" + companyId);

        // Get all the branchList where company equals to (companyId + 1)
        defaultBranchShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByWaitingRoomIsEqualToSomething() throws Exception {
        WaitingRoom waitingRoom;
        if (TestUtil.findAll(em, WaitingRoom.class).isEmpty()) {
            branchRepository.saveAndFlush(branch);
            waitingRoom = WaitingRoomResourceIT.createEntity(em);
        } else {
            waitingRoom = TestUtil.findAll(em, WaitingRoom.class).get(0);
        }
        em.persist(waitingRoom);
        em.flush();
        branch.addWaitingRoom(waitingRoom);
        branchRepository.saveAndFlush(branch);
        Long waitingRoomId = waitingRoom.getId();
        // Get all the branchList where waitingRoom equals to waitingRoomId
        defaultBranchShouldBeFound("waitingRoomId.equals=" + waitingRoomId);

        // Get all the branchList where waitingRoom equals to (waitingRoomId + 1)
        defaultBranchShouldNotBeFound("waitingRoomId.equals=" + (waitingRoomId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByQueueIsEqualToSomething() throws Exception {
        Queue queue;
        if (TestUtil.findAll(em, Queue.class).isEmpty()) {
            branchRepository.saveAndFlush(branch);
            queue = QueueResourceIT.createEntity(em);
        } else {
            queue = TestUtil.findAll(em, Queue.class).get(0);
        }
        em.persist(queue);
        em.flush();
        branch.addQueue(queue);
        branchRepository.saveAndFlush(branch);
        Long queueId = queue.getId();
        // Get all the branchList where queue equals to queueId
        defaultBranchShouldBeFound("queueId.equals=" + queueId);

        // Get all the branchList where queue equals to (queueId + 1)
        defaultBranchShouldNotBeFound("queueId.equals=" + (queueId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByWorkerIsEqualToSomething() throws Exception {
        Worker worker;
        if (TestUtil.findAll(em, Worker.class).isEmpty()) {
            branchRepository.saveAndFlush(branch);
            worker = WorkerResourceIT.createEntity(em);
        } else {
            worker = TestUtil.findAll(em, Worker.class).get(0);
        }
        em.persist(worker);
        em.flush();
        branch.addWorker(worker);
        branchRepository.saveAndFlush(branch);
        Long workerId = worker.getId();
        // Get all the branchList where worker equals to workerId
        defaultBranchShouldBeFound("workerId.equals=" + workerId);

        // Get all the branchList where worker equals to (workerId + 1)
        defaultBranchShouldNotBeFound("workerId.equals=" + (workerId + 1));
    }

    @Test
    @Transactional
    void getAllBranchesByWorkerProfilesIsEqualToSomething() throws Exception {
        WorkerProfile workerProfiles;
        if (TestUtil.findAll(em, WorkerProfile.class).isEmpty()) {
            branchRepository.saveAndFlush(branch);
            workerProfiles = WorkerProfileResourceIT.createEntity(em);
        } else {
            workerProfiles = TestUtil.findAll(em, WorkerProfile.class).get(0);
        }
        em.persist(workerProfiles);
        em.flush();
        branch.addWorkerProfiles(workerProfiles);
        branchRepository.saveAndFlush(branch);
        Long workerProfilesId = workerProfiles.getId();
        // Get all the branchList where workerProfiles equals to workerProfilesId
        defaultBranchShouldBeFound("workerProfilesId.equals=" + workerProfilesId);

        // Get all the branchList where workerProfiles equals to (workerProfilesId + 1)
        defaultBranchShouldNotBeFound("workerProfilesId.equals=" + (workerProfilesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));

        // Check, that the count call also returns 1
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).get();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch.name(UPDATED_NAME).status(UPDATED_STATUS).language(UPDATED_LANGUAGE);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranch);

        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBranch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.name(UPDATED_NAME).language(UPDATED_LANGUAGE);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBranch.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBranch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.name(UPDATED_NAME).status(UPDATED_STATUS).language(UPDATED_LANGUAGE);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBranch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBranch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, branch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
