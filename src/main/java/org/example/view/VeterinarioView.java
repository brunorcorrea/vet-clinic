package org.example.view;

import org.example.controller.VeterinarioController;
import org.example.model.Veterinario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioView {
    private final VeterinarioController veterinarioController = new VeterinarioController();
    private JPanel mainPanel;
    private JTable tabelaVeterinario;
    private JButton adicionarVeterinarioButton;

    private DefaultTableModel modeloTabela = new DefaultTableModel();

    private final List<String> colunas = List.of("Id", "Nome", "Quantidade de Agendamentos");

    public VeterinarioView() {
//        adicionarVeterinarioButton.addActionListener(e -> {
//            JFrame adicionarVeterinarioFrame = new JFrame("Vet Clinic - Adicionar Veterinário");
//            adicionarVeterinarioFrame.setContentPane(new AdicionarVeterinarioView().getMainPanel());
//            adicionarVeterinarioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            adicionarVeterinarioFrame.pack();
//            adicionarVeterinarioFrame.setLocationRelativeTo(null);
//            adicionarVeterinarioFrame.setVisible(true);
//        });
        colunas.forEach(coluna -> modeloTabela.addColumn(coluna));
        tabelaVeterinario.setModel(modeloTabela);
        popularTabelaVeterinarios();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void popularTabelaVeterinarios() {
        List<Veterinario> veterinarios = veterinarioController.listarVeterinarios();

        veterinarios.forEach(veterinario -> {
            List<Object> dados = new ArrayList<>();
            dados.add(veterinario.getId());
            dados.add(veterinario.getNome());
            dados.add((long) veterinario.getAgendamentos().size());
            modeloTabela.addRow(dados.toArray());
        });
    }
}
