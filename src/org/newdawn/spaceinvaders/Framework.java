package org.newdawn.spaceinvaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Framework extends JPanel implements ActionListener, KeyListener, MouseListener {



    enum GameState {STARTING, PLAYING, MAIN_MENU, DESCRIPTION, THEME, CHARACTER, RANKING}
    GameState gameState;


    JButton[] btn;

    JButton backBtn;

    ImageIcon[] btnImage;

    BufferedImage[] backgroundImage;



    public Framework() {
        ////
        this.setDoubleBuffered(true);
        this.setFocusable(true);


        // If you will draw your own mouse cursor or if you just want that mouse cursor disapear,
        // insert "true" into if condition and mouse cursor will be removed.
        if(false)
        {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }

        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);
        // Adds the mouse listener to JPanel to receive mouse events from this component.
        this.addMouseListener(this);




        // setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
        //setBounds(0,0,800,600);


        gameState = GameState.STARTING;


        Thread gameThread = new Thread() {
            @Override
            public void run(){
                frameworkLoop();
            }
        };
        gameThread.start();

    }



    /**
     * 이미지 url 관리*/

    private void loadContent() {

        backgroundImage = new BufferedImage[5];

        try {
            URL backgroundUrl = this.getClass().getResource("/sprites/background1.png");
            backgroundImage[0] = ImageIO.read(backgroundUrl);

            URL descriptionUrl = this.getClass().getResource("/sprites/description.png");
            backgroundImage[1] = ImageIO.read(descriptionUrl);

        }
        catch (IOException e) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, e);
        }



        btnImage = new ImageIcon[6];
        btn = new JButton[6];


        URL[] btnImageUrl = new URL[6];
        btnImageUrl[0] = getClass().getResource("/sprites/btn1.png");
        btnImageUrl[1] = getClass().getResource("/sprites/btn2.png");
        btnImageUrl[2] = getClass().getResource("/sprites/btn3.png");
        btnImageUrl[3] = getClass().getResource("/sprites/btn4.png");
        btnImageUrl[4] = getClass().getResource("/sprites/btn5.png");
        btnImageUrl[5] = getClass().getResource("/sprites/btn6.png");



        for (int i = 0; i < btn.length; i++) {
            btnImage[i] = new ImageIcon(btnImageUrl[i]);
            btn[i] = new JButton(btnImage[i]);
            btn[i].addActionListener(this);
            btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            this.add(btn[i]);
            btn[i].setVisible(true);

        }


    }

    public void btnManager() {
        if (gameState == GameState.MAIN_MENU) {
            btn[5].setVisible(false);
            setLayout(null);
            for (int i=0; i<5; i++) {
                btn[i].setBounds(110 + 120 * i, 450, 100, 40);
                this.add(btn[i]);
                btn[i].setVisible(true);
            }
        }
        else
        {
            for (int i=0; i<5; i++) btn[i].setVisible(false);
            setLayout(null);
            btn[5].setBounds(20, 15, 60, 60);
            add(btn[5]);
            btn[5].setVisible(true);
            btn[5].setContentAreaFilled(false);
            btn[5].setBorderPainted(false);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        Draw(g2d);
    }

    private void frameworkLoop() {

        while (true){
            switch (gameState) {
                case PLAYING:
                    break;

                case MAIN_MENU:

                    break;

                case DESCRIPTION:

                    break;

                case THEME:

                    break;

                case CHARACTER:

                    break;

                case RANKING:

                    break;

                case STARTING:
                    loadContent();
                    gameState = GameState.MAIN_MENU;
                    break;
            }

            repaint();

        }
    }

    public void Draw(Graphics2D g2d) {

        switch (gameState) {
            case PLAYING:
                break;

            case MAIN_MENU:
                g2d.drawImage(backgroundImage[0], 0, 0, 800, 600, null);
                break;

            case DESCRIPTION:
                g2d.drawImage(backgroundImage[1], 0, 0, 800, 600, null);

                break;

            case THEME:

                break;

            case CHARACTER:

                break;

            case RANKING:

                break;

            case STARTING:

                break;
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {


        //게임시작
        if (e.getSource() == btn[0]) {

            Game game = new Game();
            game.requestFocus();
            Thread gameThread = new Thread(game::gameLoop);
            gameThread.start();
        }
        //게임설명
        else if (e.getSource() == btn[1]) {
            gameState = GameState.DESCRIPTION;
            btnManager();
        }
        //테마설정
        else if (e.getSource() == btn[2]) {
            gameState = GameState.THEME;

        }
        //캐릭터설정
        else if (e.getSource() == btn[3]) {
            gameState = GameState.CHARACTER;
        }
        //랭킹보기
        else if (e.getSource() == btn[4]) {
            gameState = GameState.RANKING;
        }
        else if (e.getSource() == btn[5]) {
            gameState = GameState.MAIN_MENU;
            btnManager();

        }


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
