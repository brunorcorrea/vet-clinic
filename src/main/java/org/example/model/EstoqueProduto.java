package org.example.model;

public class EstoqueProduto {

    private int idEstoque;
    private int idProduto;
    private String nomeProduto;
    private String tipoProduto;
    private double precoProduto;
    private int quantidade;
    private int quantidadeMinima;
    private String necessitaReposicao;

    public EstoqueProduto(int idEstoque, int idProduto, String nomeProduto, String tipoProduto, double precoProduto, int quantidade, int quantidadeMinima, String necessitaReposicao) {
        this.idEstoque = idEstoque;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.tipoProduto = tipoProduto;
        this.precoProduto = precoProduto;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.necessitaReposicao = necessitaReposicao;
    }

    public EstoqueProduto() {

    }

    public int getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(int idEstoque) {
        this.idEstoque = idEstoque;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(double precoProduto) {
        this.precoProduto = precoProduto;
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

    public String getNecessitaReposicao() {
        return necessitaReposicao;
    }

    public void setNecessitaReposicao(String necessitaReposicao) {
        this.necessitaReposicao = necessitaReposicao;
    }
}
