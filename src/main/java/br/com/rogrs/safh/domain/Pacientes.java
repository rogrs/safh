package br.com.rogrs.safh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.rogrs.safh.domain.enumeration.Estados;

/**
 * A Pacientes.
 */
@Entity
@Table(name = "pacientes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pacientes")
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne
    private Clinicas clinicas;

    @ManyToOne
    private Enfermarias enfermarias;

    @ManyToOne
    private Leitos leitos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProntuario() {
        return prontuario;
    }

    public void setProntuario(Long prontuario) {
        this.prontuario = prontuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estados getuF() {
        return uF;
    }

    public void setuF(Estados uF) {
        this.uF = uF;
    }

    public Clinicas getClinicas() {
        return clinicas;
    }

    public void setClinicas(Clinicas clinicas) {
        this.clinicas = clinicas;
    }

    public Enfermarias getEnfermarias() {
        return enfermarias;
    }

    public void setEnfermarias(Enfermarias enfermarias) {
        this.enfermarias = enfermarias;
    }

    public Leitos getLeitos() {
        return leitos;
    }

    public void setLeitos(Leitos leitos) {
        this.leitos = leitos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pacientes pacientes = (Pacientes) o;
        if(pacientes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pacientes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pacientes{" +
            "id=" + id +
            ", prontuario='" + prontuario + "'" +
            ", nome='" + nome + "'" +
            ", cpf='" + cpf + "'" +
            ", email='" + email + "'" +
            ", cep='" + cep + "'" +
            ", logradouro='" + logradouro + "'" +
            ", numero='" + numero + "'" +
            ", complemento='" + complemento + "'" +
            ", bairro='" + bairro + "'" +
            ", cidade='" + cidade + "'" +
            ", uF='" + uF + "'" +
            '}';
    }
}
