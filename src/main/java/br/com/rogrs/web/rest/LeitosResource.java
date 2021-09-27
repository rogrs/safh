package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Leitos;
import br.com.rogrs.repository.LeitosRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Leitos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LeitosResource {

    private final Logger log = LoggerFactory.getLogger(LeitosResource.class);

    private static final String ENTITY_NAME = "leitos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeitosRepository leitosRepository;

    public LeitosResource(LeitosRepository leitosRepository) {
        this.leitosRepository = leitosRepository;
    }

    /**
     * {@code POST  /leitos} : Create a new leitos.
     *
     * @param leitos the leitos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leitos, or with status {@code 400 (Bad Request)} if the leitos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leitos")
    public ResponseEntity<Leitos> createLeitos(@Valid @RequestBody Leitos leitos) throws URISyntaxException {
        log.debug("REST request to save Leitos : {}", leitos);
        if (leitos.getId() != null) {
            throw new BadRequestAlertException("A new leitos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Leitos result = leitosRepository.save(leitos);
        return ResponseEntity
            .created(new URI("/api/leitos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leitos/:id} : Updates an existing leitos.
     *
     * @param id the id of the leitos to save.
     * @param leitos the leitos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leitos,
     * or with status {@code 400 (Bad Request)} if the leitos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leitos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leitos/{id}")
    public ResponseEntity<Leitos> updateLeitos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Leitos leitos
    ) throws URISyntaxException {
        log.debug("REST request to update Leitos : {}, {}", id, leitos);
        if (leitos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leitos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leitosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Leitos result = leitosRepository.save(leitos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leitos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leitos/:id} : Partial updates given fields of an existing leitos, field will ignore if it is null
     *
     * @param id the id of the leitos to save.
     * @param leitos the leitos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leitos,
     * or with status {@code 400 (Bad Request)} if the leitos is not valid,
     * or with status {@code 404 (Not Found)} if the leitos is not found,
     * or with status {@code 500 (Internal Server Error)} if the leitos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leitos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Leitos> partialUpdateLeitos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Leitos leitos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Leitos partially : {}, {}", id, leitos);
        if (leitos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leitos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leitosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Leitos> result = leitosRepository
            .findById(leitos.getId())
            .map(
                existingLeitos -> {
                    if (leitos.getLeito() != null) {
                        existingLeitos.setLeito(leitos.getLeito());
                    }
                    if (leitos.getTipo() != null) {
                        existingLeitos.setTipo(leitos.getTipo());
                    }

                    return existingLeitos;
                }
            )
            .map(leitosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leitos.getId().toString())
        );
    }

    /**
     * {@code GET  /leitos} : get all the leitos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leitos in body.
     */
    @GetMapping("/leitos")
    public List<Leitos> getAllLeitos() {
        log.debug("REST request to get all Leitos");
        return leitosRepository.findAll();
    }

    /**
     * {@code GET  /leitos/:id} : get the "id" leitos.
     *
     * @param id the id of the leitos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leitos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leitos/{id}")
    public ResponseEntity<Leitos> getLeitos(@PathVariable Long id) {
        log.debug("REST request to get Leitos : {}", id);
        Optional<Leitos> leitos = leitosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(leitos);
    }

    /**
     * {@code DELETE  /leitos/:id} : delete the "id" leitos.
     *
     * @param id the id of the leitos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leitos/{id}")
    public ResponseEntity<Void> deleteLeitos(@PathVariable Long id) {
        log.debug("REST request to delete Leitos : {}", id);
        leitosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
