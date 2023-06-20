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
import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import com.mycompany.myapp.domain.Worker;
import com.mycompany.myapp.domain.enumeration.WaitingRoomStatus;
import com.mycompany.myapp.repository.WaitingRoomRepository;
import com.mycompany.myapp.service.WaitingRoomService;
import com.mycompany.myapp.service.criteria.WaitingRoomCriteria;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomMapper;
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
 * Integration tests for the {@link WaitingRoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WaitingRoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WaitingRoomStatus DEFAULT_STATUS = WaitingRoomStatus.ACTIVE;
    private static final WaitingRoomStatus UPDATED_STATUS = WaitingRoomStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/waiting-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WaitingRoomRepository waitingRoomRepository;

    @Mock
    private WaitingRoomRepository waitingRoomRepositoryMock;

    @Autowired
    private WaitingRoomMapper waitingRoomMapper;

    @Mock
    private WaitingRoomService waitingRoomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaitingRoomMockMvc;

    private WaitingRoom waitingRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoom createEntity(EntityManager em) {
        WaitingRoom waitingRoom = new WaitingRoom().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return waitingRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoom createUpdatedEntity(EntityManager em) {
        WaitingRoom waitingRoom = new WaitingRoom().name(UPDATED_NAME).status(UPDATED_STATUS);
        return waitingRoom;
    }

    @BeforeEach
    public void initTest() {
        waitingRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createWaitingRoom() throws Exception {
        int databaseSizeBeforeCreate = waitingRoomRepository.findAll().size();
        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);
        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeCreate + 1);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWaitingRoomWithExistingId() throws Exception {
        // Create the WaitingRoom with an existing ID
        waitingRoom.setId(1L);
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        int databaseSizeBeforeCreate = waitingRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomRepository.findAll().size();
        // set the field null
        waitingRoom.setName(null);

        // Create the WaitingRoom, which fails.
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomRepository.findAll().size();
        // set the field null
        waitingRoom.setStatus(null);

        // Create the WaitingRoom, which fails.
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaitingRooms() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waitingRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWaitingRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(waitingRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(waitingRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWaitingRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(waitingRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(waitingRoomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get the waitingRoom
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, waitingRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waitingRoom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getWaitingRoomsByIdFiltering() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        Long id = waitingRoom.getId();

        defaultWaitingRoomShouldBeFound("id.equals=" + id);
        defaultWaitingRoomShouldNotBeFound("id.notEquals=" + id);

        defaultWaitingRoomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWaitingRoomShouldNotBeFound("id.greaterThan=" + id);

        defaultWaitingRoomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWaitingRoomShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where name equals to DEFAULT_NAME
        defaultWaitingRoomShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the waitingRoomList where name equals to UPDATED_NAME
        defaultWaitingRoomShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWaitingRoomShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the waitingRoomList where name equals to UPDATED_NAME
        defaultWaitingRoomShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where name is not null
        defaultWaitingRoomShouldBeFound("name.specified=true");

        // Get all the waitingRoomList where name is null
        defaultWaitingRoomShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByNameContainsSomething() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where name contains DEFAULT_NAME
        defaultWaitingRoomShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the waitingRoomList where name contains UPDATED_NAME
        defaultWaitingRoomShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where name does not contain DEFAULT_NAME
        defaultWaitingRoomShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the waitingRoomList where name does not contain UPDATED_NAME
        defaultWaitingRoomShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where status equals to DEFAULT_STATUS
        defaultWaitingRoomShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the waitingRoomList where status equals to UPDATED_STATUS
        defaultWaitingRoomShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWaitingRoomShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the waitingRoomList where status equals to UPDATED_STATUS
        defaultWaitingRoomShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        // Get all the waitingRoomList where status is not null
        defaultWaitingRoomShouldBeFound("status.specified=true");

        // Get all the waitingRoomList where status is null
        defaultWaitingRoomShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByAttencionChannelIsEqualToSomething() throws Exception {
        WaitingRoomAttencionChannel attencionChannel;
        if (TestUtil.findAll(em, WaitingRoomAttencionChannel.class).isEmpty()) {
            waitingRoomRepository.saveAndFlush(waitingRoom);
            attencionChannel = WaitingRoomAttencionChannelResourceIT.createEntity(em);
        } else {
            attencionChannel = TestUtil.findAll(em, WaitingRoomAttencionChannel.class).get(0);
        }
        em.persist(attencionChannel);
        em.flush();
        waitingRoom.setAttencionChannel(attencionChannel);
        waitingRoomRepository.saveAndFlush(waitingRoom);
        Long attencionChannelId = attencionChannel.getId();
        // Get all the waitingRoomList where attencionChannel equals to attencionChannelId
        defaultWaitingRoomShouldBeFound("attencionChannelId.equals=" + attencionChannelId);

        // Get all the waitingRoomList where attencionChannel equals to (attencionChannelId + 1)
        defaultWaitingRoomShouldNotBeFound("attencionChannelId.equals=" + (attencionChannelId + 1));
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByWorkerIsEqualToSomething() throws Exception {
        Worker worker;
        if (TestUtil.findAll(em, Worker.class).isEmpty()) {
            waitingRoomRepository.saveAndFlush(waitingRoom);
            worker = WorkerResourceIT.createEntity(em);
        } else {
            worker = TestUtil.findAll(em, Worker.class).get(0);
        }
        em.persist(worker);
        em.flush();
        waitingRoom.addWorker(worker);
        waitingRoomRepository.saveAndFlush(waitingRoom);
        Long workerId = worker.getId();
        // Get all the waitingRoomList where worker equals to workerId
        defaultWaitingRoomShouldBeFound("workerId.equals=" + workerId);

        // Get all the waitingRoomList where worker equals to (workerId + 1)
        defaultWaitingRoomShouldNotBeFound("workerId.equals=" + (workerId + 1));
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            waitingRoomRepository.saveAndFlush(waitingRoom);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        waitingRoom.setCompany(company);
        waitingRoomRepository.saveAndFlush(waitingRoom);
        Long companyId = company.getId();
        // Get all the waitingRoomList where company equals to companyId
        defaultWaitingRoomShouldBeFound("companyId.equals=" + companyId);

        // Get all the waitingRoomList where company equals to (companyId + 1)
        defaultWaitingRoomShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllWaitingRoomsByBranchIsEqualToSomething() throws Exception {
        Branch branch;
        if (TestUtil.findAll(em, Branch.class).isEmpty()) {
            waitingRoomRepository.saveAndFlush(waitingRoom);
            branch = BranchResourceIT.createEntity(em);
        } else {
            branch = TestUtil.findAll(em, Branch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        waitingRoom.addBranch(branch);
        waitingRoomRepository.saveAndFlush(waitingRoom);
        Long branchId = branch.getId();
        // Get all the waitingRoomList where branch equals to branchId
        defaultWaitingRoomShouldBeFound("branchId.equals=" + branchId);

        // Get all the waitingRoomList where branch equals to (branchId + 1)
        defaultWaitingRoomShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWaitingRoomShouldBeFound(String filter) throws Exception {
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waitingRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWaitingRoomShouldNotBeFound(String filter) throws Exception {
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWaitingRoom() throws Exception {
        // Get the waitingRoom
        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom
        WaitingRoom updatedWaitingRoom = waitingRoomRepository.findById(waitingRoom.getId()).get();
        // Disconnect from session so that the updates on updatedWaitingRoom are not directly saved in db
        em.detach(updatedWaitingRoom);
        updatedWaitingRoom.name(UPDATED_NAME).status(UPDATED_STATUS);
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(updatedWaitingRoom);

        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaitingRoomWithPatch() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom using partial update
        WaitingRoom partialUpdatedWaitingRoom = new WaitingRoom();
        partialUpdatedWaitingRoom.setId(waitingRoom.getId());

        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoom))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateWaitingRoomWithPatch() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom using partial update
        WaitingRoom partialUpdatedWaitingRoom = new WaitingRoom();
        partialUpdatedWaitingRoom.setId(waitingRoom.getId());

        partialUpdatedWaitingRoom.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoom))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.saveAndFlush(waitingRoom);

        int databaseSizeBeforeDelete = waitingRoomRepository.findAll().size();

        // Delete the waitingRoom
        restWaitingRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, waitingRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
