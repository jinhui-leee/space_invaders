package org.newdawn.spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends JPanel implements ActionListener {

    JFrame register;

    JButton registerbtn;
    JTextField inputID;
    JTextField inputPWD;

    Font font_basic = new Font("맑은 고딕", Font.PLAIN, 12);
    Font font_basic_bold = new Font("맑은 고딕", Font.BOLD, 12);
    Font font_basic_bold_size_14 = new Font("맑은 고딕", Font.BOLD, 14);

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    public void register() {
        // Firebase 애플리케이션 초기화
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/key.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        register = new JFrame("회원가입");

        // 회원가입 창
        JPanel registerPanel = (JPanel) register.getContentPane();
        registerPanel.setPreferredSize(new Dimension(400, 300));
        registerPanel.setLayout(null);

        register.setLocation(screenSize.width / 2 - 200, screenSize.height / 2 - 150);

        Color loginBGC = new Color(225, 235, 239);
        registerPanel.setBackground(loginBGC);
        // 회원가입 창이 가장 상위 레벨에 뜨게 함
        register.setAlwaysOnTop(true);

        JLabel IDLabel = new JLabel("아이디");
        IDLabel.setHorizontalAlignment(JLabel.CENTER);
        IDLabel.setBounds(100, 50, 60, 20);
        IDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(IDLabel);

        inputID = new JTextField();
        inputID.setBounds(170, 50, 150, 20);
        inputID.setFont(font_basic);
        registerPanel.add(inputID);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(PWDLabel);

        inputPWD = new JTextField();
        inputPWD.setBounds(170, 75, 150, 20);
        inputPWD.setFont(font_basic);
        registerPanel.add(inputPWD);

        registerbtn = new JButton("회원가입");
        registerbtn.setBounds(125, 230, 150, 40);
        registerbtn.setFont(font_basic_bold);
        registerPanel.add(registerbtn);

        registerbtn.addActionListener(this);



        register.pack();
        register.setResizable(false);
        register.setVisible(true);
    }

    // 이메일 형식 검사
    public static boolean isValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 회원가입 버튼이 클릭된 경우
        if (e.getSource() == registerbtn) {
            String id = inputID.getText();
            String pwd = inputPWD.getText();

            // 빈칸으로 제출 시
            if (id.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(register, "내용을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }

            // 이메일 형식이 아닌 경우
            else if (!Register.isValid(id)) {
                JOptionPane.showMessageDialog(register, "이메일 형식이 올바르지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            }
            else{
                try {
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setEmail(inputID.getText())
                            .setPassword(inputPWD.getText());
                    UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

                    JOptionPane.showMessageDialog(register, "가입이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                } catch (FirebaseAuthException ex) {
                    JOptionPane.showMessageDialog(register, "가입 중 오류가 발생하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }
}
