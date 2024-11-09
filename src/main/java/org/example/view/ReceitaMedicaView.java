package org.example.view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.example.controller.ReceitaMedicaViewController;
import org.example.model.Paciente;
import org.example.model.ReceitaMedica;
import org.example.view.tablemodels.ReceitaMedicaTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReceitaMedicaView {
    private final ReceitaMedicaViewController viewController = new ReceitaMedicaViewController();

    private JPanel mainPanel;
    private JTable receitaMedicaTable;
    private JComboBox<String> pacienteComboBox;
    private JButton adicionarReceitaMedicaButton;
    private JButton removerReceitaMedicaButton;
    private JTextArea medicamentosTextArea;
    private JTextArea observacoesTextArea;
    private DateTimePicker dataEmissaoDateTimePicker;
    private JButton gerarPdfButton;
    private JTextField filtroPacienteNomeTextField;
    private ReceitaMedicaTableModel tableModel;

    private List<Paciente> pacientes = new ArrayList<>();

    public ReceitaMedicaView() {
        initializeComponents();
        configureListeners();
        loadPacientes();
        loadReceitasMedicas(filtroPacienteNomeTextField.getText().trim());
    }

    private void initializeComponents() {
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void configureListeners() {
        adicionarReceitaMedicaButton.addActionListener(this::adicionarReceitaMedica);
        removerReceitaMedicaButton.addActionListener(this::removerReceitaMedica);
        gerarPdfButton.addActionListener(this::gerarPdf);

        filtroPacienteNomeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterHistoricos();
            }

            private void filterHistoricos() {
                String text = filtroPacienteNomeTextField.getText().trim();
                loadReceitasMedicas(text);
            }
        });
    }

    private void gerarPdf(ActionEvent e) {
        int selectedRow = receitaMedicaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione uma receita médica para gerar o PDF", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRowCount = receitaMedicaTable.getSelectedRowCount();
        if (selectedRowCount > 1) {
            JOptionPane.showMessageDialog(null, "Selecione apenas uma receita médica para gerar o PDF", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReceitaMedica receitaMedica = tableModel.getReceitaMedica(selectedRow);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar PDF");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                gerarPdfReceitaMedica(receitaMedica, fileToSave.getAbsolutePath() + ".pdf");
                JOptionPane.showMessageDialog(null, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                handleException("Erro ao gerar PDF", ex);
            }
        }
    }

    public void gerarPdfReceitaMedica(ReceitaMedica receitaMedica, String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(filePath);

        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        String logoPath = "src/main/resources/vet-clinic-logo-200px.png";
        ImageData imageData = ImageDataFactory.create(logoPath);
        Image logo = new Image(imageData);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(logo);

        document.add(new Paragraph("Receita Médica").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph().add(new Text("Paciente: ").setBold()).add(new Text(receitaMedica.getPaciente().getNome())));
        document.add(new Paragraph().add(new Text("Data de Emissão: ").setBold()).add(new Text(receitaMedica.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))));

        document.add(new Paragraph("Medicamentos:").setBold());
        com.itextpdf.layout.element.List medicamentosList = new com.itextpdf.layout.element.List();
        for (String medicamento : receitaMedica.getMedicamentos()) {
            medicamentosList.add(new ListItem(medicamento));
        }
        document.add(medicamentosList);

        document.add(new Paragraph("Observações:").setBold());
        com.itextpdf.layout.element.List observacoesList = new com.itextpdf.layout.element.List();
        for (String observacao : receitaMedica.getObservacoes()) {
            observacoesList.add(new ListItem(observacao));
        }
        document.add(observacoesList);

        document.showTextAligned(new Paragraph("________________________________").setFontSize(12), 297.5f, 70, pdfDoc.getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        document.showTextAligned(new Paragraph("Assinatura").setFontSize(12), 297.5f, 55, pdfDoc.getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);

        document.showTextAligned(new Paragraph("Gerado por VetClinic 2024 - desenvolvido por Bruno Ricardo Corrêa.").setFontSize(10).setItalic(), 297.5f, 20, pdfDoc.getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        document.close();
    }

    private void loadPacientes() {
        try {
            pacientes = viewController.listarPacientes();
            pacientes.forEach(paciente -> pacienteComboBox.addItem(paciente.getNome()));
        } catch (Exception e) {
            pacientes = new ArrayList<>();
            handleException("Erro ao listar pacientes", e);
        }
    }

    private void adicionarReceitaMedica(ActionEvent e) {
        String medicamentosText = medicamentosTextArea.getText();
        LocalDateTime dataEmissao = dataEmissaoDateTimePicker.getDateTimePermissive();
        int pacienteIndex = pacienteComboBox.getSelectedIndex();

        if (!validateInputs(pacienteIndex, medicamentosText, dataEmissao)) return;

        List<String> medicamentos = List.of(medicamentosTextArea.getText().split("\n"));
        List<String> observacoes = List.of(observacoesTextArea.getText().split("\n"));
        Paciente paciente = pacientes.get(pacienteIndex);
        try {
            viewController.adicionarReceitaMedica(paciente, medicamentos, observacoes, dataEmissao);
            clearInputs();
        } catch (Exception ex) {
            handleException("Erro ao adicionar receita médica", ex);
        }

        loadReceitasMedicas(filtroPacienteNomeTextField.getText().trim());
    }

    private void removerReceitaMedica(ActionEvent e) {
        int[] selectedRows = receitaMedicaTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecione ao menos uma receita médica", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja realmente remover a(s) receita(s) médica(s) selecionada(s)?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            for (int i : selectedRows) {
                try {
                    int receitaMedicaId = tableModel.getReceitaMedica(i).getId();
                    viewController.removerReceitaMedica(receitaMedicaId);
                } catch (Exception ex) {
                    handleException("Erro ao remover receita médica", ex);
                }
            }
            loadReceitasMedicas(filtroPacienteNomeTextField.getText().trim());
        }
    }

    private void loadReceitasMedicas(String pacienteNome) {
        try {
            tableModel = viewController.criarReceitaMedicaTableModel(pacienteNome);
            receitaMedicaTable.setModel(tableModel);
        } catch (Exception e) {
            handleException("Erro ao listar receitas médicas", e);
        }
    }

    private boolean validateInputs(int pacienteIndex, String medicamentos, LocalDateTime dataEmissao) {
        if (pacienteIndex < 0 || pacienteIndex >= pacientes.size()) {
            JOptionPane.showMessageDialog(null, "Paciente inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (medicamentos == null || medicamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Medicamentos não podem ser vazios", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dataEmissao == null) {
            JOptionPane.showMessageDialog(null, "Data e hora inválidas", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearInputs() {
        medicamentosTextArea.setText("");
        observacoesTextArea.setText("");
        dataEmissaoDateTimePicker.setDateTimePermissive(LocalDateTime.now());
    }

    private void handleException(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        receitaMedicaTable = new JTable();
        scrollPane1.setViewportView(receitaMedicaTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Paciente:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pacienteComboBox = new JComboBox();
        panel2.add(pacienteComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removerReceitaMedicaButton = new JButton();
        removerReceitaMedicaButton.setText("Remover Receita Médica");
        panel2.add(removerReceitaMedicaButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Data de emissão:");
        panel2.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataEmissaoDateTimePicker = new DateTimePicker();
        panel2.add(dataEmissaoDateTimePicker, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Observações:");
        panel2.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        medicamentosTextArea = new JTextArea();
        panel2.add(medicamentosTextArea, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Medicamentos:");
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        observacoesTextArea = new JTextArea();
        observacoesTextArea.setText("");
        panel2.add(observacoesTextArea, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        adicionarReceitaMedicaButton = new JButton();
        adicionarReceitaMedicaButton.setText("Adicionar Receita Médica");
        panel2.add(adicionarReceitaMedicaButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gerarPdfButton = new JButton();
        gerarPdfButton.setText("Gerar PDF da Receita Médica");
        panel2.add(gerarPdfButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Buscar por paciente:");
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filtroPacienteNomeTextField = new JTextField();
        panel3.add(filtroPacienteNomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}