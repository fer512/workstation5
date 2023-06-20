package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OrderQueueRepository;
import com.mycompany.myapp.service.OrderQueueService;
import com.mycompany.myapp.service.dto.OrderQueueDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.OrderQueue}.
 */
@RestController
@RequestMapping("/api")
public class OrderQueueResource {

    private final Logger log = LoggerFactory.getLogger(OrderQueueResource.class);

    private static final String ENTITY_NAME = "orderQueue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderQueueService orderQueueService;

    private final OrderQueueRepository orderQueueRepository;

    public OrderQueueResource(OrderQueueService orderQueueService, OrderQueueRepository orderQueueRepository) {
        this.orderQueueService = orderQueueService;
        this.orderQueueRepository = orderQueueRepository;
    }

    /**
     * {@code POST  /order-queues} : Create a new orderQueue.
     *
     * @param orderQueueDTO the orderQueueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderQueueDTO, or with status {@code 400 (Bad Request)} if the orderQueue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-queues")
    public ResponseEntity<OrderQueueDTO> createOrderQueue(@Valid @RequestBody OrderQueueDTO orderQueueDTO) throws URISyntaxException {
        log.debug("REST request to save OrderQueue : {}", orderQueueDTO);
        if (orderQueueDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderQueue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderQueueDTO result = orderQueueService.save(orderQueueDTO);
        return ResponseEntity
            .created(new URI("/api/order-queues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-queues/:id} : Updates an existing orderQueue.
     *
     * @param id the id of the orderQueueDTO to save.
     * @param orderQueueDTO the orderQueueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderQueueDTO,
     * or with status {@code 400 (Bad Request)} if the orderQueueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderQueueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-queues/{id}")
    public ResponseEntity<OrderQueueDTO> updateOrderQueue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderQueueDTO orderQueueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderQueue : {}, {}", id, orderQueueDTO);
        if (orderQueueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderQueueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderQueueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderQueueDTO result = orderQueueService.update(orderQueueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderQueueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-queues/:id} : Partial updates given fields of an existing orderQueue, field will ignore if it is null
     *
     * @param id the id of the orderQueueDTO to save.
     * @param orderQueueDTO the orderQueueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderQueueDTO,
     * or with status {@code 400 (Bad Request)} if the orderQueueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderQueueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderQueueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-queues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderQueueDTO> partialUpdateOrderQueue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderQueueDTO orderQueueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderQueue partially : {}, {}", id, orderQueueDTO);
        if (orderQueueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderQueueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderQueueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderQueueDTO> result = orderQueueService.partialUpdate(orderQueueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderQueueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-queues} : get all the orderQueues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderQueues in body.
     */
    @GetMapping("/order-queues")
    public ResponseEntity<List<OrderQueueDTO>> getAllOrderQueues(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OrderQueues");
        Page<OrderQueueDTO> page = orderQueueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-queues/:id} : get the "id" orderQueue.
     *
     * @param id the id of the orderQueueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderQueueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-queues/{id}")
    public ResponseEntity<OrderQueueDTO> getOrderQueue(@PathVariable Long id) {
        log.debug("REST request to get OrderQueue : {}", id);
        Optional<OrderQueueDTO> orderQueueDTO = orderQueueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderQueueDTO);
    }

    /**
     * {@code DELETE  /order-queues/:id} : delete the "id" orderQueue.
     *
     * @param id the id of the orderQueueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-queues/{id}")
    public ResponseEntity<Void> deleteOrderQueue(@PathVariable Long id) {
        log.debug("REST request to delete OrderQueue : {}", id);
        orderQueueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
