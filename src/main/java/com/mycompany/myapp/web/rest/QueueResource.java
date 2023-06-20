package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QueueRepository;
import com.mycompany.myapp.service.QueueQueryService;
import com.mycompany.myapp.service.QueueService;
import com.mycompany.myapp.service.criteria.QueueCriteria;
import com.mycompany.myapp.service.dto.QueueDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Queue}.
 */
@RestController
@RequestMapping("/api")
public class QueueResource {

    private final Logger log = LoggerFactory.getLogger(QueueResource.class);

    private static final String ENTITY_NAME = "queue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QueueService queueService;

    private final QueueRepository queueRepository;

    private final QueueQueryService queueQueryService;

    public QueueResource(QueueService queueService, QueueRepository queueRepository, QueueQueryService queueQueryService) {
        this.queueService = queueService;
        this.queueRepository = queueRepository;
        this.queueQueryService = queueQueryService;
    }

    /**
     * {@code POST  /queues} : Create a new queue.
     *
     * @param queueDTO the queueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queueDTO, or with status {@code 400 (Bad Request)} if the queue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/queues")
    public ResponseEntity<QueueDTO> createQueue(@Valid @RequestBody QueueDTO queueDTO) throws URISyntaxException {
        log.debug("REST request to save Queue : {}", queueDTO);
        if (queueDTO.getId() != null) {
            throw new BadRequestAlertException("A new queue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QueueDTO result = queueService.save(queueDTO);
        return ResponseEntity
            .created(new URI("/api/queues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /queues/:id} : Updates an existing queue.
     *
     * @param id the id of the queueDTO to save.
     * @param queueDTO the queueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queueDTO,
     * or with status {@code 400 (Bad Request)} if the queueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the queueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/queues/{id}")
    public ResponseEntity<QueueDTO> updateQueue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QueueDTO queueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Queue : {}, {}", id, queueDTO);
        if (queueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QueueDTO result = queueService.update(queueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /queues/:id} : Partial updates given fields of an existing queue, field will ignore if it is null
     *
     * @param id the id of the queueDTO to save.
     * @param queueDTO the queueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queueDTO,
     * or with status {@code 400 (Bad Request)} if the queueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the queueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the queueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/queues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QueueDTO> partialUpdateQueue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QueueDTO queueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Queue partially : {}, {}", id, queueDTO);
        if (queueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QueueDTO> result = queueService.partialUpdate(queueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /queues} : get all the queues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queues in body.
     */
    @GetMapping("/queues")
    public ResponseEntity<List<QueueDTO>> getAllQueues(
        QueueCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Queues by criteria: {}", criteria);

        Page<QueueDTO> page = queueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /queues/count} : count all the queues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/queues/count")
    public ResponseEntity<Long> countQueues(QueueCriteria criteria) {
        log.debug("REST request to count Queues by criteria: {}", criteria);
        return ResponseEntity.ok().body(queueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /queues/:id} : get the "id" queue.
     *
     * @param id the id of the queueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/queues/{id}")
    public ResponseEntity<QueueDTO> getQueue(@PathVariable Long id) {
        log.debug("REST request to get Queue : {}", id);
        Optional<QueueDTO> queueDTO = queueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(queueDTO);
    }

    /**
     * {@code DELETE  /queues/:id} : delete the "id" queue.
     *
     * @param id the id of the queueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/queues/{id}")
    public ResponseEntity<Void> deleteQueue(@PathVariable Long id) {
        log.debug("REST request to delete Queue : {}", id);
        queueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
