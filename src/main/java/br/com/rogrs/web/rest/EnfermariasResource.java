package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Enfermarias;
import br.com.rogrs.repository.EnfermariasRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Enfermarias}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EnfermariasResource {

    private final Logger log = LoggerFactory.getLogger(EnfermariasResource.class);

    private static final String ENTITY_NAME = "enfermarias";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnfermariasRepository enfermariasRepository;

    public EnfermariasResource(EnfermariasRepository enfermariasRepository) {
        this.enfermariasRepository = enfermariasRepository;
    }

    /**
     * {@code POST  /enfermarias} : Create a new enfermarias.
     *
     * @param enfermarias the enfermarias to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfermarias, or with status {@code 400 (Bad Request)} if the enfermarias has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enfermarias")
    public ResponseEntity<Enfermarias> createEnfermarias(@Valid @RequestBody Enfermarias enfermarias) throws URISyntaxException {
        log.debug("REST request to save Enfermarias : {}", enfermarias);
        if (enfermarias.getId() != null) {
            throw new BadRequestAlertException("A new enfermarias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enfermarias result = enfermariasRepository.save(enfermarias);
        return ResponseEntity
            .created(new URI("/api/enfermarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enfermarias/:id} : Updates an existing enfermarias.
     *
     * @param id the id of the enfermarias to save.
     * @param enfermarias the enfermarias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermarias,
     * or with status {@code 400 (Bad Request)} if the enfermarias is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfermarias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enfermarias/{id}")
    public ResponseEntity<Enfermarias> updateEnfermarias(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Enfermarias enfermarias
    ) throws URISyntaxException {
        log.debug("REST request to update Enfermarias : {}, {}", id, enfermarias);
        if (enfermarias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermarias.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermariasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Enfermarias result = enfermariasRepository.save(enfermarias);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfermarias.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /enfermarias/:id} : Partial updates given fields of an existing enfermarias, field will ignore if it is null
     *
     * @param id the id of the enfermarias to save.
     * @param enfermarias the enfermarias to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermarias,
     * or with status {@code 400 (Bad Request)} if the enfermarias is not valid,
     * or with status {@code 404 (Not Found)} if the enfermarias is not found,
     * or with status {@code 500 (Internal Server Error)} if the enfermarias couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enfermarias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Enfermarias> partialUpdateEnfermarias(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Enfermarias enfermarias
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enfermarias partially : {}, {}", id, enfermarias);
        if (enfermarias.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermarias.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermariasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Enfermarias> result = enfermariasRepository
            .findById(enfermarias.getId())
            .map(
                existingEnfermarias -> {
                    if (enfermarias.getEnfermaria() != null) {
                        existingEnfermarias.setEnfermaria(enfermarias.getEnfermaria());
                    }

                    return existingEnfermarias;
                }
            )
            .map(enfermariasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfermarias.getId().toString())
        );
    }

    /**
     * {@code GET  /enfermarias} : get all the enfermarias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enfermarias in body.
     */
    @GetMapping("/enfermarias")
    public List<Enfermarias> getAllEnfermarias() {
        log.debug("REST request to get all Enfermarias");
        return enfermariasRepository.findAll();
    }

    /**
     * {@code GET  /enfermarias/:id} : get the "id" enfermarias.
     *
     * @param id the id of the enfermarias to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfermarias, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enfermarias/{id}")
    public ResponseEntity<Enfermarias> getEnfermarias(@PathVariable Long id) {
        log.debug("REST request to get Enfermarias : {}", id);
        Optional<Enfermarias> enfermarias = enfermariasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enfermarias);
    }

    /**
     * {@code DELETE  /enfermarias/:id} : delete the "id" enfermarias.
     *
     * @param id the id of the enfermarias to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enfermarias/{id}")
    public ResponseEntity<Void> deleteEnfermarias(@PathVariable Long id) {
        log.debug("REST request to delete Enfermarias : {}", id);
        enfermariasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
