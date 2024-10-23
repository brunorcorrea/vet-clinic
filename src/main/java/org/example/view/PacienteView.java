package org.example.view;

import org.example.controller.PacienteController;
import org.example.controller.ProprietarioController;
import org.example.model.EstadoCastracao;
import org.example.model.Paciente;
import org.example.model.Proprietario;
import org.example.view.tablemodels.PacienteTableModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private JTextField nomeTextField;
    private JComboBox<String> estadoCastracaoComboBox;
    private JTextField idadeTextField;
    private JTextField racaTextField;
    private JTextField coloracaoTextField;
    private JTextField especieTextField;
    private JButton adicionarPacienteButton;
    private JButton removerPacienteButton;
    private JButton adicionarFotoButton;
    private JLabel imagemLabel;
    private JComboBox<String> proprietarioComboBox;

    private byte[] uploadedImageBytes;
    private List<Proprietario> proprietarios = new ArrayList<>();

    public PacienteView() {
        initializeComponents();
        configureListeners();
        loadPacientes();
    }

    private void initializeComponents() {
        imagemLabel.setPreferredSize(new Dimension(100, 100));
        pacienteTable.setRowHeight(100);
        estadoCastracaoComboBox.addItem(EstadoCastracao.FERTIL.getDescricao());
        estadoCastracaoComboBox.addItem(EstadoCastracao.CASTRADO.getDescricao());

        try {
            proprietarios = proprietarioController.listarProprietarios();
            proprietarios.forEach(proprietario -> proprietarioComboBox.addItem(proprietario.getNomeCompleto()));
        } catch (Exception e) {
            handleException("Erro ao listar proprietários", e);
        }
    }

    private void configureListeners() {
        adicionarPacienteButton.addActionListener(this::adicionarPaciente);
        removerPacienteButton.addActionListener(this::removerPaciente);
        adicionarFotoButton.addActionListener(this::adicionarFoto);
    }

    private void adicionarPaciente(ActionEvent e) {
        try {
            String nome = nomeTextField.getText().trim();
            String estadoCastracao = estadoCastracaoComboBox.getSelectedItem().toString();
            String raca = racaTextField.getText().trim();
            int idade = idadeTextField.getText().trim().isEmpty() ? -1 : Integer.parseInt(idadeTextField.getText().trim());
            String coloracao = coloracaoTextField.getText().trim();
            String especie = especieTextField.getText().trim();
            byte[] foto = (uploadedImageBytes != null) ? uploadedImageBytes : null;
            String nomeProprietario = (String) proprietarioComboBox.getSelectedItem();
            Proprietario proprietario = proprietarios.stream().filter(p -> p.getNomeCompleto().equals(nomeProprietario)).findFirst().orElse(null);

            validateInputs(nome, estadoCastracao, raca, idade, coloracao, especie, proprietario);

            Paciente paciente = new Paciente();
            paciente.setNome(nome);
            paciente.setEstadoCastracao(EstadoCastracao.fromDescricao(estadoCastracao));
            paciente.setIdade(idade);
            paciente.setRaca(raca);
            paciente.setColoracao(coloracao);
            paciente.setEspecie(especie);
            paciente.setFoto(foto);
            paciente.setProprietario(proprietario);

            pacienteController.adicionarPaciente(paciente);
            clearInputs();
            loadPacientes();
        } catch (Exception ex) {
            handleException("Erro ao cadastrar paciente", ex);
        }
    }

    private void removerPaciente(ActionEvent e) {
        int[] selectedRows = pacienteTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um paciente!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) paciente(s) selecionado(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    Paciente paciente = new Paciente();
                    paciente.setId((Integer) pacienteTable.getValueAt(i, 0));
                    pacienteController.removerPaciente(paciente);
                } catch (Exception ex) {
                    handleException("Erro ao remover paciente", ex);
                }
            }
            JOptionPane.showMessageDialog(null, "Paciente(s) removidos(s) com sucesso!");
            loadPacientes();
        }
    }

    private void adicionarFoto(ActionEvent e) {
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

    private void loadPacientes() {
        try {
            List<Paciente> pacientes = pacienteController.listarPacientes();
            pacienteTable.setModel(new PacienteTableModel(pacientes));
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void validateInputs(String nome, String estadoCastracao, String raca, int idade, String coloracao, String especie, Proprietario proprietario) {
        if (proprietario == null) {
            throw new IllegalArgumentException("Proprietário inválido!");
        }
        if (idade < 0) {
            throw new IllegalArgumentException("Idade inválida!");
        }
        if (nome.isEmpty() || estadoCastracao.isEmpty() || raca.isEmpty() || coloracao.isEmpty() || especie.isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos!");
        }
    }

    private void clearInputs() {
        nomeTextField.setText("");
        estadoCastracaoComboBox.setSelectedIndex(0);
        idadeTextField.setText("");
        racaTextField.setText("");
        coloracaoTextField.setText("");
        especieTextField.setText("");
        uploadedImageBytes = null;
        imagemLabel.setIcon(null);
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
    }

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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}