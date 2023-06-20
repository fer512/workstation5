package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.WaitingRoomRepository;
import com.mycompany.myapp.service.WaitingRoomQueryService;
import com.mycompany.myapp.service.WaitingRoomService;
import com.mycompany.myapp.service.criteria.WaitingRoomCriteria;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WaitingRoom}.
 */
@RestController
@RequestMapping("/api")
public class WaitingRoomResource {

    private final Logger log = LoggerFactory.getLogger(WaitingRoomResource.class);

    private static final String ENTITY_NAME = "waitingRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaitingRoomService waitingRoomService;

    private final WaitingRoomRepository waitingRoomRepository;

    private final WaitingRoomQueryService waitingRoomQueryService;

    public WaitingRoomResource(
        WaitingRoomService waitingRoomService,
        WaitingRoomRepository waitingRoomRepository,
        WaitingRoomQueryService waitingRoomQueryService
    ) {
        this.waitingRoomService = waitingRoomService;
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomQueryService = waitingRoomQueryService;
    }

    /**
     * {@code POST  /waiting-rooms} : Create a new waitingRoom.
     *
     * @param waitingRoomDTO the waitingRoomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waitingRoomDTO, or with status {@code 400 (Bad Request)} if the waitingRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waiting-rooms")
    public ResponseEntity<WaitingRoomDTO> createWaitingRoom(@Valid @RequestBody WaitingRoomDTO waitingRoomDTO) throws URISyntaxException {
        log.debug("REST request to save WaitingRoom : {}", waitingRoomDTO);
        if (waitingRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new waitingRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WaitingRoomDTO result = waitingRoomService.save(waitingRoomDTO);
        return ResponseEntity
            .created(new URI("/api/waiting-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waiting-rooms/:id} : Updates an existing waitingRoom.
     *
     * @param id the id of the waitingRoomDTO to save.
     * @param waitingRoomDTO the waitingRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waitingRoomDTO,
     * or with status {@code 400 (Bad Request)} if the waitingRoomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waitingRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waiting-rooms/{id}")
    public ResponseEntity<WaitingRoomDTO> updateWaitingRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaitingRoomDTO waitingRoomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaitingRoom : {}, {}", id, waitingRoomDTO);
        if (waitingRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waitingRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WaitingRoomDTO result = waitingRoomService.update(waitingRoomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waitingRoomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /waiting-rooms/:id} : Partial updates given fields of an existing waitingRoom, field will ignore if it is null
     *
     * @param id the id of the waitingRoomDTO to save.
     * @param waitingRoomDTO the waitingRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waitingRoomDTO,
     * or with status {@code 400 (Bad Request)} if the waitingRoomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waitingRoomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waitingRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/waiting-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaitingRoomDTO> partialUpdateWaitingRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaitingRoomDTO waitingRoomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaitingRoom partially : {}, {}", id, waitingRoomDTO);
        if (waitingRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waitingRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waitingRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaitingRoomDTO> result = waitingRoomService.partialUpdate(waitingRoomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waitingRoomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /waiting-rooms} : get all the waitingRooms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waitingRooms in body.
     */
    @GetMapping("/waiting-rooms")
    public ResponseEntity<List<WaitingRoomDTO>> getAllWaitingRooms(
        WaitingRoomCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WaitingRooms by criteria: {}", criteria);

        Page<WaitingRoomDTO> page = waitingRoomQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /waiting-rooms/count} : count all the waitingRooms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/waiting-rooms/count")
    public ResponseEntity<Long> countWaitingRooms(WaitingRoomCriteria criteria) {
        log.debug("REST request to count WaitingRooms by criteria: {}", criteria);
        return ResponseEntity.ok().body(waitingRoomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /waiting-rooms/:id} : get the "id" waitingRoom.
     *
     * @param id the id of the waitingRoomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waitingRoomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waiting-rooms/{id}")
    public ResponseEntity<WaitingRoomDTO> getWaitingRoom(@PathVariable Long id) {
        log.debug("REST request to get WaitingRoom : {}", id);
        Optional<WaitingRoomDTO> waitingRoomDTO = waitingRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waitingRoomDTO);
    }

    /**
     * {@code DELETE  /waiting-rooms/:id} : delete the "id" waitingRoom.
     *
     * @param id the id of the waitingRoomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waiting-rooms/{id}")
    public ResponseEntity<Void> deleteWaitingRoom(@PathVariable Long id) {
        log.debug("REST request to delete WaitingRoom : {}", id);
        waitingRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
