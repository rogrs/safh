package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.PrescricaoPaciente;
import com.mycompany.myapp.repository.PrescricaoPacienteRepository;
import com.mycompany.myapp.repository.search.PrescricaoPacienteSearchRepository;
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
 * REST controller for managing PrescricaoPaciente.
 */
@RestController
@RequestMapping("/api")
public class PrescricaoPacienteResource {

    private final Logger log = LoggerFactory.getLogger(PrescricaoPacienteResource.class);
        
    @Inject
    private PrescricaoPacienteRepository prescricaoPacienteRepository;
    
    @Inject
    private PrescricaoPacienteSearchRepository prescricaoPacienteSearchRepository;
    
    /**
     * POST  /prescricao-pacientes : Create a new prescricaoPaciente.
     *
     * @param prescricaoPaciente the prescricaoPaciente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prescricaoPaciente, or with status 400 (Bad Request) if the prescricaoPaciente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prescricao-pacientes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrescricaoPaciente> createPrescricaoPaciente(@Valid @RequestBody PrescricaoPaciente prescricaoPaciente) throws URISyntaxException {
        log.debug("REST request to save PrescricaoPaciente : {}", prescricaoPaciente);
        if (prescricaoPaciente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("prescricaoPaciente", "idexists", "A new prescricaoPaciente cannot already have an ID")).body(null);
        }
        PrescricaoPaciente result = prescricaoPacienteRepository.save(prescricaoPaciente);
        prescricaoPacienteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescricao-pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prescricaoPaciente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prescricao-pacientes : Updates an existing prescricaoPaciente.
     *
     * @param prescricaoPaciente the prescricaoPaciente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prescricaoPaciente,
     * or with status 400 (Bad Request) if the prescricaoPaciente is not valid,
     * or with status 500 (Internal Server Error) if the prescricaoPaciente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prescricao-pacientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrescricaoPaciente> updatePrescricaoPaciente(@Valid @RequestBody PrescricaoPaciente prescricaoPaciente) throws URISyntaxException {
        log.debug("REST request to update PrescricaoPaciente : {}", prescricaoPaciente);
        if (prescricaoPaciente.getId() == null) {
            return createPrescricaoPaciente(prescricaoPaciente);
        }
        PrescricaoPaciente result = prescricaoPacienteRepository.save(prescricaoPaciente);
        prescricaoPacienteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prescricaoPaciente", prescricaoPaciente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prescricao-pacientes : get all the prescricaoPacientes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prescricaoPacientes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/prescricao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrescricaoPaciente>> getAllPrescricaoPacientes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PrescricaoPacientes");
        Page<PrescricaoPaciente> page = prescricaoPacienteRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prescricao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prescricao-pacientes/:id : get the "id" prescricaoPaciente.
     *
     * @param id the id of the prescricaoPaciente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prescricaoPaciente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/prescricao-pacientes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrescricaoPaciente> getPrescricaoPaciente(@PathVariable Long id) {
        log.debug("REST request to get PrescricaoPaciente : {}", id);
        PrescricaoPaciente prescricaoPaciente = prescricaoPacienteRepository.findOne(id);
        return Optional.ofNullable(prescricaoPaciente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prescricao-pacientes/:id : delete the "id" prescricaoPaciente.
     *
     * @param id the id of the prescricaoPaciente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/prescricao-pacientes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrescricaoPaciente(@PathVariable Long id) {
        log.debug("REST request to delete PrescricaoPaciente : {}", id);
        prescricaoPacienteRepository.delete(id);
        prescricaoPacienteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prescricaoPaciente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/prescricao-pacientes?query=:query : search for the prescricaoPaciente corresponding
     * to the query.
     *
     * @param query the query of the prescricaoPaciente search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/prescricao-pacientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrescricaoPaciente>> searchPrescricaoPacientes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PrescricaoPacientes for query {}", query);
        Page<PrescricaoPaciente> page = prescricaoPacienteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prescricao-pacientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
