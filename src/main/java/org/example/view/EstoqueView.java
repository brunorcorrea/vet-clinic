package org.example.view;

import org.example.controller.EstoqueController;
import org.example.controller.ProdutoController;
import org.example.model.Estoque;
import org.example.model.EstoqueProduto;
import org.example.model.Produto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        adicionarProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText().trim();
                String tipo = tipoTextField.getText().trim();
                double preco = Double.parseDouble(precoTextField.getText().trim());
                int quantidade = Integer.parseInt(quantidadeTextField.getText().trim());
                int quantidadeMinima = Integer.parseInt(quantidadeMinimaTextField.getText().trim());

                if (nome.isEmpty() || tipo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome e tipo são obrigatórios");
                    return;
                }

                if (preco <= 0) {
                    JOptionPane.showMessageDialog(null, "Preço deve ser maior que zero");
                    return;
                }

                if (quantidade < 0) {
                    JOptionPane.showMessageDialog(null, "Quantidade deve ser maior ou igual zero");
                    return;
                }

                if (quantidadeMinima < 0) {
                    JOptionPane.showMessageDialog(null, "Quantidade mínima deve ser maior ou igual zero");
                    return;
                }

                Produto produto = new Produto();
                produto.setNome(nome);
                produto.setTipo(tipo);
                produto.setPreco(preco);

                Estoque estoque = new Estoque();
                estoque.setQuantidade(quantidade);
                estoque.setQuantidadeMinima(quantidadeMinima);
                estoque.setNecessitaReposicao(quantidade < quantidadeMinima);
                estoque.setProduto(produto);

                try {
                    produtoController.adicionarProduto(produto);
                    estoqueController.adicionarEstoque(estoque);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar produto: " + exception.getMessage());
                }

                buscarEstoqueEProduto();
            }
        });
        removerProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = estoqueTable.getSelectedRows();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione pelo menos um produto para remover");
                    return;
                }
                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) produto(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        EstoqueProduto estoqueProduto = new EstoqueProduto();
                        estoqueProduto.setIdEstoque((Integer) estoqueTable.getValueAt(i, 0));

                        try {
                            estoqueController.listarEstoque().forEach(estoque -> {
                                if (estoque.getId() == estoqueProduto.getIdEstoque()) {
                                    try {
                                        produtoController.removerProduto(estoque.getProduto());
                                    } catch (Exception exception) {
                                        JOptionPane.showMessageDialog(null, "Erro ao remover produto: " + exception.getMessage());
                                    }

                                    try {
                                        estoqueController.removerEstoque(estoque);
                                    } catch (Exception exception) {
                                        JOptionPane.showMessageDialog(null, "Erro ao remover estoque: " + exception.getMessage());
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover veterinário: " + ex.getMessage());
                        }
                    }

                    buscarEstoqueEProduto();
                }
            }
        });

        buscarEstoqueEProduto();
    }

    private void buscarEstoqueEProduto() {
        List<Estoque> estoque;
        try {
            estoque = estoqueController.listarEstoque();
        } catch (Exception e) {
            estoque = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar estoque: " + e.getMessage());
        }

        List<Produto> produtos;
        try {
            produtos = produtoController.listarProdutos();
        } catch (Exception e) {
            produtos = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }

        List<EstoqueProduto> estoqueProdutos = new ArrayList<>();
        for (int indice = 0; indice < estoque.size(); indice++) {
            Estoque estoqueAtual = estoque.get(indice);
            Produto produto = produtos.get(indice);
            String necessitaReposicao = estoqueAtual.isNecessitaReposicao() ? "Sim" : "Não";
            estoqueProdutos.add(new EstoqueProduto(estoqueAtual.getId(), produto.getId(), produto.getNome(), produto.getTipo(), produto.getPreco(), estoqueAtual.getQuantidade(), estoqueAtual.getQuantidadeMinima(), necessitaReposicao));
        }

        estoqueTable.setModel(new EstoqueTableModel(estoqueProdutos));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
