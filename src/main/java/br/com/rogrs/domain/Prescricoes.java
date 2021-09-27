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
 * A Prescricoes.
 */
@Entity
@Table(name = "prescricoes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prescricoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "prescricao", length = 100, nullable = false)
    private String prescricao;

    @OneToMany(mappedBy = "prescricoes")
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

    public Prescricoes id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrescricao() {
        return this.prescricao;
    }

    public Prescricoes prescricao(String prescricao) {
        this.prescricao = prescricao;
        return this;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return this.internacoesDetalhes;
    }

    public Prescricoes internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.setInternacoesDetalhes(internacoesDetalhes);
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
        if (this.internacoesDetalhes != null) {
            this.internacoesDetalhes.forEach(i -> i.setPrescricoes(null));
        }
        if (internacoesDetalhes != null) {
            internacoesDetalhes.forEach(i -> i.setPrescricoes(this));
        }
        this.internacoesDetalhes = internacoesDetalhes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prescricoes{" +
            "id=" + getId() +
            ", prescricao='" + getPrescricao() + "'" +
            "}";
    }
}
