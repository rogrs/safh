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
 * A Dietas.
 */
@Document(collection = "dietas")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dietas")
public class Dietas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 40)
    @Field("dieta")
    private String dieta;

    @Size(max = 255)
    @Field("descricao")
    private String descricao;

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

    public String getDieta() {
        return dieta;
    }

    public Dietas dieta(String dieta) {
        this.dieta = dieta;
        return this;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getDescricao() {
        return descricao;
    }

    public Dietas descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return internacoesDetalhes;
    }

    public Dietas internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
        return this;
    }

    public Dietas addInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.add(internacoesDetalhes);
        internacoesDetalhes.setDietas(this);
        return this;
    }

    public Dietas removeInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.remove(internacoesDetalhes);
        internacoesDetalhes.setDietas(null);
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
        if (!(o instanceof Dietas)) {
            return false;
        }
        return id != null && id.equals(((Dietas) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dietas{" +
            "id=" + getId() +
            ", dieta='" + getDieta() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
