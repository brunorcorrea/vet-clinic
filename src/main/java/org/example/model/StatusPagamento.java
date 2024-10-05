package org.example.model;

public enum StatusPagamento {

    PAGO("Pago"), PENDENTE("Pendente"), EM_ATRASO("Em Atraso");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public static StatusPagamento fromDescricao(String descricao) {
        for (StatusPagamento statusPagamento : StatusPagamento.values()) {
            if (statusPagamento.descricao.equals(descricao)) {
                return statusPagamento;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }
}
