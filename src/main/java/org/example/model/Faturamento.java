package org.example.model;

import java.time.LocalDateTime;

public class Faturamento {

    private int id;
    private Proprietario proprietario;
    private double valorTotal;
    private StatusPagamento status;
    private LocalDateTime dataVencimento;

    public void gerarFatura() {}
    public void atualizar() {}
    public void gerarRelatorio() {}
}
