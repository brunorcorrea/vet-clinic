package org.example.controller;

import org.example.dao.AgendamentoDAO;
import org.example.model.Agendamento;

import java.sql.SQLException;
import java.util.List;

public class AgendamentoController {

    private static AgendamentoController instance;

    private AgendamentoController() {
    }

    public static AgendamentoController getInstance() {
        if (instance == null) {
            instance = new AgendamentoController();
        }
        return instance;
    }

    public List<Agendamento> listarAgendamentos() throws SQLException {
        return AgendamentoDAO.getInstance().listar();
    }

    public void adicionarAgendamento(Agendamento agendamento) throws SQLException {
        AgendamentoDAO.getInstance().cadastrar(agendamento);
    }

    public void editarAgendamento(Agendamento agendamento) throws SQLException {
        AgendamentoDAO.getInstance().editar(agendamento);
    }

    public void removerAgendamento(Agendamento agendamento) throws SQLException {
        AgendamentoDAO.getInstance().excluir(agendamento.getId());
    }
}
