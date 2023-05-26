package org.newdawn.spaceinvaders;

import com.google.firebase.database.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;

public class Framework extends JLabel implements ActionListener, MouseListener {
    enum GameState {STARTING, PLAYING, MAIN_MENU, OPTIONS, DESCRIPTION, THEME, CHARACTER, RANKING}
    GameState gameState;

    Theme theme;

    JButton btn[];

    ImageIcon btnImage[];

    BufferedImage[][] backgroundImage;

    ImageIcon[] themeChoiceImage;

    static public int themeChoice = 0;

    JLabel []themeLabel;

    JLabel themeTitleLabel;

    JLabel characterTitleLabel;

    JLabel rankingLabel;

    public static JTable rankTable;

    JScrollPane rankingScrollPane;

    ImageIcon[] characterImage;

    int characterChoice = 0;

    static int character = 0;

    JLabel []characterLabel;

    String []btnColor;

    private static User user;

    // 사용자 모드
    public Framework() {
        // 파이어베이스 애플리케이션 초기화
        Firebase.initialize();

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

    // 사용자 모드
    public Framework(User user) {
        Framework.user = user;

        // 파이어베이스 애플리케이션 초기화
        Firebase.initialize();

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

    /** 이미지 url 관리*/

    public void loadContent() {

        theme = new Theme();

        themeTitleLabel = new JLabel();
        characterTitleLabel = new JLabel();

        rankingLabel = new JLabel();
        rankTable = new JTable();
        rankingScrollPane = new JScrollPane();

        //버튼 생성
        btnImage = new ImageIcon[10];

        btn = new JButton[13];

        URL[] btnImageUrl = new URL[10];
        //메인화면
        btnImageUrl[0] = getClass().getResource("/images/btn1.png");
        btnImageUrl[1] = getClass().getResource("/images/btn2.png");
        btnImageUrl[2] = getClass().getResource("/images/btn3.png");
        btnImageUrl[3] = getClass().getResource("/images/btn4.png");
        btnImageUrl[4] = getClass().getResource("/images/btn5.png");

        //로그인 회원가입
        btnImageUrl[5] = getClass().getResource("/images/btn6.png");
        btnImageUrl[6] = getClass().getResource("/images/btn7.png");

        //뒤로가기
        btnImageUrl[7] = getClass().getResource("/images/btn8.png");

        //선택하기 테마, 캐릭터
        btnImageUrl[8] = getClass().getResource("/images/btn9.png");
        btnImageUrl[9] = getClass().getResource("/images/btn9.png");


        String []btnStr = new String[13];
        btnStr[0] = "게임시작";
        btnStr[1] = "게임설명";
        btnStr[2] = "테마설정";
        btnStr[3] = "캐릭터설정";
        btnStr[4] = "랭킹보기";
        btnStr[5] = "로그인";
        btnStr[6] = "회원가입";
        btnStr[7] = "";
        btnStr[8] = "선택하기"; //테마
        btnStr[9] = "선택하기"; //캐릭터
        btnStr[10] = "쉬움";
        btnStr[11] = "보통";
        btnStr[12] = "어려움";


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
                btn[i].setBounds(20, 15, 60, 60);

            }
            else {
                btn[i] = new JButton(btnStr[i]);
                btn[i].setForeground(Color.WHITE);
                btn[i].setBackground(Color.decode(theme.getThemeColor()));
                if (i < 5) btn[i].setBounds(110 + 120 * i, 450, 100, 40);
                else if (i < 7) btn[i].setBounds(230 + (i-5)*190, 380, 150, 50);
                else if (i < 10) btn[i].setBounds(350, 450, 100, 40);
                else  btn[i].setBounds(220 + 120*(i-10), 420, 100, 50);

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
                // URL 객체가 null 경우
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

        characterChoiceUrl[0] = getClass().getResource("/images/ship.gif");
        characterChoiceUrl[1] = getClass().getResource("/images/ship2.png");
        characterChoiceUrl[2] = getClass().getResource("/images/ship3.png");
        characterChoiceUrl[3] = getClass().getResource("/images/ship4.png");
        characterChoiceUrl[4] = getClass().getResource("/images/ship5.png");

        for (int i=0; i<5; i++) {
            if (characterChoiceUrl[i] == null) {
                // URL 객체가 null 경우
                System.out.println("Error: Could not find image file for ship" + (i+1) + ".png");
                continue;
            }
            characterImage[i] = new ImageIcon(characterChoiceUrl[i]);
            characterLabel[i] = new JLabel(characterImage[i]);
            characterLabel[i].setBounds(137 + 123*i, 270, 33, 23);
            characterLabel[i].addMouseListener(this);
        }


        //메인화면 버튼 생성
        for(int i=0; i<7; i++) {
            this.add(btn[i]);
            btn[i].setVisible(true);
        }

    }

    /**
     * 메인 화면 버튼 관리*/
    public void manageComponent() {
        //다른 곳의 버튼 없애기 + 메인화면에서 버튼 만들기
        if (gameState == GameState.MAIN_MENU) {
            for (int i=0; i<13; i++) btn[i].setVisible(false);

            for (int i=0; i<9; i++) themeLabel[i].setVisible(false);

            for (int i=0; i<5; i++) characterLabel[i].setVisible(false);

            themeTitleLabel.setVisible(false);
            characterTitleLabel.setVisible(false);
            rankingLabel.setVisible(false);
            rankingScrollPane.setVisible(false);

            setLayout(null);
            for (int i=0; i<7; i++) {
                btn[i].setBackground(Color.decode(theme.getThemeColor()));
                btn[i].setVisible(true);
            }
        }
        else {
            //메인화면 버튼 없애기 + 뒤로가기 버튼 만들기
            for (int i=0; i<7; i++) btn[i].setVisible(false);
            setLayout(null);
            add(btn[7]);
            btn[7].setVisible(true);
            btn[7].setContentAreaFilled(false);
            btn[7].setBorderPainted(false);

            if (gameState == GameState.OPTIONS) {
                for (int i=10; i<13; i++) {
                    btn[i].setBackground(Color.decode(theme.getThemeColor()));
                    add(btn[i]);
                    btn[i].setVisible(true);
                }
            }
            else if (gameState == GameState.THEME) {
                ImageIcon themeTitleIcon = new ImageIcon("src/main/resources/images/theme_title.png");
                Image imgThemeTitle = themeTitleIcon.getImage();

                Image changeImgThemeTitle = imgThemeTitle.getScaledInstance(260, 100, Image.SCALE_SMOOTH);
                ImageIcon changeIconThemeTitle = new ImageIcon(changeImgThemeTitle);

                themeTitleLabel = new JLabel(changeIconThemeTitle);
                add(themeTitleLabel);
                themeTitleLabel.setBounds(250, 110, 300, 100);
                themeTitleLabel.setVisible(true);

                btn[8].setBounds(350, 450, 100, 40);
                btn[8].setBackground(Color.decode(theme.getThemeColor()));
                add(btn[8]);
                btn[8].setVisible(true);

                for (int i=0; i<9; i++) {
                    add(themeLabel[i]);
                    themeLabel[i].setVisible(true);
                }
            }
            else if (gameState == GameState.CHARACTER) {
                ImageIcon characterTitleIcon = new ImageIcon("src/main/resources/images/character_title.png");
                Image imgCharacterTitle = characterTitleIcon.getImage();

                Image changeImgCharacterTitle = imgCharacterTitle.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
                ImageIcon changeIconCharacterTitle = new ImageIcon(changeImgCharacterTitle);

                characterTitleLabel = new JLabel(changeIconCharacterTitle);
                add(characterTitleLabel);
                characterTitleLabel.setBounds(250, 120, 300, 100);
                characterTitleLabel.setVisible(true);

                btn[9].setBackground(Color.decode(theme.getThemeColor()));
                add(btn[9]);
                btn[9].setVisible(true);

                for (int i=0; i<5; i++) {
                    add(characterLabel[i]);
                    characterLabel[i].setVisible(true);
                }
            }
            /** ranking 화면 구현 */
            else if (gameState == GameState.RANKING) {
                // '사용자 랭킹' 텍스트 띄우기
                ImageIcon rankingIcon = new ImageIcon("src/main/resources/images/ranking_title.png");
                Image imgRankingTitle = rankingIcon.getImage();

                Image changeImgRankingTitle = imgRankingTitle.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
                ImageIcon changeIconRankingTitle = new ImageIcon(changeImgRankingTitle);

                rankingLabel = new JLabel(changeIconRankingTitle);
                add(rankingLabel);
                rankingLabel.setBounds(250, 120, 300, 100);
                rankingLabel.setVisible(true);

                // 사용자 정보 받아오기
                FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = userDatabase.getReference();
                DatabaseReference usersRef = ref.child("Users");

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<BestTimeUserPair> pairs = new ArrayList<>();
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            String decodedEmail = new String(Base64.getDecoder().decode(userId));
                            if (!userSnapshot.child(userId).child("bestTime").getValue().toString().isEmpty()) {
                                String bestTime = userSnapshot.child(userId).child("bestTime").getValue(String.class);
                                String[] tokens = bestTime.split(":");
                                int minutes = Integer.parseInt(tokens[0]);
                                int seconds = Integer.parseInt(tokens[1]);
                                int millis = Integer.parseInt(tokens[2]);
                                int bestTimeInt = minutes * 60 * 1000 + seconds * 1000 + millis;
                                pairs.add(new BestTimeUserPair(bestTimeInt, decodedEmail));
                            }
                        }
                        pairs.sort((x, y) -> {return x.getBestTime() - y.getBestTime();});

                        String[] columnNames = {"Rank", "User ID", "Best Time"};
                        Object[][] data = new Object[pairs.size()][3];
                        for (int j = 0; j < pairs.size(); j++) {
                            data[j][0] = j + 1;
                            data[j][1] = pairs.get(j).getUser();
                            int minutes = pairs.get(j).getBestTime() / 1000 / 60;
                            int seconds = (pairs.get(j).getBestTime() / 1000) % 60;
                            int millis = pairs.get(j).getBestTime() % 1000*10;
                            data[j][2] = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);

                        }
                        rankTable = new JTable(data, columnNames);
                        rankTable.setDefaultEditor(Object.class, null);
                        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
                        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
                        TableColumnModel tcm = rankTable.getColumnModel();
                        for(int c = 0 ; c < tcm.getColumnCount() ; c++){
                            tcm.getColumn(c).setCellRenderer(dtcr);
                        }
                        rankingScrollPane = new JScrollPane(rankTable);
                        add(rankingScrollPane);
                        rankingScrollPane.setBounds(150, 220, 500, 250);
                        rankingScrollPane.setVisible(true);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        draw(g2d);
    }

    /**스레드 */
    private void frameworkLoop() {

        while (true){
            switch (gameState) {
                case PLAYING:

                case MAIN_MENU:

                case DESCRIPTION:

                case THEME:

                case CHARACTER:

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

    /** 배경화면 그리기*/
    public void draw(Graphics2D g2d) {
        //TODO 위에 테마 선택 텍스트 추가, 캐릭터 선택 텍스트 추가

        switch (gameState) {
            case PLAYING:
                break;

            case MAIN_MENU:

            case OPTIONS:
                g2d.drawImage(theme.getMainBackground(), 0, 0, 800, 600, null);
                break;

            case DESCRIPTION:
                g2d.drawImage(theme.getDescriptionBackground(), 0, 0, 800, 600, null);
                break;

            case THEME:

            case CHARACTER:

            case RANKING:
                g2d.drawImage(theme.getBackground(), 0, 0, 800, 600, null);
                break;

            case STARTING:
                g2d.setColor(Color.black);
                g2d.fillRect(0,0,800,600);
                g2d.setColor(Color.white);
                g2d.drawString("Loading game....", (800-g2d.getFontMetrics().stringWidth("Loading game...."))/2,280);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //게임시작
        if (e.getSource() == btn[0]) {
            gameState = GameState.OPTIONS;
            manageComponent();
        }
        //게임설명
        else if (e.getSource() == btn[1]) {
            gameState = GameState.DESCRIPTION;
            manageComponent();
        }
        //테마설정
        else if (e.getSource() == btn[2]) {
            gameState = GameState.THEME;
            manageComponent();
        }
        //캐릭터설정
        else if (e.getSource() == btn[3]) {
            gameState = GameState.CHARACTER;
            manageComponent();
        }
        //랭킹보기
        else if (e.getSource() == btn[4]) {
            gameState = GameState.RANKING;
            manageComponent();
        }
        //로그인
        else if (e.getSource() == btn[5]) {
            Login login = new Login();
            login.login();
        }
        //회원가입
        else if (e.getSource() == btn[6]) {
            Register register = new Register();
            register.register();
        }
        //뒤로가기
        else if (e.getSource() == btn[7]) {
            gameState = GameState.MAIN_MENU;
            manageComponent();
        }
        //테마 선택
        else if (e.getSource() == btn[8]) {
            theme.setThemeState(themeChoice);
            gameState = GameState.MAIN_MENU;
            manageComponent();
        }
        //캐릭터선택
        else if (e.getSource() == btn[9]) {
            character = characterChoice;
            gameState = GameState.MAIN_MENU;
            manageComponent();
        }
        else if (e.getSource() == btn[10]) {
            Game game = new Game(0, user, theme);
            game.requestFocus();
            Thread gameThread = new Thread(game::gameLoop);
            gameThread.start();
        }
        else if (e.getSource() == btn[11]) {
            Game game = new Game(1, user, theme);
            game.requestFocus();
            Thread gameThread = new Thread(game::gameLoop);
            gameThread.start();
        }
        else if (e.getSource() == btn[12]) {
            Game game = new Game(2, user, theme);
            game.requestFocus();
            Thread gameThread = new Thread(game::gameLoop);
            gameThread.start();
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