package br.com.rogrs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Internacoes.
 */
@Entity
@Table(name = "internacoes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Internacoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_internacao", nullable = false)
    private LocalDate dataInternacao;

    @NotNull
    @Size(max = 200)
    @Column(name = "descricao", length = 200, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "internacoes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoes", "dietas", "prescricoes", "posologias" }, allowSetters = true)
    private Set<InternacoesDetalhes> internacoesDetalhes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "internacoes", "clinicas", "enfermarias", "leitos" }, allowSetters = true)
    private Pacientes pacientes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes", "internacoes" }, allowSetters = true)
    private Clinicas clinicas;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internacoes", "especialidades" }, allowSetters = true)
    private Medicos medicos;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Internacoes id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDataInternacao() {
        return this.dataInternacao;
    }

    public Internacoes dataInternacao(LocalDate dataInternacao) {
        this.dataInternacao = dataInternacao;
        return this;
    }

    public void setDataInternacao(LocalDate dataInternacao) {
        this.dataInternacao = dataInternacao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Internacoes descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<InternacoesDetalhes> getInternacoesDetalhes() {
        return this.internacoesDetalhes;
    }

    public Internacoes internacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        this.setInternacoesDetalhes(internacoesDetalhes);
        return this;
    }

    public Internacoes addInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.add(internacoesDetalhes);
        internacoesDetalhes.setInternacoes(this);
        return this;
    }

    public Internacoes removeInternacoesDetalhes(InternacoesDetalhes internacoesDetalhes) {
        this.internacoesDetalhes.remove(internacoesDetalhes);
        internacoesDetalhes.setInternacoes(null);
        return this;
    }

    public void setInternacoesDetalhes(Set<InternacoesDetalhes> internacoesDetalhes) {
        if (this.internacoesDetalhes != null) {
            this.internacoesDetalhes.forEach(i -> i.setInternacoes(null));
        }
        if (internacoesDetalhes != null) {
            internacoesDetalhes.forEach(i -> i.setInternacoes(this));
        }
        this.internacoesDetalhes = internacoesDetalhes;
    }

    public Pacientes getPacientes() {
        return this.pacientes;
    }

    public Internacoes pacientes(Pacientes pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public Clinicas getClinicas() {
        return this.clinicas;
    }

    public Internacoes clinicas(Clinicas clinicas) {
        this.setClinicas(clinicas);
        return this;
    }

    public void setClinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
    }

    public Medicos getMedicos() {
        return this.medicos;
    }

    public Internacoes medicos(Medicos medicos) {
        this.setMedicos(medicos);
        return this;
    }

    public void setMedicos(Medicos medicos) {
        this.medicos = medicos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Internacoes)) {
            return false;
        }
        return id != null && id.equals(((Internacoes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Internacoes{" +
            "id=" + getId() +
            ", dataInternacao='" + getDataInternacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
