package br.com.rogrs.web.rest;

import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
import br.com.rogrs.service.InternacoesDetalhesService;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.rogrs.domain.InternacoesDetalhes}.
 */
@RestController
@RequestMapping("/api")
public class InternacoesDetalhesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesResource.class);

    private static final String ENTITY_NAME = "internacoesDetalhes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternacoesDetalhesService internacoesDetalhesService;

    private final InternacoesDetalhesRepository internacoesDetalhesRepository;

    public InternacoesDetalhesResource(
        InternacoesDetalhesService internacoesDetalhesService,
        InternacoesDetalhesRepository internacoesDetalhesRepository
    ) {
        this.internacoesDetalhesService = internacoesDetalhesService;
        this.internacoesDetalhesRepository = internacoesDetalhesRepository;
    }

    /**
     * {@code POST  /internacoes-detalhes} : Create a new internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internacoesDetalhes, or with status {@code 400 (Bad Request)} if the internacoesDetalhes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/internacoes-detalhes")
    public ResponseEntity<InternacoesDetalhes> createInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes)
        throws URISyntaxException {
        log.debug("REST request to save InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() != null) {
            throw new BadRequestAlertException("A new internacoesDetalhes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity
            .created(new URI("/api/internacoes-detalhes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /internacoes-detalhes/:id} : Updates an existing internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to save.
     * @param internacoesDetalhes the internacoesDetalhes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoesDetalhes,
     * or with status {@code 400 (Bad Request)} if the internacoesDetalhes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internacoesDetalhes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/internacoes-detalhes/{id}")
    public ResponseEntity<InternacoesDetalhes> updateInternacoesDetalhes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InternacoesDetalhes internacoesDetalhes
    ) throws URISyntaxException {
        log.debug("REST request to update InternacoesDetalhes : {}, {}", id, internacoesDetalhes);
        if (internacoesDetalhes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internacoesDetalhes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internacoesDetalhesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoesDetalhes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /internacoes-detalhes/:id} : Partial updates given fields of an existing internacoesDetalhes, field will ignore if it is null
     *
     * @param id the id of the internacoesDetalhes to save.
     * @param internacoesDetalhes the internacoesDetalhes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoesDetalhes,
     * or with status {@code 400 (Bad Request)} if the internacoesDetalhes is not valid,
     * or with status {@code 404 (Not Found)} if the internacoesDetalhes is not found,
     * or with status {@code 500 (Internal Server Error)} if the internacoesDetalhes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/internacoes-detalhes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<InternacoesDetalhes> partialUpdateInternacoesDetalhes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InternacoesDetalhes internacoesDetalhes
    ) throws URISyntaxException {
        log.debug("REST request to partial update InternacoesDetalhes partially : {}, {}", id, internacoesDetalhes);
        if (internacoesDetalhes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internacoesDetalhes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internacoesDetalhesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InternacoesDetalhes> result = internacoesDetalhesService.partialUpdate(internacoesDetalhes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoesDetalhes.getId().toString())
        );
    }

    /**
     * {@code GET  /internacoes-detalhes} : get all the internacoesDetalhes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internacoesDetalhes in body.
     */
    @GetMapping("/internacoes-detalhes")
    public List<InternacoesDetalhes> getAllInternacoesDetalhes() {
        log.debug("REST request to get all InternacoesDetalhes");
        return internacoesDetalhesService.findAll();
    }

    /**
     * {@code GET  /internacoes-detalhes/:id} : get the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internacoesDetalhes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internacoes-detalhes/{id}")
    public ResponseEntity<InternacoesDetalhes> getInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to get InternacoesDetalhes : {}", id);
        Optional<InternacoesDetalhes> internacoesDetalhes = internacoesDetalhesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(internacoesDetalhes);
    }

    /**
     * {@code DELETE  /internacoes-detalhes/:id} : delete the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/internacoes-detalhes/{id}")
    public ResponseEntity<Void> deleteInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
