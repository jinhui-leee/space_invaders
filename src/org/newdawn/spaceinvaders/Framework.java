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

public class Framework extends JPanel implements ActionListener {


    enum GameState {STARTING, PLAYING, MAIN_MENU, DESCRIPTION, THEME, CHARACTER, RANKING}
    GameState gameState;


    JButton[] btn;

    JButton[] themeBtn;

    JButton[] characterBtn;

    ImageIcon[] btnImage;

    ImageIcon[] themeBtnImage;

    ImageIcon[] characterBtnImage;

    BufferedImage[] backgroundImage;



    public Framework() {
        ////
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        if(false) {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }





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
            //메인화면 배경
            URL backgroundUrl = this.getClass().getResource("/sprites/background1.png");
            backgroundImage[0] = ImageIO.read(backgroundUrl);

            //게임설명 배경
            URL descriptionUrl = this.getClass().getResource("/sprites/description.png");
            backgroundImage[1] = ImageIO.read(descriptionUrl);

            //테마설정 배경
            URL themeUrl = this.getClass().getResource("/sprites/background_d.png");
            backgroundImage[2] = ImageIO.read(themeUrl);

            //캐릭터설정 배경
            URL characterUrl = this.getClass().getResource("/sprites/background_d.png");
            backgroundImage[3] = ImageIO.read(characterUrl);

            //랭킹보기 배경
            URL rankingUrl = this.getClass().getResource("/sprites/background_d.png");
            backgroundImage[4] = ImageIO.read(rankingUrl);



        }
        catch (IOException e) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, e);
        }



        btnImage = new ImageIcon[10];
        btn = new JButton[10];


        URL[] btnImageUrl = new URL[10];
        //메인화면
        btnImageUrl[0] = getClass().getResource("/sprites/btn1.png");
        btnImageUrl[1] = getClass().getResource("/sprites/btn2.png");
        btnImageUrl[2] = getClass().getResource("/sprites/btn3.png");
        btnImageUrl[3] = getClass().getResource("/sprites/btn4.png");
        btnImageUrl[4] = getClass().getResource("/sprites/btn5.png");


        //로그인 회원가입
        btnImageUrl[5] = getClass().getResource("/sprites/btn6.png");
        btnImageUrl[6] = getClass().getResource("/sprites/btn7.png");

        //뒤로가기
        btnImageUrl[7] = getClass().getResource("/sprites/btn8.png");

        //선택하기
        btnImageUrl[8] = getClass().getResource("/sprites/btn9.png");
        btnImageUrl[9] = getClass().getResource("/sprites/btn9.png");


        for (int i = 0; i < btn.length; i++) {
            btnImage[i] = new ImageIcon(btnImageUrl[i]);
            btn[i] = new JButton(btnImage[i]);
            btn[i].addActionListener(this);
        }

        for(int i=0; i<5; i++) {
            btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            this.add(btn[i]);
            btn[i].setVisible(true);
        }

        for(int i=5; i<7; i++) {
            btn[i].setBounds(380 + (i-6)*40, 350, 150, 80);
            this.add(btn[i]);
            btn[i].setVisible(true);
        }


    }

    /**
     * 메인 화면 버튼 관리*/
    public void btnManager() {
        //다른 곳의 버튼 없애기 + 메인화면에서 버튼 만들기
        if (gameState == GameState.MAIN_MENU) {
            btn[7].setVisible(false);
            btn[8].setVisible(false);
            btn[9].setVisible(false);
            setLayout(null);
            for (int i=0; i<5; i++) {
                btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            }

            for(int i=5; i<7; i++) {
                btn[i].setBounds(380 + (i-6)*40, 350, 150, 80);
            }

            for (int i=0; i<7; i++) {
                this.add(btn[i]);
                btn[i].setVisible(true);
            }
        }
        else
        {
            //메인화면 버튼 없애기 + 뒤로가기 버튼 만들기
            for (int i=0; i<7; i++) btn[i].setVisible(false);
            setLayout(null);
            btn[7].setBounds(20, 15, 60, 60);
            add(btn[7]);
            btn[7].setVisible(true);
            btn[7].setContentAreaFilled(false);
            btn[7].setBorderPainted(false);

            if (gameState == GameState.THEME) {
                btn[8].setBounds(350, 450, 100, 40);
                add(btn[8]);
                btn[8].setVisible(true);
            }
            else if (gameState == GameState.CHARACTER) {
                btn[8].setBounds(350, 450, 100, 40);
                add(btn[8]);
                btn[8].setVisible(true);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        Draw(g2d);
    }

    /**스레드 */
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

    /**
     * 배경화면 그리기*/
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
                g2d.drawImage(backgroundImage[2], 0, 0, 800, 600, null);
                break;

            case CHARACTER:
                g2d.drawImage(backgroundImage[3], 0, 0, 800, 600, null);

                break;

            case RANKING:
                g2d.drawImage(backgroundImage[4], 0, 0, 800, 600, null);
                break;

            case STARTING:
                break;
        }

    }


    /**
     * 버튼 이벤트 처리*/
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
            btnManager();

        }
        //캐릭터설정
        else if (e.getSource() == btn[3]) {
            gameState = GameState.CHARACTER;
            btnManager();
        }
        //랭킹보기
        else if (e.getSource() == btn[4]) {
            gameState = GameState.RANKING;
            btnManager();
        }
        //로그인
        else if (e.getSource() == btn[5]) {

        }
        //회원가입
        else if (e.getSource() == btn[6]) {

        }

        //뒤로가기
        else if (e.getSource() == btn[7]) {
            gameState = GameState.MAIN_MENU;
            btnManager();
        }
        //테마 선택
        else if (e.getSource() == btn[8]) {
            gameState = GameState.MAIN_MENU;
            btnManager();
        }
        //캐릭터선택
        else if (e.getSource() == btn[9]) {
            gameState = GameState.MAIN_MENU;
            btnManager();
        }


    }

}
