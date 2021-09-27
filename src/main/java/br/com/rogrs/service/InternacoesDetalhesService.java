package br.com.rogrs.service;

import br.com.rogrs.domain.InternacoesDetalhes;
import br.com.rogrs.repository.InternacoesDetalhesRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InternacoesDetalhes}.
 */
@Service
@Transactional
public class InternacoesDetalhesService {

    private final Logger log = LoggerFactory.getLogger(InternacoesDetalhesService.class);

    private final InternacoesDetalhesRepository internacoesDetalhesRepository;

    public InternacoesDetalhesService(InternacoesDetalhesRepository internacoesDetalhesRepository) {
        this.internacoesDetalhesRepository = internacoesDetalhesRepository;
    }

    /**
     * Save a internacoesDetalhes.
     *
     * @param internacoesDetalhes the entity to save.
     * @return the persisted entity.
     */
    public InternacoesDetalhes save(InternacoesDetalhes internacoesDetalhes) {
        log.debug("Request to save InternacoesDetalhes : {}", internacoesDetalhes);
        return internacoesDetalhesRepository.save(internacoesDetalhes);
    }

    /**
     * Partially update a internacoesDetalhes.
     *
     * @param internacoesDetalhes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InternacoesDetalhes> partialUpdate(InternacoesDetalhes internacoesDetalhes) {
        log.debug("Request to partially update InternacoesDetalhes : {}", internacoesDetalhes);

        return internacoesDetalhesRepository
            .findById(internacoesDetalhes.getId())
            .map(
                existingInternacoesDetalhes -> {
                    if (internacoesDetalhes.getDataDetalhe() != null) {
                        existingInternacoesDetalhes.setDataDetalhe(internacoesDetalhes.getDataDetalhe());
                    }
                    if (internacoesDetalhes.getHorario() != null) {
                        existingInternacoesDetalhes.setHorario(internacoesDetalhes.getHorario());
                    }
                    if (internacoesDetalhes.getQtd() != null) {
                        existingInternacoesDetalhes.setQtd(internacoesDetalhes.getQtd());
                    }

                    return existingInternacoesDetalhes;
                }
            )
            .map(internacoesDetalhesRepository::save);
    }

    /**
     * Get all the internacoesDetalhes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
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
    }
}
