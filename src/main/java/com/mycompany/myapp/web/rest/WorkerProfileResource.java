package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.WorkerProfileRepository;
import com.mycompany.myapp.service.WorkerProfileQueryService;
import com.mycompany.myapp.service.WorkerProfileService;
import com.mycompany.myapp.service.criteria.WorkerProfileCriteria;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkerProfile}.
 */
@RestController
@RequestMapping("/api")
public class WorkerProfileResource {

    private final Logger log = LoggerFactory.getLogger(WorkerProfileResource.class);

    private static final String ENTITY_NAME = "workerProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkerProfileService workerProfileService;

    private final WorkerProfileRepository workerProfileRepository;

    private final WorkerProfileQueryService workerProfileQueryService;

    public WorkerProfileResource(
        WorkerProfileService workerProfileService,
        WorkerProfileRepository workerProfileRepository,
        WorkerProfileQueryService workerProfileQueryService
    ) {
        this.workerProfileService = workerProfileService;
        this.workerProfileRepository = workerProfileRepository;
        this.workerProfileQueryService = workerProfileQueryService;
    }

    /**
     * {@code POST  /worker-profiles} : Create a new workerProfile.
     *
     * @param workerProfileDTO the workerProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workerProfileDTO, or with status {@code 400 (Bad Request)} if the workerProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/worker-profiles")
    public ResponseEntity<WorkerProfileDTO> createWorkerProfile(@Valid @RequestBody WorkerProfileDTO workerProfileDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkerProfile : {}", workerProfileDTO);
        if (workerProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new workerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkerProfileDTO result = workerProfileService.save(workerProfileDTO);
        return ResponseEntity
            .created(new URI("/api/worker-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /worker-profiles/:id} : Updates an existing workerProfile.
     *
     * @param id the id of the workerProfileDTO to save.
     * @param workerProfileDTO the workerProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerProfileDTO,
     * or with status {@code 400 (Bad Request)} if the workerProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workerProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/worker-profiles/{id}")
    public ResponseEntity<WorkerProfileDTO> updateWorkerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkerProfileDTO workerProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkerProfile : {}, {}", id, workerProfileDTO);
        if (workerProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkerProfileDTO result = workerProfileService.update(workerProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /worker-profiles/:id} : Partial updates given fields of an existing workerProfile, field will ignore if it is null
     *
     * @param id the id of the workerProfileDTO to save.
     * @param workerProfileDTO the workerProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerProfileDTO,
     * or with status {@code 400 (Bad Request)} if the workerProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workerProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workerProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/worker-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkerProfileDTO> partialUpdateWorkerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkerProfileDTO workerProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkerProfile partially : {}, {}", id, workerProfileDTO);
        if (workerProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkerProfileDTO> result = workerProfileService.partialUpdate(workerProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /worker-profiles} : get all the workerProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workerProfiles in body.
     */
    @GetMapping("/worker-profiles")
    public ResponseEntity<List<WorkerProfileDTO>> getAllWorkerProfiles(
        WorkerProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WorkerProfiles by criteria: {}", criteria);

        Page<WorkerProfileDTO> page = workerProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /worker-profiles/count} : count all the workerProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/worker-profiles/count")
    public ResponseEntity<Long> countWorkerProfiles(WorkerProfileCriteria criteria) {
        log.debug("REST request to count WorkerProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(workerProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /worker-profiles/:id} : get the "id" workerProfile.
     *
     * @param id the id of the workerProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workerProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/worker-profiles/{id}")
    public ResponseEntity<WorkerProfileDTO> getWorkerProfile(@PathVariable Long id) {
        log.debug("REST request to get WorkerProfile : {}", id);
        Optional<WorkerProfileDTO> workerProfileDTO = workerProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workerProfileDTO);
    }

    /**
     * {@code DELETE  /worker-profiles/:id} : delete the "id" workerProfile.
     *
     * @param id the id of the workerProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/worker-profiles/{id}")
    public ResponseEntity<Void> deleteWorkerProfile(@PathVariable Long id) {
        log.debug("REST request to delete WorkerProfile : {}", id);
        workerProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
