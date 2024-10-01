package org.example.dao;

import org.example.model.Estoque;
import org.example.model.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO extends DAO {
    private static EstoqueDAO instance;

    EstoqueDAO() {
        getConnection();
        createTable();
    }

    public static EstoqueDAO getInstance() {
        return (instance == null ? (instance = new EstoqueDAO()) : instance);
    }

    public void cadastrar(Estoque estoque) throws SQLException {
        String sql = "INSERT INTO estoque (idProduto, quantidade, quantidadeMinima, necessitaReposicao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, estoque.getProduto().getId());
            stmt.setInt(2, estoque.getQuantidade());
            stmt.setInt(3, estoque.getQuantidadeMinima());
            stmt.setInt(4, estoque.isNecessitaReposicao() ? 1 : 0);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM estoque WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editar(Estoque estoque) throws SQLException {
        String sql = "UPDATE estoque SET idProduto = ?, quantidade = ?, quantidadeMinima = ?, necessitaReposicao = ? WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, estoque.getProduto().getId());
            stmt.setInt(2, estoque.getQuantidade());
            stmt.setInt(3, estoque.getQuantidadeMinima());
            stmt.setInt(4, estoque.isNecessitaReposicao() ? 1 : 0);
            stmt.setInt(5, estoque.getId());
            stmt.executeUpdate();
        }
    }

    public List<Estoque> listar() throws SQLException {
        List<Estoque> estoques = new ArrayList<>();
        String sql = "SELECT * FROM estoque";
        try (Statement stmt = DAO.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Estoque estoque = new Estoque();
                estoque.setId(rs.getInt("id"));
                estoque.setProduto(buscarProdutoPorId(rs.getInt("idProduto")));
                estoque.setQuantidade(rs.getInt("quantidade"));
                estoque.setQuantidadeMinima(rs.getInt("quantidadeMinima"));
                estoque.setNecessitaReposicao(rs.getInt("necessitaReposicao") == 1);
                estoques.add(estoque);
            }
        }
        return estoques;
    }

    private Produto buscarProdutoPorId(int id) {
        Produto produto = new Produto();
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement stmt = DAO.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setTipo(rs.getString("tipo"));
                    produto.setPreco(rs.getDouble("preco"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }
}
