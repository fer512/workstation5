package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.WaitingRoomAttencionChannelRepository;
import com.mycompany.myapp.service.WaitingRoomAttencionChannelService;
import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WaitingRoomAttencionChannel}.
 */
@RestController
@RequestMapping("/api")
public class WaitingRoomAttencionChannelResource {

    private final Logger log = LoggerFactory.getLogger(WaitingRoomAttencionChannelResource.class);

    private static final String ENTITY_NAME = "waitingRoomAttencionChannel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaitingRoomAttencionChannelService waitingRoomAttencionChannelService;

    private final WaitingRoomAttencionChannelRepository waitingRoomAttencionChannelRepository;

    public WaitingRoomAttencionChannelResource(
        WaitingRoomAttencionChannelService waitingRoomAttencionChannelService,
        WaitingRoomAttencionChannelRepository waitingRoomAttencionChannelRepository
    ) {
        this.waitingRoomAttencionChannelService = waitingRoomAttencionChannelService;
        this.waitingRoomAttencionChannelRepository = waitingRoomAttencionChannelRepository;
    }

    /**
     * {@code POST  /waiting-room-attencion-channels} : Create a new waitingRoomAttencionChannel.
     *
     * @param waitingRoomAttencionChannelDTO the waitingRoomAttencionChannelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waitingRoomAttencionChannelDTO, or with status {@code 400 (Bad Request)} if the waitingRoomAttencionChannel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waiting-room-attencion-channels")
    public ResponseEntity<WaitingRoomAttencionChannelDTO> createWaitingRoomAttencionChannel(
        @Valid @RequestBody WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WaitingRoomAttencionChannel : {}", waitingRoomAttencionChannelDTO);
        if (waitingRoomAttencionChannelDTO.getId() != null) {
            throw new BadRequestAlertException("A new waitingRoomAttencionChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WaitingRoomAttencionChannelDTO result = waitingRoomAttencionChannelService.save(waitingRoomAttencionChannelDTO);
        return ResponseEntity
            .created(new URI("/api/waiting-room-attencion-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waiting-room-attencion-channels/:id} : Updates an existing waitingRoomAttencionChannel.
     *
     * @param id the id of the waitingRoomAttencionChannelDTO to save.
     * @param waitingRoomAttencionChannelDTO the waitingRoomAttencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waitingRoomAttencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the waitingRoomAttencionChannelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waitingRoomAttencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waiting-room-attencion-channels/{id}")
    public ResponseEntity<WaitingRoomAttencionChannelDTO> updateWaitingRoomAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaitingRoomAttencionChannel : {}, {}", id, waitingRoomAttencionChannelDTO);
        if (waitingRoomAttencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waitingRoomAttencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRoomAttencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WaitingRoomAttencionChannelDTO result = waitingRoomAttencionChannelService.update(waitingRoomAttencionChannelDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waitingRoomAttencionChannelDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /waiting-room-attencion-channels/:id} : Partial updates given fields of an existing waitingRoomAttencionChannel, field will ignore if it is null
     *
     * @param id the id of the waitingRoomAttencionChannelDTO to save.
     * @param waitingRoomAttencionChannelDTO the waitingRoomAttencionChannelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waitingRoomAttencionChannelDTO,
     * or with status {@code 400 (Bad Request)} if the waitingRoomAttencionChannelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waitingRoomAttencionChannelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waitingRoomAttencionChannelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/waiting-room-attencion-channels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaitingRoomAttencionChannelDTO> partialUpdateWaitingRoomAttencionChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaitingRoomAttencionChannel partially : {}, {}", id, waitingRoomAttencionChannelDTO);
        if (waitingRoomAttencionChannelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waitingRoomAttencionChannelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRoomAttencionChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaitingRoomAttencionChannelDTO> result = waitingRoomAttencionChannelService.partialUpdate(waitingRoomAttencionChannelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waitingRoomAttencionChannelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /waiting-room-attencion-channels} : get all the waitingRoomAttencionChannels.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waitingRoomAttencionChannels in body.
     */
    @GetMapping("/waiting-room-attencion-channels")
    public ResponseEntity<List<WaitingRoomAttencionChannelDTO>> getAllWaitingRoomAttencionChannels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("waitingroom-is-null".equals(filter)) {
            log.debug("REST request to get all WaitingRoomAttencionChannels where waitingRoom is null");
            return new ResponseEntity<>(waitingRoomAttencionChannelService.findAllWhereWaitingRoomIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of WaitingRoomAttencionChannels");
        Page<WaitingRoomAttencionChannelDTO> page = waitingRoomAttencionChannelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /waiting-room-attencion-channels/:id} : get the "id" waitingRoomAttencionChannel.
     *
     * @param id the id of the waitingRoomAttencionChannelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waitingRoomAttencionChannelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waiting-room-attencion-channels/{id}")
    public ResponseEntity<WaitingRoomAttencionChannelDTO> getWaitingRoomAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to get WaitingRoomAttencionChannel : {}", id);
        Optional<WaitingRoomAttencionChannelDTO> waitingRoomAttencionChannelDTO = waitingRoomAttencionChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waitingRoomAttencionChannelDTO);
    }

    /**
     * {@code DELETE  /waiting-room-attencion-channels/:id} : delete the "id" waitingRoomAttencionChannel.
     *
     * @param id the id of the waitingRoomAttencionChannelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waiting-room-attencion-channels/{id}")
    public ResponseEntity<Void> deleteWaitingRoomAttencionChannel(@PathVariable Long id) {
        log.debug("REST request to delete WaitingRoomAttencionChannel : {}", id);
        waitingRoomAttencionChannelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
