package com.poo.blackjack;

import com.poo.blackjack.entities.*;
import com.poo.blackjack.entities.Jogador;
import com.poo.blackjack.entities.Participante;
import com.poo.blackjack.entities.Shoe;

import java.util.Scanner;

public class Blackjack {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Bem-vindo ao Blackjack OO! ---");
        System.out.print("Digite seu nome, jogador: ");
        String nomeJogador = scanner.nextLine();

        Jogador jogador = new Jogador(nomeJogador);
        Participante croupier = new Participante();

        while (true) {
            System.out.println("\n--- Iniciando nova rodada ---");
            Shoe shoe = new Shoe(4);
            jogador.limparMao();
            croupier.limparMao();

            jogador.adicionarCarta(shoe.comprarCarta());
            croupier.adicionarCarta(shoe.comprarCarta());
            jogador.adicionarCarta(shoe.comprarCarta());
            croupier.adicionarCarta(shoe.comprarCarta());

            jogador.realizarJogada(shoe, jogador);

            int pontosJogador = jogador.getPontos();
            System.out.println("Pontuação final de " + nomeJogador + ": " + pontosJogador);

            if (pontosJogador > 21) {
                System.out.println("Você estourou! O Croupier vence.");
            } else {
                croupier.realizarJogada(shoe);
                int pontosCroupier = croupier.getPontos();
                System.out.println("Pontuação final do Croupier: " + pontosCroupier);

                if (pontosCroupier > 21 || pontosJogador > pontosCroupier) {
                    System.out.println("Parabéns, " + nomeJogador + "! Você venceu!");
                } else if (pontosJogador < pontosCroupier) {
                    System.out.println("O Croupier venceu.");
                } else {
                    System.out.println("Empate!");
                }
            }

            System.out.print("\nDeseja jogar outra rodada? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) {
                break;
            }
        }
        System.out.println("\nObrigado por jogar!");
        scanner.close();
    }
}