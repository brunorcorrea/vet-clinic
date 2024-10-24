package org.example.controller;

import org.example.model.Proprietario;
import org.example.view.tablemodels.ProprietarioTableModel;

import java.util.List;

public class ProprietarioViewController {
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();

    public void adicionarProprietario(String cpf, String nomeCompleto, String telefone, String endereco) throws Exception {
        Proprietario proprietario = new Proprietario();
        proprietario.setCpf(cpf);
        proprietario.setNomeCompleto(nomeCompleto);
        proprietario.setTelefone(telefone);
        proprietario.setEndereco(endereco);
        proprietarioController.adicionarProprietario(proprietario);
    }

    public void removerProprietario(int proprietarioId) throws Exception {
        Proprietario proprietario = new Proprietario();
        proprietario.setId(proprietarioId);
        proprietarioController.removerProprietario(proprietario);
    }

    public List<Proprietario> listarProprietarios() throws Exception {
        return proprietarioController.listarProprietarios();
    }

    public ProprietarioTableModel criarProprietarioTableModel() throws Exception {
        List<Proprietario> proprietarios = listarProprietarios();
        return new ProprietarioTableModel(proprietarios);
    }
}