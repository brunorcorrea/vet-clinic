package org.example.view.tablemodels;

import org.example.controller.FaturamentoController;
import org.example.model.Faturamento;
import org.example.model.StatusPagamento;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FaturaTableModel extends GenericTableModel {

    FaturamentoController faturamentoController = FaturamentoController.getInstance();

    public FaturaTableModel(List vDados) {
        super(vDados, new String[]{"Proprietário", "Valor Total", "Status", "Data de Vencimento"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2, 3 -> String.class;
            case 1 -> Double.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    public Faturamento getFaturamento(int rowIndex) {
        return (Faturamento) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Faturamento faturamento = (Faturamento) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> faturamento.getProprietario().getNomeCompleto();
            case 1 -> faturamento.getValorTotal();
            case 2 -> faturamento.getStatus().getDescricao();
            case 3 -> faturamento.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Faturamento faturamento = (Faturamento) vDados.get(rowIndex);

        switch (columnIndex) {
            case 1 -> {
                try {
                    double valor = Double.parseDouble(aValue.toString());

                    if (valor < 0) {
                        JOptionPane.showMessageDialog(null, "Valor inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    faturamento.setValorTotal(valor);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Valor inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            case 2 -> {
                String status = ((String) aValue).trim();
                try {
                    faturamento.setStatus(StatusPagamento.fromDescricao(status));
                } catch (IllegalArgumentException e) {
                    String message = "Status inválido: " + status + ". Os status válidos são: " + StatusPagamento.EM_ATRASO.getDescricao() + ", " + StatusPagamento.PAGO.getDescricao() + " ou " + StatusPagamento.PENDENTE.getDescricao() + ".";
                    JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            case 3 -> {
                String dataHoraStr = ((String) aValue).trim();
                try {
                    LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    faturamento.setDataVencimento(dataHora);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Data e Hora inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            faturamentoController.editarFaturamento(faturamento);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar faturamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0);
        return !columnsNotEditable.contains(columnIndex);
    }
}