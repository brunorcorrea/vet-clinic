package org.example.view;

import org.example.controller.ReceitaMedicaController;
import org.example.model.ReceitaMedica;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReceitaMedicaTableModel extends GenericTableModel {

    ReceitaMedicaController receitaMedicaController = ReceitaMedicaController.getInstance();

    public ReceitaMedicaTableModel(List vDados) {
        super(vDados, new String[]{"Id", "Paciente", "Medicamentos", "Observações", "Data de Emissão"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1, 2, 3 -> String.class;
            case 4 -> LocalDateTime.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReceitaMedica receitaMedica = (ReceitaMedica) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> receitaMedica.getId();
            case 1 -> receitaMedica.getPaciente().getNome();
            case 2 -> String.join(", ", receitaMedica.getMedicamentos());
            case 3 -> String.join(", ", receitaMedica.getObservacoes());
            case 4 -> receitaMedica.getDataEmissao();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ReceitaMedica receitaMedica = (ReceitaMedica) vDados.get(rowIndex);

        switch (columnIndex) {
            case 2 -> receitaMedica.setMedicamentos(List.of(((String) aValue).split(", ")));
            case 3 -> receitaMedica.setObservacoes(List.of(((String) aValue).split(", ")));
            case 4 -> receitaMedica.setDataEmissao((LocalDateTime) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            receitaMedicaController.editarReceitaMedica(receitaMedica);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar Receita Médica: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 1);
        return !columnsNotEditable.contains(columnIndex);
    }
}
