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

    public ProprietarioTableModel criarProprietarioTableModel(String nome) throws Exception {
        List<Proprietario> proprietarios = listarProprietarios();
        if (nome != null && !nome.isEmpty()) {
            filtrarProprietarios(proprietarios, nome);
        }

        return new ProprietarioTableModel(proprietarios);
    }

    private void filtrarProprietarios(List<Proprietario> proprietarios, String nome) {
        proprietarios.removeIf(proprietario -> shouldRemoveProprietario(nome, proprietario));
    }

    private boolean shouldRemoveProprietario(String nome, Proprietario proprietario) {
        return proprietario == null || proprietario.getNomeCompleto() == null ||
                !proprietario.getNomeCompleto().toLowerCase().contains(nome.toLowerCase());
    }
}