package org.example.controller;

import org.example.model.Estoque;
import org.example.model.EstoqueProduto;
import org.example.model.Produto;
import org.example.view.tablemodels.EstoqueTableModel;

import java.util.ArrayList;
import java.util.List;

public class EstoqueViewController {
    private final EstoqueController estoqueController = EstoqueController.getInstance();
    private final ProdutoController produtoController = ProdutoController.getInstance();

    public List<Estoque> listarEstoque() throws Exception {
        return estoqueController.listarEstoque();
    }

    public List<Produto> listarProdutos() throws Exception {
        return produtoController.listarProdutos();
    }

    public void adicionarProduto(String nome, String tipo, double preco, int quantidade, int quantidadeMinima) throws Exception {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTipo(tipo);
        produto.setPreco(preco);

        produtoController.adicionarProduto(produto);

        int id = produtoController.listarProdutos().getLast().getId();
        produto.setId(id);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(quantidade);
        estoque.setQuantidadeMinima(quantidadeMinima);
        estoque.setNecessitaReposicao(quantidade < quantidadeMinima);
        estoque.setProduto(produto);

        estoqueController.adicionarEstoque(estoque);
    }

    public void removerProduto(int estoqueId, int produtoId) throws Exception {
        Estoque estoque = new Estoque();
        estoque.setId(estoqueId);
        estoqueController.removerEstoque(estoque);

        Produto produto = new Produto();
        produto.setId(produtoId);
        produtoController.removerProduto(produto);
    }

    public EstoqueTableModel criarEstoqueTableModel() throws Exception {
        List<Estoque> estoque = listarEstoque();
        List<Produto> produtos = listarProdutos();

        List<EstoqueProduto> estoqueProdutos = new ArrayList<>();
        for (int indice = 0; indice < estoque.size(); indice++) {
            Estoque estoqueAtual = estoque.get(indice);
            Produto produto = produtos.get(indice);
            String necessitaReposicao = estoqueAtual.isNecessitaReposicao() ? "Sim" : "Não";
            estoqueProdutos.add(new EstoqueProduto(estoqueAtual.getId(), produto.getId(), produto.getNome(), produto.getTipo(), produto.getPreco(), estoqueAtual.getQuantidade(), estoqueAtual.getQuantidadeMinima(), necessitaReposicao));
        }

        return new EstoqueTableModel(estoqueProdutos);
    }
}