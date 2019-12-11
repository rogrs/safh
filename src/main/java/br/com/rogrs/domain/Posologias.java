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
 * A Posologias.
 */
@Document(collection = "posologias")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "posologias")
public class Posologias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 40)
    @Field("posologia")
    private String posologia;

    @DBRef
    @Field("medicamentos")
    private Set<Medicamentos> medicamentos = new HashSet<>();

    @DBRef
    @Field("internacoesDetalhes")
    private Set<InternacoesDetalhes> internacoesDetalhes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosologia() {
        return posologia;
    }

    public Posologias posologia(String posologia) {
        this.posologia = posologia;
        return this;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public Set<Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public Posologias medicamentos(Set<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
        return this;
    }

    public Posologias addMedicamentos(Medicamentos medicamentos) {
        this.medicamentos.add(medicamentos);
        medicamentos.setPosologiaPadrao(this);
        return this;
    }

    public Posologias removeMedicamentos(Medicamentos medicamentos) {
        this.medicamentos.remove(medicamentos);
        medicamentos.setPosologiaPadrao(null);
        return this;
    }

    public void setMedicamentos(Set<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return internacoesDetalhes;
    }

    public Posologias internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
        return this;
    }

    public Posologias addInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.add(internacoesDetalhes);
        internacoesDetalhes.setPosologias(this);
        return this;
    }

    public Posologias removeInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.remove(internacoesDetalhes);
        internacoesDetalhes.setPosologias(null);
        return this;
    }

    public void setInternacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posologias)) {
            return false;
        }
        return id != null && id.equals(((Posologias) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Posologias{" +
            "id=" + getId() +
            ", posologia='" + getPosologia() + "'" +
            "}";
    }
}
