package org.newdawn.spaceinvaders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;


public class Register extends JPanel implements ActionListener {

    JFrame registerFrame;

    JButton registerBtn;

    JTextField idTextField;
    JTextField pwTextField;
    JTextField nameTextField;

    Font font_basic = new Font("맑은 고딕", Font.PLAIN, 12);
    Font font_basic_bold = new Font("맑은 고딕", Font.BOLD, 12);
    Font font_basic_bold_size_14 = new Font("맑은 고딕", Font.BOLD, 14);

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    public void register() {

        registerFrame = new JFrame("회원가입");

        // 회원가입 창
        JPanel registerPanel = (JPanel) registerFrame.getContentPane();
        registerPanel.setPreferredSize(new Dimension(400, 300));
        registerPanel.setLayout(null);

        registerFrame.setLocation(screenSize.width / 2 - 200, screenSize.height / 2 - 150);

        Color loginBGC = new Color(225, 235, 239);
        registerPanel.setBackground(loginBGC);
        // 회원가입 창이 가장 상위 레벨에 뜨게 함
        registerFrame.setAlwaysOnTop(true);


        JLabel IDLabel = new JLabel("아이디");
        IDLabel.setHorizontalAlignment(JLabel.CENTER);
        IDLabel.setBounds(100, 50, 60, 20);
        IDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(IDLabel);
        idTextField = new JTextField();
        idTextField.setBounds(170, 50, 150, 20);
        idTextField.setFont(font_basic);
        registerPanel.add(idTextField);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(PWDLabel);
        pwTextField = new JTextField();
        pwTextField.setBounds(170, 75, 150, 20);
        pwTextField.setFont(font_basic);
        registerPanel.add(pwTextField);

        JLabel NameLabel = new JLabel("닉네임");
        NameLabel.setHorizontalAlignment(JLabel.CENTER);
        NameLabel.setBounds(100, 100, 60, 20);
        NameLabel.setFont(font_basic_bold_size_14);
        registerPanel.add(NameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(170, 100, 150, 20);
        nameTextField.setFont(font_basic);
        registerPanel.add(nameTextField);

        registerBtn = new JButton("회원가입");
        registerBtn.setBounds(125, 230, 150, 40);
        registerBtn.setFont(font_basic_bold);
        registerPanel.add(registerBtn);

        registerBtn.addActionListener(this);

        registerFrame.pack();
        registerFrame.setResizable(false);
        registerFrame.setVisible(true);
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
        if (e.getSource() == registerBtn) {
            String id = idTextField.getText();
            String pwd = pwTextField.getText();
            String name = nameTextField.getText();
            Integer gold = 0;
            String bestTime = "";
            String encodedEmail = Base64.getEncoder().encodeToString(id.getBytes());

            // 빈칸으로 제출 시
            if (id.isEmpty() || pwd.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "내용을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }

            // 이메일 형식이 아닌 경우
            else if (!Register.isValid(id)) {
                JOptionPane.showMessageDialog(registerFrame, "이메일 형식이 올바르지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            }

            else{
                try {
                    // Authentication에 저장
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setEmail(id)
                            .setPassword(pwd)
                            .setDisplayName(name)
                            .setDisplayName(gold.toString())
                            .setDisplayName(bestTime);
                    UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

                    // Realtime Database 저장
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    DatabaseReference usersRef = ref.child("Users").child(encodedEmail);

                    Map<String, User> users = new HashMap<>();
                    users.put(encodedEmail, new User(id, pwd, name, gold, bestTime));

                    usersRef.setValueAsync(users);

                    JOptionPane.showMessageDialog(registerFrame, "가입이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    registerFrame.dispose();
                } catch (FirebaseAuthException ex) {
                    JOptionPane.showMessageDialog(registerFrame, "가입 중 오류가 발생하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }
}