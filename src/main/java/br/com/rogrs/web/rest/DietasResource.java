package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Dietas;
import br.com.rogrs.repository.DietasRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Dietas}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DietasResource {

    private final Logger log = LoggerFactory.getLogger(DietasResource.class);

    private static final String ENTITY_NAME = "dietas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietasRepository dietasRepository;

    public DietasResource(DietasRepository dietasRepository) {
        this.dietasRepository = dietasRepository;
    }

    /**
     * {@code POST  /dietas} : Create a new dietas.
     *
     * @param dietas the dietas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietas, or with status {@code 400 (Bad Request)} if the dietas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dietas")
    public ResponseEntity<Dietas> createDietas(@Valid @RequestBody Dietas dietas) throws URISyntaxException {
        log.debug("REST request to save Dietas : {}", dietas);
        if (dietas.getId() != null) {
            throw new BadRequestAlertException("A new dietas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dietas result = dietasRepository.save(dietas);
        return ResponseEntity
            .created(new URI("/api/dietas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dietas/:id} : Updates an existing dietas.
     *
     * @param id the id of the dietas to save.
     * @param dietas the dietas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietas,
     * or with status {@code 400 (Bad Request)} if the dietas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dietas/{id}")
    public ResponseEntity<Dietas> updateDietas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Dietas dietas
    ) throws URISyntaxException {
        log.debug("REST request to update Dietas : {}, {}", id, dietas);
        if (dietas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dietas result = dietasRepository.save(dietas);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietas.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dietas/:id} : Partial updates given fields of an existing dietas, field will ignore if it is null
     *
     * @param id the id of the dietas to save.
     * @param dietas the dietas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietas,
     * or with status {@code 400 (Bad Request)} if the dietas is not valid,
     * or with status {@code 404 (Not Found)} if the dietas is not found,
     * or with status {@code 500 (Internal Server Error)} if the dietas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dietas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Dietas> partialUpdateDietas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dietas dietas
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dietas partially : {}, {}", id, dietas);
        if (dietas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dietas> result = dietasRepository
            .findById(dietas.getId())
            .map(
                existingDietas -> {
                    if (dietas.getDieta() != null) {
                        existingDietas.setDieta(dietas.getDieta());
                    }
                    if (dietas.getDescricao() != null) {
                        existingDietas.setDescricao(dietas.getDescricao());
                    }

                    return existingDietas;
                }
            )
            .map(dietasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietas.getId().toString())
        );
    }

    /**
     * {@code GET  /dietas} : get all the dietas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietas in body.
     */
    @GetMapping("/dietas")
    public List<Dietas> getAllDietas() {
        log.debug("REST request to get all Dietas");
        return dietasRepository.findAll();
    }

    /**
     * {@code GET  /dietas/:id} : get the "id" dietas.
     *
     * @param id the id of the dietas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dietas/{id}")
    public ResponseEntity<Dietas> getDietas(@PathVariable Long id) {
        log.debug("REST request to get Dietas : {}", id);
        Optional<Dietas> dietas = dietasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dietas);
    }

    /**
     * {@code DELETE  /dietas/:id} : delete the "id" dietas.
     *
     * @param id the id of the dietas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dietas/{id}")
    public ResponseEntity<Void> deleteDietas(@PathVariable Long id) {
        log.debug("REST request to delete Dietas : {}", id);
        dietasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
