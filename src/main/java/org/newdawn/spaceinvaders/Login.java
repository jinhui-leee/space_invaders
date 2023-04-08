package org.newdawn.spaceinvaders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class Login {
    public static JFrame login;

    static Font font_basic = new Font("맑은 고딕",Font.PLAIN,12);
    static Font font_basic_bold = new Font("맑은 고딕",Font.BOLD,12);
    static Font font_basic_bold_size_14 = new Font("맑은 고딕",Font.BOLD,14);
    static Font font_basic_bold_size_16 = new Font("맑은 고딕",Font.BOLD,16);

    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();

    public static void login(){
        login = new JFrame("로그인/회원가입");

//		로그인/회원가입 창
        JPanel loginPanel = (JPanel) login.getContentPane();
        loginPanel.setPreferredSize(new Dimension(400, 300));
        loginPanel.setLayout(null);

        login.setLocation(screenSize.width/2 - 200, screenSize.height/2 - 150);

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

        JTextField inputID = new JTextField();
        inputID.setBounds(170, 50, 150, 20);
        inputID.setFont(font_basic);
        loginPanel.add(inputID);

        JLabel PWDLabel = new JLabel("비밀번호");
        PWDLabel.setHorizontalAlignment(JLabel.CENTER);
        PWDLabel.setBounds(100, 75, 60, 20);
        PWDLabel.setFont(font_basic_bold_size_14);
        loginPanel.add(PWDLabel);

        JTextField inputPWD = new JTextField();
        inputPWD.setBounds(170, 75, 150, 20);
        inputPWD.setFont(font_basic);
        loginPanel.add(inputPWD);

        JButton loginbtn = new JButton("로그인");
        loginbtn.setBounds(150, 200, 100, 40);
        loginbtn.setFont(font_basic_bold);
        loginPanel.add(loginbtn);

        JButton registerbtn = new JButton("회원가입");
        registerbtn.setBounds(150, 250, 100, 40);
        registerbtn.setFont(font_basic_bold);
        loginPanel.add(registerbtn);

        login.pack();
        login.setResizable(false);
        login.setVisible(true);



        //로그인 버튼을 눌렀을때
        loginbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String id = inputID.getText().trim();
                String pw = inputPWD.getText().trim();
                String login = "";

                if(id.length()==0 || pw.length()==0) {
                    JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 입력하셔야 됩니다.", "아이디나 비번을 입력!", JOptionPane.DEFAULT_OPTION);
                    return;
                }

                //텍스트 파일에 저장된 모든 글자를 가져온다 | 구분자로 아이디와 비번이 맞는게 있다면 로그인 시켜주면 된다.
                try{
                    BufferedReader reader = new BufferedReader(new FileReader("C:\\java_member\\member.txt"));

                    String str;
                    ArrayList<String> txtmember = new ArrayList<>();
                    while ((str = reader.readLine()) != null) {
                        txtmember.add(str);
                    }

                    reader.close();

                    //memberlist에 아이디와 비밀번호 저장하기
                    HashMap<String,String> memberlist = new HashMap<>();

                    for(int i=0; i<txtmember.size(); i++) {
                        // | 구분자 기준으로 잘라난 텍스트를 memberlist에 넣어주기.
                        String[] tempresult = txtmember.get(i).toString().split("\\|");
                        memberlist.put(tempresult[0],tempresult[1]);
                    }

                    //txt 파일에서 가져온 아이디 비번과 입력한 아이디 비번이 맞는지 확인
                    for ( String key : memberlist.keySet() ) {
                        if(id.equals(key.trim()) && pw.equals(memberlist.get(key))) {
                            //로그인성공!
                            login = "성공";
                        }
                    }

                }catch(Exception errmsg){
                    errmsg.printStackTrace();
                }

                if(login.equals("성공")) {
                    JOptionPane.showMessageDialog(null, "로그인 성공", "로그인 확인!", JOptionPane.DEFAULT_OPTION);
                    return;
                }else {
                    JOptionPane.showMessageDialog(null, "로그인 실패", "계정 정보를 확인해 주세요.", JOptionPane.DEFAULT_OPTION);
                }
            }
        });
    }
}