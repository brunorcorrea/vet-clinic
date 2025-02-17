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
            case 1 -> {
                String nomeCompleto = (String) aValue;

                if (nomeCompleto.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome completo não pode ser vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                proprietario.setNomeCompleto(nomeCompleto);
            }
            case 2 -> {
                String telefone = (String) aValue;

                if (telefone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Telefone não pode ser vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!telefone.matches("\\(\\d{2}\\)\\s\\d{5}-\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "Telefone deve estar no formato (XX) XXXXX-XXXX", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                proprietario.setTelefone(telefone);
            }
            case 3 -> {
                String endereco = (String) aValue;

                if (endereco.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Endereço não pode ser vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                proprietario.setEndereco(endereco);
            }
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

    public boolean hasProprietarioWithCPF(String cpf) {
        for (Object proprietario : vDados) {
            if (((Proprietario) proprietario).getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }
}