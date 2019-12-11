package br.com.rogrs.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A InternacoesDetalhes.
 */
@Document(collection = "internacoes_detalhes")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "internacoesdetalhes")
public class InternacoesDetalhes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Field("data_detalhe")
    private LocalDate dataDetalhe;

    @NotNull
    @Field("horario")
    private LocalDate horario;

    @NotNull
    @Field("qtd")
    private Float qtd;

    @DBRef
    @Field("internacoes")
    @JsonIgnoreProperties("internacoesDetalhes")
    private Internacoes internacoes;

    @DBRef
    @Field("dietas")
    @JsonIgnoreProperties("internacoesDetalhes")
    private Dietas dietas;

    @DBRef
    @Field("prescricoes")
    @JsonIgnoreProperties("internacoesDetalhes")
    private Prescricoes prescricoes;

    @DBRef
    @Field("posologias")
    @JsonIgnoreProperties("internacoesDetalhes")
    private Posologias posologias;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDataDetalhe() {
        return dataDetalhe;
    }

    public InternacoesDetalhes dataDetalhe(LocalDate dataDetalhe) {
        this.dataDetalhe = dataDetalhe;
        return this;
    }

    public void setDataDetalhe(LocalDate dataDetalhe) {
        this.dataDetalhe = dataDetalhe;
    }

    public LocalDate getHorario() {
        return horario;
    }

    public InternacoesDetalhes horario(LocalDate horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(LocalDate horario) {
        this.horario = horario;
    }

    public Float getQtd() {
        return qtd;
    }

    public InternacoesDetalhes qtd(Float qtd) {
        this.qtd = qtd;
        return this;
    }

    public void setQtd(Float qtd) {
        this.qtd = qtd;
    }

    public Internacoes getInternacoes() {
        return internacoes;
    }

    public InternacoesDetalhes internacoes(Internacoes internacoes) {
        this.internacoes = internacoes;
        return this;
    }

    public void setInternacoes(Internacoes internacoes) {
        this.internacoes = internacoes;
    }

    public Dietas getDietas() {
        return dietas;
    }

    public InternacoesDetalhes dietas(Dietas dietas) {
        this.dietas = dietas;
        return this;
    }

    public void setDietas(Dietas dietas) {
        this.dietas = dietas;
    }

    public Prescricoes getPrescricoes() {
        return prescricoes;
    }

    public InternacoesDetalhes prescricoes(Prescricoes prescricoes) {
        this.prescricoes = prescricoes;
        return this;
    }

    public void setPrescricoes(Prescricoes prescricoes) {
        this.prescricoes = prescricoes;
    }

    public Posologias getPosologias() {
        return posologias;
    }

    public InternacoesDetalhes posologias(Posologias posologias) {
        this.posologias = posologias;
        return this;
    }

    public void setPosologias(Posologias posologias) {
        this.posologias = posologias;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternacoesDetalhes)) {
            return false;
        }
        return id != null && id.equals(((InternacoesDetalhes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InternacoesDetalhes{" +
            "id=" + getId() +
            ", dataDetalhe='" + getDataDetalhe() + "'" +
            ", horario='" + getHorario() + "'" +
            ", qtd=" + getQtd() +
            "}";
    }
}
