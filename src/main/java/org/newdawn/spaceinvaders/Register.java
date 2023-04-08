package org.newdawn.spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Register extends JPanel implements ActionListener {
    public static JFrame register;

    JButton registerbtn;
    JTextField inputID;
    JTextField inputPWD;

    static Font font_basic = new Font("맑은 고딕", Font.PLAIN, 12);
    static Font font_basic_bold = new Font("맑은 고딕", Font.BOLD, 12);
    static Font font_basic_bold_size_14 = new Font("맑은 고딕", Font.BOLD, 14);

    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();

    static public void register() {
        register = new JFrame("회원가입");

        // 회원가입 창
        JPanel registerPanel = (JPanel) register.getContentPane();
        registerPanel.setPreferredSize(new Dimension(400, 300));
        registerPanel.setLayout(null);

        register.setLocation(screenSize.width / 2 - 200, screenSize.height / 2 - 150);

        Color loginBGC = new Color(225, 235, 239);
        registerPanel.setBackground(loginBGC);
        // 로그인 창이 가장 상위 레벨에 뜨게 함
        register.setAlwaysOnTop(true);

        JLabel IDLabel = new JLabel("아이디");
        IDLabel.setHorizontalAlignment(JLabel.CENTER);
        IDLabel.setBounds(100, 50, 60, 20);
        IDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(IDLabel);

        JTextField inputID = new JTextField();
        inputID.setBounds(170, 50, 150, 20);
        inputID.setFont(font_basic);
        registerPanel.add(inputID);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(PWDLabel);

        JTextField inputPWD = new JTextField();
        inputPWD.setBounds(170, 75, 150, 20);
        inputPWD.setFont(font_basic);
        registerPanel.add(inputPWD);

        JButton registerbtn = new JButton("회원가입");
        registerbtn.setBounds(125, 230, 150, 40);
        registerbtn.setFont(font_basic_bold);
        registerPanel.add(registerbtn);

        register.pack();
        register.setResizable(false);
        register.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 회원가입 버튼이 클릭된 경우
        if (e.getSource() == registerbtn) {
            String id = inputID.getText();
            String pw = inputPWD.getText();

            // 회원가입에 성공한 경우
            if (id.equals("admin") && pw.equals("1234")) {
                JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                // 로그인 성공 시 게임 화면으로 전환하는 코드 추가
                return;
            }

            // 회원가입에 실패한 경우
            JOptionPane.showMessageDialog(null, "정보를 올바르게 입력하세요.", "알림", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}