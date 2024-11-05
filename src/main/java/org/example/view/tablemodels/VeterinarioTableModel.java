package org.example.view.tablemodels;

import org.example.controller.VeterinarioController;
import org.example.model.Veterinario;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class VeterinarioTableModel extends GenericTableModel {

    VeterinarioController veterinarioController = VeterinarioController.getInstance();

    public VeterinarioTableModel(List vDados) {
        super(vDados, new String[]{"Nome", "Quantidade de Agendamentos"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Integer.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Veterinario veterinario = (Veterinario) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> veterinario.getNome();
            case 1 -> veterinario.getAgendamentos().size();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Veterinario veterinario = (Veterinario) vDados.get(rowIndex);

        if (columnIndex == 0) {
            veterinario.setNome((String) aValue);
        } else {
            throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            veterinarioController.editarVeterinario(veterinario);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar veterinário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 1;
    }
}