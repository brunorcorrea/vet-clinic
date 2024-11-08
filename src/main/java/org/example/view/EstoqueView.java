package org.example.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.EstoqueViewController;
import org.example.view.tablemodels.EstoqueTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

import static org.example.utils.Formatter.createDecimalNumberFormatter;
import static org.example.utils.Formatter.createIntegerNumberFormatter;

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
    private JTextField filtroProdutoNomeTextField;
    private EstoqueTableModel tableModel;

    public EstoqueView() {
        $$$setupUI$$$();
        configureListeners();
        loadEstoqueEProduto(filtroProdutoNomeTextField.getText().trim());
    }

    private void configureListeners() {
        adicionarProdutoButton.addActionListener(this::adicionarProduto);
        removerProdutoButton.addActionListener(this::removerProduto);

        filtroProdutoNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterEstoques();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterEstoques();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterEstoques();
            }

            private void filterEstoques() {
                String text = filtroProdutoNomeTextField.getText().trim();
                loadEstoqueEProduto(text);
            }
        });
    }

    private void adicionarProduto(ActionEvent e) {
        try {
            String nome = nomeTextField.getText().trim();
            String tipo = tipoTextField.getText().trim();

            NumberFormat format = NumberFormat.getInstance();
            double preco = format.parse(precoTextField.getText().trim()).doubleValue();
            int quantidade = format.parse(quantidadeTextField.getText().trim()).intValue();
            int quantidadeMinima = format.parse(quantidadeMinimaTextField.getText().trim()).intValue();

            validateInputs(nome, tipo, preco, quantidade, quantidadeMinima);

            viewController.adicionarProduto(nome, tipo, preco, quantidade, quantidadeMinima);
            clearInputs();
            loadEstoqueEProduto(filtroProdutoNomeTextField.getText().trim());
        } catch (Exception ex) {
            handleException("Erro ao adicionar produto", ex);
        }
    }

    private void removerProduto(ActionEvent e) {
        int[] selectedRows = estoqueTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um produto", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) produto(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int estoqueId = tableModel.getEstoqueProduto(i).getIdEstoque();
                    int produtoId = tableModel.getEstoqueProduto(i).getIdProduto();
                    viewController.removerProduto(estoqueId, produtoId);
                } catch (Exception ex) {
                    handleException("Erro ao remover produto", ex);
                }
            }
            loadEstoqueEProduto(filtroProdutoNomeTextField.getText().trim());
        }
    }

    private void loadEstoqueEProduto(String nomeProduto) {
        try {
            tableModel = viewController.criarEstoqueTableModel(nomeProduto);
            estoqueTable.setModel(tableModel);
        } catch (Exception e) {
            handleException("Erro ao listar produtos.", e);
        }
    }

    private void validateInputs(String nome, String tipo, double preco, int quantidade, int quantidadeMinima) {
        if (nome.isEmpty() || tipo.isEmpty()) {
            throw new IllegalArgumentException("Nome e tipo são obrigatórios");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual zero");
        }
        if (quantidadeMinima < 0) {
            throw new IllegalArgumentException("Quantidade mínima deve ser maior que zero");
        }
    }

    private void clearInputs() {
        nomeTextField.setText("");
        tipoTextField.setText("");
        setDefaultPrecoTextFieldValue();
        setQuantidadeTextFieldDefaultValue();
        setQuantidadeMinimaTextFieldDefaultValue();
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        var decimalNumberFormatter = createDecimalNumberFormatter();
        var integerNumberFormatter = createIntegerNumberFormatter();

        precoTextField = new JFormattedTextField(decimalNumberFormatter);
        setDefaultPrecoTextFieldValue();
        precoTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (precoTextField.getText().isEmpty()) {
                    setDefaultPrecoTextFieldValue();
                }
            }
        });

        quantidadeTextField = new JFormattedTextField(integerNumberFormatter);
        setQuantidadeTextFieldDefaultValue();
        quantidadeTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (quantidadeTextField.getText().isEmpty()) {
                    setQuantidadeTextFieldDefaultValue();
                }
            }
        });

        quantidadeMinimaTextField = new JFormattedTextField(integerNumberFormatter);
        setQuantidadeMinimaTextFieldDefaultValue();
        quantidadeMinimaTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (quantidadeMinimaTextField.getText().isEmpty()) {
                    setQuantidadeMinimaTextFieldDefaultValue();
                }
            }
        });
    }

    private void setQuantidadeMinimaTextFieldDefaultValue() {
        quantidadeMinimaTextField.setText("0");
    }

    private void setQuantidadeTextFieldDefaultValue() {
        quantidadeTextField.setText("0");
    }

    private void setDefaultPrecoTextFieldValue() {
        precoTextField.setText("0.0");
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        estoqueTable = new JTable();
        scrollPane1.setViewportView(estoqueTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nome:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeTextField = new JTextField();
        panel2.add(nomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Tipo:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoTextField = new JTextField();
        panel2.add(tipoTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Preço:");
        panel2.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(precoTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Quantidade:");
        panel2.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(quantidadeTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Quantidade Mínima:");
        panel2.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(quantidadeMinimaTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        adicionarProdutoButton = new JButton();
        adicionarProdutoButton.setText("Adicionar Produto");
        panel2.add(adicionarProdutoButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerProdutoButton = new JButton();
        removerProdutoButton.setText("Remover Produto");
        panel2.add(removerProdutoButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Buscar por produto:");
        panel3.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroProdutoNomeTextField = new JTextField();
        filtroProdutoNomeTextField.setText("");
        panel3.add(filtroProdutoNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}