package org.example.controller;

import org.example.dao.ProdutoDAO;
import org.example.model.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {

    private static ProdutoController instance;

    private ProdutoController() {
    }

    public static ProdutoController getInstance() {
        if (instance == null) {
            instance = new ProdutoController();
        }
        return instance;
    }

    public List<Produto> listarProdutos() throws SQLException {
        return ProdutoDAO.getInstance().listar();
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        ProdutoDAO.getInstance().cadastrar(produto);
    }

    public void editarProduto(Produto produto) throws SQLException {
        ProdutoDAO.getInstance().editar(produto);
    }

    public void removerProduto(Produto produto) throws SQLException {
        ProdutoDAO.getInstance().excluir(produto.getId());
    }
}
