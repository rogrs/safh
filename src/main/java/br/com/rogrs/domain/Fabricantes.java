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
 * A Fabricantes.
 */
@Entity
@Table(name = "fabricantes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fabricantes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "fabricante", length = 60, nullable = false)
    private String fabricante;

    @OneToMany(mappedBy = "fabricantes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posologiaPadrao", "fabricantes" }, allowSetters = true)
    private Set<Medicamentos> medicamentos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fabricantes id(Long id) {
        this.id = id;
        return this;
    }

    public String getFabricante() {
        return this.fabricante;
    }

    public Fabricantes fabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Set<Medicamentos> getMedicamentos() {
        return this.medicamentos;
    }

    public Fabricantes medicamentos(Set<Medicamentos> medicamentos) {
        this.setMedicamentos(medicamentos);
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
        if (this.medicamentos != null) {
            this.medicamentos.forEach(i -> i.setFabricantes(null));
        }
        if (medicamentos != null) {
            medicamentos.forEach(i -> i.setFabricantes(this));
        }
        this.medicamentos = medicamentos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fabricantes{" +
            "id=" + getId() +
            ", fabricante='" + getFabricante() + "'" +
            "}";
    }
}
