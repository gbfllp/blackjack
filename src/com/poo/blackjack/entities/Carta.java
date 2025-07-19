package com.poo.blackjack.entities;

public class Carta {
    private final String naipe;
    private final String valor;
    private final int pontos;

    public Carta(String naipe, String valor) {
        this.naipe = naipe;
        this.valor = valor;
        this.pontos = calcularPontos();
    }

    private int calcularPontos() {
        if ("ÁS".equals(valor)) {
            return 11;
        }
        if ("VALETE".equals(valor) || "DAMA".equals(valor) || "REI".equals(valor)) {
            return 10;
        }
        return Integer.parseInt(valor);
    }

    public int getPontos() {
        return pontos;
    }

    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}