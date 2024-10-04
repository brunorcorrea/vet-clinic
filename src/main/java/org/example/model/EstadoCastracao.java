package org.example.model;

public enum EstadoCastracao {

    FERTIL("Fértil"), CASTRADO("Castrado");

    private final String descricao;

    EstadoCastracao(String descricao) {
        this.descricao = descricao;
    }

    public static EstadoCastracao fromDescricao(String descricao) {
        for (EstadoCastracao estadoCastracao : EstadoCastracao.values()) {
            if (estadoCastracao.descricao.equals(descricao)) {
                return estadoCastracao;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }
}
