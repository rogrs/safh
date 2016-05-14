package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "sobrenome", nullable = false)
    private String sobrenome;

    @NotNull
    @Pattern(regexp = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}")
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    @Column(name = "email")
    private String email;

    @NotNull
    @Pattern(regexp = "\\([0-9]{2}\\) [0-9]{4,6}-[0-9]{3,4}$")
    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Pattern(regexp = "\\([0-9]{2}\\) [0-9]{4,6}-[0-9]{3,4}$")
    @Column(name = "celular")
    private String celular;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
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
            ", nome='" + nome + "'" +
            ", sobrenome='" + sobrenome + "'" +
            ", cpf='" + cpf + "'" +
            ", nascimento='" + nascimento + "'" +
            ", naturalidade='" + naturalidade + "'" +
            ", email='" + email + "'" +
            ", telefone='" + telefone + "'" +
            ", celular='" + celular + "'" +
            '}';
    }
}
