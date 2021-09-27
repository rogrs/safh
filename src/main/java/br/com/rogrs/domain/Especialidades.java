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
 * A Especialidades.
 */
@Entity
@Table(name = "especialidades")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Especialidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "especialidade", length = 60, nullable = false)
    private String especialidade;

    @OneToMany(mappedBy = "especialidades")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "especialidades" }, allowSetters = true)
    private Set<Medicos> medicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Especialidades id(Long id) {
        this.id = id;
        return this;
    }

    public String getEspecialidade() {
        return this.especialidade;
    }

    public Especialidades especialidade(String especialidade) {
        this.especialidade = especialidade;
        return this;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Set<Medicos> getMedicos() {
        return this.medicos;
    }

    public Especialidades medicos(Set<Medicos> medicos) {
        this.setMedicos(medicos);
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
        if (this.medicos != null) {
            this.medicos.forEach(i -> i.setEspecialidades(null));
        }
        if (medicos != null) {
            medicos.forEach(i -> i.setEspecialidades(this));
        }
        this.medicos = medicos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Especialidades{" +
            "id=" + getId() +
            ", especialidade='" + getEspecialidade() + "'" +
            "}";
    }
}
