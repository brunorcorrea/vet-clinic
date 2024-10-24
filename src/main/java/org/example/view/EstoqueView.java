package org.example.view;

import org.example.controller.EstoqueViewController;
import org.example.model.EstoqueProduto;
import org.example.view.tablemodels.EstoqueTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EstoqueView {
    private final EstoqueViewController viewController = new EstoqueViewController();

    private JPanel mainPanel;
    private JTable estoqueTable;
    private JTextField nomeTextField;
    private JTextField tipoTextField;
    private JTextField precoTextField;
    private JTextField quantidadeTextField;
    private JTextField quantidadeMinimaTextField;
    private JButton adicionarProdutoButton;
    private JButton removerProdutoButton;

    public EstoqueView() {
        configureListeners();
        buscarEstoqueEProduto();
    }

    private void configureListeners() {
        adicionarProdutoButton.addActionListener(this::adicionarProduto);
        removerProdutoButton.addActionListener(this::removerProduto);
    }

    private void adicionarProduto(ActionEvent e) {
        try {
            String nome = nomeTextField.getText().trim();
            String tipo = tipoTextField.getText().trim();
            double preco = Double.parseDouble(precoTextField.getText().trim());
            int quantidade = Integer.parseInt(quantidadeTextField.getText().trim());
            int quantidadeMinima = Integer.parseInt(quantidadeMinimaTextField.getText().trim());

            validateInputs(nome, tipo, preco, quantidade, quantidadeMinima);

            viewController.adicionarProduto(nome, tipo, preco, quantidade, quantidadeMinima);
            buscarEstoqueEProduto();
        } catch (Exception ex) {
            handleException("Erro ao adicionar produto", ex);
        }
    }

    private void removerProduto(ActionEvent e) {
        int[] selectedRows = estoqueTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione pelo menos um produto para remover");
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) produto(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int estoqueId = (Integer) estoqueTable.getValueAt(i, 0);
                    viewController.removerProduto(estoqueId);
                } catch (Exception ex) {
                    handleException("Erro ao remover produto", ex);
                }
            }
            buscarEstoqueEProduto();
        }
    }

    private void buscarEstoqueEProduto() {
        try {
            EstoqueTableModel model = viewController.criarEstoqueTableModel();
            estoqueTable.setModel(model);
        } catch (Exception e) {
            handleException("Erro ao listar estoque ou produtos", e);
        }
    }

    private void validateInputs(String nome, String tipo, double preco, int quantidade, int quantidadeMinima) {
        if (nome.isEmpty() || tipo.isEmpty()) {
            throw new IllegalArgumentException("Nome e tipo são obrigatórios");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual zero");
        }
        if (quantidadeMinima < 0) {
            throw new IllegalArgumentException("Quantidade mínima deve ser maior ou igual zero");
        }
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}