package org.newdawn.spaceinvaders;

import com.google.firebase.database.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;


public class Login extends JPanel implements ActionListener {

    JFrame loginFrame;

    JButton loginBtn;
    JTextField idTextField;
    JTextField pwTextField;

    static Font font_basic = new Font("맑은 고딕",Font.PLAIN,12);
    static Font font_basic_bold = new Font("맑은 고딕",Font.BOLD,12);
    static Font font_basic_bold_size_14 = new Font("맑은 고딕",Font.BOLD,14);

    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();


    public void login() {

        loginFrame = new JFrame("로그인");

//		로그인 창
        JPanel loginPanel = (JPanel) loginFrame.getContentPane();
        loginPanel.setPreferredSize(new Dimension(400, 300));
        loginPanel.setLayout(null);

        loginFrame.setLocation(screenSize.width / 2 - 200, screenSize.height / 2 - 150);

        Color loginBGC = new Color(225, 235, 239);
        loginPanel.setBackground(loginBGC);

        JLabel GuestLabel = new JLabel("게스트로 플레이를 원할 시 창을 닫아주세요 !");
        GuestLabel.setHorizontalAlignment(JLabel.CENTER);
        GuestLabel.setBounds(75, 15, 250, 20);
        GuestLabel.setFont(font_basic_bold);
        loginPanel.add(GuestLabel);

        JLabel IDLabel = new JLabel("아이디");
        IDLabel.setHorizontalAlignment(JLabel.CENTER);
        IDLabel.setBounds(100, 50, 60, 20);
        IDLabel.setFont(font_basic_bold_size_14);
        loginPanel.add(IDLabel);

        idTextField = new JTextField();
        idTextField.setBounds(170, 50, 150, 20);
        idTextField.setFont(font_basic);
        loginPanel.add(idTextField);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        loginPanel.add(PWDLabel);

        pwTextField = new JTextField();
        pwTextField.setBounds(170, 75, 150, 20);
        pwTextField.setFont(font_basic);
        loginPanel.add(pwTextField);

        loginBtn = new JButton("로그인");
        loginBtn.setBounds(125, 230, 150, 40);
        loginBtn.setFont(font_basic_bold);
        loginPanel.add(loginBtn);

        loginBtn.addActionListener(this);

        loginFrame.pack();
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);

        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 로그인 버튼이 클릭된 경우
        if (e.getSource() == loginBtn) {

            String id = idTextField.getText();
            String pw = pwTextField.getText();
            String encodedEmail = Base64.getEncoder().encodeToString(id.getBytes());

            // 빈칸으로 제출 시
            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "아이디 또는 비밀번호를 입력하셔야 됩니다.", "아이디나 비번을 입력!", JOptionPane.DEFAULT_OPTION);
            }
            else {
                // 사용자 정보 받아오기
                FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = userDatabase.getReference();
                DatabaseReference usersRef = ref.child("Users").child(encodedEmail);

                usersRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // 입력된 이메일을 키로 하는 데이터를 가져옵니다.
                        if (dataSnapshot.exists()) {
                            User user = null;
                            // 데이터가 존재하는 경우, 비밀번호를 가져옵니다.
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                user = dataSnapshot.getValue(User.class);
                            }
                            if (pw.equals(user.getPassword())) {
                                // 비밀번호가 일치하는 경우, 로그인 성공 처리를 합니다.
                                JOptionPane.showMessageDialog(loginFrame, "로그인 성공 ! " + user.getName() + "님, 반갑습니다.", "로그인 성공 !", JOptionPane.DEFAULT_OPTION);
                                loginFrame.dispose();
                                // Window로 user 정보 전달
                                Framework framework = new Framework(user);

                            } else {
                                // 비밀번호가 일치하지 않는 경우, 로그인 실패 처리를 합니다.
                                JOptionPane.showMessageDialog(loginFrame, "비밀번호가 일치하지 않습니다.", "비밀번호 불일치", JOptionPane.DEFAULT_OPTION);
                            }
                        } else {
                            // 입력된 이메일을 키로 하는 데이터가 존재하지 않는 경우, 로그인 실패 처리를 합니다.
                            JOptionPane.showMessageDialog(loginFrame, "등록되지 않은 이메일입니다.", "이메일 불일치", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                    //                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 데이터 가져오기를 실패한 경우, 로그인 실패 처리를 합니다.
                        JOptionPane.showMessageDialog(loginFrame, "로그인에 실패했습니다.", "로그인 실패", JOptionPane.DEFAULT_OPTION);
                    }
                });
            }
        }
    }
}