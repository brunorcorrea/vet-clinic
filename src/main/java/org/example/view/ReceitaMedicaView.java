package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.PacienteController;
import org.example.controller.ReceitaMedicaController;
import org.example.model.*;
import org.example.view.tablemodels.ReceitaMedicaTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedicaView {
    private final ReceitaMedicaController receitaMedicaController = ReceitaMedicaController.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();

    private JPanel mainPanel;
    private JTable receitaMedicaTable;
    private JComboBox pacienteComboBox;
    private JButton adicionarReceitaMedicaButton;
    private JButton removerReceitaMedicaButton;
    private JTextArea medicamentosTextArea;
    private JTextArea observacoesTextArea;
    private DateTimePicker dataEmissaoDateTimePicker;

    private List<Paciente> pacientes = new ArrayList<>();

    public ReceitaMedicaView() {
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));

        adicionarReceitaMedicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Paciente paciente = pacientes.get(pacienteComboBox.getSelectedIndex());
                List<String> medicamentos = List.of(medicamentosTextArea.getText().split("\n"));
                List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
                LocalDateTime dataEmissao = dataEmissaoDateTimePicker.getDateTimePermissive();

                if (paciente == null) {
                    JOptionPane.showMessageDialog(null, "Paciente inválido!");
                    return;
                }

                if (medicamentos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Medicamentos inválidos!");
                    return;
                }

                if (observacoes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Observações inválidas!");
                    return;
                }

                if (dataEmissao == null) {
                    JOptionPane.showMessageDialog(null, "Data e hora inválidas!");
                    return;
                }

                if (dataEmissao.isBefore(LocalDateTime.now())) {
                    int response = JOptionPane.showConfirmDialog(null, "Data e hora estão no passado. Deseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.NO_OPTION) {
                        return;
                    }
                }

                ReceitaMedica receitaMedica = new ReceitaMedica();
                receitaMedica.setPaciente(paciente);
                receitaMedica.setMedicamentos(medicamentos);
                receitaMedica.setObservacoes(observacoes);
                receitaMedica.setDataEmissao(dataEmissao);

                try {
                    receitaMedicaController.adicionarReceitaMedica(receitaMedica);
                    medicamentosTextArea.setText("");
                    observacoesTextArea.setText("");
                    dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
                    JOptionPane.showMessageDialog(null, "Receita médica adicionado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar receita médica: " + ex.getMessage());
                }

                buscarReceitasMedicas();
            }
        });
        removerReceitaMedicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = receitaMedicaTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos um agendamento!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) receita(s) médica(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        ReceitaMedica receitaMedica = new ReceitaMedica();
                        receitaMedica.setId((Integer) receitaMedicaTable.getValueAt(i, 0));

                        try {
                            receitaMedicaController.removerReceitaMedica(receitaMedica);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover receita médica: " + ex.getMessage());
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Receita(s) médica(s) removidas(s) com sucesso!");
                }

                buscarReceitasMedicas();
            }
        });

        buscarReceitasMedicas();
    }

    private void buscarReceitasMedicas() {
        List<ReceitaMedica> receitasMedicas;
        try {
            receitasMedicas = receitaMedicaController.listarReceitasMedica();
        } catch (Exception e) {
            receitasMedicas = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar receitas médicas: " + e.getMessage());
        }

        receitaMedicaTable.setModel(new ReceitaMedicaTableModel(receitasMedicas));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
