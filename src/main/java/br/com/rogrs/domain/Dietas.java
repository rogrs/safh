package br.com.rogrs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dietas.
 */
@Entity
@Table(name = "dietas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dietas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "dieta", length = 40, nullable = false)
    private String dieta;

    @Size(max = 255)
    @Column(name = "descricao", length = 255)
    private String descricao;

    @OneToMany(mappedBy = "dietas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "dietas", "prescricoes", "posologias" }, allowSetters = true)
    private Set<InternacoesDetalhes> internacoesDetalhes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dietas id(Long id) {
        this.id = id;
        return this;
    }

    public String getDieta() {
        return this.dieta;
    }

    public Dietas dieta(String dieta) {
        this.dieta = dieta;
        return this;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Dietas descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return this.internacoesDetalhes;
    }

    public Dietas internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.setInternacoesDetalhes(internacoesDetalhes);
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
        if (this.internacoesDetalhes != null) {
            this.internacoesDetalhes.forEach(i -> i.setDietas(null));
        }
        if (internacoesDetalhes != null) {
            internacoesDetalhes.forEach(i -> i.setDietas(this));
        }
        this.internacoesDetalhes = internacoesDetalhes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dietas{" +
            "id=" + getId() +
            ", dieta='" + getDieta() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
