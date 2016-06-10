package br.com.rogrs.safh.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.safh.domain.Posologias;
import br.com.rogrs.safh.repository.PosologiasRepository;
import br.com.rogrs.safh.repository.search.PosologiasSearchRepository;
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
 * REST controller for managing Posologias.
 */
@RestController
@RequestMapping("/api")
public class PosologiasResource {

    private final Logger log = LoggerFactory.getLogger(PosologiasResource.class);
        
    @Inject
    private PosologiasRepository posologiasRepository;
    
    @Inject
    private PosologiasSearchRepository posologiasSearchRepository;
    
    /**
     * POST  /posologias : Create a new posologias.
     *
     * @param posologias the posologias to create
     * @return the ResponseEntity with status 201 (Created) and with body the new posologias, or with status 400 (Bad Request) if the posologias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/posologias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Posologias> createPosologias(@Valid @RequestBody Posologias posologias) throws URISyntaxException {
        log.debug("REST request to save Posologias : {}", posologias);
        if (posologias.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("posologias", "idexists", "A new posologias cannot already have an ID")).body(null);
        }
        Posologias result = posologiasRepository.save(posologias);
        posologiasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/posologias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("posologias", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /posologias : Updates an existing posologias.
     *
     * @param posologias the posologias to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated posologias,
     * or with status 400 (Bad Request) if the posologias is not valid,
     * or with status 500 (Internal Server Error) if the posologias couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/posologias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Posologias> updatePosologias(@Valid @RequestBody Posologias posologias) throws URISyntaxException {
        log.debug("REST request to update Posologias : {}", posologias);
        if (posologias.getId() == null) {
            return createPosologias(posologias);
        }
        Posologias result = posologiasRepository.save(posologias);
        posologiasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("posologias", posologias.getId().toString()))
            .body(result);
    }

    /**
     * GET  /posologias : get all the posologias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of posologias in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/posologias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Posologias>> getAllPosologias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Posologias");
        Page<Posologias> page = posologiasRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posologias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /posologias/:id : get the "id" posologias.
     *
     * @param id the id of the posologias to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the posologias, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/posologias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Posologias> getPosologias(@PathVariable Long id) {
        log.debug("REST request to get Posologias : {}", id);
        Posologias posologias = posologiasRepository.findOne(id);
        return Optional.ofNullable(posologias)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /posologias/:id : delete the "id" posologias.
     *
     * @param id the id of the posologias to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/posologias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePosologias(@PathVariable Long id) {
        log.debug("REST request to delete Posologias : {}", id);
        posologiasRepository.delete(id);
        posologiasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("posologias", id.toString())).build();
    }

    /**
     * SEARCH  /_search/posologias?query=:query : search for the posologias corresponding
     * to the query.
     *
     * @param query the query of the posologias search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/posologias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Posologias>> searchPosologias(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Posologias for query {}", query);
        Page<Posologias> page = posologiasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/posologias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
