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
 * A Fabricantes.
 */
@Document(collection = "fabricantes")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fabricantes")
public class Fabricantes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 60)
    @Field("fabricante")
    private String fabricante;

    @DBRef
    @Field("medicamentos")
    private Set<Medicamentos> medicamentos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public Fabricantes fabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Set<Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public Fabricantes medicamentos(Set<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
        return this;
    }

    public Fabricantes addMedicamentos(Medicamentos medicamentos) {
        this.medicamentos.add(medicamentos);
        medicamentos.setFabricantes(this);
        return this;
    }

    public Fabricantes removeMedicamentos(Medicamentos medicamentos) {
        this.medicamentos.remove(medicamentos);
        medicamentos.setFabricantes(null);
        return this;
    }

    public void setMedicamentos(Set<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fabricantes)) {
            return false;
        }
        return id != null && id.equals(((Fabricantes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fabricantes{" +
            "id=" + getId() +
            ", fabricante='" + getFabricante() + "'" +
            "}";
    }
}
