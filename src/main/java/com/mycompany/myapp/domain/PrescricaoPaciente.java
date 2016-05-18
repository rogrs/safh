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
 * A PrescricaoPaciente.
 */
@Entity
@Table(name = "prescricao_paciente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "prescricaopaciente")
public class PrescricaoPaciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @NotNull
    @Column(name = "quant", nullable = false)
    private Float quant;

    @Column(name = "obse")
    private String obse;

    @ManyToOne
    private Pacientes pacientes;

    @ManyToOne
    private Medicamentos medicamentos;

    @ManyToOne
    private Posologias posologias;

    @ManyToOne
    private Dietas dietas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Float getQuant() {
        return quant;
    }

    public void setQuant(Float quant) {
        this.quant = quant;
    }

    public String getObse() {
        return obse;
    }

    public void setObse(String obse) {
        this.obse = obse;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Medicamentos getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Medicamentos medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Posologias getPosologias() {
        return posologias;
    }

    public void setPosologias(Posologias posologias) {
        this.posologias = posologias;
    }

    public Dietas getDietas() {
        return dietas;
    }

    public void setDietas(Dietas dietas) {
        this.dietas = dietas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrescricaoPaciente prescricaoPaciente = (PrescricaoPaciente) o;
        if(prescricaoPaciente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prescricaoPaciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PrescricaoPaciente{" +
            "id=" + id +
            ", dataEntrada='" + dataEntrada + "'" +
            ", quant='" + quant + "'" +
            ", obse='" + obse + "'" +
            '}';
    }
}
