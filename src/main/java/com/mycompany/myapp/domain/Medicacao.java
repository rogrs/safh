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
 * A Medicacao.
 */
@Entity
@Table(name = "medicacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicacao")
public class Medicacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_medicacao", nullable = false)
    private LocalDate dataMedicacao;

    @NotNull
    @Column(name = "verificar", nullable = false)
    private String verificar;

    @NotNull
    @Column(name = "valor", nullable = false)
    private String valor;

    @ManyToOne
    private Pacientes pacientes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataMedicacao() {
        return dataMedicacao;
    }

    public void setDataMedicacao(LocalDate dataMedicacao) {
        this.dataMedicacao = dataMedicacao;
    }

    public String getVerificar() {
        return verificar;
    }

    public void setVerificar(String verificar) {
        this.verificar = verificar;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
        Medicacao medicacao = (Medicacao) o;
        if(medicacao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, medicacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Medicacao{" +
            "id=" + id +
            ", dataMedicacao='" + dataMedicacao + "'" +
            ", verificar='" + verificar + "'" +
            ", valor='" + valor + "'" +
            '}';
    }
}
