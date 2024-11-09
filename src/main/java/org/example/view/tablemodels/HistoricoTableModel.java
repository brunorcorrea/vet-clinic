package org.example.view.tablemodels;

import org.example.controller.HistoricoController;
import org.example.model.Historico;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoricoTableModel extends GenericTableModel {

    HistoricoController historicoController = HistoricoController.getInstance();

    public HistoricoTableModel(List vDados) {
        super(vDados, new String[]{"Paciente", "Peso", "Vacinas", "Doenças", "Observações", "Data e Hora"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2, 3, 4 -> String.class;
            case 5 -> LocalDateTime.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    public Historico getHistorico(int rowIndex) {
        return (Historico) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Historico historico = (Historico) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> historico.getPaciente().getNome();
            case 1 -> historico.getPeso();
            case 2 -> String.join(", ", historico.getVacinas());
            case 3 -> String.join(", ", historico.getDoencas());
            case 4 -> String.join(", ", historico.getObservacoes());
            case 5 -> historico.getDataHora();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Historico historico = (Historico) vDados.get(rowIndex);

        switch (columnIndex) {
            case 1 -> {
                try {
                    String peso = (String) aValue;

                    if (peso.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Peso inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    historico.setPeso(peso);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Peso inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            case 2 -> historico.setVacinas(List.of(((String) aValue).split(", ")));
            case 3 -> historico.setDoencas(List.of(((String) aValue).split(", ")));
            case 4 -> historico.setObservacoes(List.of(((String) aValue).split(", ")));
            case 5 -> historico.setDataHora((LocalDateTime) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            historicoController.editarHistorico(historico);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar histórico: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 5);
        return !columnsNotEditable.contains(columnIndex);
    }
}