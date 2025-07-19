package com.poo.blackjack.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shoe {
    private List<Carta> cartas;

    public Shoe(int numeroDeBaralhos) {
        this.cartas = new ArrayList<>();
        String[] naipes = {"Copas", "Paus", "Ouros", "Espadas"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "VALETE", "DAMA", "REI", "ÁS"};

        for (int i = 0; i < numeroDeBaralhos; i++) {
            for (String naipe : naipes) {
                for (String valor : valores) {
                    this.cartas.add(new Carta(naipe, valor));
                }
            }
        }
        embaralhar();
    }

    public void embaralhar() {
        Collections.shuffle(this.cartas);
    }

    public Carta comprarCarta() {
        if (cartas.isEmpty()) {
            throw new RuntimeException("Acabaram as cartas!");
        }
        return this.cartas.remove(0);
    }
}