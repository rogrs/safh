package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Especialidades;
import br.com.rogrs.repository.EspecialidadesRepository;
import br.com.rogrs.repository.search.EspecialidadesSearchRepository;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link br.com.rogrs.domain.Especialidades}.
 */
@RestController
@RequestMapping("/api")
public class EspecialidadesResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadesResource.class);

    private static final String ENTITY_NAME = "especialidades";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecialidadesRepository especialidadesRepository;

    private final EspecialidadesSearchRepository especialidadesSearchRepository;

    public EspecialidadesResource(EspecialidadesRepository especialidadesRepository, EspecialidadesSearchRepository especialidadesSearchRepository) {
        this.especialidadesRepository = especialidadesRepository;
        this.especialidadesSearchRepository = especialidadesSearchRepository;
    }

    /**
     * {@code POST  /especialidades} : Create a new especialidades.
     *
     * @param especialidades the especialidades to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especialidades, or with status {@code 400 (Bad Request)} if the especialidades has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especialidades")
    public ResponseEntity<Especialidades> createEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to save Especialidades : {}", especialidades);
        if (especialidades.getId() != null) {
            throw new BadRequestAlertException("A new especialidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especialidades} : Updates an existing especialidades.
     *
     * @param especialidades the especialidades to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidades,
     * or with status {@code 400 (Bad Request)} if the especialidades is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especialidades couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especialidades")
    public ResponseEntity<Especialidades> updateEspecialidades(@Valid @RequestBody Especialidades especialidades) throws URISyntaxException {
        log.debug("REST request to update Especialidades : {}", especialidades);
        if (especialidades.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Especialidades result = especialidadesRepository.save(especialidades);
        especialidadesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especialidades.getId().toString()))
            .body(result);
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
    public ResponseEntity<Especialidades> getEspecialidades(@PathVariable String id) {
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
    public ResponseEntity<Void> deleteEspecialidades(@PathVariable String id) {
        log.debug("REST request to delete Especialidades : {}", id);
        especialidadesRepository.deleteById(id);
        especialidadesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/especialidades?query=:query} : search for the especialidades corresponding
     * to the query.
     *
     * @param query the query of the especialidades search.
     * @return the result of the search.
     */
    @GetMapping("/_search/especialidades")
    public List<Especialidades> searchEspecialidades(@RequestParam String query) {
        log.debug("REST request to search Especialidades for query {}", query);
        return StreamSupport
            .stream(especialidadesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
