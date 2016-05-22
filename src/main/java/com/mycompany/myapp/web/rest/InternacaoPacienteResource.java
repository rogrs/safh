package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.InternacaoPaciente;
import com.mycompany.myapp.repository.InternacaoPacienteRepository;
import com.mycompany.myapp.repository.search.InternacaoPacienteSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InternacaoPaciente.
 */
@RestController
@RequestMapping("/api")
public class InternacaoPacienteResource {

    private final Logger log = LoggerFactory.getLogger(InternacaoPacienteResource.class);
        
    @Inject
    private InternacaoPacienteRepository internacaoPacienteRepository;
    
    @Inject
    private InternacaoPacienteSearchRepository internacaoPacienteSearchRepository;
    
    /**
     * POST  /internacao-pacientes : Create a new internacaoPaciente.
     *
     * @param internacaoPaciente the internacaoPaciente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internacaoPaciente, or with status 400 (Bad Request) if the internacaoPaciente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacao-pacientes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacaoPaciente> createInternacaoPaciente(@Valid @RequestBody InternacaoPaciente internacaoPaciente) throws URISyntaxException {
        log.debug("REST request to save InternacaoPaciente : {}", internacaoPaciente);
        if (internacaoPaciente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("internacaoPaciente", "idexists", "A new internacaoPaciente cannot already have an ID")).body(null);
        }
        InternacaoPaciente result = internacaoPacienteRepository.save(internacaoPaciente);
        internacaoPacienteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/internacao-pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("internacaoPaciente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /internacao-pacientes : Updates an existing internacaoPaciente.
     *
     * @param internacaoPaciente the internacaoPaciente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internacaoPaciente,
     * or with status 400 (Bad Request) if the internacaoPaciente is not valid,
     * or with status 500 (Internal Server Error) if the internacaoPaciente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacao-pacientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacaoPaciente> updateInternacaoPaciente(@Valid @RequestBody InternacaoPaciente internacaoPaciente) throws URISyntaxException {
        log.debug("REST request to update InternacaoPaciente : {}", internacaoPaciente);
        if (internacaoPaciente.getId() == null) {
            return createInternacaoPaciente(internacaoPaciente);
        }
        InternacaoPaciente result = internacaoPacienteRepository.save(internacaoPaciente);
        internacaoPacienteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("internacaoPaciente", internacaoPaciente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /internacao-pacientes : get all the internacaoPacientes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internacaoPacientes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/internacao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InternacaoPaciente>> getAllInternacaoPacientes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InternacaoPacientes");
        Page<InternacaoPaciente> page = internacaoPacienteRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internacao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /internacao-pacientes/:id : get the "id" internacaoPaciente.
     *
     * @param id the id of the internacaoPaciente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internacaoPaciente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/internacao-pacientes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacaoPaciente> getInternacaoPaciente(@PathVariable Long id) {
        log.debug("REST request to get InternacaoPaciente : {}", id);
        InternacaoPaciente internacaoPaciente = internacaoPacienteRepository.findOne(id);
        return Optional.ofNullable(internacaoPaciente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /internacao-pacientes/:id : delete the "id" internacaoPaciente.
     *
     * @param id the id of the internacaoPaciente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/internacao-pacientes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInternacaoPaciente(@PathVariable Long id) {
        log.debug("REST request to delete InternacaoPaciente : {}", id);
        internacaoPacienteRepository.delete(id);
        internacaoPacienteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("internacaoPaciente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/internacao-pacientes?query=:query : search for the internacaoPaciente corresponding
     * to the query.
     *
     * @param query the query of the internacaoPaciente search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/internacao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InternacaoPaciente>> searchInternacaoPacientes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InternacaoPacientes for query {}", query);
        Page<InternacaoPaciente> page = internacaoPacienteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/internacao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
