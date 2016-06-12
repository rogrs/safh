package br.com.rogrs.safh.service;

import br.com.rogrs.safh.domain.InternacoesDetalhes;
import br.com.rogrs.safh.repository.InternacoesDetalhesRepository;
import br.com.rogrs.safh.repository.search.InternacoesDetalhesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InternacoesDetalhes.
 */
@Service
@Transactional
public class InternacoesDetalhesService {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesService.class);
    
    @Inject
    private InternacoesDetalhesRepository internacoesDetalhesRepository;
    
    @Inject
    private InternacoesDetalhesSearchRepository internacoesDetalhesSearchRepository;
    
    /**
     * Save a internacoesDetalhes.
     * 
     * @param internacoesDetalhes the entity to save
     * @return the persisted entity
     */
    public InternacoesDetalhes save(InternacoesDetalhes internacoesDetalhes) {
        log.debug("Request to save InternacoesDetalhes : {}", internacoesDetalhes);
        InternacoesDetalhes result = internacoesDetalhesRepository.save(internacoesDetalhes);
        internacoesDetalhesSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the internacoesDetalhes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<InternacoesDetalhes> findAll(Pageable pageable) {
        log.debug("Request to get all InternacoesDetalhes");
        Page<InternacoesDetalhes> result = internacoesDetalhesRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one internacoesDetalhes by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public InternacoesDetalhes findOne(Long id) {
        log.debug("Request to get InternacoesDetalhes : {}", id);
        InternacoesDetalhes internacoesDetalhes = internacoesDetalhesRepository.findOne(id);
        return internacoesDetalhes;
    }

    /**
     *  Delete the  internacoesDetalhes by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesRepository.delete(id);
        internacoesDetalhesSearchRepository.delete(id);
    }

    /**
     * Search for the internacoesDetalhes corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InternacoesDetalhes> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InternacoesDetalhes for query {}", query);
        return internacoesDetalhesSearchRepository.search(queryStringQuery(query), pageable);
    }
}
