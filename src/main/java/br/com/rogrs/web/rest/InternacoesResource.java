package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Internacoes;
import br.com.rogrs.repository.InternacoesRepository;
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
 * REST controller for managing {@link br.com.rogrs.domain.Internacoes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InternacoesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesResource.class);

    private static final String ENTITY_NAME = "internacoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternacoesRepository internacoesRepository;

    public InternacoesResource(InternacoesRepository internacoesRepository) {
        this.internacoesRepository = internacoesRepository;
    }

    /**
     * {@code POST  /internacoes} : Create a new internacoes.
     *
     * @param internacoes the internacoes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internacoes, or with status {@code 400 (Bad Request)} if the internacoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/internacoes")
    public ResponseEntity<Internacoes> createInternacoes(@Valid @RequestBody Internacoes internacoes) throws URISyntaxException {
        log.debug("REST request to save Internacoes : {}", internacoes);
        if (internacoes.getId() != null) {
            throw new BadRequestAlertException("A new internacoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Internacoes result = internacoesRepository.save(internacoes);
        return ResponseEntity
            .created(new URI("/api/internacoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /internacoes/:id} : Updates an existing internacoes.
     *
     * @param id the id of the internacoes to save.
     * @param internacoes the internacoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoes,
     * or with status {@code 400 (Bad Request)} if the internacoes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internacoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/internacoes/{id}")
    public ResponseEntity<Internacoes> updateInternacoes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Internacoes internacoes
    ) throws URISyntaxException {
        log.debug("REST request to update Internacoes : {}, {}", id, internacoes);
        if (internacoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internacoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internacoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Internacoes result = internacoesRepository.save(internacoes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /internacoes/:id} : Partial updates given fields of an existing internacoes, field will ignore if it is null
     *
     * @param id the id of the internacoes to save.
     * @param internacoes the internacoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internacoes,
     * or with status {@code 400 (Bad Request)} if the internacoes is not valid,
     * or with status {@code 404 (Not Found)} if the internacoes is not found,
     * or with status {@code 500 (Internal Server Error)} if the internacoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/internacoes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Internacoes> partialUpdateInternacoes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Internacoes internacoes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Internacoes partially : {}, {}", id, internacoes);
        if (internacoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internacoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internacoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Internacoes> result = internacoesRepository
            .findById(internacoes.getId())
            .map(
                existingInternacoes -> {
                    if (internacoes.getDataInternacao() != null) {
                        existingInternacoes.setDataInternacao(internacoes.getDataInternacao());
                    }
                    if (internacoes.getDescricao() != null) {
                        existingInternacoes.setDescricao(internacoes.getDescricao());
                    }

                    return existingInternacoes;
                }
            )
            .map(internacoesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internacoes.getId().toString())
        );
    }

    /**
     * {@code GET  /internacoes} : get all the internacoes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internacoes in body.
     */
    @GetMapping("/internacoes")
    public List<Internacoes> getAllInternacoes() {
        log.debug("REST request to get all Internacoes");
        return internacoesRepository.findAll();
    }

    /**
     * {@code GET  /internacoes/:id} : get the "id" internacoes.
     *
     * @param id the id of the internacoes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internacoes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internacoes/{id}")
    public ResponseEntity<Internacoes> getInternacoes(@PathVariable Long id) {
        log.debug("REST request to get Internacoes : {}", id);
        Optional<Internacoes> internacoes = internacoesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(internacoes);
    }

    /**
     * {@code DELETE  /internacoes/:id} : delete the "id" internacoes.
     *
     * @param id the id of the internacoes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/internacoes/{id}")
    public ResponseEntity<Void> deleteInternacoes(@PathVariable Long id) {
        log.debug("REST request to delete Internacoes : {}", id);
        internacoesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
