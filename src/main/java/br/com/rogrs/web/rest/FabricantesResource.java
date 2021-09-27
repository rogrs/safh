package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Fabricantes;
import br.com.rogrs.repository.FabricantesRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Fabricantes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FabricantesResource {

    private final Logger log = LoggerFactory.getLogger(FabricantesResource.class);

    private static final String ENTITY_NAME = "fabricantes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FabricantesRepository fabricantesRepository;

    public FabricantesResource(FabricantesRepository fabricantesRepository) {
        this.fabricantesRepository = fabricantesRepository;
    }

    /**
     * {@code POST  /fabricantes} : Create a new fabricantes.
     *
     * @param fabricantes the fabricantes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fabricantes, or with status {@code 400 (Bad Request)} if the fabricantes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fabricantes")
    public ResponseEntity<Fabricantes> createFabricantes(@Valid @RequestBody Fabricantes fabricantes) throws URISyntaxException {
        log.debug("REST request to save Fabricantes : {}", fabricantes);
        if (fabricantes.getId() != null) {
            throw new BadRequestAlertException("A new fabricantes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fabricantes result = fabricantesRepository.save(fabricantes);
        return ResponseEntity
            .created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fabricantes/:id} : Updates an existing fabricantes.
     *
     * @param id the id of the fabricantes to save.
     * @param fabricantes the fabricantes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricantes,
     * or with status {@code 400 (Bad Request)} if the fabricantes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fabricantes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fabricantes/{id}")
    public ResponseEntity<Fabricantes> updateFabricantes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fabricantes fabricantes
    ) throws URISyntaxException {
        log.debug("REST request to update Fabricantes : {}, {}", id, fabricantes);
        if (fabricantes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fabricantes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fabricantesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fabricantes result = fabricantesRepository.save(fabricantes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fabricantes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fabricantes/:id} : Partial updates given fields of an existing fabricantes, field will ignore if it is null
     *
     * @param id the id of the fabricantes to save.
     * @param fabricantes the fabricantes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricantes,
     * or with status {@code 400 (Bad Request)} if the fabricantes is not valid,
     * or with status {@code 404 (Not Found)} if the fabricantes is not found,
     * or with status {@code 500 (Internal Server Error)} if the fabricantes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fabricantes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Fabricantes> partialUpdateFabricantes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fabricantes fabricantes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fabricantes partially : {}, {}", id, fabricantes);
        if (fabricantes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fabricantes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fabricantesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fabricantes> result = fabricantesRepository
            .findById(fabricantes.getId())
            .map(
                existingFabricantes -> {
                    if (fabricantes.getFabricante() != null) {
                        existingFabricantes.setFabricante(fabricantes.getFabricante());
                    }

                    return existingFabricantes;
                }
            )
            .map(fabricantesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fabricantes.getId().toString())
        );
    }

    /**
     * {@code GET  /fabricantes} : get all the fabricantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fabricantes in body.
     */
    @GetMapping("/fabricantes")
    public List<Fabricantes> getAllFabricantes() {
        log.debug("REST request to get all Fabricantes");
        return fabricantesRepository.findAll();
    }

    /**
     * {@code GET  /fabricantes/:id} : get the "id" fabricantes.
     *
     * @param id the id of the fabricantes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fabricantes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fabricantes/{id}")
    public ResponseEntity<Fabricantes> getFabricantes(@PathVariable Long id) {
        log.debug("REST request to get Fabricantes : {}", id);
        Optional<Fabricantes> fabricantes = fabricantesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fabricantes);
    }

    /**
     * {@code DELETE  /fabricantes/:id} : delete the "id" fabricantes.
     *
     * @param id the id of the fabricantes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fabricantes/{id}")
    public ResponseEntity<Void> deleteFabricantes(@PathVariable Long id) {
        log.debug("REST request to delete Fabricantes : {}", id);
        fabricantesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
