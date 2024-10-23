package org.example.view;

import org.example.controller.ProprietarioController;
import org.example.model.Proprietario;
import org.example.view.tablemodels.ProprietarioTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProprietarioView {
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();
    private JPanel mainPanel;
    private JTable proprietarioTable;
    private JButton adicionarProprietarioButton;
    private JButton removerProprietarioButton;
    private JTextField cpfTextField;
    private JTextField nomeCompletoTextField;
    private JTextField telefoneTextField;
    private JTextField enderecoTextField;

    public ProprietarioView() {
        configureListeners();
        loadProprietarios();
    }

    private void configureListeners() {
        adicionarProprietarioButton.addActionListener(this::adicionarProprietario);
        removerProprietarioButton.addActionListener(this::removerProprietario);
    }

    private void adicionarProprietario(ActionEvent e) {
        String cpf = cpfTextField.getText().trim();
        String nomeCompleto = nomeCompletoTextField.getText().trim();
        String telefone = telefoneTextField.getText().trim();
        String endereco = enderecoTextField.getText().trim();

        if (cpf.isEmpty() || nomeCompleto.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            return;
        }

        Proprietario proprietario = new Proprietario();
        proprietario.setCpf(cpf);
        proprietario.setNomeCompleto(nomeCompleto);
        proprietario.setTelefone(telefone);
        proprietario.setEndereco(endereco);

        try {
            proprietarioController.adicionarProprietario(proprietario);
            clearInputs();
            loadProprietarios();
        } catch (Exception ex) {
            handleException("Erro ao cadastrar proprietário", ex);
        }
    }

    private void removerProprietario(ActionEvent e) {
        int[] selectedRows = proprietarioTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um proprietário!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) proprietário(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    Proprietario proprietario = new Proprietario();
                    proprietario.setId((Integer) proprietarioTable.getValueAt(i, 0));
                    proprietarioController.removerProprietario(proprietario);
                } catch (Exception ex) {
                    handleException("Erro ao remover proprietário", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Proprietário(s) removidos(s) com sucesso!");
            loadProprietarios();
        }
    }

    private void loadProprietarios() {
        try {
            List<Proprietario> proprietarios = proprietarioController.listarProprietarios();
            proprietarioTable.setModel(new ProprietarioTableModel(proprietarios));
        } catch (Exception e) {
            handleException("Erro ao listar proprietários", e);
        }
    }

    private void clearInputs() {
        cpfTextField.setText("");
        nomeCompletoTextField.setText("");
        telefoneTextField.setText("");
        enderecoTextField.setText("");
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}