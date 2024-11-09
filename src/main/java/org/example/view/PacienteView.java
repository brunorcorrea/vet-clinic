package org.example.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.example.controller.PacienteViewController;
import org.example.model.EstadoCastracao;
import org.example.model.Proprietario;
import org.example.view.tablemodels.PacienteTableModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JTextField filtroProprietarioNomeTextField;
    private PacienteTableModel tableModel;

    private byte[] uploadedImageBytes;
    private List<Proprietario> proprietarios = new ArrayList<>();

    public PacienteView() {
        initializeComponents();
        configureListeners();
        loadPacientes(filtroProprietarioNomeTextField.getText().trim());
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

        filtroProprietarioNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterPacientes();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterPacientes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterPacientes();
            }

            private void filterPacientes() {
                String text = filtroProprietarioNomeTextField.getText().trim();
                loadPacientes(text);
            }
        });
    }

    private void adicionarPaciente(ActionEvent e) {
        try {
            String nome = nomeTextField.getText().trim();
            String estadoCastracao = estadoCastracaoComboBox.getSelectedItem().toString();
            String raca = racaTextField.getText().trim();
            String idadeString = idadeTextField.getText().trim();
            String coloracao = coloracaoTextField.getText().trim();
            String especie = especieTextField.getText().trim();
            byte[] foto = (uploadedImageBytes != null) ? uploadedImageBytes : null;
            int proprietarioIndex = proprietarioComboBox.getSelectedIndex();

            validateInputs(nome, estadoCastracao, raca, idadeString, coloracao, especie, proprietarioIndex);

            int idade = Integer.parseInt(idadeTextField.getText().trim());
            Proprietario proprietario = proprietarios.get(proprietarioIndex);
            viewController.adicionarPaciente(nome, estadoCastracao, raca, idade, coloracao, especie, foto, proprietario);
            clearInputs();
            loadPacientes(filtroProprietarioNomeTextField.getText().trim());
        } catch (Exception ex) {
            handleException("Erro ao cadastrar paciente", ex);
        }
    }

    private void removerPaciente(ActionEvent e) {
        int[] selectedRows = pacienteTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos um paciente", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover o(s) paciente(s) selecionado(s) e todos os seus dados relacionados?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int pacienteId = tableModel.getPaciente(i).getId();
                    viewController.removerPaciente(pacienteId);
                } catch (Exception ex) {
                    handleException("Erro ao remover paciente e seus dados relacionados", ex);
                }
            }
            loadPacientes(filtroProprietarioNomeTextField.getText().trim());
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

    private void loadPacientes(String nomeProprietario) {
        try {
            tableModel = viewController.criarPacienteTableModel(nomeProprietario);
            pacienteTable.setModel(tableModel);
        } catch (Exception e) {
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void validateInputs(String nome, String estadoCastracao, String raca, String idadeString, String coloracao, String especie, int proprietarioIndex) {
        if (proprietarioIndex < 0 || proprietarioIndex >= proprietarios.size()) {
            throw new IllegalArgumentException("Proprietário inválido!");
        }
        if (idadeString.isEmpty() || !idadeString.matches("\\d+")) {
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
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pacienteTable = new JTable();
        scrollPane1.setViewportView(pacienteTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nome:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeTextField = new JTextField();
        panel2.add(nomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Estado de Castração:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        estadoCastracaoComboBox = new JComboBox();
        panel2.add(estadoCastracaoComboBox, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Idade (anos):");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idadeTextField = new JTextField();
        panel2.add(idadeTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Raça:");
        panel2.add(label4, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        racaTextField = new JTextField();
        panel2.add(racaTextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Coloração:");
        panel2.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        coloracaoTextField = new JTextField();
        panel2.add(coloracaoTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Foto:");
        panel2.add(label6, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarFotoButton = new JButton();
        adicionarFotoButton.setText("Adicionar Foto");
        panel2.add(adicionarFotoButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Espécie:");
        panel2.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        especieTextField = new JTextField();
        panel2.add(especieTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        imagemLabel = new JLabel();
        imagemLabel.setText("");
        panel2.add(imagemLabel, new GridConstraints(0, 4, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Proprietário:");
        panel2.add(label8, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        proprietarioComboBox = new JComboBox();
        panel2.add(proprietarioComboBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adicionarPacienteButton = new JButton();
        adicionarPacienteButton.setText("Adicionar Paciente");
        panel2.add(adicionarPacienteButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerPacienteButton = new JButton();
        removerPacienteButton.setText("Remover Paciente");
        panel2.add(removerPacienteButton, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Buscar por proprietário:");
        panel3.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroProprietarioNomeTextField = new JTextField();
        filtroProprietarioNomeTextField.setText("");
        panel3.add(filtroProprietarioNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}