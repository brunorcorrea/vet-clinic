package org.example.controller;

import org.example.dao.EstoqueDAO;
import org.example.model.Estoque;

import java.sql.SQLException;
import java.util.List;

public class EstoqueController {

    private static EstoqueController instance;

    private EstoqueController() {
    }

    public static EstoqueController getInstance() {
        if (instance == null) {
            instance = new EstoqueController();
        }
        return instance;
    }

    public List<Estoque> listarEstoque() throws SQLException {
        return EstoqueDAO.getInstance().listar();
    }

    public void adicionarEstoque(Estoque estoque) throws SQLException {
        EstoqueDAO.getInstance().cadastrar(estoque);
    }

    public void editarEstoque(Estoque estoque) throws SQLException {
        EstoqueDAO.getInstance().editar(estoque);
    }

    public void removerEstoque(Estoque estoque) throws SQLException {
        EstoqueDAO.getInstance().excluir(estoque.getId());
    }
}
