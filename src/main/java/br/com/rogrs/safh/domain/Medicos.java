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

import br.com.rogrs.safh.domain.enumeration.Estados;

/**
 * A Medicos.
 */
@Entity
@Table(name = "medicos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicos")
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotNull
    @Size(max = 40)
    @Column(name = "crm", length = 40, nullable = false)
    private String crm;

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

    @OneToMany(mappedBy = "medicos")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Internacoes> internacoes = new HashSet<>();

    @ManyToOne
    private Especialidades especialidades;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Medicos nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public Medicos crm(String crm) {
        this.crm = crm;
        return this;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getCpf() {
        return cpf;
    }

    public Medicos cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public Medicos email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public Medicos cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public Medicos logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public Medicos numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public Medicos complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public Medicos bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Medicos cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estados getuF() {
        return uF;
    }

    public Medicos uF(Estados uF) {
        this.uF = uF;
        return this;
    }

    public void setuF(Estados uF) {
        this.uF = uF;
    }

    public Set<Internacoes> getInternacoes() {
        return internacoes;
    }

    public Medicos internacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
        return this;
    }

    public Medicos addInternacoes(Internacoes internacoes) {
        this.internacoes.add(internacoes);
        internacoes.setMedicos(this);
        return this;
    }

    public Medicos removeInternacoes(Internacoes internacoes) {
        this.internacoes.remove(internacoes);
        internacoes.setMedicos(null);
        return this;
    }

    public void setInternacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
    }

    public Especialidades getEspecialidades() {
        return especialidades;
    }

    public Medicos especialidades(Especialidades especialidades) {
        this.especialidades = especialidades;
        return this;
    }

    public void setEspecialidades(Especialidades especialidades) {
        this.especialidades = especialidades;
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
        Medicos medicos = (Medicos) o;
        if (medicos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medicos{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", crm='" + getCrm() + "'" +
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
