package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.HistoricoController;
import org.example.controller.PacienteController;
import org.example.model.Historico;
import org.example.model.Paciente;
import org.example.view.tablemodels.HistoricoTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoView {
    private final HistoricoController historicoController = HistoricoController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    private JPanel mainPanel;
    private JTable historicoTable;
    private JComboBox pacienteComboBox;
    private JTextArea vacinasTextArea;
    private JTextArea doencasTextArea;
    private JTextField pesoTextField;
    private JButton adicionarAoHistoricoButton;
    private JButton removerDoHistoricoButton;
    private DateTimePicker dataHoraDateTimePicker;
    private JTextArea observacoesTextArea;

    private List<Paciente> pacientes = new ArrayList<>();

    public HistoricoView() {
        dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar históricos: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        adicionarAoHistoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
                List<String> vacinas = List.of(vacinasTextArea.getText().split("\n"));
                List<String> doencas = List.of(doencasTextArea.getText().split("\n"));
                String peso = pesoTextField.getText() != null ? pesoTextField.getText().trim() : "";
                List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
                LocalDateTime dataHora = dataHoraDateTimePicker.getDateTimePermissive();

                if (paciente == null) {
                    JOptionPane.showMessageDialog(null, "Paciente inválido!");
                    return;
                }

                if (vacinas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vacinas inválidos!");
                    return;
                }

                if (doencas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Doenças inválidas!");
                    return;
                }

                if (peso.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Peso inválido!");
                    return;
                }

                if  (observacoes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Observações inválidas!");
                    return;
                }

                if (dataHora == null) {
                    JOptionPane.showMessageDialog(null, "Data e hora inválidas!");
                    return;
                }

                Historico historico = new Historico();
                historico.setPaciente(paciente);
                historico.setVacinas(vacinas);
                historico.setDoencas(doencas);
                historico.setPeso(peso);
                historico.setDataHora(dataHora);
                historico.setObservacoes(observacoes);

                try {
                    historicoController.adicionarHistorico(historico);
                    vacinasTextArea.setText("");
                    doencasTextArea.setText("");
                    pesoTextField.setText("");
                    observacoesTextArea.setText("");
                    dataHoraDateTimePicker.setDateTimePermissive(LocalDateTime.now());
                    JOptionPane.showMessageDialog(null, "Histórico adicionado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar histórico: " + ex.getMessage());
                }

                buscarHistoricos();
            }
        });
        removerDoHistoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = historicoTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos um histórico!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) histórico(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        Historico historico = new Historico();
                        historico.setId((Integer) historicoTable.getValueAt(i, 0));

                        try {
                            historicoController.removerHistorico(historico);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover histórico: " + ex.getMessage());
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Histórico(s) removidos(s) com sucesso!");
                }

                buscarHistoricos();
            }
        });

        buscarHistoricos();
    }

    private void buscarHistoricos() {
        List<Historico> historicos;
        try {
            historicos = historicoController.listarHistoricos();
        } catch (Exception e) {
            historicos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar históricos: " + e.getMessage());
        }

        historicoTable.setModel(new HistoricoTableModel(historicos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
