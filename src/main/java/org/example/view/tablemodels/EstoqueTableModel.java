package org.example.view.tablemodels;

import org.example.controller.EstoqueController;
import org.example.controller.ProdutoController;
import org.example.model.Estoque;
import org.example.model.EstoqueProduto;
import org.example.model.Produto;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class EstoqueTableModel extends GenericTableModel {

    EstoqueController estoqueController = EstoqueController.getInstance();
    ProdutoController produtoController = ProdutoController.getInstance();

    public EstoqueTableModel(List vDados) {
        super(vDados, new String[]{"Nome", "Tipo", "Preço", "Quantidade", "Quantidade Mínima", "Necessita Reposição"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 5 -> String.class;
            case 2 -> Double.class;
            case 3, 4 -> Integer.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    public EstoqueProduto getEstoqueProduto(int rowIndex) {
        return (EstoqueProduto) vDados.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EstoqueProduto estoqueProduto = (EstoqueProduto) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> estoqueProduto.getNomeProduto();
            case 1 -> estoqueProduto.getTipoProduto();
            case 2 -> estoqueProduto.getPrecoProduto();
            case 3 -> estoqueProduto.getQuantidade();
            case 4 -> estoqueProduto.getQuantidadeMinima();
            case 5 -> estoqueProduto.getNecessitaReposicao();
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        EstoqueProduto estoqueProduto = (EstoqueProduto) vDados.get(rowIndex);
        Produto produto = new Produto();
        produto.setId(estoqueProduto.getIdProduto());
        produto.setNome(estoqueProduto.getNomeProduto());
        produto.setTipo(estoqueProduto.getTipoProduto());
        produto.setPreco(estoqueProduto.getPrecoProduto());

        Estoque estoque = new Estoque();
        estoque.setId(estoqueProduto.getIdEstoque());
        estoque.setProduto(produto);
        estoque.setQuantidade(estoqueProduto.getQuantidade());
        estoque.setQuantidadeMinima(estoqueProduto.getQuantidadeMinima());

        try {
            switch (columnIndex) {
                case 0 -> {
                    String nome = (String) aValue;

                    if (nome.isBlank()) throw new IllegalArgumentException("Nome não pode ser vazio");

                    produto.setNome(nome);
                    estoqueProduto.setNomeProduto(nome);
                }
                case 1 -> {
                    String tipo = (String) aValue;

                    if (tipo.isBlank()) throw new IllegalArgumentException("Tipo não pode ser vazio");

                    produto.setTipo(tipo);
                    estoqueProduto.setTipoProduto(tipo);
                }
                case 2 -> {
                    double preco = Double.parseDouble(aValue.toString());
                    if (preco <= 0) throw new IllegalArgumentException("Preço deve ser maior que zero");
                    produto.setPreco(preco);
                    estoqueProduto.setPrecoProduto(preco);
                }
                case 3 -> {
                    int quantidade = Integer.parseInt(aValue.toString());
                    if (quantidade < 0) throw new IllegalArgumentException("Quantidade deve ser maior ou igual zero");
                    estoque.setQuantidade(quantidade);
                    estoqueProduto.setQuantidade(quantidade);
                }
                case 4 -> {
                    int quantidadeMinima = Integer.parseInt(aValue.toString());
                    if (quantidadeMinima < 0)
                        throw new IllegalArgumentException("Quantidade mínima deve ser maior ou igual zero");
                    estoque.setQuantidadeMinima(quantidadeMinima);
                    estoqueProduto.setQuantidadeMinima(quantidadeMinima);
                }
                default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }

            boolean necessitaReposicao = estoque.getQuantidade() < estoque.getQuantidadeMinima();
            estoque.setNecessitaReposicao(necessitaReposicao);
            estoqueProduto.setNecessitaReposicao(necessitaReposicao ? "Sim" : "Não");
            fireTableCellUpdated(rowIndex, 5);

            produtoController.editarProduto(produto);
            estoqueController.editarEstoque(estoque);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar produto/estoque: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(5);
        return !columnsNotEditable.contains(columnIndex);
    }
}