package org.example.controller;

import org.example.dao.HistoricoDAO;
import org.example.model.Historico;

import java.sql.SQLException;
import java.util.List;

public class HistoricoController {

    private static HistoricoController instance;

    private HistoricoController() {
    }

    public static HistoricoController getInstance() {
        if (instance == null) {
            instance = new HistoricoController();
        }
        return instance;
    }

    public List<Historico> listarHistoricos() throws SQLException {
        return HistoricoDAO.getInstance().listar();
    }

    public void adicionarHistorico(Historico historico) throws SQLException {
        HistoricoDAO.getInstance().cadastrar(historico);
    }

    public void editarHistorico(Historico historico) throws SQLException {
        HistoricoDAO.getInstance().editar(historico);
    }

    public void removerHistorico(Historico historico) throws SQLException {
        HistoricoDAO.getInstance().excluir(historico.getId());
    }

    public void removerHistoricosPorPaciente(int pacienteId) throws SQLException {
        HistoricoDAO.getInstance().excluirPorPaciente(pacienteId);
    }
}
