package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.example.controller.FaturamentoController;
import org.example.controller.ProprietarioController;
import org.example.model.Faturamento;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;
import org.example.view.tablemodels.FaturaTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FaturaView {
    private final FaturamentoController faturamentoController = FaturamentoController.getInstance();
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();

    private JPanel mainPanel;
    private JTable faturaTable;
    private JComboBox proprietarioComboBox;
    private JTextField valorTotalTextField;
    private JComboBox statusComboBox;
    private JButton adicionarFaturaButton;
    private JButton removerFaturaButton;
    private DateTimePicker dataVencimentoDateTimePicker;

    private List<Proprietario> proprietarios = new ArrayList<>();

    public FaturaView() {
        dataVencimentoDateTimePicker.setDateTimePermissive(LocalDateTime.now());

        statusComboBox.addItem(StatusPagamento.EM_ATRASO.getDescricao());
        statusComboBox.addItem(StatusPagamento.PENDENTE.getDescricao());
        statusComboBox.addItem(StatusPagamento.PAGO.getDescricao());

        try {
            proprietarios = proprietarioController.listarProprietarios();
        } catch (Exception e) {
            proprietarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + e.getMessage());
        }

        proprietarios.forEach(proprietario -> proprietarioComboBox.addItem(proprietario.getNomeCompleto()));

        adicionarFaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Proprietario proprietario = proprietarios.get(proprietarioComboBox.getSelectedIndex());
                StatusPagamento status = StatusPagamento.fromDescricao((String) statusComboBox.getSelectedItem());
                LocalDateTime dataVencimento = dataVencimentoDateTimePicker.getDateTimePermissive();
                double valorTotal = valorTotalTextField.getText() != null ? Double.parseDouble(valorTotalTextField.getText()) : 0;

                if(proprietario == null) {
                    JOptionPane.showMessageDialog(null, "Proprietário inválido!");
                    return;
                }

                if(valorTotal <= 0) {
                    JOptionPane.showMessageDialog(null, "Valor total inválido!");
                    return;
                }

                if(status == null) {
                    JOptionPane.showMessageDialog(null, "Status inválido!");
                    return;
                }

                if(dataVencimento == null) {
                    JOptionPane.showMessageDialog(null, "Data de vencimento inválida!");
                    return;
                }

                Faturamento faturamento = new Faturamento();
                faturamento.setProprietario(proprietario);
                faturamento.setValorTotal(valorTotal);
                faturamento.setStatus(status);
                faturamento.setDataVencimento(dataVencimento);

                try {
                    faturamentoController.adicionarFaturamento(faturamento);
                    JOptionPane.showMessageDialog(null, "Fatura adicionado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar fatura: " + ex.getMessage());
                }

                buscarFaturamentos();
            }
        });
        removerFaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = faturaTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos uma fatura!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) fatura(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        Faturamento faturamento = new Faturamento();
                        faturamento.setId((Integer) faturaTable.getValueAt(i, 0));

                        try {
                            faturamentoController.removerFaturamento(faturamento);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover fatura: " + ex.getMessage());
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Fatura(s) removidas(s) com sucesso!");
                }

                buscarFaturamentos();
            }
        });

        buscarFaturamentos();
    }

    private void buscarFaturamentos() {
        List<Faturamento> faturamentos;
        try {
            faturamentos = faturamentoController.listarFaturamentos();
        } catch (Exception e) {
            faturamentos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar faturamentos: " + e.getMessage());
        }

        faturaTable.setModel(new FaturaTableModel(faturamentos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
