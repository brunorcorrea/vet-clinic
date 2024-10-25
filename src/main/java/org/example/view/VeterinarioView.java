package org.example.view;

import org.example.controller.VeterinarioViewController;
import org.example.view.tablemodels.VeterinarioTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class VeterinarioView {
    private final VeterinarioViewController viewController = new VeterinarioViewController();
    private JPanel mainPanel;
    private JTable veterinarioTable;
    private JButton adicionarVeterinarioButton;
    private JTextField nomeVeterinarioTextField;
    private JButton removerVeterinarioButton;

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

        try {
            viewController.adicionarVeterinario(nomeVeterinario);
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
                    int veterinarioId = (Integer) veterinarioTable.getValueAt(i, 0);
                    viewController.removerVeterinario(veterinarioId);
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
            VeterinarioTableModel model = viewController.criarVeterinarioTableModel();
            veterinarioTable.setModel(model);
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