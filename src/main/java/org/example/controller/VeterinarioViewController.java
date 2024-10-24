package org.example.controller;

import org.example.model.Veterinario;
import org.example.view.tablemodels.VeterinarioTableModel;

import java.util.List;

public class VeterinarioViewController {
    private final VeterinarioController veterinarioController = VeterinarioController.getInstance();

    public void adicionarVeterinario(String nomeVeterinario) throws Exception {
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(nomeVeterinario);
        veterinarioController.adicionarVeterinario(veterinario);
    }

    public void removerVeterinario(int veterinarioId) throws Exception {
        Veterinario veterinario = new Veterinario();
        veterinario.setId(veterinarioId);
        veterinarioController.removerVeterinario(veterinario);
    }

    public List<Veterinario> listarVeterinarios() throws Exception {
        return veterinarioController.listarVeterinarios();
    }

    public VeterinarioTableModel criarVeterinarioTableModel() throws Exception {
        List<Veterinario> veterinarios = listarVeterinarios();
        return new VeterinarioTableModel(veterinarios);
    }
}