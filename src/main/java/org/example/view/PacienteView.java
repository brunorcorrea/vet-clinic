package org.example.view;

import org.example.controller.PacienteController;
import org.example.controller.ProprietarioController;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;
import org.example.model.Proprietario;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacienteView {
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();
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
    private JComboBox proprietarioComboBox;

    private byte[] uploadedImageBytes;
    private List<Proprietario> proprietarios = new ArrayList<>();

    private byte[] imageToByteArray(Image image) {
        try {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PacienteView() {
        imagemLabel.setPreferredSize(new Dimension(100, 100));
        pacienteTable.setRowHeight(100);
        estadoCastracaoComboBox.addItem(EstadoCastracao.FERTIL.getDescricao());
        estadoCastracaoComboBox.addItem(EstadoCastracao.CASTRADO.getDescricao());

        try {
            proprietarios = proprietarioController.listarProprietarios();
        } catch (Exception e) {
            proprietarios = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Erro ao listar proprietários: " + e.getMessage());
        }

        proprietarios.forEach(proprietario -> proprietarioComboBox.addItem(proprietario.getNomeCompleto()));

        adicionarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText().trim();
                String estadoCastracao = estadoCastracaoComboBox.getSelectedItem().toString();
                String raca = racaTextField.getText().trim();
                int idade = idadeTextField.getText().trim().isEmpty() ? -1 : Integer.parseInt(idadeTextField.getText().trim());
                String coloracao = coloracaoTextField.getText().trim();
                String especie = especieTextField.getText().trim();
                byte[] foto = (uploadedImageBytes != null) ? uploadedImageBytes : null;
                String nomeProprietario = (String) proprietarioComboBox.getSelectedItem();
                Proprietario proprietario = proprietarios.stream().filter(p -> p.getNomeCompleto().equals(nomeProprietario)).findFirst()
                        .orElse(null);

                if (proprietario == null) {
                    JOptionPane.showMessageDialog(null, "Proprietário inválido!");
                    return;
                }

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
                paciente.setProprietario(proprietario);

                try {
                    pacienteController.adicionarPaciente(paciente);
                    nomeTextField.setText("");
                    estadoCastracaoComboBox.setSelectedIndex(0);
                    idadeTextField.setText("");
                    racaTextField.setText("");
                    coloracaoTextField.setText("");
                    especieTextField.setText("");
                    uploadedImageBytes = null;
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
                int[] selectedRows = pacienteTable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione ao menos um paciente!");
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) paciente(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i : selectedRows) {
                        Paciente paciente = new Paciente();
                        paciente.setId((Integer) pacienteTable.getValueAt(i, 0));

                        try {
                            PacienteController.getInstance().removerPaciente(paciente);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao remover paciente: " + ex.getMessage());
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Paciente(s) removidos(s) com sucesso!");
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
                    uploadedImageBytes = imageToByteArray(resizedImage);
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

        pacienteTable.setModel(new PacienteTableModel(pacientes));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
