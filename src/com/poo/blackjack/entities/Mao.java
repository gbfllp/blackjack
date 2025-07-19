package com.poo.blackjack.entities;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private List<Carta> cartas = new ArrayList<>();

    public void adicionarCarta(Carta carta) {
        cartas.add(carta);
    }

    public int getPontos() {
        int totalPontos = 0;
        int ases = 0;
        for (Carta carta : cartas) {
            totalPontos += carta.getPontos();
            if (carta.getPontos() == 11) {
                ases++;
            }
        }
        while (totalPontos > 21 && ases > 0) {
            totalPontos -= 10;
            ases--;
        }
        return totalPontos;
    }

    public void limpar() {
        cartas.clear();
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}