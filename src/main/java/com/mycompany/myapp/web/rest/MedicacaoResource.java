package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Medicacao;
import com.mycompany.myapp.repository.MedicacaoRepository;
import com.mycompany.myapp.repository.search.MedicacaoSearchRepository;
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
 * REST controller for managing Medicacao.
 */
@RestController
@RequestMapping("/api")
public class MedicacaoResource {

    private final Logger log = LoggerFactory.getLogger(MedicacaoResource.class);
        
    @Inject
    private MedicacaoRepository medicacaoRepository;
    
    @Inject
    private MedicacaoSearchRepository medicacaoSearchRepository;
    
    /**
     * POST  /medicacaos : Create a new medicacao.
     *
     * @param medicacao the medicacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicacao, or with status 400 (Bad Request) if the medicacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicacaos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicacao> createMedicacao(@Valid @RequestBody Medicacao medicacao) throws URISyntaxException {
        log.debug("REST request to save Medicacao : {}", medicacao);
        if (medicacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("medicacao", "idexists", "A new medicacao cannot already have an ID")).body(null);
        }
        Medicacao result = medicacaoRepository.save(medicacao);
        medicacaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("medicacao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicacaos : Updates an existing medicacao.
     *
     * @param medicacao the medicacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicacao,
     * or with status 400 (Bad Request) if the medicacao is not valid,
     * or with status 500 (Internal Server Error) if the medicacao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/medicacaos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicacao> updateMedicacao(@Valid @RequestBody Medicacao medicacao) throws URISyntaxException {
        log.debug("REST request to update Medicacao : {}", medicacao);
        if (medicacao.getId() == null) {
            return createMedicacao(medicacao);
        }
        Medicacao result = medicacaoRepository.save(medicacao);
        medicacaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("medicacao", medicacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicacaos : get all the medicacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicacaos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/medicacaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicacao>> getAllMedicacaos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Medicacaos");
        Page<Medicacao> page = medicacaoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medicacaos/:id : get the "id" medicacao.
     *
     * @param id the id of the medicacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicacao, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/medicacaos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medicacao> getMedicacao(@PathVariable Long id) {
        log.debug("REST request to get Medicacao : {}", id);
        Medicacao medicacao = medicacaoRepository.findOne(id);
        return Optional.ofNullable(medicacao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medicacaos/:id : delete the "id" medicacao.
     *
     * @param id the id of the medicacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/medicacaos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMedicacao(@PathVariable Long id) {
        log.debug("REST request to delete Medicacao : {}", id);
        medicacaoRepository.delete(id);
        medicacaoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medicacao", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicacaos?query=:query : search for the medicacao corresponding
     * to the query.
     *
     * @param query the query of the medicacao search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/medicacaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medicacao>> searchMedicacaos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Medicacaos for query {}", query);
        Page<Medicacao> page = medicacaoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
