package br.com.rogrs.service;

import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
import br.com.rogrs.repository.search.InternacoesDetalhesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link InternacoesDetalhes}.
 */
@Service
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
     * @return the list of entities.
     */
    public List<InternacoesDetalhes> findAll() {
        log.debug("Request to get all InternacoesDetalhes");
        return internacoesDetalhesRepository.findAll();
    }


    /**
     * Get one internacoesDetalhes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<InternacoesDetalhes> findOne(String id) {
        log.debug("Request to get InternacoesDetalhes : {}", id);
        return internacoesDetalhesRepository.findById(id);
    }

    /**
     * Delete the internacoesDetalhes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete InternacoesDetalhes : {}", id);
        internacoesDetalhesRepository.deleteById(id);
        internacoesDetalhesSearchRepository.deleteById(id);
    }

    /**
     * Search for the internacoesDetalhes corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    public List<InternacoesDetalhes> search(String query) {
        log.debug("Request to search InternacoesDetalhes for query {}", query);
        return StreamSupport
            .stream(internacoesDetalhesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
