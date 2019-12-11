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
 * A Prescricoes.
 */
@Document(collection = "prescricoes")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prescricoes")
public class Prescricoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 100)
    @Field("prescricao")
    private String prescricao;

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

    public String getPrescricao() {
        return prescricao;
    }

    public Prescricoes prescricao(String prescricao) {
        this.prescricao = prescricao;
        return this;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return internacoesDetalhes;
    }

    public Prescricoes internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.internacoesDetalhes = internacoesDetalhes;
        return this;
    }

    public Prescricoes addInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.add(internacoesDetalhes);
        internacoesDetalhes.setPrescricoes(this);
        return this;
    }

    public Prescricoes removeInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.remove(internacoesDetalhes);
        internacoesDetalhes.setPrescricoes(null);
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
        if (!(o instanceof Prescricoes)) {
            return false;
        }
        return id != null && id.equals(((Prescricoes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Prescricoes{" +
            "id=" + getId() +
            ", prescricao='" + getPrescricao() + "'" +
            "}";
    }
}
