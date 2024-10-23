package org.example.view;

import org.example.controller.VeterinarioController;
import org.example.model.Veterinario;
import org.example.view.tablemodels.VeterinarioTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VeterinarioView {
    private final VeterinarioController veterinarioController = VeterinarioController.getInstance();
    private JPanel mainPanel;
    private JTable veterinarioTable;
    private JButton adicionarVeterinarioButton;
    private JTextField nomeVeterinarioTextField;
    private JButton removerVeterinarioButton;
    private JLabel nomeLabel;

    public VeterinarioView() {
        configureListeners();
        loadVeterinarios();
    }

    private void configureListeners() {
        adicionarVeterinarioButton.addActionListener(this::adicionarVeterinario);
        removerVeterinarioButton.addActionListener(this::removerVeterinario);
    }

    private void adicionarVeterinario(ActionEvent e) {
        String nomeVeterinario = nomeVeterinarioTextField.getText().trim();
        if (nomeVeterinario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome do veterinário não pode ser vazio");
            return;
        }

        Veterinario veterinario = new Veterinario();
        veterinario.setNome(nomeVeterinario);

        try {
            veterinarioController.adicionarVeterinario(veterinario);
            nomeVeterinarioTextField.setText("");
            JOptionPane.showMessageDialog(null, "Veterinário adicionado com sucesso!");
        } catch (Exception ex) {
            handleException("Erro ao adicionar veterinário", ex);
        }

        loadVeterinarios();
    }

    private void removerVeterinario(ActionEvent e) {
        int[] selectedRows = veterinarioTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos um veterinário para remover");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) veterinário(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    Veterinario veterinario = new Veterinario();
                    veterinario.setId((Integer) veterinarioTable.getValueAt(i, 0));
                    veterinarioController.removerVeterinario(veterinario);
                } catch (Exception ex) {
                    handleException("Erro ao remover veterinário", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Veterinário(s) removido(s) com sucesso!");
            loadVeterinarios();
        }
    }

    private void loadVeterinarios() {
        try {
            List<Veterinario> veterinarios = veterinarioController.listarVeterinarios();
            veterinarioTable.setModel(new VeterinarioTableModel(veterinarios));
        } catch (Exception e) {
            handleException("Erro ao listar veterinários", e);
        }
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}