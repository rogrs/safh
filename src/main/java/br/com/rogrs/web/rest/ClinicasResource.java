package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Clinicas;
import br.com.rogrs.repository.ClinicasRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Clinicas}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClinicasResource {

    private final Logger log = LoggerFactory.getLogger(ClinicasResource.class);

    private static final String ENTITY_NAME = "clinicas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClinicasRepository clinicasRepository;

    public ClinicasResource(ClinicasRepository clinicasRepository) {
        this.clinicasRepository = clinicasRepository;
    }

    /**
     * {@code POST  /clinicas} : Create a new clinicas.
     *
     * @param clinicas the clinicas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clinicas, or with status {@code 400 (Bad Request)} if the clinicas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clinicas")
    public ResponseEntity<Clinicas> createClinicas(@Valid @RequestBody Clinicas clinicas) throws URISyntaxException {
        log.debug("REST request to save Clinicas : {}", clinicas);
        if (clinicas.getId() != null) {
            throw new BadRequestAlertException("A new clinicas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clinicas result = clinicasRepository.save(clinicas);
        return ResponseEntity
            .created(new URI("/api/clinicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clinicas/:id} : Updates an existing clinicas.
     *
     * @param id the id of the clinicas to save.
     * @param clinicas the clinicas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicas,
     * or with status {@code 400 (Bad Request)} if the clinicas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clinicas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clinicas/{id}")
    public ResponseEntity<Clinicas> updateClinicas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Clinicas clinicas
    ) throws URISyntaxException {
        log.debug("REST request to update Clinicas : {}, {}", id, clinicas);
        if (clinicas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clinicas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clinicasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clinicas result = clinicasRepository.save(clinicas);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clinicas.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clinicas/:id} : Partial updates given fields of an existing clinicas, field will ignore if it is null
     *
     * @param id the id of the clinicas to save.
     * @param clinicas the clinicas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicas,
     * or with status {@code 400 (Bad Request)} if the clinicas is not valid,
     * or with status {@code 404 (Not Found)} if the clinicas is not found,
     * or with status {@code 500 (Internal Server Error)} if the clinicas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clinicas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Clinicas> partialUpdateClinicas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Clinicas clinicas
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clinicas partially : {}, {}", id, clinicas);
        if (clinicas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clinicas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clinicasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clinicas> result = clinicasRepository
            .findById(clinicas.getId())
            .map(
                existingClinicas -> {
                    if (clinicas.getClinica() != null) {
                        existingClinicas.setClinica(clinicas.getClinica());
                    }
                    if (clinicas.getDescricao() != null) {
                        existingClinicas.setDescricao(clinicas.getDescricao());
                    }

                    return existingClinicas;
                }
            )
            .map(clinicasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clinicas.getId().toString())
        );
    }

    /**
     * {@code GET  /clinicas} : get all the clinicas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clinicas in body.
     */
    @GetMapping("/clinicas")
    public List<Clinicas> getAllClinicas() {
        log.debug("REST request to get all Clinicas");
        return clinicasRepository.findAll();
    }

    /**
     * {@code GET  /clinicas/:id} : get the "id" clinicas.
     *
     * @param id the id of the clinicas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clinicas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clinicas/{id}")
    public ResponseEntity<Clinicas> getClinicas(@PathVariable Long id) {
        log.debug("REST request to get Clinicas : {}", id);
        Optional<Clinicas> clinicas = clinicasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clinicas);
    }

    /**
     * {@code DELETE  /clinicas/:id} : delete the "id" clinicas.
     *
     * @param id the id of the clinicas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clinicas/{id}")
    public ResponseEntity<Void> deleteClinicas(@PathVariable Long id) {
        log.debug("REST request to delete Clinicas : {}", id);
        clinicasRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
