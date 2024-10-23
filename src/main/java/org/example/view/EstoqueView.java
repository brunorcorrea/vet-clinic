package org.example.view;

import org.example.controller.EstoqueController;
import org.example.controller.ProdutoController;
import org.example.model.Estoque;
import org.example.model.EstoqueProduto;
import org.example.model.Produto;
import org.example.view.tablemodels.EstoqueTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EstoqueView {
    private final EstoqueController estoqueController = EstoqueController.getInstance();
    private final ProdutoController produtoController = ProdutoController.getInstance();

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

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setTipo(tipo);
            produto.setPreco(preco);

            Estoque estoque = new Estoque();
            estoque.setQuantidade(quantidade);
            estoque.setQuantidadeMinima(quantidadeMinima);
            estoque.setNecessitaReposicao(quantidade < quantidadeMinima);
            estoque.setProduto(produto);

            produtoController.adicionarProduto(produto);
            estoqueController.adicionarEstoque(estoque);

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
                    EstoqueProduto estoqueProduto = new EstoqueProduto();
                    estoqueProduto.setIdEstoque((Integer) estoqueTable.getValueAt(i, 0));

                    estoqueController.listarEstoque().forEach(estoque -> {
                        if (estoque.getId() == estoqueProduto.getIdEstoque()) {
                            try {
                                produtoController.removerProduto(estoque.getProduto());
                                estoqueController.removerEstoque(estoque);
                            } catch (Exception exception) {
                                handleException("Erro ao remover produto ou estoque", exception);
                            }
                        }
                    });
                } catch (Exception ex) {
                    handleException("Erro ao remover produto", ex);
                }
            }
            buscarEstoqueEProduto();
        }
    }

    private void buscarEstoqueEProduto() {
        try {
            List<Estoque> estoque = estoqueController.listarEstoque();
            List<Produto> produtos = produtoController.listarProdutos();

            List<EstoqueProduto> estoqueProdutos = new ArrayList<>();
            for (int indice = 0; indice < estoque.size(); indice++) {
                Estoque estoqueAtual = estoque.get(indice);
                Produto produto = produtos.get(indice);
                String necessitaReposicao = estoqueAtual.isNecessitaReposicao() ? "Sim" : "Não";
                estoqueProdutos.add(new EstoqueProduto(estoqueAtual.getId(), produto.getId(), produto.getNome(), produto.getTipo(), produto.getPreco(), estoqueAtual.getQuantidade(), estoqueAtual.getQuantidadeMinima(), necessitaReposicao));
            }

            estoqueTable.setModel(new EstoqueTableModel(estoqueProdutos));
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