package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Medicos;
import br.com.rogrs.repository.MedicosRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Medicos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MedicosResource {

    private final Logger log = LoggerFactory.getLogger(MedicosResource.class);

    private static final String ENTITY_NAME = "medicos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicosRepository medicosRepository;

    public MedicosResource(MedicosRepository medicosRepository) {
        this.medicosRepository = medicosRepository;
    }

    /**
     * {@code POST  /medicos} : Create a new medicos.
     *
     * @param medicos the medicos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicos, or with status {@code 400 (Bad Request)} if the medicos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicos")
    public ResponseEntity<Medicos> createMedicos(@Valid @RequestBody Medicos medicos) throws URISyntaxException {
        log.debug("REST request to save Medicos : {}", medicos);
        if (medicos.getId() != null) {
            throw new BadRequestAlertException("A new medicos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicos result = medicosRepository.save(medicos);
        return ResponseEntity
            .created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicos/:id} : Updates an existing medicos.
     *
     * @param id the id of the medicos to save.
     * @param medicos the medicos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicos,
     * or with status {@code 400 (Bad Request)} if the medicos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicos/{id}")
    public ResponseEntity<Medicos> updateMedicos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Medicos medicos
    ) throws URISyntaxException {
        log.debug("REST request to update Medicos : {}, {}", id, medicos);
        if (medicos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Medicos result = medicosRepository.save(medicos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medicos/:id} : Partial updates given fields of an existing medicos, field will ignore if it is null
     *
     * @param id the id of the medicos to save.
     * @param medicos the medicos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicos,
     * or with status {@code 400 (Bad Request)} if the medicos is not valid,
     * or with status {@code 404 (Not Found)} if the medicos is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medicos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Medicos> partialUpdateMedicos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Medicos medicos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medicos partially : {}, {}", id, medicos);
        if (medicos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medicos> result = medicosRepository
            .findById(medicos.getId())
            .map(
                existingMedicos -> {
                    if (medicos.getNome() != null) {
                        existingMedicos.setNome(medicos.getNome());
                    }
                    if (medicos.getCrm() != null) {
                        existingMedicos.setCrm(medicos.getCrm());
                    }
                    if (medicos.getCpf() != null) {
                        existingMedicos.setCpf(medicos.getCpf());
                    }
                    if (medicos.getEmail() != null) {
                        existingMedicos.setEmail(medicos.getEmail());
                    }
                    if (medicos.getCep() != null) {
                        existingMedicos.setCep(medicos.getCep());
                    }
                    if (medicos.getLogradouro() != null) {
                        existingMedicos.setLogradouro(medicos.getLogradouro());
                    }
                    if (medicos.getNumero() != null) {
                        existingMedicos.setNumero(medicos.getNumero());
                    }
                    if (medicos.getComplemento() != null) {
                        existingMedicos.setComplemento(medicos.getComplemento());
                    }
                    if (medicos.getBairro() != null) {
                        existingMedicos.setBairro(medicos.getBairro());
                    }
                    if (medicos.getCidade() != null) {
                        existingMedicos.setCidade(medicos.getCidade());
                    }
                    if (medicos.getuF() != null) {
                        existingMedicos.setuF(medicos.getuF());
                    }

                    return existingMedicos;
                }
            )
            .map(medicosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicos.getId().toString())
        );
    }

    /**
     * {@code GET  /medicos} : get all the medicos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicos in body.
     */
    @GetMapping("/medicos")
    public List<Medicos> getAllMedicos() {
        log.debug("REST request to get all Medicos");
        return medicosRepository.findAll();
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medicos.
     *
     * @param id the id of the medicos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medicos> getMedicos(@PathVariable Long id) {
        log.debug("REST request to get Medicos : {}", id);
        Optional<Medicos> medicos = medicosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicos);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medicos.
     *
     * @param id the id of the medicos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicos/{id}")
    public ResponseEntity<Void> deleteMedicos(@PathVariable Long id) {
        log.debug("REST request to delete Medicos : {}", id);
        medicosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
