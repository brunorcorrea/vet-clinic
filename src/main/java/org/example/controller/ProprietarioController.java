package org.example.controller;

import org.example.dao.ProprietarioDAO;
import org.example.model.Proprietario;

import java.sql.SQLException;
import java.util.List;

public class ProprietarioController {

    private static ProprietarioController instance;

    private ProprietarioController() {
    }

    public static ProprietarioController getInstance() {
        if (instance == null) {
            instance = new ProprietarioController();
        }
        return instance;
    }

    public List<Proprietario> listarProprietarios() throws SQLException {
        return ProprietarioDAO.getInstance().listar();
    }

    public void adicionarProprietario(Proprietario proprietario) throws SQLException {
        ProprietarioDAO.getInstance().cadastrar(proprietario);
    }

    public void editarProprietario(Proprietario proprietario) throws SQLException {
        ProprietarioDAO.getInstance().editar(proprietario);
    }

    public void removerProprietario(Proprietario proprietario) throws SQLException {
        ProprietarioDAO.getInstance().excluir(proprietario.getId());
    }
}
