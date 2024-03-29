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
 * A Posologias.
 */
@Entity
@Table(name = "posologias")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Posologias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "posologia", length = 40, nullable = false)
    private String posologia;

    @OneToMany(mappedBy = "posologiaPadrao")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posologiaPadrao", "fabricantes" }, allowSetters = true)
    private Set<Medicamentos> medicamentos = new HashSet<>();

    @OneToMany(mappedBy = "posologias")
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

    public Posologias id(Long id) {
        this.id = id;
        return this;
    }

    public String getPosologia() {
        return this.posologia;
    }

    public Posologias posologia(String posologia) {
        this.posologia = posologia;
        return this;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public Set<Medicamentos> getMedicamentos() {
        return this.medicamentos;
    }

    public Posologias medicamentos(Set<Medicamentos> medicamentos) {
        this.setMedicamentos(medicamentos);
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
        if (this.medicamentos != null) {
            this.medicamentos.forEach(i -> i.setPosologiaPadrao(null));
        }
        if (medicamentos != null) {
            medicamentos.forEach(i -> i.setPosologiaPadrao(this));
        }
        this.medicamentos = medicamentos;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return this.internacoesDetalhes;
    }

    public Posologias internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.setInternacoesDetalhes(internacoesDetalhes);
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
        if (this.internacoesDetalhes != null) {
            this.internacoesDetalhes.forEach(i -> i.setPosologias(null));
        }
        if (internacoesDetalhes != null) {
            internacoesDetalhes.forEach(i -> i.setPosologias(this));
        }
        this.internacoesDetalhes = internacoesDetalhes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Posologias{" +
            "id=" + getId() +
            ", posologia='" + getPosologia() + "'" +
            "}";
    }
}
