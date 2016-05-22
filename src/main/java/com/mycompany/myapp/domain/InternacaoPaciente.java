package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A InternacaoPaciente.
 */
@Entity
@Table(name = "internacao_paciente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "internacaopaciente")
public class InternacaoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_internacao", nullable = false)
    private LocalDate dataInternacao;

    @ManyToOne
    private Pacientes pacientes;

    @ManyToOne
    private Medicos nome;

    @ManyToOne
    private EvolucaoPaciente evolucaoPaciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInternacao() {
        return dataInternacao;
    }

    public void setDataInternacao(LocalDate dataInternacao) {
        this.dataInternacao = dataInternacao;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Medicos getNome() {
        return nome;
    }

    public void setNome(Medicos medicos) {
        this.nome = medicos;
    }

    public EvolucaoPaciente getEvolucaoPaciente() {
        return evolucaoPaciente;
    }

    public void setEvolucaoPaciente(EvolucaoPaciente evolucaoPaciente) {
        this.evolucaoPaciente = evolucaoPaciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InternacaoPaciente internacaoPaciente = (InternacaoPaciente) o;
        if(internacaoPaciente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, internacaoPaciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InternacaoPaciente{" +
            "id=" + id +
            ", dataInternacao='" + dataInternacao + "'" +
            '}';
    }
}
