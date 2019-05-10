package br.com.rogrs.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Especialidades.
 */
@Entity
@Table(name = "especialidades")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "especialidades")
public class Especialidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "especialidade", length = 60, nullable = false)
    private String especialidade;

    @OneToMany(mappedBy = "especialidades")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Medicos> medicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public Especialidades especialidade(String especialidade) {
        this.especialidade = especialidade;
        return this;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Set<Medicos> getMedicos() {
        return medicos;
    }

    public Especialidades medicos(Set<Medicos> medicos) {
        this.medicos = medicos;
        return this;
    }

    public Especialidades addMedicos(Medicos medicos) {
        this.medicos.add(medicos);
        medicos.setEspecialidades(this);
        return this;
    }

    public Especialidades removeMedicos(Medicos medicos) {
        this.medicos.remove(medicos);
        medicos.setEspecialidades(null);
        return this;
    }

    public void setMedicos(Set<Medicos> medicos) {
        this.medicos = medicos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Especialidades)) {
            return false;
        }
        return id != null && id.equals(((Especialidades) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Especialidades{" +
            "id=" + getId() +
            ", especialidade='" + getEspecialidade() + "'" +
            "}";
    }
}
