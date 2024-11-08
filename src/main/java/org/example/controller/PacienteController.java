package org.example.controller;

import org.example.dao.PacienteDAO;
import org.example.model.Paciente;

import java.sql.SQLException;
import java.util.List;

public class PacienteController {

    private static PacienteController instance;

    private PacienteController() {
    }

    public static PacienteController getInstance() {
        if (instance == null) {
            instance = new PacienteController();
        }
        return instance;
    }

    public List<Paciente> listarPacientes() throws SQLException {
        return PacienteDAO.getInstance().listar();
    }

    public void adicionarPaciente(Paciente paciente) throws SQLException {
        PacienteDAO.getInstance().cadastrar(paciente);
    }

    public void editarPaciente(Paciente paciente) throws SQLException {
        PacienteDAO.getInstance().editar(paciente);
    }

    public void removerPaciente(Paciente paciente) throws SQLException {
        PacienteDAO.getInstance().excluir(paciente.getId());
    }

    public List<Integer> listarPacientesIdsPorProprietario(int proprietarioId) throws SQLException {
        return PacienteDAO.getInstance().listarIdsPorProprietario(proprietarioId);
    }
}
