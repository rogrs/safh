package br.com.rogrs.safh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Enfermarias.
 */
@Entity
@Table(name = "enfermarias")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enfermarias")
public class Enfermarias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "enfermaria", length = 60, nullable = false)
    private String enfermaria;

    @OneToMany(mappedBy = "enfermarias")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pacientes> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnfermaria() {
        return enfermaria;
    }

    public Enfermarias enfermaria(String enfermaria) {
        this.enfermaria = enfermaria;
        return this;
    }

    public void setEnfermaria(String enfermaria) {
        this.enfermaria = enfermaria;
    }

    public Set<Pacientes> getPacientes() {
        return pacientes;
    }

    public Enfermarias pacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
        return this;
    }

    public Enfermarias addPacientes(Pacientes pacientes) {
        this.pacientes.add(pacientes);
        pacientes.setEnfermarias(this);
        return this;
    }

    public Enfermarias removePacientes(Pacientes pacientes) {
        this.pacientes.remove(pacientes);
        pacientes.setEnfermarias(null);
        return this;
    }

    public void setPacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enfermarias enfermarias = (Enfermarias) o;
        if (enfermarias.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), enfermarias.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Enfermarias{" +
            "id=" + getId() +
            ", enfermaria='" + getEnfermaria() + "'" +
            "}";
    }
}
