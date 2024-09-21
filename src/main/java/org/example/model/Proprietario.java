package org.example.model;

public class Proprietario {

    private int id;
    private String cpf;
    private String nomeCompleto;
    private String telefone;
    private String endereco;

    public void cadastrar() {
        //TODO implement this method
    }

    public void excluir() {
        //TODO implement this method
    }

    public void editar() {
        //TODO implement this method
    }

    public void listar() {
        //TODO implement this method
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {

        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
