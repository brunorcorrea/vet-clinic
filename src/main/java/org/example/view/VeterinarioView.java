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
    private JTextField textFieldNomeVeterinario;
    private JButton removerVeterinarioButton;

    public VeterinarioView() {
        adicionarVeterinarioButton.addActionListener(e -> {
            String nomeVeterinario = textFieldNomeVeterinario.getText().trim();
            if (nomeVeterinario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome do veterinário não pode ser vazio");
                return;
            }

            Veterinario veterinario = new Veterinario();
            veterinario.setNome(nomeVeterinario);

            try {
                veterinarioController.adicionarVeterinario(veterinario);
                textFieldNomeVeterinario.setText("");
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
            int rowIndex = veterinarioTable.getSelectedRow();
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(null, "Selecione um veterinário para remover");
                return;
            }

            Veterinario veterinario = new Veterinario();
            veterinario.setId((int) veterinarioTable.getValueAt(rowIndex, 0));

            try {
                veterinarioController.removerVeterinario(veterinario);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover veterinário: " + ex.getMessage());
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
