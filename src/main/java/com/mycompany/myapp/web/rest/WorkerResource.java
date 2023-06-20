package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.WorkerRepository;
import com.mycompany.myapp.service.WorkerQueryService;
import com.mycompany.myapp.service.WorkerService;
import com.mycompany.myapp.service.criteria.WorkerCriteria;
import com.mycompany.myapp.service.dto.WorkerDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Worker}.
 */
@RestController
@RequestMapping("/api")
public class WorkerResource {

    private final Logger log = LoggerFactory.getLogger(WorkerResource.class);

    private static final String ENTITY_NAME = "worker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkerService workerService;

    private final WorkerRepository workerRepository;

    private final WorkerQueryService workerQueryService;

    public WorkerResource(WorkerService workerService, WorkerRepository workerRepository, WorkerQueryService workerQueryService) {
        this.workerService = workerService;
        this.workerRepository = workerRepository;
        this.workerQueryService = workerQueryService;
    }

    /**
     * {@code POST  /workers} : Create a new worker.
     *
     * @param workerDTO the workerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workerDTO, or with status {@code 400 (Bad Request)} if the worker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workers")
    public ResponseEntity<WorkerDTO> createWorker(@Valid @RequestBody WorkerDTO workerDTO) throws URISyntaxException {
        log.debug("REST request to save Worker : {}", workerDTO);
        if (workerDTO.getId() != null) {
            throw new BadRequestAlertException("A new worker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkerDTO result = workerService.save(workerDTO);
        return ResponseEntity
            .created(new URI("/api/workers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workers/:id} : Updates an existing worker.
     *
     * @param id the id of the workerDTO to save.
     * @param workerDTO the workerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerDTO,
     * or with status {@code 400 (Bad Request)} if the workerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> updateWorker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkerDTO workerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Worker : {}, {}", id, workerDTO);
        if (workerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkerDTO result = workerService.update(workerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workers/:id} : Partial updates given fields of an existing worker, field will ignore if it is null
     *
     * @param id the id of the workerDTO to save.
     * @param workerDTO the workerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerDTO,
     * or with status {@code 400 (Bad Request)} if the workerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkerDTO> partialUpdateWorker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkerDTO workerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Worker partially : {}, {}", id, workerDTO);
        if (workerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkerDTO> result = workerService.partialUpdate(workerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workers} : get all the workers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workers in body.
     */
    @GetMapping("/workers")
    public ResponseEntity<List<WorkerDTO>> getAllWorkers(
        WorkerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Workers by criteria: {}", criteria);

        Page<WorkerDTO> page = workerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workers/count} : count all the workers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/workers/count")
    public ResponseEntity<Long> countWorkers(WorkerCriteria criteria) {
        log.debug("REST request to count Workers by criteria: {}", criteria);
        return ResponseEntity.ok().body(workerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workers/:id} : get the "id" worker.
     *
     * @param id the id of the workerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workers/{id}")
    public ResponseEntity<WorkerDTO> getWorker(@PathVariable Long id) {
        log.debug("REST request to get Worker : {}", id);
        Optional<WorkerDTO> workerDTO = workerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workerDTO);
    }

    /**
     * {@code DELETE  /workers/:id} : delete the "id" worker.
     *
     * @param id the id of the workerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        log.debug("REST request to delete Worker : {}", id);
        workerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
