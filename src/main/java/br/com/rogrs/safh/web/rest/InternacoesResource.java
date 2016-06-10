package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Internacoes;
import br.com.rogrs.safh.repository.InternacoesRepository;
import br.com.rogrs.safh.repository.search.InternacoesSearchRepository;
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
 * REST controller for managing Internacoes.
 */
@RestController
@RequestMapping("/api")
public class InternacoesResource {

    private final Logger log = LoggerFactory.getLogger(InternacoesResource.class);
        
    @Inject
    private InternacoesRepository internacoesRepository;
    
    @Inject
    private InternacoesSearchRepository internacoesSearchRepository;
    
    /**
     * POST  /internacoes : Create a new internacoes.
     *
     * @param internacoes the internacoes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internacoes, or with status 400 (Bad Request) if the internacoes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacoes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Internacoes> createInternacoes(@Valid @RequestBody Internacoes internacoes) throws URISyntaxException {
        log.debug("REST request to save Internacoes : {}", internacoes);
        if (internacoes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("internacoes", "idexists", "A new internacoes cannot already have an ID")).body(null);
        }
        Internacoes result = internacoesRepository.save(internacoes);
        internacoesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/internacoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("internacoes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /internacoes : Updates an existing internacoes.
     *
     * @param internacoes the internacoes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internacoes,
     * or with status 400 (Bad Request) if the internacoes is not valid,
     * or with status 500 (Internal Server Error) if the internacoes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/internacoes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Internacoes> updateInternacoes(@Valid @RequestBody Internacoes internacoes) throws URISyntaxException {
        log.debug("REST request to update Internacoes : {}", internacoes);
        if (internacoes.getId() == null) {
            return createInternacoes(internacoes);
        }
        Internacoes result = internacoesRepository.save(internacoes);
        internacoesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("internacoes", internacoes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /internacoes : get all the internacoes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internacoes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/internacoes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Internacoes>> getAllInternacoes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Internacoes");
        Page<Internacoes> page = internacoesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internacoes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /internacoes/:id : get the "id" internacoes.
     *
     * @param id the id of the internacoes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internacoes, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/internacoes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Internacoes> getInternacoes(@PathVariable Long id) {
        log.debug("REST request to get Internacoes : {}", id);
        Internacoes internacoes = internacoesRepository.findOne(id);
        return Optional.ofNullable(internacoes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /internacoes/:id : delete the "id" internacoes.
     *
     * @param id the id of the internacoes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/internacoes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInternacoes(@PathVariable Long id) {
        log.debug("REST request to delete Internacoes : {}", id);
        internacoesRepository.delete(id);
        internacoesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("internacoes", id.toString())).build();
    }

    /**
     * SEARCH  /_search/internacoes?query=:query : search for the internacoes corresponding
     * to the query.
     *
     * @param query the query of the internacoes search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/internacoes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Internacoes>> searchInternacoes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Internacoes for query {}", query);
        Page<Internacoes> page = internacoesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/internacoes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
