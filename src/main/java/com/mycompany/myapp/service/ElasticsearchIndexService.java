package com.mycompany.myapp.service;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private ClinicasRepository clinicasRepository;

    @Inject
    private ClinicasSearchRepository clinicasSearchRepository;

    @Inject
    private DietasRepository dietasRepository;

    @Inject
    private DietasSearchRepository dietasSearchRepository;

    @Inject
    private EnfermariasRepository enfermariasRepository;

    @Inject
    private EnfermariasSearchRepository enfermariasSearchRepository;

    @Inject
    private EspecialidadesRepository especialidadesRepository;

    @Inject
    private EspecialidadesSearchRepository especialidadesSearchRepository;

    @Inject
    private EvolucaoPacienteRepository evolucaoPacienteRepository;

    @Inject
    private EvolucaoPacienteSearchRepository evolucaoPacienteSearchRepository;

    @Inject
    private FabricantesRepository fabricantesRepository;

    @Inject
    private FabricantesSearchRepository fabricantesSearchRepository;

    @Inject
    private InternacaoPacienteRepository internacaoPacienteRepository;

    @Inject
    private InternacaoPacienteSearchRepository internacaoPacienteSearchRepository;

    @Inject
    private LeitosRepository leitosRepository;

    @Inject
    private LeitosSearchRepository leitosSearchRepository;

    @Inject
    private MedicamentosRepository medicamentosRepository;

    @Inject
    private MedicamentosSearchRepository medicamentosSearchRepository;

    @Inject
    private MedicosRepository medicosRepository;

    @Inject
    private MedicosSearchRepository medicosSearchRepository;

    @Inject
    private PacientesRepository pacientesRepository;

    @Inject
    private PacientesSearchRepository pacientesSearchRepository;

    @Inject
    private PosologiasRepository posologiasRepository;

    @Inject
    private PosologiasSearchRepository posologiasSearchRepository;

    @Inject
    private PrescricaoPacienteRepository prescricaoPacienteRepository;

    @Inject
    private PrescricaoPacienteSearchRepository prescricaoPacienteSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Clinicas.class, clinicasRepository, clinicasSearchRepository);
        reindexForClass(Dietas.class, dietasRepository, dietasSearchRepository);
        reindexForClass(Enfermarias.class, enfermariasRepository, enfermariasSearchRepository);
        reindexForClass(Especialidades.class, especialidadesRepository, especialidadesSearchRepository);
        reindexForClass(EvolucaoPaciente.class, evolucaoPacienteRepository, evolucaoPacienteSearchRepository);
        reindexForClass(Fabricantes.class, fabricantesRepository, fabricantesSearchRepository);
        reindexForClass(InternacaoPaciente.class, internacaoPacienteRepository, internacaoPacienteSearchRepository);
        reindexForClass(Leitos.class, leitosRepository, leitosSearchRepository);
        reindexForClass(Medicamentos.class, medicamentosRepository, medicamentosSearchRepository);
        reindexForClass(Medicos.class, medicosRepository, medicosSearchRepository);
        reindexForClass(Pacientes.class, pacientesRepository, pacientesSearchRepository);
        reindexForClass(Posologias.class, posologiasRepository, posologiasSearchRepository);
        reindexForClass(PrescricaoPaciente.class, prescricaoPacienteRepository, prescricaoPacienteSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
