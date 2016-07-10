package br.com.rogrs.safh.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rogrs.safh.domain.Medicamentos;
import br.com.rogrs.safh.repository.MedicamentosRepository;
import br.com.rogrs.safh.repository.search.MedicamentosSearchRepository;
import br.com.rogrs.safh.web.rest.util.HeaderUtil;
import br.com.rogrs.safh.web.rest.util.PaginationUtil;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Medicamentos.
 */
@RestController
@RequestMapping("/api")
public class MedicamentosResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentosResource.class);
        
    @Inject
    private MedicamentosRepository medicamentosRepository;
    
    @Inject
    private MedicamentosSearchRepository medicamentosSearchRepository;
    
    /**
     * POST  /medicamentos : Create a new medicamentos.
     *
     * @param medicamentos the medicamentos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicamentos, or with status 400 (Bad Request) if the medicamentos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicamentos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicamentos> createMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to save Medicamentos : {}", medicamentos);
        if (medicamentos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("medicamentos", "idexists", "A new medicamentos cannot already have an ID")).body(null);
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("medicamentos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicamentos : Updates an existing medicamentos.
     *
     * @param medicamentos the medicamentos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicamentos,
     * or with status 400 (Bad Request) if the medicamentos is not valid,
     * or with status 500 (Internal Server Error) if the medicamentos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicamentos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicamentos> updateMedicamentos(@Valid @RequestBody Medicamentos medicamentos) throws URISyntaxException {
        log.debug("REST request to update Medicamentos : {}", medicamentos);
        if (medicamentos.getId() == null) {
            return createMedicamentos(medicamentos);
        }
        Medicamentos result = medicamentosRepository.save(medicamentos);
        medicamentosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("medicamentos", medicamentos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicamentos : get all the medicamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicamentos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/medicamentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicamentos>> getAllMedicamentos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Medicamentos");
        Page<Medicamentos> page = medicamentosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medicamentos/:id : get the "id" medicamentos.
     *
     * @param id the id of the medicamentos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicamentos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/medicamentos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicamentos> getMedicamentos(@PathVariable Long id) {
        log.debug("REST request to get Medicamentos : {}", id);
        Medicamentos medicamentos = medicamentosRepository.findOne(id);
        return Optional.ofNullable(medicamentos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medicamentos/:id : delete the "id" medicamentos.
     *
     * @param id the id of the medicamentos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/medicamentos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMedicamentos(@PathVariable Long id) {
        log.debug("REST request to delete Medicamentos : {}", id);
        medicamentosRepository.delete(id);
        medicamentosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medicamentos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicamentos?query=:query : search for the medicamentos corresponding
     * to the query.
     *
     * @param query the query of the medicamentos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/medicamentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicamentos>> searchMedicamentos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Medicamentos for query {}", query);
        Page<Medicamentos> page = medicamentosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
