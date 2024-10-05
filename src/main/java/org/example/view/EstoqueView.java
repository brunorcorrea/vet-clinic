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

            }
        });
        removerProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

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
