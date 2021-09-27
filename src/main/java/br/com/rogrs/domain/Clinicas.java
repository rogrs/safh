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
 * A Clinicas.
 */
@Entity
@Table(name = "clinicas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clinicas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "clinica", length = 80, nullable = false)
    private String clinica;

    @Size(max = 100)
    @Column(name = "descricao", length = 100)
    private String descricao;

    @OneToMany(mappedBy = "clinicas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "clinicas", "enfermarias", "leitos" }, allowSetters = true)
    private Set<Pacientes> pacientes = new HashSet<>();

    @OneToMany(mappedBy = "clinicas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoesDetalhes", "pacientes", "clinicas", "medicos" }, allowSetters = true)
    private Set<Internacoes> internacoes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clinicas id(Long id) {
        this.id = id;
        return this;
    }

    public String getClinica() {
        return this.clinica;
    }

    public Clinicas clinica(String clinica) {
        this.clinica = clinica;
        return this;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Clinicas descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Pacientes> getPacientes() {
        return this.pacientes;
    }

    public Clinicas pacientes(Set<Pacientes> pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public Clinicas addPacientes(Pacientes pacientes) {
        this.pacientes.add(pacientes);
        pacientes.setClinicas(this);
        return this;
    }

    public Clinicas removePacientes(Pacientes pacientes) {
        this.pacientes.remove(pacientes);
        pacientes.setClinicas(null);
        return this;
    }

    public void setPacientes(Set<Pacientes> pacientes) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setClinicas(null));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.setClinicas(this));
        }
        this.pacientes = pacientes;
    }

    public Set<Internacoes> getInternacoes() {
        return this.internacoes;
    }

    public Clinicas internacoes(Set<Internacoes> internacoes) {
        this.setInternacoes(internacoes);
        return this;
    }

    public Clinicas addInternacoes(Internacoes internacoes) {
        this.internacoes.add(internacoes);
        internacoes.setClinicas(this);
        return this;
    }

    public Clinicas removeInternacoes(Internacoes internacoes) {
        this.internacoes.remove(internacoes);
        internacoes.setClinicas(null);
        return this;
    }

    public void setInternacoes(Set<Internacoes> internacoes) {
        if (this.internacoes != null) {
            this.internacoes.forEach(i -> i.setClinicas(null));
        }
        if (internacoes != null) {
            internacoes.forEach(i -> i.setClinicas(this));
        }
        this.internacoes = internacoes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clinicas)) {
            return false;
        }
        return id != null && id.equals(((Clinicas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clinicas{" +
            "id=" + getId() +
            ", clinica='" + getClinica() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
