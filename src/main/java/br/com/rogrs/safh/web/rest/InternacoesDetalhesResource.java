package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.InternacoesDetalhes;
import br.com.rogrs.safh.service.InternacoesDetalhesService;
import br.com.rogrs.safh.web.rest.util.HeaderUtil;
import br.com.rogrs.safh.web.rest.util.PaginationUtil;
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
 * REST controller for managing InternacoesDetalhes.
 */
@RestController
@RequestMapping("/api")
public class InternacoesDetalhesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesResource.class);
        
    @Inject
    private InternacoesDetalhesService internacoesDetalhesService;
    
    /**
     * POST  /internacoes-detalhes : Create a new internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internacoesDetalhes, or with status 400 (Bad Request) if the internacoesDetalhes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacoes-detalhes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacoesDetalhes> createInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to save InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("internacoesDetalhes", "idexists", "A new internacoesDetalhes cannot already have an ID")).body(null);
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.created(new URI("/api/internacoes-detalhes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("internacoesDetalhes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /internacoes-detalhes : Updates an existing internacoesDetalhes.
     *
     * @param internacoesDetalhes the internacoesDetalhes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internacoesDetalhes,
     * or with status 400 (Bad Request) if the internacoesDetalhes is not valid,
     * or with status 500 (Internal Server Error) if the internacoesDetalhes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacoes-detalhes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacoesDetalhes> updateInternacoesDetalhes(@Valid @RequestBody InternacoesDetalhes internacoesDetalhes) throws URISyntaxException {
        log.debug("REST request to update InternacoesDetalhes : {}", internacoesDetalhes);
        if (internacoesDetalhes.getId() == null) {
            return createInternacoesDetalhes(internacoesDetalhes);
        }
        InternacoesDetalhes result = internacoesDetalhesService.save(internacoesDetalhes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("internacoesDetalhes", internacoesDetalhes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /internacoes-detalhes : get all the internacoesDetalhes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internacoesDetalhes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/internacoes-detalhes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InternacoesDetalhes>> getAllInternacoesDetalhes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InternacoesDetalhes");
        Page<InternacoesDetalhes> page = internacoesDetalhesService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internacoes-detalhes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /internacoes-detalhes/:id : get the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internacoesDetalhes, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/internacoes-detalhes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InternacoesDetalhes> getInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to get InternacoesDetalhes : {}", id);
        InternacoesDetalhes internacoesDetalhes = internacoesDetalhesService.findOne(id);
        return Optional.ofNullable(internacoesDetalhes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /internacoes-detalhes/:id : delete the "id" internacoesDetalhes.
     *
     * @param id the id of the internacoesDetalhes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/internacoes-detalhes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInternacoesDetalhes(@PathVariable Long id) {
        log.debug("REST request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("internacoesDetalhes", id.toString())).build();
    }

    /**
     * SEARCH  /_search/internacoes-detalhes?query=:query : search for the internacoesDetalhes corresponding
     * to the query.
     *
     * @param query the query of the internacoesDetalhes search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/internacoes-detalhes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InternacoesDetalhes>> searchInternacoesDetalhes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InternacoesDetalhes for query {}", query);
        Page<InternacoesDetalhes> page = internacoesDetalhesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/internacoes-detalhes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
