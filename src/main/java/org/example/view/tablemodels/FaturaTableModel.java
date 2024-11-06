package org.example.view.tablemodels;

import org.example.controller.FaturamentoController;
import org.example.model.Faturamento;
import org.example.model.StatusPagamento;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class FaturaTableModel extends GenericTableModel {

    FaturamentoController faturamentoController = FaturamentoController.getInstance();

    public FaturaTableModel(List vDados) {
        super(vDados, new String[]{"Proprietário", "Valor Total", "Status", "Data de Vencimento"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2 -> String.class;
            case 1 -> Double.class;
            case 3 -> LocalDateTime.class;
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
            case 3 -> faturamento.getDataVencimento();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Faturamento faturamento = (Faturamento) vDados.get(rowIndex);

        if (columnIndex == 2) {
            faturamento.setStatus(StatusPagamento.fromDescricao((String) aValue));
        } else {
            throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            faturamentoController.editarFaturamento(faturamento);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar faturamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 1, 3);
        return !columnsNotEditable.contains(columnIndex);
    }
}