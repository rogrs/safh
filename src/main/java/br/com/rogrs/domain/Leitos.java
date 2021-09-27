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
 * A Leitos.
 */
@Entity
@Table(name = "leitos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Leitos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "leito", length = 60, nullable = false)
    private String leito;

    @Size(max = 40)
    @Column(name = "tipo", length = 40)
    private String tipo;

    @OneToMany(mappedBy = "leitos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "clinicas", "enfermarias", "leitos" }, allowSetters = true)
    private Set<Pacientes> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Leitos id(Long id) {
        this.id = id;
        return this;
    }

    public String getLeito() {
        return this.leito;
    }

    public Leitos leito(String leito) {
        this.leito = leito;
        return this;
    }

    public void setLeito(String leito) {
        this.leito = leito;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Leitos tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<Pacientes> getPacientes() {
        return this.pacientes;
    }

    public Leitos pacientes(Set<Pacientes> pacientes) {
        this.setPacientes(pacientes);
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
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setLeitos(null));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.setLeitos(this));
        }
        this.pacientes = pacientes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Leitos{" +
            "id=" + getId() +
            ", leito='" + getLeito() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
