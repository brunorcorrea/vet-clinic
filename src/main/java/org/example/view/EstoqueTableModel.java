package org.example.view;

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

        Estoque estoque = new Estoque();
        estoque.setId(estoqueProduto.getIdEstoque());

        switch (columnIndex) {
            case 1 -> produto.setNome((String) aValue);
            case 2 -> produto.setTipo((String) aValue);
            case 3 -> produto.setPreco((Double) aValue);
            case 4 -> estoque.setQuantidade((Integer) aValue);
            case 5 -> estoque.setQuantidadeMinima((Integer) aValue);
            case 6 -> estoque.setNecessitaReposicao(estoque.getQuantidade() < estoque.getQuantidadeMinima());
            default -> throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }

        try {
            produtoController.editarProduto(produto);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar produto: " + e.getMessage());
        }

        try {
            estoqueController.editarEstoque(estoque);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar estoque: " + e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        List<Integer> columnsNotEditable = List.of(0, 6);
        return !columnsNotEditable.contains(columnIndex);
    }
}
