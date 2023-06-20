package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.domain.Worker;
import com.mycompany.myapp.domain.enumeration.WorkerStatus;
import com.mycompany.myapp.repository.WorkerRepository;
import com.mycompany.myapp.service.WorkerService;
import com.mycompany.myapp.service.criteria.WorkerCriteria;
import com.mycompany.myapp.service.dto.WorkerDTO;
import com.mycompany.myapp.service.mapper.WorkerMapper;
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
 * Integration tests for the {@link WorkerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WorkerStatus DEFAULT_STATUS = WorkerStatus.ACTIVE;
    private static final WorkerStatus UPDATED_STATUS = WorkerStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/workers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkerRepository workerRepository;

    @Mock
    private WorkerRepository workerRepositoryMock;

    @Autowired
    private WorkerMapper workerMapper;

    @Mock
    private WorkerService workerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkerMockMvc;

    private Worker worker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worker createEntity(EntityManager em) {
        Worker worker = new Worker().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return worker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worker createUpdatedEntity(EntityManager em) {
        Worker worker = new Worker().name(UPDATED_NAME).status(UPDATED_STATUS);
        return worker;
    }

    @BeforeEach
    public void initTest() {
        worker = createEntity(em);
    }

    @Test
    @Transactional
    void createWorker() throws Exception {
        int databaseSizeBeforeCreate = workerRepository.findAll().size();
        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);
        restWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isCreated());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate + 1);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorker.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWorkerWithExistingId() throws Exception {
        // Create the Worker with an existing ID
        worker.setId(1L);
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        int databaseSizeBeforeCreate = workerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerRepository.findAll().size();
        // set the field null
        worker.setName(null);

        // Create the Worker, which fails.
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        restWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isBadRequest());

        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerRepository.findAll().size();
        // set the field null
        worker.setStatus(null);

        // Create the Worker, which fails.
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        restWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isBadRequest());

        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkers() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkersWithEagerRelationshipsIsEnabled() throws Exception {
        when(workerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get the worker
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL_ID, worker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(worker.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getWorkersByIdFiltering() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        Long id = worker.getId();

        defaultWorkerShouldBeFound("id.equals=" + id);
        defaultWorkerShouldNotBeFound("id.notEquals=" + id);

        defaultWorkerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkerShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where name equals to DEFAULT_NAME
        defaultWorkerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the workerList where name equals to UPDATED_NAME
        defaultWorkerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWorkerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the workerList where name equals to UPDATED_NAME
        defaultWorkerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where name is not null
        defaultWorkerShouldBeFound("name.specified=true");

        // Get all the workerList where name is null
        defaultWorkerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkersByNameContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where name contains DEFAULT_NAME
        defaultWorkerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the workerList where name contains UPDATED_NAME
        defaultWorkerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where name does not contain DEFAULT_NAME
        defaultWorkerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the workerList where name does not contain UPDATED_NAME
        defaultWorkerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where status equals to DEFAULT_STATUS
        defaultWorkerShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the workerList where status equals to UPDATED_STATUS
        defaultWorkerShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWorkerShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the workerList where status equals to UPDATED_STATUS
        defaultWorkerShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        // Get all the workerList where status is not null
        defaultWorkerShouldBeFound("status.specified=true");

        // Get all the workerList where status is null
        defaultWorkerShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkersByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            workerRepository.saveAndFlush(worker);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        worker.setCompany(company);
        workerRepository.saveAndFlush(worker);
        Long companyId = company.getId();
        // Get all the workerList where company equals to companyId
        defaultWorkerShouldBeFound("companyId.equals=" + companyId);

        // Get all the workerList where company equals to (companyId + 1)
        defaultWorkerShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllWorkersByBranchIsEqualToSomething() throws Exception {
        Branch branch;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            workerRepository.saveAndFlush(worker);
            branch = BranchResourceIT.createEntity(em);
        } else {
            branch = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        worker.addBranch(branch);
        workerRepository.saveAndFlush(worker);
        Long branchId = branch.getId();
        // Get all the workerList where branch equals to branchId
        defaultWorkerShouldBeFound("branchId.equals=" + branchId);

        // Get all the workerList where branch equals to (branchId + 1)
        defaultWorkerShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    @Test
    @Transactional
    void getAllWorkersByWaitingRoomIsEqualToSomething() throws Exception {
        WaitingRoom waitingRoom;
        if (TestUtil.findAll(em, WaitingRoom.class).isEmpty()) {
            workerRepository.saveAndFlush(worker);
            waitingRoom = WaitingRoomResourceIT.createEntity(em);
        } else {
            waitingRoom = TestUtil.findAll(em, WaitingRoom.class).get(0);
        }
        em.persist(waitingRoom);
        em.flush();
        worker.setWaitingRoom(waitingRoom);
        workerRepository.saveAndFlush(worker);
        Long waitingRoomId = waitingRoom.getId();
        // Get all the workerList where waitingRoom equals to waitingRoomId
        defaultWorkerShouldBeFound("waitingRoomId.equals=" + waitingRoomId);

        // Get all the workerList where waitingRoom equals to (waitingRoomId + 1)
        defaultWorkerShouldNotBeFound("waitingRoomId.equals=" + (waitingRoomId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkerShouldBeFound(String filter) throws Exception {
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worker.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkerShouldNotBeFound(String filter) throws Exception {
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorker() throws Exception {
        // Get the worker
        restWorkerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Update the worker
        Worker updatedWorker = workerRepository.findById(worker.getId()).get();
        // Disconnect from session so that the updates on updatedWorker are not directly saved in db
        em.detach(updatedWorker);
        updatedWorker.name(UPDATED_NAME).status(UPDATED_STATUS);
        WorkerDTO workerDTO = workerMapper.toDto(updatedWorker);

        restWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorker.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkerWithPatch() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Update the worker using partial update
        Worker partialUpdatedWorker = new Worker();
        partialUpdatedWorker.setId(worker.getId());

        partialUpdatedWorker.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorker))
            )
            .andExpect(status().isOk());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorker.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateWorkerWithPatch() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        int databaseSizeBeforeUpdate = workerRepository.findAll().size();

        // Update the worker using partial update
        Worker partialUpdatedWorker = new Worker();
        partialUpdatedWorker.setId(worker.getId());

        partialUpdatedWorker.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorker))
            )
            .andExpect(status().isOk());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
        Worker testWorker = workerList.get(workerList.size() - 1);
        assertThat(testWorker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorker.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorker() throws Exception {
        int databaseSizeBeforeUpdate = workerRepository.findAll().size();
        worker.setId(count.incrementAndGet());

        // Create the Worker
        WorkerDTO workerDTO = workerMapper.toDto(worker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Worker in the database
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorker() throws Exception {
        // Initialize the database
        workerRepository.saveAndFlush(worker);

        int databaseSizeBeforeDelete = workerRepository.findAll().size();

        // Delete the worker
        restWorkerMockMvc
            .perform(delete(ENTITY_API_URL_ID, worker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Worker> workerList = workerRepository.findAll();
        assertThat(workerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
