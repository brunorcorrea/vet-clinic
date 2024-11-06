package org.example.view.tablemodels;

import org.example.controller.ProprietarioController;
import org.example.model.Proprietario;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class ProprietarioTableModel extends GenericTableModel {

    ProprietarioController proprietarioController = ProprietarioController.getInstance();

    public ProprietarioTableModel(List vDados) {
        super(vDados, new String[]{"CPF", "Nome Completo", "Telefone", "Endereço"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2, 3 -> String.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }


    public Proprietario getProprietario(int rowIndex) {
        return (Proprietario) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Proprietario proprietario = (Proprietario) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> proprietario.getCpf();
            case 1 -> proprietario.getNomeCompleto();
            case 2 -> proprietario.getTelefone();
            case 3 -> proprietario.getEndereco();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Proprietario proprietario = (Proprietario) vDados.get(rowIndex);

        switch (columnIndex) {
            case 1 -> proprietario.setNomeCompleto((String) aValue);
            case 2 -> proprietario.setTelefone((String) aValue);
            case 3 -> proprietario.setEndereco((String) aValue);
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            proprietarioController.editarProprietario(proprietario);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar proprietário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
}