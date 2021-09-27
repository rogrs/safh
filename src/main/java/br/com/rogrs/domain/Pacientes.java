package br.com.rogrs.domain;

import br.com.rogrs.domain.enumeration.Estados;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pacientes.
 */
@Entity
@Table(name = "pacientes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "prontuario", nullable = false)
    private Long prontuario;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Size(max = 11)
    @Column(name = "cpf", length = 11)
    private String cpf;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 10)
    @Column(name = "cep", length = 10)
    private String cep;

    @Size(max = 80)
    @Column(name = "logradouro", length = 80)
    private String logradouro;

    @Size(max = 10)
    @Column(name = "numero", length = 10)
    private String numero;

    @Size(max = 60)
    @Column(name = "complemento", length = 60)
    private String complemento;

    @Size(max = 60)
    @Column(name = "bairro", length = 60)
    private String bairro;

    @Size(max = 60)
    @Column(name = "cidade", length = 60)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "u_f")
    private Estados uF;

    @OneToMany(mappedBy = "pacientes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internacoesDetalhes", "pacientes", "clinicas", "medicos" }, allowSetters = true)
    private Set<Internacoes> internacoes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes", "internacoes" }, allowSetters = true)
    private Clinicas clinicas;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes" }, allowSetters = true)
    private Enfermarias enfermarias;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes" }, allowSetters = true)
    private Leitos leitos;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pacientes id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProntuario() {
        return this.prontuario;
    }

    public Pacientes prontuario(Long prontuario) {
        this.prontuario = prontuario;
        return this;
    }

    public void setProntuario(Long prontuario) {
        this.prontuario = prontuario;
    }

    public String getNome() {
        return this.nome;
    }

    public Pacientes nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Pacientes cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public Pacientes email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return this.cep;
    }

    public Pacientes cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public Pacientes logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return this.numero;
    }

    public Pacientes numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public Pacientes complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Pacientes bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Pacientes cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estados getuF() {
        return this.uF;
    }

    public Pacientes uF(Estados uF) {
        this.uF = uF;
        return this;
    }

    public void setuF(Estados uF) {
        this.uF = uF;
    }

    public Set<Internacoes> getInternacoes() {
        return this.internacoes;
    }

    public Pacientes internacoes(Set<Internacoes> internacoes) {
        this.setInternacoes(internacoes);
        return this;
    }

    public Pacientes addInternacoes(Internacoes internacoes) {
        this.internacoes.add(internacoes);
        internacoes.setPacientes(this);
        return this;
    }

    public Pacientes removeInternacoes(Internacoes internacoes) {
        this.internacoes.remove(internacoes);
        internacoes.setPacientes(null);
        return this;
    }

    public void setInternacoes(Set<Internacoes> internacoes) {
        if (this.internacoes != null) {
            this.internacoes.forEach(i -> i.setPacientes(null));
        }
        if (internacoes != null) {
            internacoes.forEach(i -> i.setPacientes(this));
        }
        this.internacoes = internacoes;
    }

    public Clinicas getClinicas() {
        return this.clinicas;
    }

    public Pacientes clinicas(Clinicas clinicas) {
        this.setClinicas(clinicas);
        return this;
    }

    public void setClinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
    }

    public Enfermarias getEnfermarias() {
        return this.enfermarias;
    }

    public Pacientes enfermarias(Enfermarias enfermarias) {
        this.setEnfermarias(enfermarias);
        return this;
    }

    public void setEnfermarias(Enfermarias enfermarias) {
        this.enfermarias = enfermarias;
    }

    public Leitos getLeitos() {
        return this.leitos;
    }

    public Pacientes leitos(Leitos leitos) {
        this.setLeitos(leitos);
        return this;
    }

    public void setLeitos(Leitos leitos) {
        this.leitos = leitos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pacientes)) {
            return false;
        }
        return id != null && id.equals(((Pacientes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pacientes{" +
            "id=" + getId() +
            ", prontuario=" + getProntuario() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", email='" + getEmail() + "'" +
            ", cep='" + getCep() + "'" +
            ", logradouro='" + getLogradouro() + "'" +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", uF='" + getuF() + "'" +
            "}";
    }
}
