package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Prescricoes;
import br.com.rogrs.repository.PrescricoesRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Prescricoes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrescricoesResource {

    private final Logger log = LoggerFactory.getLogger(PrescricoesResource.class);

    private static final String ENTITY_NAME = "prescricoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescricoesRepository prescricoesRepository;

    public PrescricoesResource(PrescricoesRepository prescricoesRepository) {
        this.prescricoesRepository = prescricoesRepository;
    }

    /**
     * {@code POST  /prescricoes} : Create a new prescricoes.
     *
     * @param prescricoes the prescricoes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescricoes, or with status {@code 400 (Bad Request)} if the prescricoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prescricoes")
    public ResponseEntity<Prescricoes> createPrescricoes(@Valid @RequestBody Prescricoes prescricoes) throws URISyntaxException {
        log.debug("REST request to save Prescricoes : {}", prescricoes);
        if (prescricoes.getId() != null) {
            throw new BadRequestAlertException("A new prescricoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prescricoes result = prescricoesRepository.save(prescricoes);
        return ResponseEntity
            .created(new URI("/api/prescricoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prescricoes/:id} : Updates an existing prescricoes.
     *
     * @param id the id of the prescricoes to save.
     * @param prescricoes the prescricoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescricoes,
     * or with status {@code 400 (Bad Request)} if the prescricoes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescricoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prescricoes/{id}")
    public ResponseEntity<Prescricoes> updatePrescricoes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Prescricoes prescricoes
    ) throws URISyntaxException {
        log.debug("REST request to update Prescricoes : {}, {}", id, prescricoes);
        if (prescricoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescricoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescricoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prescricoes result = prescricoesRepository.save(prescricoes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prescricoes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prescricoes/:id} : Partial updates given fields of an existing prescricoes, field will ignore if it is null
     *
     * @param id the id of the prescricoes to save.
     * @param prescricoes the prescricoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescricoes,
     * or with status {@code 400 (Bad Request)} if the prescricoes is not valid,
     * or with status {@code 404 (Not Found)} if the prescricoes is not found,
     * or with status {@code 500 (Internal Server Error)} if the prescricoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prescricoes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Prescricoes> partialUpdatePrescricoes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prescricoes prescricoes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prescricoes partially : {}, {}", id, prescricoes);
        if (prescricoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescricoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescricoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prescricoes> result = prescricoesRepository
            .findById(prescricoes.getId())
            .map(
                existingPrescricoes -> {
                    if (prescricoes.getPrescricao() != null) {
                        existingPrescricoes.setPrescricao(prescricoes.getPrescricao());
                    }

                    return existingPrescricoes;
                }
            )
            .map(prescricoesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prescricoes.getId().toString())
        );
    }

    /**
     * {@code GET  /prescricoes} : get all the prescricoes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prescricoes in body.
     */
    @GetMapping("/prescricoes")
    public List<Prescricoes> getAllPrescricoes() {
        log.debug("REST request to get all Prescricoes");
        return prescricoesRepository.findAll();
    }

    /**
     * {@code GET  /prescricoes/:id} : get the "id" prescricoes.
     *
     * @param id the id of the prescricoes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescricoes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prescricoes/{id}")
    public ResponseEntity<Prescricoes> getPrescricoes(@PathVariable Long id) {
        log.debug("REST request to get Prescricoes : {}", id);
        Optional<Prescricoes> prescricoes = prescricoesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prescricoes);
    }

    /**
     * {@code DELETE  /prescricoes/:id} : delete the "id" prescricoes.
     *
     * @param id the id of the prescricoes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prescricoes/{id}")
    public ResponseEntity<Void> deletePrescricoes(@PathVariable Long id) {
        log.debug("REST request to delete Prescricoes : {}", id);
        prescricoesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
