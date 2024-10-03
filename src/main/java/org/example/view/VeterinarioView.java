package org.example.view;

import org.example.controller.VeterinarioController;
import org.example.model.Veterinario;

import javax.swing.*;
import java.util.ArrayList;
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
        adicionarVeterinarioButton.addActionListener(e -> {
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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar veterinário: " + ex.getMessage());
            }

            List<Veterinario> veterinarios;
            try {
                veterinarios = veterinarioController.listarVeterinarios();
            } catch (Exception ex) {
                veterinarios = new ArrayList<>();
                JOptionPane.showMessageDialog(null, "Erro ao listar veterinários: " + ex.getMessage());
            }

            veterinarioTable.setModel(new VeterinarioTableModel(veterinarios));
        });
        removerVeterinarioButton.addActionListener(e -> {
            int[] rowIndex = veterinarioTable.getSelectedRows();
            if (rowIndex.length == 0) {
                JOptionPane.showMessageDialog(null, "Selecione pelo menos um veterinário para remover");
                return;
            }

            for (int i : rowIndex) {
                Veterinario veterinario = new Veterinario();
                veterinario.setId((Integer) veterinarioTable.getValueAt(i, 0));

                try {
                    veterinarioController.removerVeterinario(veterinario);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao remover veterinário: " + ex.getMessage());
                }
            }

            List<Veterinario> veterinarios;
            try {
                veterinarios = veterinarioController.listarVeterinarios();
            } catch (Exception ex) {
                veterinarios = new ArrayList<>();
                JOptionPane.showMessageDialog(null, "Erro ao listar veterinários: " + ex.getMessage());
            }

            veterinarioTable.setModel(new VeterinarioTableModel(veterinarios));
        });

        List<Veterinario> veterinarios;
        try {
            veterinarios = veterinarioController.listarVeterinarios();
        } catch (Exception e) {
            veterinarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar veterinários: " + e.getMessage());
        }

        veterinarioTable.setModel(new VeterinarioTableModel(veterinarios));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
