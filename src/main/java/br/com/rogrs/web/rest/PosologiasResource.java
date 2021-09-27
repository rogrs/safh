package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Posologias;
import br.com.rogrs.repository.PosologiasRepository;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.rogrs.domain.Posologias}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PosologiasResource {

    private final Logger log = LoggerFactory.getLogger(PosologiasResource.class);

    private static final String ENTITY_NAME = "posologias";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PosologiasRepository posologiasRepository;

    public PosologiasResource(PosologiasRepository posologiasRepository) {
        this.posologiasRepository = posologiasRepository;
    }

    /**
     * {@code POST  /posologias} : Create a new posologias.
     *
     * @param posologias the posologias to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posologias, or with status {@code 400 (Bad Request)} if the posologias has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posologias")
    public ResponseEntity<Posologias> createPosologias(@Valid @RequestBody Posologias posologias) throws URISyntaxException {
        log.debug("REST request to save Posologias : {}", posologias);
        if (posologias.getId() != null) {
            throw new BadRequestAlertException("A new posologias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Posologias result = posologiasRepository.save(posologias);
        return ResponseEntity
            .created(new URI("/api/posologias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posologias/:id} : Updates an existing posologias.
     *
     * @param id the id of the posologias to save.
     * @param posologias the posologias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posologias,
     * or with status {@code 400 (Bad Request)} if the posologias is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posologias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posologias/{id}")
    public ResponseEntity<Posologias> updatePosologias(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Posologias posologias
    ) throws URISyntaxException {
        log.debug("REST request to update Posologias : {}, {}", id, posologias);
        if (posologias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posologias.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posologiasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Posologias result = posologiasRepository.save(posologias);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posologias.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /posologias/:id} : Partial updates given fields of an existing posologias, field will ignore if it is null
     *
     * @param id the id of the posologias to save.
     * @param posologias the posologias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posologias,
     * or with status {@code 400 (Bad Request)} if the posologias is not valid,
     * or with status {@code 404 (Not Found)} if the posologias is not found,
     * or with status {@code 500 (Internal Server Error)} if the posologias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/posologias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Posologias> partialUpdatePosologias(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Posologias posologias
    ) throws URISyntaxException {
        log.debug("REST request to partial update Posologias partially : {}, {}", id, posologias);
        if (posologias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posologias.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!posologiasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Posologias> result = posologiasRepository
            .findById(posologias.getId())
            .map(
                existingPosologias -> {
                    if (posologias.getPosologia() != null) {
                        existingPosologias.setPosologia(posologias.getPosologia());
                    }

                    return existingPosologias;
                }
            )
            .map(posologiasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posologias.getId().toString())
        );
    }

    /**
     * {@code GET  /posologias} : get all the posologias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posologias in body.
     */
    @GetMapping("/posologias")
    public List<Posologias> getAllPosologias() {
        log.debug("REST request to get all Posologias");
        return posologiasRepository.findAll();
    }

    /**
     * {@code GET  /posologias/:id} : get the "id" posologias.
     *
     * @param id the id of the posologias to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posologias, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posologias/{id}")
    public ResponseEntity<Posologias> getPosologias(@PathVariable Long id) {
        log.debug("REST request to get Posologias : {}", id);
        Optional<Posologias> posologias = posologiasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(posologias);
    }

    /**
     * {@code DELETE  /posologias/:id} : delete the "id" posologias.
     *
     * @param id the id of the posologias to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posologias/{id}")
    public ResponseEntity<Void> deletePosologias(@PathVariable Long id) {
        log.debug("REST request to delete Posologias : {}", id);
        posologiasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
