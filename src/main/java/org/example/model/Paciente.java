package org.example.model;

public class Paciente {

    private int id;
    private String nome;
    private EstadoCastracao estadoCastracao;
    private int idade;
    private String raca;
    private String coloracao;
    private String especie;
    private byte[] foto;
    private Proprietario proprietario;

    public Paciente() {
    }

    public Paciente(int id, String nome, EstadoCastracao estadoCastracao, int idade, String raca, String coloracao, String especie, byte[] foto, Proprietario proprietario) {
        this.id = id;
        this.nome = nome;
        this.estadoCastracao = estadoCastracao;
        this.idade = idade;
        this.raca = raca;
        this.coloracao = coloracao;
        this.especie = especie;
        this.foto = foto;
        this.proprietario = proprietario;
    }

    public void cadastrar() {
    }

    public void excluir() {
    }

    public void editar() {
    }

    public void listar() {
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }
}
