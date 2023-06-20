package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AttencionChannelRepository;
import com.mycompany.myapp.service.AttencionChannelService;
import com.mycompany.myapp.service.dto.AttencionChannelDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AttencionChannel}.
 */
@RestController
@RequestMapping("/api")
public class AttencionChannelResource {

    private final Logger log = LoggerFactory.getLogger(AttencionChannelResource.class);

    private static final String ENTITY_NAME = "attencionChannel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttencionChannelService attencionChannelService;

    private final AttencionChannelRepository attencionChannelRepository;

    public AttencionChannelResource(
        AttencionChannelService attencionChannelService,
        AttencionChannelRepository attencionChannelRepository
    ) {
        this.attencionChannelService = attencionChannelService;
        this.attencionChannelRepository = attencionChannelRepository;
    }

    /**
     * {@code POST  /attencion-channels} : Create a new attencionChannel.
     *
     * @param attencionChannelDTO the attencionChannelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attencionChannelDTO, or with status {@code 400 (Bad Request)} if the attencionChannel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attencion-channels")
    public ResponseEntity<AttencionChannelDTO> createAttencionChannel(@Valid @RequestBody AttencionChannelDTO attencionChannelDTO)
        throws URISyntaxException {
        log.debug("REST request to save AttencionChannel : {}", attencionChannelDTO);
        if (attencionChannelDTO.getId() != null) {
            throw new BadRequestAlertException("A new attencionChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttencionChannelDTO result = attencionChannelService.save(attencionChannelDTO);
        return ResponseEntity
            .created(new URI("/api/attencion-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attencion-channels/:id} : Updates an existing attencionChannel.
     *
     * @param id the id of the attencionChannelDTO to save.
     * @param attencionChannelDTO the attencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the attencionChannelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attencion-channels/{id}")
    public ResponseEntity<AttencionChannelDTO> updateAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AttencionChannelDTO attencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AttencionChannel : {}, {}", id, attencionChannelDTO);
        if (attencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttencionChannelDTO result = attencionChannelService.update(attencionChannelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attencionChannelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attencion-channels/:id} : Partial updates given fields of an existing attencionChannel, field will ignore if it is null
     *
     * @param id the id of the attencionChannelDTO to save.
     * @param attencionChannelDTO the attencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the attencionChannelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attencionChannelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attencion-channels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttencionChannelDTO> partialUpdateAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AttencionChannelDTO attencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttencionChannel partially : {}, {}", id, attencionChannelDTO);
        if (attencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttencionChannelDTO> result = attencionChannelService.partialUpdate(attencionChannelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attencionChannelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /attencion-channels} : get all the attencionChannels.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attencionChannels in body.
     */
    @GetMapping("/attencion-channels")
    public ResponseEntity<List<AttencionChannelDTO>> getAllAttencionChannels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("queue-is-null".equals(filter)) {
            log.debug("REST request to get all AttencionChannels where queue is null");
            return new ResponseEntity<>(attencionChannelService.findAllWhereQueueIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of AttencionChannels");
        Page<AttencionChannelDTO> page = attencionChannelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attencion-channels/:id} : get the "id" attencionChannel.
     *
     * @param id the id of the attencionChannelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attencionChannelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attencion-channels/{id}")
    public ResponseEntity<AttencionChannelDTO> getAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to get AttencionChannel : {}", id);
        Optional<AttencionChannelDTO> attencionChannelDTO = attencionChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attencionChannelDTO);
    }

    /**
     * {@code DELETE  /attencion-channels/:id} : delete the "id" attencionChannel.
     *
     * @param id the id of the attencionChannelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attencion-channels/{id}")
    public ResponseEntity<Void> deleteAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to delete AttencionChannel : {}", id);
        attencionChannelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
