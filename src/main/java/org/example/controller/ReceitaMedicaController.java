package org.example.controller;

import org.example.dao.ReceitaMedicaDAO;
import org.example.model.ReceitaMedica;

import java.sql.SQLException;
import java.util.List;

public class ReceitaMedicaController {

    private static ReceitaMedicaController instance;

    private ReceitaMedicaController() {
    }

    public static ReceitaMedicaController getInstance() {
        if (instance == null) {
            instance = new ReceitaMedicaController();
        }
        return instance;
    }

    public List<ReceitaMedica> listarReceitasMedica() throws SQLException {
        return ReceitaMedicaDAO.getInstance().listar();
    }

    public void adicionarReceitaMedica(ReceitaMedica receitaMedica) throws SQLException {
        ReceitaMedicaDAO.getInstance().cadastrar(receitaMedica);
    }

    public void editarReceitaMedica(ReceitaMedica receitaMedica) throws SQLException {
        ReceitaMedicaDAO.getInstance().editar(receitaMedica);
    }

    public void removerReceitaMedica(ReceitaMedica receitaMedica) throws SQLException {
        ReceitaMedicaDAO.getInstance().excluir(receitaMedica.getId());
    }

    public void removerReceitasPorPaciente(int pacienteId) throws SQLException {
        ReceitaMedicaDAO.getInstance().excluirPorPaciente(pacienteId);
    }
}
