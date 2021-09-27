package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Medicamentos;
import br.com.rogrs.repository.MedicamentosRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Medicamentos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MedicamentosResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentosResource.class);

    private static final String ENTITY_NAME = "medicamentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentosRepository medicamentosRepository;

    public MedicamentosResource(MedicamentosRepository medicamentosRepository) {
        this.medicamentosRepository = medicamentosRepository;
    }

    /**
     * {@code POST  /medicamentos} : Create a new medicamentos.
     *
     * @param medicamentos the medicamentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamentos, or with status {@code 400 (Bad Request)} if the medicamentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicamentos")
    public ResponseEntity<Medicamentos> createMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to save Medicamentos : {}", medicamentos);
        if (medicamentos.getId() != null) {
            throw new BadRequestAlertException("A new medicamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        return ResponseEntity
            .created(new URI("/api/medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicamentos/:id} : Updates an existing medicamentos.
     *
     * @param id the id of the medicamentos to save.
     * @param medicamentos the medicamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentos,
     * or with status {@code 400 (Bad Request)} if the medicamentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicamentos/{id}")
    public ResponseEntity<Medicamentos> updateMedicamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Medicamentos medicamentos
    ) throws URISyntaxException {
        log.debug("REST request to update Medicamentos : {}, {}", id, medicamentos);
        if (medicamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Medicamentos result = medicamentosRepository.save(medicamentos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medicamentos/:id} : Partial updates given fields of an existing medicamentos, field will ignore if it is null
     *
     * @param id the id of the medicamentos to save.
     * @param medicamentos the medicamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentos,
     * or with status {@code 400 (Bad Request)} if the medicamentos is not valid,
     * or with status {@code 404 (Not Found)} if the medicamentos is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medicamentos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Medicamentos> partialUpdateMedicamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Medicamentos medicamentos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medicamentos partially : {}, {}", id, medicamentos);
        if (medicamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medicamentos> result = medicamentosRepository
            .findById(medicamentos.getId())
            .map(
                existingMedicamentos -> {
                    if (medicamentos.getDescricao() != null) {
                        existingMedicamentos.setDescricao(medicamentos.getDescricao());
                    }
                    if (medicamentos.getRegistroMinisterioSaude() != null) {
                        existingMedicamentos.setRegistroMinisterioSaude(medicamentos.getRegistroMinisterioSaude());
                    }
                    if (medicamentos.getCodigoBarras() != null) {
                        existingMedicamentos.setCodigoBarras(medicamentos.getCodigoBarras());
                    }
                    if (medicamentos.getQtdAtual() != null) {
                        existingMedicamentos.setQtdAtual(medicamentos.getQtdAtual());
                    }
                    if (medicamentos.getQtdMin() != null) {
                        existingMedicamentos.setQtdMin(medicamentos.getQtdMin());
                    }
                    if (medicamentos.getQtdMax() != null) {
                        existingMedicamentos.setQtdMax(medicamentos.getQtdMax());
                    }
                    if (medicamentos.getObservacoes() != null) {
                        existingMedicamentos.setObservacoes(medicamentos.getObservacoes());
                    }
                    if (medicamentos.getApresentacao() != null) {
                        existingMedicamentos.setApresentacao(medicamentos.getApresentacao());
                    }

                    return existingMedicamentos;
                }
            )
            .map(medicamentosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentos.getId().toString())
        );
    }

    /**
     * {@code GET  /medicamentos} : get all the medicamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicamentos in body.
     */
    @GetMapping("/medicamentos")
    public List<Medicamentos> getAllMedicamentos() {
        log.debug("REST request to get all Medicamentos");
        return medicamentosRepository.findAll();
    }

    /**
     * {@code GET  /medicamentos/:id} : get the "id" medicamentos.
     *
     * @param id the id of the medicamentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicamentos/{id}")
    public ResponseEntity<Medicamentos> getMedicamentos(@PathVariable Long id) {
        log.debug("REST request to get Medicamentos : {}", id);
        Optional<Medicamentos> medicamentos = medicamentosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicamentos);
    }

    /**
     * {@code DELETE  /medicamentos/:id} : delete the "id" medicamentos.
     *
     * @param id the id of the medicamentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicamentos/{id}")
    public ResponseEntity<Void> deleteMedicamentos(@PathVariable Long id) {
        log.debug("REST request to delete Medicamentos : {}", id);
        medicamentosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
