package org.newdawn.spaceinvaders;

import javax.swing.*;

public class Window extends JFrame {

    /**
     * JFrame*/
    public Window()
    {
        this.setTitle("Space Invaders Main Menu");

        if(false)
        {
            this.setUndecorated(true);
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else
        {
            this.setSize(800, 600);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
    }
}
