package org.example.controller;

import org.example.model.Faturamento;
import org.example.model.Proprietario;
import org.example.model.StatusPagamento;
import org.example.view.tablemodels.FaturaTableModel;

import java.time.LocalDateTime;
import java.util.List;

public class FaturaViewController {
    private final FaturamentoController faturamentoController = FaturamentoController.getInstance();
    private final ProprietarioController proprietarioController = ProprietarioController.getInstance();

    public List<Proprietario> listarProprietarios() throws Exception {
        return proprietarioController.listarProprietarios();
    }

    public void adicionarFatura(Proprietario proprietario, double valorTotal, StatusPagamento status, LocalDateTime dataVencimento) throws Exception {
        Faturamento faturamento = new Faturamento();
        faturamento.setProprietario(proprietario);
        faturamento.setValorTotal(valorTotal);
        faturamento.setStatus(status);
        faturamento.setDataVencimento(dataVencimento);
        faturamentoController.adicionarFaturamento(faturamento);
    }

    public void removerFatura(int faturamentoId) throws Exception {
        Faturamento faturamento = new Faturamento();
        faturamento.setId(faturamentoId);
        faturamentoController.removerFaturamento(faturamento);
    }

    public List<Faturamento> listarFaturamentos() throws Exception {
        return faturamentoController.listarFaturamentos();
    }

    public FaturaTableModel criarFaturaTableModel(String nomeProprietario) throws Exception {
        List<Faturamento> faturamentos = listarFaturamentos();

        if (nomeProprietario != null && !nomeProprietario.isEmpty()) {
            filtrarFaturamentos(faturamentos, nomeProprietario);
        }

        return new FaturaTableModel(faturamentos);
    }

    private void filtrarFaturamentos(List<Faturamento> faturamentos, String nomeProprietario) {
        faturamentos.removeIf(faturamento -> shouldRemoveFaturamento(nomeProprietario, faturamento));
    }

    private boolean shouldRemoveFaturamento(String nomeProprietario, Faturamento faturamento) {
        return faturamento == null || faturamento.getProprietario() == null ||
                faturamento.getProprietario().getNomeCompleto() == null ||
                !faturamento.getProprietario().getNomeCompleto().toLowerCase().contains(nomeProprietario.toLowerCase());
    }
}