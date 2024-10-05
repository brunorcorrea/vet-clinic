package org.example.controller;

import org.example.dao.FaturamentoDAO;
import org.example.model.Faturamento;

import java.sql.SQLException;
import java.util.List;

public class FaturamentoController {

    private static FaturamentoController instance;

    private FaturamentoController() {
    }

    public static FaturamentoController getInstance() {
        if (instance == null) {
            instance = new FaturamentoController();
        }
        return instance;
    }

    public List<Faturamento> listarFaturamentos() throws SQLException {
        return FaturamentoDAO.getInstance().listar();
    }

    public void adicionarFaturamento(Faturamento faturamento) throws SQLException {
        FaturamentoDAO.getInstance().cadastrar(faturamento);
    }

    public void editarFaturamento(Faturamento faturamento) throws SQLException {
        FaturamentoDAO.getInstance().editar(faturamento);
    }

    public void removerFaturamento(Faturamento faturamento) throws SQLException {
        FaturamentoDAO.getInstance().excluir(faturamento.getId());
    }
}
