package org.newdawn.spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class Window extends JPanel implements ActionListener {

    private JFrame frame;
    private JLabel label;

    JButton btn[];

    ImageIcon btnImage[];



    public Window() {
        JFrame jFrame = new JFrame("Space Invaders Main Menu");
        JPanel panel = (JPanel) jFrame.getContentPane();

        //panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800,600));

        // setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
        setBounds(0,0,800,600);
        panel.add(this);
        setLayout(new BorderLayout());

        // 이미지를 배경으로 설정
        URL backgroundUrl = getClass().getResource("/sprites/background1.png");
        ImageIcon image = new ImageIcon(backgroundUrl);
        label = new JLabel(image);
        add(label);

        btnImage = new ImageIcon[5];

        URL btnImageUrl[] = new URL[5];
        btnImageUrl[0] = getClass().getResource("/sprites/btn1.png");
        btnImageUrl[1] = getClass().getResource("/sprites/btn2.png");
        btnImageUrl[2] = getClass().getResource("/sprites/btn3.png");
        btnImageUrl[3] = getClass().getResource("/sprites/btn4.png");
        btnImageUrl[4] = getClass().getResource("/sprites/btn5.png");

        btn = new JButton[5];

        for (int i = 0; i < 5; i++) {
            btnImage[i] = new ImageIcon(btnImageUrl[i]);
            btn[i] = new JButton(btnImage[i]);
            btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            btn[i].addActionListener(this);
            label.add(btn[i]);
            btn[i].setVisible(true);

        }

        jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0); //윈도우 닫히면 종료
            }
        });

        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn[0]) {

            Game game = new Game();
            game.requestFocus();
            Thread gameThread = new Thread(() -> game.gameLoop());
            gameThread.start();
        }
    }
}