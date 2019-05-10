package br.com.rogrs.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, br.com.rogrs.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, br.com.rogrs.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, br.com.rogrs.domain.User.class.getName());
            createCache(cm, br.com.rogrs.domain.Authority.class.getName());
            createCache(cm, br.com.rogrs.domain.User.class.getName() + ".authorities");
            createCache(cm, br.com.rogrs.domain.Clinicas.class.getName());
            createCache(cm, br.com.rogrs.domain.Clinicas.class.getName() + ".pacientes");
            createCache(cm, br.com.rogrs.domain.Clinicas.class.getName() + ".internacoes");
            createCache(cm, br.com.rogrs.domain.Dietas.class.getName());
            createCache(cm, br.com.rogrs.domain.Dietas.class.getName() + ".internacoesDetalhes");
            createCache(cm, br.com.rogrs.domain.Enfermarias.class.getName());
            createCache(cm, br.com.rogrs.domain.Enfermarias.class.getName() + ".pacientes");
            createCache(cm, br.com.rogrs.domain.Especialidades.class.getName());
            createCache(cm, br.com.rogrs.domain.Especialidades.class.getName() + ".medicos");
            createCache(cm, br.com.rogrs.domain.Fabricantes.class.getName());
            createCache(cm, br.com.rogrs.domain.Fabricantes.class.getName() + ".medicamentos");
            createCache(cm, br.com.rogrs.domain.Internacoes.class.getName());
            createCache(cm, br.com.rogrs.domain.Internacoes.class.getName() + ".internacoesDetalhes");
            createCache(cm, br.com.rogrs.domain.InternacoesDetalhes.class.getName());
            createCache(cm, br.com.rogrs.domain.Leitos.class.getName());
            createCache(cm, br.com.rogrs.domain.Leitos.class.getName() + ".pacientes");
            createCache(cm, br.com.rogrs.domain.Medicamentos.class.getName());
            createCache(cm, br.com.rogrs.domain.Medicos.class.getName());
            createCache(cm, br.com.rogrs.domain.Medicos.class.getName() + ".internacoes");
            createCache(cm, br.com.rogrs.domain.Pacientes.class.getName());
            createCache(cm, br.com.rogrs.domain.Pacientes.class.getName() + ".internacoes");
            createCache(cm, br.com.rogrs.domain.Posologias.class.getName());
            createCache(cm, br.com.rogrs.domain.Posologias.class.getName() + ".medicamentos");
            createCache(cm, br.com.rogrs.domain.Posologias.class.getName() + ".internacoesDetalhes");
            createCache(cm, br.com.rogrs.domain.Prescricoes.class.getName());
            createCache(cm, br.com.rogrs.domain.Prescricoes.class.getName() + ".internacoesDetalhes");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
