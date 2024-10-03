package org.example.view;

import org.example.controller.ProprietarioController;
import org.example.model.Proprietario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private JLabel cpfLabel;
    private JLabel telefoneLabel;
    private JLabel nomeCompletoLabel;
    private JLabel enderecoLabel;

    public ProprietarioView() {
        adicionarProprietarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    ProprietarioController.getInstance().adicionarProprietario(proprietario);
                    cpfTextField.setText("");
                    nomeCompletoTextField.setText("");
                    telefoneTextField.setText("");
                    enderecoTextField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar proprietário: " + ex.getMessage());
                }

                List<Proprietario> proprietarios;
                try {
                    proprietarios = proprietarioController.listarProprietarios();
                } catch (Exception ex) {
                    proprietarios = new ArrayList<>();
                    JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + ex.getMessage());
                }

                proprietarioTable.setModel(new ProprietarioTableModel(proprietarios));
            }
        });
        removerProprietarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = proprietarioTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos um proprietário!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) proprietário(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        Proprietario proprietario = new Proprietario();
                        proprietario.setId((Integer) proprietarioTable.getValueAt(i, 0));

                        try {
                            ProprietarioController.getInstance().removerProprietario(proprietario);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover proprietário: " + ex.getMessage());
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Proprietário(s) removidos(s) com sucesso!");
                }

                List<Proprietario> proprietarios;
                try {
                    proprietarios = proprietarioController.listarProprietarios();
                } catch (Exception ex) {
                    proprietarios = new ArrayList<>();
                    JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + ex.getMessage());
                }

                proprietarioTable.setModel(new ProprietarioTableModel(proprietarios));
            }
        });

        List<Proprietario> proprietarios;
        try {
            proprietarios = proprietarioController.listarProprietarios();
        } catch (Exception e) {
            proprietarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + e.getMessage());
        }

        proprietarioTable.setModel(new ProprietarioTableModel(proprietarios));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
