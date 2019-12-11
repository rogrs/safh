package br.com.rogrs.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.com.rogrs.domain.enumeration.Estados;

/**
 * A Pacientes.
 */
@Document(collection = "pacientes")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pacientes")
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Field("prontuario")
    private Long prontuario;

    @NotNull
    @Size(max = 255)
    @Field("nome")
    private String nome;

    @Size(max = 11)
    @Field("cpf")
    private String cpf;

    @Size(max = 100)
    @Field("email")
    private String email;

    @Size(max = 10)
    @Field("cep")
    private String cep;

    @Size(max = 80)
    @Field("logradouro")
    private String logradouro;

    @Size(max = 10)
    @Field("numero")
    private String numero;

    @Size(max = 60)
    @Field("complemento")
    private String complemento;

    @Size(max = 60)
    @Field("bairro")
    private String bairro;

    @Size(max = 60)
    @Field("cidade")
    private String cidade;

    @Field("u_f")
    private Estados uF;

    @DBRef
    @Field("internacoes")
    private Set<Internacoes> internacoes = new HashSet<>();

    @DBRef
    @Field("clinicas")
    @JsonIgnoreProperties("pacientes")
    private Clinicas clinicas;

    @DBRef
    @Field("enfermarias")
    @JsonIgnoreProperties("pacientes")
    private Enfermarias enfermarias;

    @DBRef
    @Field("leitos")
    @JsonIgnoreProperties("pacientes")
    private Leitos leitos;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProntuario() {
        return prontuario;
    }

    public Pacientes prontuario(Long prontuario) {
        this.prontuario = prontuario;
        return this;
    }

    public void setProntuario(Long prontuario) {
        this.prontuario = prontuario;
    }

    public String getNome() {
        return nome;
    }

    public Pacientes nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Pacientes cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public Pacientes email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public Pacientes cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public Pacientes logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public Pacientes numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public Pacientes complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public Pacientes bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Pacientes cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estados getuF() {
        return uF;
    }

    public Pacientes uF(Estados uF) {
        this.uF = uF;
        return this;
    }

    public void setuF(Estados uF) {
        this.uF = uF;
    }

    public Set<Internacoes> getInternacoes() {
        return internacoes;
    }

    public Pacientes internacoes(Set<Internacoes> internacoes) {
        this.internacoes = internacoes;
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
        this.internacoes = internacoes;
    }

    public Clinicas getClinicas() {
        return clinicas;
    }

    public Pacientes clinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
        return this;
    }

    public void setClinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
    }

    public Enfermarias getEnfermarias() {
        return enfermarias;
    }

    public Pacientes enfermarias(Enfermarias enfermarias) {
        this.enfermarias = enfermarias;
        return this;
    }

    public void setEnfermarias(Enfermarias enfermarias) {
        this.enfermarias = enfermarias;
    }

    public Leitos getLeitos() {
        return leitos;
    }

    public Pacientes leitos(Leitos leitos) {
        this.leitos = leitos;
        return this;
    }

    public void setLeitos(Leitos leitos) {
        this.leitos = leitos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

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
