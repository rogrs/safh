package br.com.rogrs.safh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A InternacoesDetalhes.
 */
@Entity
@Table(name = "internacoes_detalhes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "internacoesdetalhes")
public class InternacoesDetalhes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_detalhe", nullable = false)
    private LocalDate dataDetalhe;

    @NotNull
    @Column(name = "horario", nullable = false)
    private LocalDate horario;

    @NotNull
    @Column(name = "qtd", nullable = false)
    private Float qtd;

    @ManyToOne
    private Internacoes internacoes;

    @ManyToOne
    private Dietas dietas;

    @ManyToOne
    private Prescricoes prescricoes;

    @ManyToOne
    private Posologias posologias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDetalhe() {
        return dataDetalhe;
    }

    public void setDataDetalhe(LocalDate dataDetalhe) {
        this.dataDetalhe = dataDetalhe;
    }

    public LocalDate getHorario() {
        return horario;
    }

    public void setHorario(LocalDate horario) {
        this.horario = horario;
    }

    public Float getQtd() {
        return qtd;
    }

    public void setQtd(Float qtd) {
        this.qtd = qtd;
    }

    public Internacoes getInternacoes() {
        return internacoes;
    }

    public void setInternacoes(Internacoes internacoes) {
        this.internacoes = internacoes;
    }

    public Dietas getDietas() {
        return dietas;
    }

    public void setDietas(Dietas dietas) {
        this.dietas = dietas;
    }

    public Prescricoes getPrescricoes() {
        return prescricoes;
    }

    public void setPrescricoes(Prescricoes prescricoes) {
        this.prescricoes = prescricoes;
    }

    public Posologias getPosologias() {
        return posologias;
    }

    public void setPosologias(Posologias posologias) {
        this.posologias = posologias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InternacoesDetalhes internacoesDetalhes = (InternacoesDetalhes) o;
        if(internacoesDetalhes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, internacoesDetalhes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InternacoesDetalhes{" +
            "id=" + id +
            ", dataDetalhe='" + dataDetalhe + "'" +
            ", horario='" + horario + "'" +
            ", qtd='" + qtd + "'" +
            '}';
    }
}
