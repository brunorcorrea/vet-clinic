package org.example.controller;

import org.example.dao.VeterinarioDAO;
import org.example.model.Veterinario;

import java.sql.SQLException;
import java.util.List;

public class VeterinarioController {

    private static VeterinarioController instance;

    private VeterinarioController() {
    }

    public static VeterinarioController getInstance() {
        if (instance == null) {
            instance = new VeterinarioController();
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
