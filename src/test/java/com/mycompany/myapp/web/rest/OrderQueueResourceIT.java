package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrderQueue;
import com.mycompany.myapp.repository.OrderQueueRepository;
import com.mycompany.myapp.service.dto.OrderQueueDTO;
import com.mycompany.myapp.service.mapper.OrderQueueMapper;
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
 * Integration tests for the {@link OrderQueueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderQueueResourceIT {

    private static final Long DEFAULT_ORDER = 1L;
    private static final Long UPDATED_ORDER = 2L;

    private static final String ENTITY_API_URL = "/api/order-queues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderQueueRepository orderQueueRepository;

    @Autowired
    private OrderQueueMapper orderQueueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderQueueMockMvc;

    private OrderQueue orderQueue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderQueue createEntity(EntityManager em) {
        OrderQueue orderQueue = new OrderQueue().order(DEFAULT_ORDER);
        return orderQueue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderQueue createUpdatedEntity(EntityManager em) {
        OrderQueue orderQueue = new OrderQueue().order(UPDATED_ORDER);
        return orderQueue;
    }

    @BeforeEach
    public void initTest() {
        orderQueue = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderQueue() throws Exception {
        int databaseSizeBeforeCreate = orderQueueRepository.findAll().size();
        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);
        restOrderQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderQueueDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeCreate + 1);
        OrderQueue testOrderQueue = orderQueueList.get(orderQueueList.size() - 1);
        assertThat(testOrderQueue.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createOrderQueueWithExistingId() throws Exception {
        // Create the OrderQueue with an existing ID
        orderQueue.setId(1L);
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        int databaseSizeBeforeCreate = orderQueueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderQueueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderQueueRepository.findAll().size();
        // set the field null
        orderQueue.setOrder(null);

        // Create the OrderQueue, which fails.
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        restOrderQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderQueueDTO)))
            .andExpect(status().isBadRequest());

        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderQueues() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        // Get all the orderQueueList
        restOrderQueueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderQueue.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())));
    }

    @Test
    @Transactional
    void getOrderQueue() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        // Get the orderQueue
        restOrderQueueMockMvc
            .perform(get(ENTITY_API_URL_ID, orderQueue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderQueue.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderQueue() throws Exception {
        // Get the orderQueue
        restOrderQueueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderQueue() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();

        // Update the orderQueue
        OrderQueue updatedOrderQueue = orderQueueRepository.findById(orderQueue.getId()).get();
        // Disconnect from session so that the updates on updatedOrderQueue are not directly saved in db
        em.detach(updatedOrderQueue);
        updatedOrderQueue.order(UPDATED_ORDER);
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(updatedOrderQueue);

        restOrderQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderQueueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
        OrderQueue testOrderQueue = orderQueueList.get(orderQueueList.size() - 1);
        assertThat(testOrderQueue.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderQueueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderQueueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderQueueWithPatch() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();

        // Update the orderQueue using partial update
        OrderQueue partialUpdatedOrderQueue = new OrderQueue();
        partialUpdatedOrderQueue.setId(orderQueue.getId());

        partialUpdatedOrderQueue.order(UPDATED_ORDER);

        restOrderQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderQueue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderQueue))
            )
            .andExpect(status().isOk());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
        OrderQueue testOrderQueue = orderQueueList.get(orderQueueList.size() - 1);
        assertThat(testOrderQueue.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateOrderQueueWithPatch() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();

        // Update the orderQueue using partial update
        OrderQueue partialUpdatedOrderQueue = new OrderQueue();
        partialUpdatedOrderQueue.setId(orderQueue.getId());

        partialUpdatedOrderQueue.order(UPDATED_ORDER);

        restOrderQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderQueue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderQueue))
            )
            .andExpect(status().isOk());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
        OrderQueue testOrderQueue = orderQueueList.get(orderQueueList.size() - 1);
        assertThat(testOrderQueue.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderQueueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderQueue() throws Exception {
        int databaseSizeBeforeUpdate = orderQueueRepository.findAll().size();
        orderQueue.setId(count.incrementAndGet());

        // Create the OrderQueue
        OrderQueueDTO orderQueueDTO = orderQueueMapper.toDto(orderQueue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderQueueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderQueueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderQueue in the database
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderQueue() throws Exception {
        // Initialize the database
        orderQueueRepository.saveAndFlush(orderQueue);

        int databaseSizeBeforeDelete = orderQueueRepository.findAll().size();

        // Delete the orderQueue
        restOrderQueueMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderQueue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderQueue> orderQueueList = orderQueueRepository.findAll();
        assertThat(orderQueueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
