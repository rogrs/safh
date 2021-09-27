package br.com.rogrs.web.rest;

import br.com.rogrs.domain.Pacientes;
import br.com.rogrs.repository.PacientesRepository;
import br.com.rogrs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.rogrs.domain.Pacientes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PacientesResource {

    private final Logger log = LoggerFactory.getLogger(PacientesResource.class);

    private static final String ENTITY_NAME = "pacientes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacientesRepository pacientesRepository;

    public PacientesResource(PacientesRepository pacientesRepository) {
        this.pacientesRepository = pacientesRepository;
    }

    /**
     * {@code POST  /pacientes} : Create a new pacientes.
     *
     * @param pacientes the pacientes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacientes, or with status {@code 400 (Bad Request)} if the pacientes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pacientes")
    public ResponseEntity<Pacientes> createPacientes(@Valid @RequestBody Pacientes pacientes) throws URISyntaxException {
        log.debug("REST request to save Pacientes : {}", pacientes);
        if (pacientes.getId() != null) {
            throw new BadRequestAlertException("A new pacientes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pacientes result = pacientesRepository.save(pacientes);
        return ResponseEntity
            .created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pacientes/:id} : Updates an existing pacientes.
     *
     * @param id the id of the pacientes to save.
     * @param pacientes the pacientes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacientes,
     * or with status {@code 400 (Bad Request)} if the pacientes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacientes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Pacientes> updatePacientes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pacientes pacientes
    ) throws URISyntaxException {
        log.debug("REST request to update Pacientes : {}, {}", id, pacientes);
        if (pacientes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacientes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacientesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pacientes result = pacientesRepository.save(pacientes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacientes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pacientes/:id} : Partial updates given fields of an existing pacientes, field will ignore if it is null
     *
     * @param id the id of the pacientes to save.
     * @param pacientes the pacientes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacientes,
     * or with status {@code 400 (Bad Request)} if the pacientes is not valid,
     * or with status {@code 404 (Not Found)} if the pacientes is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacientes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pacientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Pacientes> partialUpdatePacientes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pacientes pacientes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pacientes partially : {}, {}", id, pacientes);
        if (pacientes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacientes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacientesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pacientes> result = pacientesRepository
            .findById(pacientes.getId())
            .map(
                existingPacientes -> {
                    if (pacientes.getProntuario() != null) {
                        existingPacientes.setProntuario(pacientes.getProntuario());
                    }
                    if (pacientes.getNome() != null) {
                        existingPacientes.setNome(pacientes.getNome());
                    }
                    if (pacientes.getCpf() != null) {
                        existingPacientes.setCpf(pacientes.getCpf());
                    }
                    if (pacientes.getEmail() != null) {
                        existingPacientes.setEmail(pacientes.getEmail());
                    }
                    if (pacientes.getCep() != null) {
                        existingPacientes.setCep(pacientes.getCep());
                    }
                    if (pacientes.getLogradouro() != null) {
                        existingPacientes.setLogradouro(pacientes.getLogradouro());
                    }
                    if (pacientes.getNumero() != null) {
                        existingPacientes.setNumero(pacientes.getNumero());
                    }
                    if (pacientes.getComplemento() != null) {
                        existingPacientes.setComplemento(pacientes.getComplemento());
                    }
                    if (pacientes.getBairro() != null) {
                        existingPacientes.setBairro(pacientes.getBairro());
                    }
                    if (pacientes.getCidade() != null) {
                        existingPacientes.setCidade(pacientes.getCidade());
                    }
                    if (pacientes.getuF() != null) {
                        existingPacientes.setuF(pacientes.getuF());
                    }

                    return existingPacientes;
                }
            )
            .map(pacientesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacientes.getId().toString())
        );
    }

    /**
     * {@code GET  /pacientes} : get all the pacientes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacientes in body.
     */
    @GetMapping("/pacientes")
    public List<Pacientes> getAllPacientes() {
        log.debug("REST request to get all Pacientes");
        return pacientesRepository.findAll();
    }

    /**
     * {@code GET  /pacientes/:id} : get the "id" pacientes.
     *
     * @param id the id of the pacientes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacientes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Pacientes> getPacientes(@PathVariable Long id) {
        log.debug("REST request to get Pacientes : {}", id);
        Optional<Pacientes> pacientes = pacientesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pacientes);
    }

    /**
     * {@code DELETE  /pacientes/:id} : delete the "id" pacientes.
     *
     * @param id the id of the pacientes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletePacientes(@PathVariable Long id) {
        log.debug("REST request to delete Pacientes : {}", id);
        pacientesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
