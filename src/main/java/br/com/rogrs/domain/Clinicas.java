package br.com.rogrs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Clinicas.
 */
@Document(collection = "clinicas")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "clinicas")
public class Clinicas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 80)
    @Field("clinica")
    private String clinica;

    @Size(max = 100)
    @Field("descricao")
    private String descricao;

    @DBRef
    @Field("pacientes")
    private Set<Pacientes> pacientes = new HashSet<>();

    @DBRef
    @Field("internacoes")
    private Set<Internacoes> internacoes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinica() {
        return clinica;
    }

    public Clinicas clinica(String clinica) {
        this.clinica = clinica;
        return this;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public String getDescricao() {
        return descricao;
    }

    public Clinicas descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Pacientes> getPacientes() {
        return pacientes;
    }

    public Clinicas pacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
        return this;
    }

    public Clinicas addPacientes(Pacientes pacientes) {
        this.pacientes.add(pacientes);
        pacientes.setClinicas(this);
        return this;
    }

    public Clinicas removePacientes(Pacientes pacientes) {
        this.pacientes.remove(pacientes);
        pacientes.setClinicas(null);
        return this;
    }

    public void setPacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
    }

    public Set<Internacoes> getInternacoes() {
        return internacoes;
    }

    public Clinicas internacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
        return this;
    }

    public Clinicas addInternacoes(Internacoes internacoes) {
        this.internacoes.add(internacoes);
        internacoes.setClinicas(this);
        return this;
    }

    public Clinicas removeInternacoes(Internacoes internacoes) {
        this.internacoes.remove(internacoes);
        internacoes.setClinicas(null);
        return this;
    }

    public void setInternacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clinicas)) {
            return false;
        }
        return id != null && id.equals(((Clinicas) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Clinicas{" +
            "id=" + getId() +
            ", clinica='" + getClinica() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
