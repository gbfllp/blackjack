package com.poo.blackjack;

import javax.swing.SwingUtilities;

public class Blackjack {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlackjackGUI();
            }
        });
    }
}