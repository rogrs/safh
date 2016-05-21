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
 * A EvolucaoPaciente.
 */
@Entity
@Table(name = "evolucao_paciente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "evolucaopaciente")
public class EvolucaoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "data_evolucao")
    private LocalDate dataEvolucao;

    @NotNull
    @Size(max = 200)
    @Column(name = "descricao", length = 200, nullable = false)
    private String descricao;

    @ManyToOne
    private Pacientes pacientes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEvolucao() {
        return dataEvolucao;
    }

    public void setDataEvolucao(LocalDate dataEvolucao) {
        this.dataEvolucao = dataEvolucao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EvolucaoPaciente evolucaoPaciente = (EvolucaoPaciente) o;
        if(evolucaoPaciente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, evolucaoPaciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EvolucaoPaciente{" +
            "id=" + id +
            ", dataEvolucao='" + dataEvolucao + "'" +
            ", descricao='" + descricao + "'" +
            '}';
    }
}
