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
 * A Leitos.
 */
@Document(collection = "leitos")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leitos")
public class Leitos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 60)
    @Field("leito")
    private String leito;

    @Size(max = 40)
    @Field("tipo")
    private String tipo;

    @DBRef
    @Field("pacientes")
    private Set<Pacientes> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeito() {
        return leito;
    }

    public Leitos leito(String leito) {
        this.leito = leito;
        return this;
    }

    public void setLeito(String leito) {
        this.leito = leito;
    }

    public String getTipo() {
        return tipo;
    }

    public Leitos tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<Pacientes> getPacientes() {
        return pacientes;
    }

    public Leitos pacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
        return this;
    }

    public Leitos addPacientes(Pacientes pacientes) {
        this.pacientes.add(pacientes);
        pacientes.setLeitos(this);
        return this;
    }

    public Leitos removePacientes(Pacientes pacientes) {
        this.pacientes.remove(pacientes);
        pacientes.setLeitos(null);
        return this;
    }

    public void setPacientes(Set<Pacientes> pacientes) {
        this.pacientes = pacientes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Leitos)) {
            return false;
        }
        return id != null && id.equals(((Leitos) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Leitos{" +
            "id=" + getId() +
            ", leito='" + getLeito() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
