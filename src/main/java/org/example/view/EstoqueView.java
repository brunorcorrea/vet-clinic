package org.example.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EstoqueView {
    private JPanel mainPanel;
    private JTable table1;
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
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
