package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.WorkerProfileAttencionChannelRepository;
import com.mycompany.myapp.service.WorkerProfileAttencionChannelService;
import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WorkerProfileAttencionChannel}.
 */
@RestController
@RequestMapping("/api")
public class WorkerProfileAttencionChannelResource {

    private final Logger log = LoggerFactory.getLogger(WorkerProfileAttencionChannelResource.class);

    private static final String ENTITY_NAME = "workerProfileAttencionChannel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkerProfileAttencionChannelService workerProfileAttencionChannelService;

    private final WorkerProfileAttencionChannelRepository workerProfileAttencionChannelRepository;

    public WorkerProfileAttencionChannelResource(
        WorkerProfileAttencionChannelService workerProfileAttencionChannelService,
        WorkerProfileAttencionChannelRepository workerProfileAttencionChannelRepository
    ) {
        this.workerProfileAttencionChannelService = workerProfileAttencionChannelService;
        this.workerProfileAttencionChannelRepository = workerProfileAttencionChannelRepository;
    }

    /**
     * {@code POST  /worker-profile-attencion-channels} : Create a new workerProfileAttencionChannel.
     *
     * @param workerProfileAttencionChannelDTO the workerProfileAttencionChannelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workerProfileAttencionChannelDTO, or with status {@code 400 (Bad Request)} if the workerProfileAttencionChannel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/worker-profile-attencion-channels")
    public ResponseEntity<WorkerProfileAttencionChannelDTO> createWorkerProfileAttencionChannel(
        @Valid @RequestBody WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkerProfileAttencionChannel : {}", workerProfileAttencionChannelDTO);
        if (workerProfileAttencionChannelDTO.getId() != null) {
            throw new BadRequestAlertException("A new workerProfileAttencionChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkerProfileAttencionChannelDTO result = workerProfileAttencionChannelService.save(workerProfileAttencionChannelDTO);
        return ResponseEntity
            .created(new URI("/api/worker-profile-attencion-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /worker-profile-attencion-channels/:id} : Updates an existing workerProfileAttencionChannel.
     *
     * @param id the id of the workerProfileAttencionChannelDTO to save.
     * @param workerProfileAttencionChannelDTO the workerProfileAttencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerProfileAttencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the workerProfileAttencionChannelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workerProfileAttencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/worker-profile-attencion-channels/{id}")
    public ResponseEntity<WorkerProfileAttencionChannelDTO> updateWorkerProfileAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkerProfileAttencionChannel : {}, {}", id, workerProfileAttencionChannelDTO);
        if (workerProfileAttencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerProfileAttencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerProfileAttencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkerProfileAttencionChannelDTO result = workerProfileAttencionChannelService.update(workerProfileAttencionChannelDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerProfileAttencionChannelDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /worker-profile-attencion-channels/:id} : Partial updates given fields of an existing workerProfileAttencionChannel, field will ignore if it is null
     *
     * @param id the id of the workerProfileAttencionChannelDTO to save.
     * @param workerProfileAttencionChannelDTO the workerProfileAttencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workerProfileAttencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the workerProfileAttencionChannelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workerProfileAttencionChannelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workerProfileAttencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/worker-profile-attencion-channels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkerProfileAttencionChannelDTO> partialUpdateWorkerProfileAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkerProfileAttencionChannel partially : {}, {}", id, workerProfileAttencionChannelDTO);
        if (workerProfileAttencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workerProfileAttencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerProfileAttencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkerProfileAttencionChannelDTO> result = workerProfileAttencionChannelService.partialUpdate(
            workerProfileAttencionChannelDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workerProfileAttencionChannelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /worker-profile-attencion-channels} : get all the workerProfileAttencionChannels.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workerProfileAttencionChannels in body.
     */
    @GetMapping("/worker-profile-attencion-channels")
    public ResponseEntity<List<WorkerProfileAttencionChannelDTO>> getAllWorkerProfileAttencionChannels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("workerprofile-is-null".equals(filter)) {
            log.debug("REST request to get all WorkerProfileAttencionChannels where workerProfile is null");
            return new ResponseEntity<>(workerProfileAttencionChannelService.findAllWhereWorkerProfileIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of WorkerProfileAttencionChannels");
        Page<WorkerProfileAttencionChannelDTO> page = workerProfileAttencionChannelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /worker-profile-attencion-channels/:id} : get the "id" workerProfileAttencionChannel.
     *
     * @param id the id of the workerProfileAttencionChannelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workerProfileAttencionChannelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/worker-profile-attencion-channels/{id}")
    public ResponseEntity<WorkerProfileAttencionChannelDTO> getWorkerProfileAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to get WorkerProfileAttencionChannel : {}", id);
        Optional<WorkerProfileAttencionChannelDTO> workerProfileAttencionChannelDTO = workerProfileAttencionChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workerProfileAttencionChannelDTO);
    }

    /**
     * {@code DELETE  /worker-profile-attencion-channels/:id} : delete the "id" workerProfileAttencionChannel.
     *
     * @param id the id of the workerProfileAttencionChannelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/worker-profile-attencion-channels/{id}")
    public ResponseEntity<Void> deleteWorkerProfileAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to delete WorkerProfileAttencionChannel : {}", id);
        workerProfileAttencionChannelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
