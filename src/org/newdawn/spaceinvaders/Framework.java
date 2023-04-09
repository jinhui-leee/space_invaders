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

public class Framework extends JPanel implements ActionListener, MouseListener{




    enum GameState {STARTING, PLAYING, MAIN_MENU, DESCRIPTION, THEME, CHARACTER, RANKING}
    GameState gameState;


    JButton[] btn;

    ImageIcon[] btnImage;


    BufferedImage[][] backgroundImage;

    ImageIcon[] themeChoiceImage;

    int themeChoice = 0;

    int theme = 0;

    JLabel []themeLabel;

    ImageIcon[] characterImage;

    int characterChoice = 0;

    static int character = 0;

    JLabel []characterLabel;

    String []btnColor;



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

        //배경화면 생성
        backgroundImage = new BufferedImage[9][5];
        URL []backgroundUrl = new URL[9];

        backgroundUrl[0] = this.getClass().getResource("/sprites/background1.png");
        backgroundUrl[1] = this.getClass().getResource("/sprites/background2.png");
        backgroundUrl[2] = this.getClass().getResource("/sprites/background3.png");
        backgroundUrl[3] = this.getClass().getResource("/sprites/background4.png");
        backgroundUrl[4] = this.getClass().getResource("/sprites/background5.png");
        backgroundUrl[5] = this.getClass().getResource("/sprites/background6.png");
        backgroundUrl[6] = this.getClass().getResource("/sprites/background7.png");
        backgroundUrl[7] = this.getClass().getResource("/sprites/background8.png");
        backgroundUrl[8] = this.getClass().getResource("/sprites/background9.png");

        //설명화면 생성
        URL []descriptionUrl = new URL[9];

        descriptionUrl[0] = this.getClass().getResource("/sprites/description.png");
        descriptionUrl[1] = this.getClass().getResource("/sprites/description2.png");
        descriptionUrl[2] = this.getClass().getResource("/sprites/description3.png");
        descriptionUrl[3] = this.getClass().getResource("/sprites/description4.png");
        descriptionUrl[4] = this.getClass().getResource("/sprites/description5.png");
        descriptionUrl[5] = this.getClass().getResource("/sprites/description6.png");
        descriptionUrl[6] = this.getClass().getResource("/sprites/description7.png");
        descriptionUrl[7] = this.getClass().getResource("/sprites/description8.png");
        descriptionUrl[8] = this.getClass().getResource("/sprites/description9.png");

        //테마화면 생성
        URL []themeUrl = new URL[9];

        themeUrl[0] = this.getClass().getResource("/sprites/background_d.png");
        themeUrl[1] = this.getClass().getResource("/sprites/background_d2.png");
        themeUrl[2] = this.getClass().getResource("/sprites/background_d3.png");
        themeUrl[3] = this.getClass().getResource("/sprites/background_d4.png");
        themeUrl[4] = this.getClass().getResource("/sprites/background_d5.png");
        themeUrl[5] = this.getClass().getResource("/sprites/background_d6.png");
        themeUrl[6] = this.getClass().getResource("/sprites/background_d7.png");
        themeUrl[7] = this.getClass().getResource("/sprites/background_d8.png");
        themeUrl[8] = this.getClass().getResource("/sprites/background_d9.png");





        try {
            //메인화면 배경
            for (int i=0; i<9; i++) backgroundImage[i][0] = ImageIO.read(backgroundUrl[i]);
            //게임설명 배경
            for (int i=0; i<9; i++) backgroundImage[i][1] = ImageIO.read(descriptionUrl[i]);
            //테마설정 배경
            for (int i=0; i<9; i++) backgroundImage[i][2] = ImageIO.read(themeUrl[i]);
            //캐릭터 설정 배경
            for (int i=0; i<9; i++) backgroundImage[i][3] = ImageIO.read(themeUrl[i]);
            //랭킹보기 배경
            for (int i=0; i<9; i++) backgroundImage[i][4] = ImageIO.read(themeUrl[i]);
        }
        catch (IOException e) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, e);
        }


        //버튼 생성
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

        //선택하기 테마, 캐릭터
        btnImageUrl[8] = getClass().getResource("/sprites/btn9.png");
        btnImageUrl[9] = getClass().getResource("/sprites/btn9.png");

        String []btnStr = new String[10];
        btnStr[0] = "게임시작";
        btnStr[1] = "게임설명";
        btnStr[2] = "테마설정";
        btnStr[3] = "캐릭터설정";
        btnStr[4] = "랭킹보기";
        btnStr[5] = "로그인";
        btnStr[6] = "회원가입";
        btnStr[7] = "";
        btnStr[8] = "선택하기";
        btnStr[9] = "선택하기";

        btnColor = new String[9];
        btnColor[0] = "#451d52";
        btnColor[1] = "#4175dc";
        btnColor[2] = "#113c49";
        btnColor[3] = "#dd6d3e";
        btnColor[4] = "#303030";
        btnColor[5] = "#6c493d";
        btnColor[6] = "#f7b006";
        btnColor[7] = "#7d7353";
        btnColor[8] = "#ea3600";


        for (int i = 0; i < btn.length; i++) {
            //btnImage[i] = new ImageIcon(btnImageUrl[i]);
            //btn[i] = new JButton(btnImage[i]);
            if (i == 7) {
                btnImage[i] = new ImageIcon(btnImageUrl[i]);
                btn[i] = new JButton(btnImage[i]);
            }
            else {
                btn[i] = new JButton(btnStr[i]);
                btn[i].setForeground(Color.WHITE);
                btn[i].setBackground(Color.decode(btnColor[0]));
            }

            btn[i].addActionListener(this);
        }


        //테마 설정 아이콘
        themeChoiceImage = new ImageIcon[9];
        URL []themeChoiceUrl = new URL[9];
        themeLabel = new JLabel[9];


        themeChoiceUrl[0] = getClass().getResource("/icon/themeIcon1.png");
        themeChoiceUrl[1] = getClass().getResource("/icon/themeIcon2.png");
        themeChoiceUrl[2] = getClass().getResource("/icon/themeIcon3.png");
        themeChoiceUrl[3] = getClass().getResource("/icon/themeIcon4.png");
        themeChoiceUrl[4] = getClass().getResource("/icon/themeIcon5.png");
        themeChoiceUrl[5] = getClass().getResource("/icon/themeIcon6.png");
        themeChoiceUrl[6] = getClass().getResource("/icon/themeIcon7.png");
        themeChoiceUrl[7] = getClass().getResource("/icon/themeIcon8.png");
        themeChoiceUrl[8] = getClass().getResource("/icon/themeIcon9.png");

        for (int i=0; i<9; i++) {
            if (themeChoiceUrl[i] == null) {
                // URL 객체가 null인 경우
                System.out.println("Error: Could not find image file for themeChoice : " + i);
                continue;
            }
            themeChoiceImage[i] = new ImageIcon(themeChoiceUrl[i]);
            themeLabel[i] = new JLabel(themeChoiceImage[i]);
            if (i<5) themeLabel[i].setBounds(130 + 110*i, 200, 100, 100);
            else themeLabel[i].setBounds(170 + 110*(i-5), 320, 100, 100);
            themeLabel[i].addMouseListener(this);
        }

        //캐릭터 선택 아이콘
        characterImage = new ImageIcon[5];
        URL []characterChoiceUrl = new URL[5];
        characterLabel = new JLabel[5];

        characterChoiceUrl[0] = getClass().getResource("/sprites/ship.gif");
        characterChoiceUrl[1] = getClass().getResource("/sprites/ship2.png");
        characterChoiceUrl[2] = getClass().getResource("/sprites/ship3.png");
        characterChoiceUrl[3] = getClass().getResource("/sprites/ship4.png");
        characterChoiceUrl[4] = getClass().getResource("/sprites/ship5.png");

        for (int i=0; i<5; i++) {
            if (characterChoiceUrl[i] == null) {
                // URL 객체가 null인 경우
                System.out.println("Error: Could not find image file for ship" + (i+1) + ".png");
                continue;
            }
            characterImage[i] = new ImageIcon(characterChoiceUrl[i]);
            characterLabel[i] = new JLabel(characterImage[i]);
            characterLabel[i].setBounds(137 + 123*i, 200, 33, 23);
            characterLabel[i].addMouseListener(this);
        }


        //메인화면 버튼 생성
        for(int i=0; i<7; i++) {
            if (i < 5) btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            else btn[i].setBounds(230 + (i-5)*190, 380, 150, 50);
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
            for (int i=0; i<9; i++) themeLabel[i].setVisible(false);

            for (int i=0; i<5; i++) characterLabel[i].setVisible(false);

            setLayout(null);
            for (int i=0; i<7; i++) {
                if (i < 5) btn[i].setBounds(110 + 120 * i, 450, 100, 40);
                else btn[i].setBounds(230 + (i-5)*190, 380, 150, 50);
                btn[i].setBackground(Color.decode(btnColor[theme]));
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
                btn[8].setBackground(Color.decode(btnColor[theme]));
                add(btn[8]);
                btn[8].setVisible(true);

                //테마 선택 행성 아이콘
                for (int i=0; i<9; i++) {
                    add(themeLabel[i]);
                    themeLabel[i].setVisible(true);
                }

            }
            else if (gameState == GameState.CHARACTER) {
                btn[9].setBounds(350, 450, 100, 40);
                btn[9].setBackground(Color.decode(btnColor[theme]));
                add(btn[9]);
                btn[9].setVisible(true);

                for (int i=0; i<5; i++) {
                    add(characterLabel[i]);
                    characterLabel[i].setVisible(true);
                }
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
                g2d.drawImage(backgroundImage[theme][0], 0, 0, 800, 600, null);
                break;

            case DESCRIPTION:
                g2d.drawImage(backgroundImage[theme][1], 0, 0, 800, 600, null);

                break;

            case THEME:
                g2d.drawImage(backgroundImage[theme][2], 0, 0, 800, 600, null);
                break;

            case CHARACTER:
                g2d.drawImage(backgroundImage[theme][3], 0, 0, 800, 600, null);

                break;

            case RANKING:
                g2d.drawImage(backgroundImage[theme][4], 0, 0, 800, 600, null);
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
            Login.login();
        }
        //회원가입
        else if (e.getSource() == btn[6]) {
            Register.register();
        }

        //뒤로가기
        else if (e.getSource() == btn[7]) {
            gameState = GameState.MAIN_MENU;
            btnManager();
        }
        //테마 선택
        else if (e.getSource() == btn[8]) {
            theme = themeChoice;
            gameState = GameState.MAIN_MENU;
            btnManager();
        }
        //캐릭터선택
        else if (e.getSource() == btn[9]) {
            character = characterChoice;
            gameState = GameState.MAIN_MENU;
            btnManager();
        }



    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == themeLabel[0]) {
            themeChoice = 0;
        }
        else if (e.getSource() == themeLabel[1]) {
            themeChoice = 1;
        }
        else if (e.getSource() == themeLabel[2]) {
            themeChoice = 2;
        }
        else if (e.getSource() == themeLabel[3]) {
            themeChoice = 3;
        }
        else if (e.getSource() == themeLabel[4]) {
            themeChoice = 4;
        }
        else if (e.getSource() == themeLabel[5]) {
            themeChoice = 5;
        }
        else if (e.getSource() == themeLabel[6]) {
            themeChoice = 6;
        }
        else if (e.getSource() == themeLabel[7]) {
            themeChoice = 7;
        }
        else if (e.getSource() == themeLabel[8]) {
            themeChoice = 8;
        }
        else if (e.getSource() == characterLabel[0]) {
            characterChoice = 0;
        }
        else if (e.getSource() == characterLabel[1]) {
            characterChoice = 1;
        }
        else if (e.getSource() == characterLabel[2]) {
            characterChoice = 2;
        }
        else if (e.getSource() == characterLabel[3]) {
            characterChoice = 3;
        }
        else if (e.getSource() == characterLabel[4]) {
            characterChoice = 4;
        }


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