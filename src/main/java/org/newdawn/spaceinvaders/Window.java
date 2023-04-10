package org.newdawn.spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class Window extends JLabel implements ActionListener {

    public JFrame window;
    private JLabel label;

    JButton btn[];

    ImageIcon btnImage[];

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    private static User user;

    // 게스트 모드
    public Window() {

        // 파이어베이스 애플리케이션 초기화
        Firebase.initialize();

        window = new JFrame("Space Invaders Main Menu");
        JPanel panel = (JPanel) window.getContentPane();

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800,600));
        window.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);

        // setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
        setBounds(0,0,800,600);
        panel.add(this);
        setLayout(new BorderLayout());

        // 이미지를 배경으로 설정
        URL backgroundUrl = getClass().getResource("/images/background1.png");
        ImageIcon image = new ImageIcon(backgroundUrl);
        label = new JLabel(image);
        add(label);

        btnImage = new ImageIcon[7];

        URL btnImageUrl[] = new URL[7];
        btnImageUrl[0] = getClass().getResource("/images/btn1.png");
        btnImageUrl[1] = getClass().getResource("/images/btn2.png");
        btnImageUrl[2] = getClass().getResource("/images/btn3.png");
        btnImageUrl[3] = getClass().getResource("/images/btn4.png");
        btnImageUrl[4] = getClass().getResource("/images/btn5.png");
        btnImageUrl[5] = getClass().getResource("/images/btn6.png");
        btnImageUrl[6] = getClass().getResource("/images/btn7.png");

        btn = new JButton[7];

        for (int i = 0; i < 7; i++) {
            btnImage[i] = new ImageIcon(btnImageUrl[i]);
            btn[i] = new JButton(btnImage[i]);
            if(i<=4) {
                btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            }
            if(i==5){
                btn[5].setBounds(250,520,100,40);
            }
            if(i==6){
                btn[6].setBounds(450,520,100,40);
            }
            btn[i].addActionListener(this);
            label.add(btn[i]);
            btn[i].setVisible(true);
        }

        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0); //윈도우 닫히면 종료
            }
        });

        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }

    // 사용자 모드
    public Window(User user) {
        // 필수......
        this.user = user;

//        JOptionPane.showMessageDialog(window, user.name+"님으로 게임을 진행합니다..", "로그인 성공 !", JOptionPane.DEFAULT_OPTION);

        // 파이어베이스 애플리케이션 초기화
        Firebase.initialize();

        JFrame jFrame = new JFrame("Space Invaders Main Menu");
        JPanel panel = (JPanel) jFrame.getContentPane();

        //panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800,600));
        jFrame.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);

        // setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
        setBounds(0,0,800,600);
        panel.add(this);
        setLayout(new BorderLayout());

        // 이미지를 배경으로 설정
        URL backgroundUrl = getClass().getResource("/images/background1.png");
        ImageIcon image = new ImageIcon(backgroundUrl);
        label = new JLabel(image);
        add(label);

        btnImage = new ImageIcon[7];

        URL btnImageUrl[] = new URL[7];
        btnImageUrl[0] = getClass().getResource("/images/btn1.png");
        btnImageUrl[1] = getClass().getResource("/images/btn2.png");
        btnImageUrl[2] = getClass().getResource("/images/btn3.png");
        btnImageUrl[3] = getClass().getResource("/images/btn4.png");
        btnImageUrl[4] = getClass().getResource("/images/btn5.png");
        btnImageUrl[5] = getClass().getResource("/images/btn6.png");
        btnImageUrl[6] = getClass().getResource("/images/btn7.png");

        btn = new JButton[7];

        for (int i = 0; i < 7; i++) {
            btnImage[i] = new ImageIcon(btnImageUrl[i]);
            btn[i] = new JButton(btnImage[i]);
            if(i<=4) {
                btn[i].setBounds(110 + 120 * i, 450, 100, 40);
            }
            if(i==5){
                btn[5].setBounds(250,520,100,40);
            }
            if(i==6){
                btn[6].setBounds(450,520,100,40);
            }
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
        // 게임 시작 버튼 클릭 시 게임 화면 뜸
        if (e.getSource() == btn[0]) {
            // 사용자로 게임 실행
            if(user!=null){
                Game game = new Game(user);
                game.setVisible(true);
                game.requestFocus();
                Thread gameThread = new Thread(() -> game.gameLoop());
                gameThread.start();
            }
            // 게스트로 게임 실행
            else{
                Game game = new Game();
                game.setVisible(true);
                game.requestFocus();
                Thread gameThread = new Thread(() -> game.gameLoop());
                gameThread.start();
            }
        }
        // 로그인 버튼 클릭 시 로그인 화면 뜸
        else if(e.getSource() == btn[5]){
            Login login = new Login();
            login.login();
        }
        // 회원가입 버튼 클릭 시 회원가입 화면 뜸
        else if(e.getSource() == btn[6]){
            Register register = new Register();
            register.register();
        }
    }
}