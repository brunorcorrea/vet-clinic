package org.example.model;

public class Estoque {

    private int id;
    private Produto produto;
    private int quantidade;
    private int quantidadeMinima;
    private boolean necessitaReposicao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public boolean isNecessitaReposicao() {
        return necessitaReposicao;
    }

    public void setNecessitaReposicao(boolean necessitaReposicao) {
        this.necessitaReposicao = necessitaReposicao;
    }
}
