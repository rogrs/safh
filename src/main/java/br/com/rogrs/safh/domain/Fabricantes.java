package br.com.rogrs.safh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Fabricantes.
 */
@Entity
@Table(name = "fabricantes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fabricantes")
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
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Medicamentos> medicamentos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fabricantes fabricantes = (Fabricantes) o;
        if (fabricantes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fabricantes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Fabricantes{" +
            "id=" + getId() +
            ", fabricante='" + getFabricante() + "'" +
            "}";
    }
}
