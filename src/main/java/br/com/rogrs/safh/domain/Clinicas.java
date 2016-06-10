package br.com.rogrs.safh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Clinicas.
 */
@Entity
@Table(name = "clinicas")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clinicas")
public class Clinicas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "clinica", length = 80, nullable = false)
    private String clinica;

    @Size(max = 100)
    @Column(name = "descricao", length = 100)
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClinica() {
        return clinica;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clinicas clinicas = (Clinicas) o;
        if(clinicas.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clinicas.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Clinicas{" +
            "id=" + id +
            ", clinica='" + clinica + "'" +
            ", descricao='" + descricao + "'" +
            '}';
    }
}
