package org.example.view;

import org.example.controller.PacienteController;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PacienteView {
    private final PacienteController pacienteController = PacienteController.getInstance();
    private JPanel mainPanel;
    private JTable pacienteTable;
    private JLabel nomeLabel;
    private JTextField nomeTextField;
    private JLabel estadoDeCastracaoLabel;
    private JComboBox estadoCastracaoComboBox;
    private JLabel idadeLabel;
    private JTextField idadeTextField;
    private JLabel racaLabel;
    private JTextField racaTextField;
    private JTextField coloracaoTextField;
    private JLabel especieLabel;
    private JTextField especieTextField;
    private JLabel fotoLabel;
    private JButton adicionarPacienteButton;
    private JButton removerPacienteButton;
    private JButton adicionarFotoButton;
    private JLabel imagemLabel;
    private JLabel coloracaoLabel;

    public PacienteView() {
        imagemLabel.setPreferredSize(new Dimension(100, 100));
        estadoCastracaoComboBox.addItem(EstadoCastracao.FERTIL.getDescricao());
        estadoCastracaoComboBox.addItem(EstadoCastracao.CASTRADO.getDescricao());

        adicionarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText().trim();
                String estadoCastracao = estadoCastracaoComboBox.getSelectedItem().toString();
                String raca = racaTextField.getText().trim();
                int idade = idadeTextField.getText().trim().isEmpty() ? -1 : Integer.parseInt(idadeTextField.getText().trim());
                String coloracao = coloracaoTextField.getText().trim();
                String especie = especieTextField.getText().trim();
                byte[] foto = (fotoLabel.getIcon() != null) ? fotoLabel.getIcon().toString().getBytes() : null;

                if (idade < 0) {
                    JOptionPane.showMessageDialog(null, "Idade inválida!");
                    return;
                }

                if (nome.isEmpty() || estadoCastracao.isEmpty() || raca.isEmpty() || coloracao.isEmpty() || especie.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                    return;
                }

                Paciente paciente = new Paciente();
                paciente.setNome(nome);
                paciente.setEstadoCastracao(EstadoCastracao.fromDescricao(estadoCastracao));
                paciente.setIdade(idade);
                paciente.setRaca(raca);
                paciente.setColoracao(coloracao);
                paciente.setEspecie(especie);
                paciente.setFoto(foto);

                try {
                    pacienteController.adicionarPaciente(paciente);
                    nomeTextField.setText("");
                    estadoCastracaoComboBox.setSelectedIndex(0);
                    idadeTextField.setText("");
                    racaTextField.setText("");
                    coloracaoTextField.setText("");
                    especieTextField.setText("");
                    fotoLabel.setIcon(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar paciente: " + ex.getMessage());
                }

                List<Paciente> pacientes;
                try {
                    pacientes = pacienteController.listarPacientes();
                } catch (Exception ex) {
                    pacientes = new ArrayList<>();
                    JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + ex.getMessage());
                }

                pacienteTable.setModel(new PacienteTableModel(pacientes));
            }
        });
        removerPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        adicionarFotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens", "jpeg", "jpg", "png", "gif", "bmp");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image image = imageIcon.getImage();
                    Image resizedImage = image.getScaledInstance(imagemLabel.getWidth(), imagemLabel.getHeight(), Image.SCALE_SMOOTH);
                    imagemLabel.setIcon(new ImageIcon(resizedImage));
                }
            }
        });

        List<Paciente> pacientes;
        try {
            pacientes = pacienteController.listarPacientes();
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar pacientes: " + e.getMessage());
        }

        pacienteTable.setModel(new ProprietarioTableModel(pacientes));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
