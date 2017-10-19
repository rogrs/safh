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
 * A Clinicas.
 */
@Entity
@Table(name = "clinicas")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clinicas")
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
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pacientes> pacientes = new HashSet<>();

    @OneToMany(mappedBy = "clinicas")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Internacoes> internacoes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClinica() {
        return clinica;
    }

    public Clinicas clinica(String clinica) {
        this.clinica = clinica;
        return this;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public String getDescricao() {
        return descricao;
    }

    public Clinicas descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Pacientes> getPacientes() {
        return pacientes;
    }

    public Clinicas pacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
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
        this.pacientes = pacientes;
    }

    public Set<Internacoes> getInternacoes() {
        return internacoes;
    }

    public Clinicas internacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
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
        this.internacoes = internacoes;
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
        Clinicas clinicas = (Clinicas) o;
        if (clinicas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clinicas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Clinicas{" +
            "id=" + getId() +
            ", clinica='" + getClinica() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
