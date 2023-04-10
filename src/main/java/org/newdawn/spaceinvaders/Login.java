package org.newdawn.spaceinvaders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
//import com.google.firebase.auth.F
//import com.google.firebase.auth.UserRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;


public class Login extends JPanel implements ActionListener {

    JFrame login;

    JButton loginbtn;
    JTextField inputID;
    JTextField inputPWD;

    static Font font_basic = new Font("맑은 고딕",Font.PLAIN,12);
    static Font font_basic_bold = new Font("맑은 고딕",Font.BOLD,12);
    static Font font_basic_bold_size_14 = new Font("맑은 고딕",Font.BOLD,14);
    static Font font_basic_bold_size_16 = new Font("맑은 고딕",Font.BOLD,16);

    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();

    public boolean loginState = false;

    public void login() {

        login = new JFrame("로그인");

//		로그인 창
        JPanel loginPanel = (JPanel) login.getContentPane();
        loginPanel.setPreferredSize(new Dimension(400, 300));
        loginPanel.setLayout(null);

        login.setLocation(screenSize.width / 2 - 200, screenSize.height / 2 - 150);

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

        inputID = new JTextField();
        inputID.setBounds(170, 50, 150, 20);
        inputID.setFont(font_basic);
        loginPanel.add(inputID);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        loginPanel.add(PWDLabel);

        inputPWD = new JTextField();
        inputPWD.setBounds(170, 75, 150, 20);
        inputPWD.setFont(font_basic);
        loginPanel.add(inputPWD);

        loginbtn = new JButton("로그인");
        loginbtn.setBounds(125, 230, 150, 40);
        loginbtn.setFont(font_basic_bold);
        loginPanel.add(loginbtn);

        loginbtn.addActionListener(this);

        login.pack();
        login.setResizable(false);
        login.setVisible(true);

        login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


        public void actionPerformed(ActionEvent e) {
            // 로그인 버튼이 클릭된 경우
            if (e.getSource() == loginbtn) {

            String id = inputID.getText();
            String pw = inputPWD.getText();
            String encodedEmail = Base64.getEncoder().encodeToString(id.getBytes());

            // 빈칸으로 제출 시
            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(login, "아이디 또는 비밀번호를 입력하셔야 됩니다.", "아이디나 비번을 입력!", JOptionPane.DEFAULT_OPTION);
            }

            else {

                FirebaseDatabase userdatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = userdatabase.getReference();
                DatabaseReference usersRef = ref.child(encodedEmail);

                usersRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // 입력된 이메일을 키로 하는 데이터를 가져옵니다.
                        if (dataSnapshot.exists()) {
                            // 데이터가 존재하는 경우, 비밀번호를 가져옵니다.
                            String storedPassword = null;
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                storedPassword = postSnapshot.getValue(String.class);
                            }
                            if (pw.equals(storedPassword)) {
                                // 비밀번호가 일치하는 경우, 로그인 성공 처리를 합니다.
                                User user = dataSnapshot.getValue(User.class);
                                JOptionPane.showMessageDialog(login, "로그인 성공 ! "+user.name+"님, 반갑습니다.", "로그인 성공 !", JOptionPane.DEFAULT_OPTION);
                                login.dispose();
                                Window userWindow = new Window(user);
                                userWindow.setVisible(true);
//                                Window userWindow = new Window(user);

                                // 사용자 정보 가져옴
//                                User user = dataSnapshot.getValue(User.class);

//                                Game game = new Game(user);

                            } else {
                                // 비밀번호가 일치하지 않는 경우, 로그인 실패 처리를 합니다.
                                JOptionPane.showMessageDialog(login, "비밀번호가 일치하지 않습니다.", "비밀번호 불일치", JOptionPane.DEFAULT_OPTION);
                            }
                        } else {
                            // 입력된 이메일을 키로 하는 데이터가 존재하지 않는 경우, 로그인 실패 처리를 합니다.
                            JOptionPane.showMessageDialog(login, "등록되지 않은 이메일입니다.", "이메일 불일치", JOptionPane.DEFAULT_OPTION);
                        }
                    }
//                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 데이터 가져오기를 실패한 경우, 로그인 실패 처리를 합니다.
                        JOptionPane.showMessageDialog(login, "로그인에 실패했습니다.", "로그인 실패", JOptionPane.DEFAULT_OPTION);
                    }
                });
            }
        }
    }
}