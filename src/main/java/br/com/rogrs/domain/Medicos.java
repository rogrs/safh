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
 * A Medicos.
 */
@Document(collection = "medicos")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "medicos")
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Size(max = 255)
    @Field("nome")
    private String nome;

    @NotNull
    @Size(max = 40)
    @Field("crm")
    private String crm;

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
    @Field("especialidades")
    @JsonIgnoreProperties("medicos")
    private Especialidades especialidades;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (!(o instanceof Medicos)) {
            return false;
        }
        return id != null && id.equals(((Medicos) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
