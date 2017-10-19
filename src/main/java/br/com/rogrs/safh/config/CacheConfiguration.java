package br.com.rogrs.safh.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Clinicas.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Clinicas.class.getName() + ".pacientes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Clinicas.class.getName() + ".internacoes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Dietas.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Dietas.class.getName() + ".internacoesDetalhes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Enfermarias.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Enfermarias.class.getName() + ".pacientes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Especialidades.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Especialidades.class.getName() + ".medicos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Fabricantes.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Fabricantes.class.getName() + ".medicamentos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Internacoes.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Internacoes.class.getName() + ".internacoesDetalhes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.InternacoesDetalhes.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Leitos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Leitos.class.getName() + ".pacientes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Medicamentos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Medicos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Medicos.class.getName() + ".internacoes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Pacientes.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Pacientes.class.getName() + ".internacoes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Posologias.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Posologias.class.getName() + ".medicamentos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Posologias.class.getName() + ".internacoesDetalhes", jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Prescricoes.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.safh.domain.Prescricoes.class.getName() + ".internacoesDetalhes", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
