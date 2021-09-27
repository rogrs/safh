package br.com.rogrs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enfermarias.
 */
@Entity
@Table(name = "enfermarias")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "clinicas", "enfermarias", "leitos" }, allowSetters = true)
    private Set<Pacientes> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enfermarias id(Long id) {
        this.id = id;
        return this;
    }

    public String getEnfermaria() {
        return this.enfermaria;
    }

    public Enfermarias enfermaria(String enfermaria) {
        this.enfermaria = enfermaria;
        return this;
    }

    public void setEnfermaria(String enfermaria) {
        this.enfermaria = enfermaria;
    }

    public Set<Pacientes> getPacientes() {
        return this.pacientes;
    }

    public Enfermarias pacientes(Set<Pacientes> pacientes) {
        this.setPacientes(pacientes);
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
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setEnfermarias(null));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.setEnfermarias(this));
        }
        this.pacientes = pacientes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfermarias)) {
            return false;
        }
        return id != null && id.equals(((Enfermarias) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfermarias{" +
            "id=" + getId() +
            ", enfermaria='" + getEnfermaria() + "'" +
            "}";
    }
}
