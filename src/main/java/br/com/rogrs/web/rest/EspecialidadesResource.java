package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Especialidades;
import br.com.rogrs.repository.EspecialidadesRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Especialidades}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EspecialidadesResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadesResource.class);

    private static final String ENTITY_NAME = "especialidades";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecialidadesRepository especialidadesRepository;

    public EspecialidadesResource(EspecialidadesRepository especialidadesRepository) {
        this.especialidadesRepository = especialidadesRepository;
    }

    /**
     * {@code POST  /especialidades} : Create a new especialidades.
     *
     * @param especialidades the especialidades to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especialidades, or with status {@code 400 (Bad Request)} if the especialidades has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especialidades")
    public ResponseEntity<Especialidades> createEspecialidades(@Valid @RequestBody Especialidades especialidades)
        throws URISyntaxException {
        log.debug("REST request to save Especialidades : {}", especialidades);
        if (especialidades.getId() != null) {
            throw new BadRequestAlertException("A new especialidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        return ResponseEntity
            .created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especialidades/:id} : Updates an existing especialidades.
     *
     * @param id the id of the especialidades to save.
     * @param especialidades the especialidades to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidades,
     * or with status {@code 400 (Bad Request)} if the especialidades is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especialidades couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especialidades/{id}")
    public ResponseEntity<Especialidades> updateEspecialidades(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Especialidades especialidades
    ) throws URISyntaxException {
        log.debug("REST request to update Especialidades : {}, {}", id, especialidades);
        if (especialidades.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidades.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especialidadesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Especialidades result = especialidadesRepository.save(especialidades);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especialidades.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /especialidades/:id} : Partial updates given fields of an existing especialidades, field will ignore if it is null
     *
     * @param id the id of the especialidades to save.
     * @param especialidades the especialidades to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidades,
     * or with status {@code 400 (Bad Request)} if the especialidades is not valid,
     * or with status {@code 404 (Not Found)} if the especialidades is not found,
     * or with status {@code 500 (Internal Server Error)} if the especialidades couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/especialidades/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Especialidades> partialUpdateEspecialidades(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Especialidades especialidades
    ) throws URISyntaxException {
        log.debug("REST request to partial update Especialidades partially : {}, {}", id, especialidades);
        if (especialidades.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especialidades.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especialidadesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Especialidades> result = especialidadesRepository
            .findById(especialidades.getId())
            .map(
                existingEspecialidades -> {
                    if (especialidades.getEspecialidade() != null) {
                        existingEspecialidades.setEspecialidade(especialidades.getEspecialidade());
                    }

                    return existingEspecialidades;
                }
            )
            .map(especialidadesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especialidades.getId().toString())
        );
    }

    /**
     * {@code GET  /especialidades} : get all the especialidades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especialidades in body.
     */
    @GetMapping("/especialidades")
    public List<Especialidades> getAllEspecialidades() {
        log.debug("REST request to get all Especialidades");
        return especialidadesRepository.findAll();
    }

    /**
     * {@code GET  /especialidades/:id} : get the "id" especialidades.
     *
     * @param id the id of the especialidades to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especialidades, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/especialidades/{id}")
    public ResponseEntity<Especialidades> getEspecialidades(@PathVariable Long id) {
        log.debug("REST request to get Especialidades : {}", id);
        Optional<Especialidades> especialidades = especialidadesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(especialidades);
    }

    /**
     * {@code DELETE  /especialidades/:id} : delete the "id" especialidades.
     *
     * @param id the id of the especialidades to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<Void> deleteEspecialidades(@PathVariable Long id) {
        log.debug("REST request to delete Especialidades : {}", id);
        especialidadesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
