package br.com.rogrs.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Internacoes.
 */
@Entity
@Table(name = "internacoes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "internacoes")
public class Internacoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "data_internacao", nullable = false)
    private LocalDate dataInternacao;

    @NotNull
    @Size(max = 200)
    @Column(name = "descricao", length = 200, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "internacoes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InternacoesDetalhes> internacoesDetalhes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("internacoes")
    private Pacientes pacientes;

    @ManyToOne
    @JsonIgnoreProperties("internacoes")
    private Clinicas clinicas;

    @ManyToOne
    @JsonIgnoreProperties("internacoes")
    private Medicos medicos;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInternacao() {
        return dataInternacao;
    }

    public Internacoes dataInternacao(LocalDate dataInternacao) {
        this.dataInternacao = dataInternacao;
        return this;
    }

    public void setDataInternacao(LocalDate dataInternacao) {
        this.dataInternacao = dataInternacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Internacoes descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return internacoesDetalhes;
    }

    public Internacoes internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
        return this;
    }

    public Internacoes addInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.add(internacoesDetalhes);
        internacoesDetalhes.setInternacoes(this);
        return this;
    }

    public Internacoes removeInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.remove(internacoesDetalhes);
        internacoesDetalhes.setInternacoes(null);
        return this;
    }

    public void setInternacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public Internacoes pacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
        return this;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Clinicas getClinicas() {
        return clinicas;
    }

    public Internacoes clinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
        return this;
    }

    public void setClinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
    }

    public Medicos getMedicos() {
        return medicos;
    }

    public Internacoes medicos(Medicos medicos) {
        this.medicos = medicos;
        return this;
    }

    public void setMedicos(Medicos medicos) {
        this.medicos = medicos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Internacoes)) {
            return false;
        }
        return id != null && id.equals(((Internacoes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Internacoes{" +
            "id=" + getId() +
            ", dataInternacao='" + getDataInternacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
