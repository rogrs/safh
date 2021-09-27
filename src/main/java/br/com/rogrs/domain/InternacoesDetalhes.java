package br.com.rogrs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InternacoesDetalhes.
 */
@Entity
@Table(name = "internacoes_detalhes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InternacoesDetalhes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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
    @JsonIgnoreProperties(value = { "internacoesDetalhes", "pacientes", "clinicas", "medicos" }, allowSetters = true)
    private Internacoes internacoes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internacoesDetalhes" }, allowSetters = true)
    private Dietas dietas;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internacoesDetalhes" }, allowSetters = true)
    private Prescricoes prescricoes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "medicamentos", "internacoesDetalhes" }, allowSetters = true)
    private Posologias posologias;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InternacoesDetalhes id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDataDetalhe() {
        return this.dataDetalhe;
    }

    public InternacoesDetalhes dataDetalhe(LocalDate dataDetalhe) {
        this.dataDetalhe = dataDetalhe;
        return this;
    }

    public void setDataDetalhe(LocalDate dataDetalhe) {
        this.dataDetalhe = dataDetalhe;
    }

    public LocalDate getHorario() {
        return this.horario;
    }

    public InternacoesDetalhes horario(LocalDate horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(LocalDate horario) {
        this.horario = horario;
    }

    public Float getQtd() {
        return this.qtd;
    }

    public InternacoesDetalhes qtd(Float qtd) {
        this.qtd = qtd;
        return this;
    }

    public void setQtd(Float qtd) {
        this.qtd = qtd;
    }

    public Internacoes getInternacoes() {
        return this.internacoes;
    }

    public InternacoesDetalhes internacoes(Internacoes internacoes) {
        this.setInternacoes(internacoes);
        return this;
    }

    public void setInternacoes(Internacoes internacoes) {
        this.internacoes = internacoes;
    }

    public Dietas getDietas() {
        return this.dietas;
    }

    public InternacoesDetalhes dietas(Dietas dietas) {
        this.setDietas(dietas);
        return this;
    }

    public void setDietas(Dietas dietas) {
        this.dietas = dietas;
    }

    public Prescricoes getPrescricoes() {
        return this.prescricoes;
    }

    public InternacoesDetalhes prescricoes(Prescricoes prescricoes) {
        this.setPrescricoes(prescricoes);
        return this;
    }

    public void setPrescricoes(Prescricoes prescricoes) {
        this.prescricoes = prescricoes;
    }

    public Posologias getPosologias() {
        return this.posologias;
    }

    public InternacoesDetalhes posologias(Posologias posologias) {
        this.setPosologias(posologias);
        return this;
    }

    public void setPosologias(Posologias posologias) {
        this.posologias = posologias;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
