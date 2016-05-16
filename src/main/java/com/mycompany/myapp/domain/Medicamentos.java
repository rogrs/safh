package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Medicamentos.
 */
@Entity
@Table(name = "medicamentos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicamentos")
public class Medicamentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "registro_ministerio_saude", nullable = false)
    private String registroMinisterioSaude;

    @Column(name = "codigo_barras")
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
    private Fabricantes fabricantes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegistroMinisterioSaude() {
        return registroMinisterioSaude;
    }

    public void setRegistroMinisterioSaude(String registroMinisterioSaude) {
        this.registroMinisterioSaude = registroMinisterioSaude;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Float getQtdAtual() {
        return qtdAtual;
    }

    public void setQtdAtual(Float qtdAtual) {
        this.qtdAtual = qtdAtual;
    }

    public Float getQtdMin() {
        return qtdMin;
    }

    public void setQtdMin(Float qtdMin) {
        this.qtdMin = qtdMin;
    }

    public Float getQtdMax() {
        return qtdMax;
    }

    public void setQtdMax(Float qtdMax) {
        this.qtdMax = qtdMax;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public Fabricantes getFabricantes() {
        return fabricantes;
    }

    public void setFabricantes(Fabricantes fabricantes) {
        this.fabricantes = fabricantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Medicamentos medicamentos = (Medicamentos) o;
        if(medicamentos.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, medicamentos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Medicamentos{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", registroMinisterioSaude='" + registroMinisterioSaude + "'" +
            ", codigoBarras='" + codigoBarras + "'" +
            ", qtdAtual='" + qtdAtual + "'" +
            ", qtdMin='" + qtdMin + "'" +
            ", qtdMax='" + qtdMax + "'" +
            ", observacoes='" + observacoes + "'" +
            ", apresentacao='" + apresentacao + "'" +
            '}';
    }
}
