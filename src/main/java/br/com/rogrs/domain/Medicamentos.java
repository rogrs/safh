package br.com.rogrs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medicamentos.
 */
@Entity
@Table(name = "medicamentos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medicamentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    @NotNull
    @Size(max = 60)
    @Column(name = "registro_ministerio_saude", length = 60, nullable = false)
    private String registroMinisterioSaude;

    @NotNull
    @Size(max = 13)
    @Column(name = "codigo_barras", length = 13, nullable = false)
    private String codigoBarras;

    @Column(name = "qtd_atual")
    private Float qtdAtual;

    @Column(name = "qtd_min")
    private Float qtdMin;

    @Column(name = "qtd_max")
    private Float qtdMax;

    @Size(max = 8000)
    @Column(name = "observacoes", length = 8000)
    private String observacoes;

    @Column(name = "apresentacao")
    private String apresentacao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "medicamentos", "internacoesDetalhes" }, allowSetters = true)
    private Posologias posologiaPadrao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "medicamentos" }, allowSetters = true)
    private Fabricantes fabricantes;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicamentos id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Medicamentos descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegistroMinisterioSaude() {
        return this.registroMinisterioSaude;
    }

    public Medicamentos registroMinisterioSaude(String registroMinisterioSaude) {
        this.registroMinisterioSaude = registroMinisterioSaude;
        return this;
    }

    public void setRegistroMinisterioSaude(String registroMinisterioSaude) {
        this.registroMinisterioSaude = registroMinisterioSaude;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    public Medicamentos codigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
        return this;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Float getQtdAtual() {
        return this.qtdAtual;
    }

    public Medicamentos qtdAtual(Float qtdAtual) {
        this.qtdAtual = qtdAtual;
        return this;
    }

    public void setQtdAtual(Float qtdAtual) {
        this.qtdAtual = qtdAtual;
    }

    public Float getQtdMin() {
        return this.qtdMin;
    }

    public Medicamentos qtdMin(Float qtdMin) {
        this.qtdMin = qtdMin;
        return this;
    }

    public void setQtdMin(Float qtdMin) {
        this.qtdMin = qtdMin;
    }

    public Float getQtdMax() {
        return this.qtdMax;
    }

    public Medicamentos qtdMax(Float qtdMax) {
        this.qtdMax = qtdMax;
        return this;
    }

    public void setQtdMax(Float qtdMax) {
        this.qtdMax = qtdMax;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Medicamentos observacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getApresentacao() {
        return this.apresentacao;
    }

    public Medicamentos apresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
        return this;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public Posologias getPosologiaPadrao() {
        return this.posologiaPadrao;
    }

    public Medicamentos posologiaPadrao(Posologias posologias) {
        this.setPosologiaPadrao(posologias);
        return this;
    }

    public void setPosologiaPadrao(Posologias posologias) {
        this.posologiaPadrao = posologias;
    }

    public Fabricantes getFabricantes() {
        return this.fabricantes;
    }

    public Medicamentos fabricantes(Fabricantes fabricantes) {
        this.setFabricantes(fabricantes);
        return this;
    }

    public void setFabricantes(Fabricantes fabricantes) {
        this.fabricantes = fabricantes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicamentos)) {
            return false;
        }
        return id != null && id.equals(((Medicamentos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicamentos{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", registroMinisterioSaude='" + getRegistroMinisterioSaude() + "'" +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", qtdAtual=" + getQtdAtual() +
            ", qtdMin=" + getQtdMin() +
            ", qtdMax=" + getQtdMax() +
            ", observacoes='" + getObservacoes() + "'" +
            ", apresentacao='" + getApresentacao() + "'" +
            "}";
    }
}
