package org.example.view;

import org.example.controller.PacienteViewController;
import org.example.model.EstadoCastracao;
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
    private final PacienteViewController viewController = new PacienteViewController();
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
            proprietarios = viewController.listarProprietarios();
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

            viewController.adicionarPaciente(nome, estadoCastracao, raca, idade, coloracao, especie, foto, proprietario);
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
                    int pacienteId = (Integer) pacienteTable.getValueAt(i, 0);
                    viewController.removerPaciente(pacienteId);
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
            PacienteTableModel model = viewController.criarPacienteTableModel();
            pacienteTable.setModel(model);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pacienteTable = new JTable();
        scrollPane1.setViewportView(pacienteTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nome:");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeTextField = new JTextField();
        panel2.add(nomeTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Estado de Castração:");
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        estadoCastracaoComboBox = new JComboBox();
        panel2.add(estadoCastracaoComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Idade (anos):");
        panel2.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idadeTextField = new JTextField();
        panel2.add(idadeTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Raça:");
        panel2.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        racaTextField = new JTextField();
        panel2.add(racaTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Coloração:");
        panel2.add(label5, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        coloracaoTextField = new JTextField();
        panel2.add(coloracaoTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Foto:");
        panel2.add(label6, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarFotoButton = new JButton();
        adicionarFotoButton.setText("Adicionar Foto");
        panel2.add(adicionarFotoButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Espécie:");
        panel2.add(label7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        especieTextField = new JTextField();
        panel2.add(especieTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        imagemLabel = new JLabel();
        imagemLabel.setText("");
        panel2.add(imagemLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Proprietário:");
        panel2.add(label8, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        proprietarioComboBox = new JComboBox();
        panel2.add(proprietarioComboBox, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarPacienteButton = new JButton();
        adicionarPacienteButton.setText("Adicionar Paciente");
        panel2.add(adicionarPacienteButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerPacienteButton = new JButton();
        removerPacienteButton.setText("Remover Paciente");
        panel2.add(removerPacienteButton, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}