package org.example.model;

public class Paciente {

    private int id;
    private String nome;
    private EstadoCastracao estadoCastracao;
    private int idade;
    private String raca;
    private String coloracao;
    private String especie;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoCastracao getEstadoCastracao() {
        return estadoCastracao;
    }

    public void setEstadoCastracao(EstadoCastracao estadoCastracao) {
        this.estadoCastracao = estadoCastracao;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getColoracao() {
        return coloracao;
    }

    public void setColoracao(String coloracao) {
        this.coloracao = coloracao;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }
}
