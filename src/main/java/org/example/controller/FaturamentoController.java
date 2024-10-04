package org.example.controller;

import org.example.dao.VeterinarioDAO;
import org.example.model.Veterinario;

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

    public List<Veterinario> listarVeterinarios() throws SQLException {
        return VeterinarioDAO.getInstance().listar();
    }

    public void adicionarVeterinario(Veterinario veterinario) throws SQLException {
        VeterinarioDAO.getInstance().cadastrar(veterinario);
    }

    public void editarVeterinario(Veterinario veterinario) throws SQLException {
        VeterinarioDAO.getInstance().editar(veterinario);
    }

    public void removerVeterinario(Veterinario veterinario) throws SQLException {
        VeterinarioDAO.getInstance().excluir(veterinario.getId());
    }
}
