package br.com.rogrs.service;

import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
import br.com.rogrs.repository.search.InternacoesDetalhesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link InternacoesDetalhes}.
 */
@Service
@Transactional
public class InternacoesDetalhesService {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesService.class);

    private final InternacoesDetalhesRepository internacoesDetalhesRepository;

    private final InternacoesDetalhesSearchRepository internacoesDetalhesSearchRepository;

    public InternacoesDetalhesService(InternacoesDetalhesRepository internacoesDetalhesRepository, InternacoesDetalhesSearchRepository internacoesDetalhesSearchRepository) {
        this.internacoesDetalhesRepository = internacoesDetalhesRepository;
        this.internacoesDetalhesSearchRepository = internacoesDetalhesSearchRepository;
    }

    /**
     * Save a internacoesDetalhes.
     *
     * @param internacoesDetalhes the entity to save.
     * @return the persisted entity.
     */
    public InternacoesDetalhes save(InternacoesDetalhes internacoesDetalhes) {
        log.debug("Request to save InternacoesDetalhes : {}", internacoesDetalhes);
        InternacoesDetalhes result = internacoesDetalhesRepository.save(internacoesDetalhes);
        internacoesDetalhesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the internacoesDetalhes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InternacoesDetalhes> findAll(Pageable pageable) {
        log.debug("Request to get all InternacoesDetalhes");
        return internacoesDetalhesRepository.findAll(pageable);
    }


    /**
     * Get one internacoesDetalhes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InternacoesDetalhes> findOne(Long id) {
        log.debug("Request to get InternacoesDetalhes : {}", id);
        return internacoesDetalhesRepository.findById(id);
    }

    /**
     * Delete the internacoesDetalhes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesRepository.deleteById(id);
        internacoesDetalhesSearchRepository.deleteById(id);
    }

    /**
     * Search for the internacoesDetalhes corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InternacoesDetalhes> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InternacoesDetalhes for query {}", query);
        return internacoesDetalhesSearchRepository.search(queryStringQuery(query), pageable);    }
}
