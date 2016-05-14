package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "clinica", nullable = false)
    private String clinica;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "descricao")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Leitos> leitos = new HashSet<>();

    @OneToMany(mappedBy = "enfermaria")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Enfermarias> enfermarias = new HashSet<>();

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

    public Set<Leitos> getLeitos() {
        return leitos;
    }

    public void setLeitos(Set<Leitos> leitos) {
        this.leitos = leitos;
    }

    public Set<Enfermarias> getEnfermarias() {
        return enfermarias;
    }

    public void setEnfermarias(Set<Enfermarias> enfermarias) {
        this.enfermarias = enfermarias;
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
