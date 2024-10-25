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
        super(vDados, new String[]{"Id", "Nome", "Tipo", "Preço", "Quantidade", "Quantidade Mínima", "Necessita Reposição"});
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 4, 5 -> Integer.class;
            case 1, 2, 6 -> String.class;
            case 3 -> Double.class;
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EstoqueProduto estoqueProduto = (EstoqueProduto) vDados.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> estoqueProduto.getIdEstoque();
            case 1 -> estoqueProduto.getNomeProduto();
            case 2 -> estoqueProduto.getTipoProduto();
            case 3 -> estoqueProduto.getPrecoProduto();
            case 4 -> estoqueProduto.getQuantidade();
            case 5 -> estoqueProduto.getQuantidadeMinima();
            case 6 -> estoqueProduto.getNecessitaReposicao();
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
                case 1 -> {
                    produto.setNome((String) aValue);
                    estoqueProduto.setNomeProduto((String) aValue);
                }
                case 2 -> {
                    produto.setTipo((String) aValue);
                    estoqueProduto.setTipoProduto((String) aValue);
                }
                case 3 -> {
                    double preco = Double.parseDouble(aValue.toString());
                    if (preco <= 0) throw new IllegalArgumentException("Preço deve ser maior que zero");
                    produto.setPreco(preco);
                    estoqueProduto.setPrecoProduto(preco);
                }
                case 4 -> {
                    int quantidade = Integer.parseInt(aValue.toString());
                    if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser maior ou igual zero");
                    estoque.setQuantidade(quantidade);
                    estoqueProduto.setQuantidade(quantidade);
                }
                case 5 -> {
                    int quantidadeMinima = Integer.parseInt(aValue.toString());
                    if (quantidadeMinima < 0)
                        throw new IllegalArgumentException("Quantidade mínima deve ser maior que zero");
                    estoque.setQuantidadeMinima(quantidadeMinima);
                    estoqueProduto.setQuantidadeMinima(quantidadeMinima);
                }
                default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
            }

            boolean necessitaReposicao = estoque.getQuantidade() < estoque.getQuantidadeMinima();
            estoque.setNecessitaReposicao(necessitaReposicao);
            estoqueProduto.setNecessitaReposicao(necessitaReposicao ? "Sim" : "Não");
            fireTableCellUpdated(rowIndex, 6);

            produtoController.editarProduto(produto);
            estoqueController.editarEstoque(estoque);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar produto/estoque: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 6);
        return !columnsNotEditable.contains(columnIndex);
    }
}