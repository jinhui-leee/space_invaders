package org.newdawn.spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import org.newdawn.spaceinvaders.entity.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic.
 *
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 *
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 *
 * @author Kevin Glass
 */
public class Game extends Canvas implements ActionListener, WindowListener
{
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = ;
    /** The stragey that allows us to use accelerate page flipping */
    private final BufferStrategy strategy;
    int timer;
    int timecheck;
    int min=0;
    int second=0;

    private final JLabel[] lifeLabel;

    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;

    /** The list of all the entities that exist in our game */
    private final ArrayList entities = new ArrayList();

    /** The list of entities that need to be removed from the game this loop */
    private final ArrayList removeList = new ArrayList();

    /** The entity representing the player */
    private ShipEntity ship;
    private Entity item;
    private Entity obstacle;
    private int itemnum=0;

    private boolean itemact=false;

    /** The speed at which the player's ship should move (pixels/sec) */
    private double moveSpeed = 300;

    /** The time at which last fired a shot */
    private long lastFire = 0;

    /** The interval between our players shot (ms) */
    private long firingInterval = 500;

    /** The number of aliens left on the screen */
    private int alienCount;

    /** The message to display which waiting for a key press */
    private String message = "";

    /** True if we're holding up game play until a key has been pressed */
    private boolean waitingForKeyPress = true;

    /** True if the left cursor key is currently pressed */
    private boolean leftPressed = false;

    /** True if the right cursor key is currently pressed */
    private boolean rightPressed = false;

    private boolean upPressed = false;

    private boolean downPressed = false;

    /** True if we are firing */
    private boolean firePressed = false;

    /** True if game logic needs to be applied this loop, normally as a result of a game event */
    private boolean logicRequiredThisLoop = false;

    /** The last time at which we recorded the frame rate */
    private long lastFpsTime;

    /** The current number of frames recorded */
    private int fps;

    /** The normal title of the game window */
    private final String windowTitle = "Space Invaders 102";

    /** The game window that we'll update with the frame count */
    private final JFrame container;

    /**스테이지 레벨*/
    private int stageLevel = 0;

    private final int bossStageLevel = 5;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    /** 글자 크기 */
    static Font font_basic = new Font("맑은 고딕",Font.PLAIN,12);
    static Font font_basic_bold = new Font("맑은 고딕",Font.BOLD,12);
    static Font font_basic_bold_size_14 = new Font("맑은 고딕",Font.BOLD,14);
    static Font font_basic_bold_size_16 = new Font("맑은 고딕",Font.BOLD,16);


    private final JButton audioBtn;

    private final Music music;

    private final ImageIcon changeIconAudioOff;
    private final ImageIcon changeIconAudioOn;

    public ImageIcon changeIconGetGold;

    private static User user;

    /** 골드 초기값 = 0 */
    private int getGoldCnt = 0;
    private final JLabel getGoldLabel;

    /** 경과 시간 초기값 = 0 */
    private final int time = 0;
    private final JLabel timeLabel;

    long startTime;

    private long timeRecord;

    private final int gameDifficulty;

    private final BufferedImage storeBackgroundImage;

    private final String btnColor;

    private JButton []itemPurchaseBtn;

    private JPanel panel;

    private int shotSpeed = -330;

    //총알 발사 간격 아이템 사용 이후, 원래 총알 간격으로 돌아가기 위한 총알 간격
    private long defaultFiringInterval = 500;
    private JLabel[] itemStoreLabel;
    private int shipLife;

    private int []itemPurchaseCnt;


    /**
     * Construct our game and set it running.
     */
    public Game(int gameDifficulty, User user, String themeColor, BufferedImage image) {
        // create a frame to contain our game
        container = new JFrame("Space Invaders 102");

        // get hold the content of the frame and set up the resolution of the game
        //JPanel
        panel = (JPanel) container.getContentPane();

        //panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800,600));


        // setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
        setBounds(0,0,800,600);
        container.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);

        this.btnColor = themeColor;
        this.storeBackgroundImage = image;

        // Music 객체 받아오고 재생
        music = new Music();
        music.playMusic();

        // 음악 재생 및 정지
        // 이미지 로드
        ImageIcon audioOn = new ImageIcon("src/main/resources/images/audioOn.png");
        ImageIcon audioOff = new ImageIcon("src/main/resources/images/audioOff.png");
        Image imgAudioOff = audioOff.getImage();
        Image imgAudioOn = audioOn.getImage();

        // 이미지 크기 변경
        Image changeImgAudioOff = imgAudioOff.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        ImageIcon changeIconAudioOff = new ImageIcon(changeImgAudioOff);

        Image changeAudioOn = imgAudioOn.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        ImageIcon changeIconAudioOn = new ImageIcon(changeAudioOn);

        this.changeIconAudioOff = changeIconAudioOff;
        this.changeIconAudioOn = changeIconAudioOn;

        // 음악 재생 버튼 생성
        audioBtn = new JButton(changeIconAudioOff);
        audioBtn.setBounds(753,20,30,30);
        audioBtn.addActionListener(this);
        panel.add(audioBtn);

        // 획득 골드 아이콘 표시
        ImageIcon getGold = new ImageIcon("src/main/resources/images/gold.png");
        Image imgGetGold = getGold.getImage();

        Image changeImgGetGold = imgGetGold.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        ImageIcon changeIconGetGold = new ImageIcon(changeImgGetGold);

        this.changeIconGetGold = changeIconGetGold;

        JLabel imageGetGoldLabel = new JLabel(changeIconGetGold);
        imageGetGoldLabel.setBounds(650,25,20,20);
        panel.add(imageGetGoldLabel);

        //생명 표시 (최대 5개)
        URL shipLifeUrl = getClass().getResource("/icon/itemIcon4.png");
        ImageIcon shipLifeImageIcon = new ImageIcon(shipLifeUrl);
        Image shipLifeImage = shipLifeImageIcon.getImage();

        shipLifeImage = shipLifeImage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        shipLifeImageIcon = new ImageIcon(shipLifeImage);

        lifeLabel = new JLabel[5];

        for (int i=0; i<5; i++) {
            lifeLabel[i] = new JLabel(shipLifeImageIcon);
            lifeLabel[i].setBounds(525 + i*25,25,20,20);
            panel.add(lifeLabel[i]);
            lifeLabel[i].setVisible(false);
        }
        lifeLabel[4].setVisible(true);



        //아이템 구입 버튼
        itemPurchaseBtn = new JButton[5];
        for (int i=0; i<5; i++) {
            itemPurchaseBtn[i] = new JButton("구매하기");
            itemPurchaseBtn[i].setBounds(170 + i*120, 430,100,40);
        }
        itemPurchaseBtn[4] = new JButton("NEXT");
        itemPurchaseBtn[4].setBounds(350 , 530,100,40);
        for (int i=0; i<itemPurchaseBtn.length; i++) {
            itemPurchaseBtn[i].setForeground(Color.WHITE);
            itemPurchaseBtn[i].setBackground(Color.decode(getBtnColor()));
            itemPurchaseBtn[i].addActionListener(this);
            panel.add(itemPurchaseBtn[i]);
            itemPurchaseBtn[i].setVisible(false);
        }

        //아이템 상점 이미지
        URL []itemStoreUrl = new URL[4];
        ImageIcon[] itemStoreImage = new ImageIcon[4];
        itemStoreLabel = new JLabel[4];

        itemStoreUrl[0] = getClass().getResource("/icon/itemIcon1.png");
        itemStoreUrl[1] = getClass().getResource("/icon/itemIcon2.png");
        itemStoreUrl[2] = getClass().getResource("/icon/itemIcon3.png");
        itemStoreUrl[3] = getClass().getResource("/icon/itemIcon4.png");

        for (int i=0; i<itemStoreUrl.length; i++) {
            itemStoreImage[i] = new ImageIcon(itemStoreUrl[i]);
            itemStoreLabel[i] = new JLabel(itemStoreImage[i]);
            itemStoreLabel[i].setBounds(itemPurchaseBtn[i].getX() + itemPurchaseBtn[0].getWidth() / 2 - itemStoreImage[i].getIconWidth()/2, 230, 98, 98);
            panel.add(itemStoreLabel[i]);
            itemStoreLabel[i].setVisible(false);
        }


        // 사용자 모드
        if (user!=null){
            // 사용자 정보 받아오기
            Game.user = user;

            // 로그인한 사용자 표시 (로그인 시 사용한 ID를 통해 파이어베이스에서 사용자 정보 가져옴)
            JLabel getUserNameLabel = new JLabel(user.name);
            getUserNameLabel.setBounds(550,25,50,20);
            panel.add(getUserNameLabel);

            // 획득 골드 숫자 표시
            getGoldLabel = new JLabel();
            getGoldLabel.setText(user.gold.toString());
            Color goldBGC = new Color(210, 216, 217);
            getGoldLabel.setBackground(goldBGC);
            getGoldLabel.setBounds(680,25,50,20);
            panel.add(getGoldLabel);
        }

        else{
            // 획득 골드 숫자 표시
            getGoldLabel = new JLabel();
            getGoldLabel.setText(String.valueOf(getGoldCnt));
            Color goldBGC = new Color(210, 216, 217);
            getGoldLabel.setBackground(goldBGC);
            getGoldLabel.setBounds(680,25,50,20);
            panel.add(getGoldLabel);
        }

        // 경과 시간 표시
        // TODO 시간 가운데 정렬 바로 반영
        timeLabel = new JLabel();
        timeLabel.setFont(font_basic_bold);
        timeLabel.setText(Integer.toString(time));
        timeLabel.setForeground(Color.white);
        timeLabel.setBackground(Color.BLUE);
        timeLabel.setBounds(20,20,80,25);
        panel.add(timeLabel);

        itemPurchaseCnt = new int[5];
        Arrays.fill(itemPurchaseCnt, 0);

        // 윈도우 리스너 이벤트 add
        addWindowListener(this);




        panel.add(this);


        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        //setIgnoreRepaint(true);
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        this.gameDifficulty = gameDifficulty;

        initEntities();
    }



    // 음악 재생 및 정지
    public void actionPerformed(ActionEvent e) {
        // 오디오 버튼 클릭 시 이미지 변경 및 소리 조절
        if (e.getSource() == audioBtn) {
            if (music.isPlaying()) {
                music.stopMusic();
                audioBtn.setIcon(this.changeIconAudioOn);
//                audioBtn.setFocusable(false);
            } else {
                music.playMusic();
                System.out.print(music.isPlaying());
                audioBtn.setIcon(this.changeIconAudioOff);
//                audioBtn.setFocusable(false);

            }
        }
        //아이템 구매 : ship +20 속도 증가, 20원
        else if (e.getSource() == itemPurchaseBtn[0]) {

            if (user != null) {
                if (user.gold >= 20) {
                    user.gold -= 20;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(user.gold)));
                    moveSpeed += 20;
                    itemPurchaseCnt[0]++;
                    itemPurchaseCnt[4]++;
                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
            else {
                if (getGoldCnt >= 20) {
                    getGoldCnt -= 20;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(getGoldCnt)));
                    moveSpeed += 20;
                    itemPurchaseCnt[0]++;
                    itemPurchaseCnt[4]++;


                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }


        }
        //아이템 구매 : 총알 속도 -5 증가, 20원
        else if (e.getSource() == itemPurchaseBtn[1]) {
            if (user != null) {
                if (user.gold >= 20) {
                    user.gold -= 20;
                    shotSpeed -= 5;
                    itemPurchaseCnt[1]++;
                    itemPurchaseCnt[4]++;

                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(user.gold)));


                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
            else {
                if (getGoldCnt >= 20) {
                    getGoldCnt -= 20;
                    shotSpeed -= 5;
                    itemPurchaseCnt[1]++;
                    itemPurchaseCnt[4]++;

                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(getGoldCnt)));
                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
        }
        //아이템 구매 : 총알 발사 간격 -10, 50원
        else if (e.getSource() == itemPurchaseBtn[2]) {
            if (user != null) {
                if (user.gold >= 50) {
                    user.gold -= 50;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(user.gold)));
                    firingInterval-=10;
                    itemPurchaseCnt[2]++;
                    itemPurchaseCnt[4]++;


                    defaultFiringInterval = firingInterval;
                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
            else {
                if (getGoldCnt >= 50) {
                    getGoldCnt -= 50;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(getGoldCnt)));
                    firingInterval-=10;
                    itemPurchaseCnt[2]++;
                    itemPurchaseCnt[4]++;


                    defaultFiringInterval = firingInterval;


                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
        }
        //아이템 구매 : 생명 +1 200원
        else if (e.getSource() == itemPurchaseBtn[3]) {
            if (ship.getLife() >= 5) {
                JOptionPane.showMessageDialog(null, "생명은 최대 5개입니다.");
            }
            else if (user != null) {
                if (user.gold >= 200) {
                    user.gold -= 200;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(user.gold)));
                    shipLife = ship.getLife()+1;
                    ship.setLife(shipLife);
                    itemPurchaseCnt[3]++;
                    itemPurchaseCnt[4]++;

                    lifeLabel[5-ship.getLife()].setVisible(true);

                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }
            else {
                if (getGoldCnt >= 200) {
                    getGoldCnt -= 200;
                    SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(getGoldCnt)));
                    shipLife = ship.getLife()+1;
                    ship.setLife(shipLife);
                    itemPurchaseCnt[3]++;
                    itemPurchaseCnt[4]++;

                    lifeLabel[5-ship.getLife()].setVisible(true);


                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }

        }
        //다음 스테이지 넘어가기
        else if (e.getSource() == itemPurchaseBtn[4]) {
            gameRunning = true;
            btnManager();
            startGame();

        }
    }

    public void btnManager() {
        if (gameRunning) {
            for (int i=0; i<itemPurchaseBtn.length; i++) {
                itemPurchaseBtn[i].setVisible(false);
            }
            for (int i=0; i<itemPurchaseBtn.length-1; i++) {
                itemStoreLabel[i].setVisible(false);
            }
        }
        else {
            for (int i=0; i<itemPurchaseBtn.length; i++) {
                itemPurchaseBtn[i].setVisible(true);
            }
            for (int i=0; i<itemPurchaseBtn.length-1; i++) {
                itemStoreLabel[i].setVisible(true);
            }

        }
    }
    @Override
    public void windowOpened(WindowEvent e) {
        // 윈도우 창이 열릴 때 처리할 내용
//		music.playMusic();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        // 윈도우 창이 닫힐 때 처리할 내용
//		if (music != null) {
        if (music.isPlaying()) {
            music.stopMusic();
//			audioBtn.setIcon(this.changeIconAudioOn);
        }
//			music.stopMusic();
//		}
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // 윈도우 창이 닫힌 후 처리할 내용
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // 윈도우 창이 최소화될 때 처리할 내용
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // 윈도우 창이 최소화에서 복원될 때 처리할 내용
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // 윈도우 창이 활성화될 때 처리할 내용
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // 윈도우 창이 비활성화될 때 처리할 내용
    }

    public void requestFocus() {
        container.requestFocus();
    }



    /**
     * Start a fresh game, this should clear out any old data and
     * create a new set.
     */
    private void startGame() {
        // clear out any existing entities and intialise a new set
        entities.clear();
        initEntities();
        CreateItemEntities();
        resetItem();

        if (stageLevel == 0) {
            for (int i=0; i<4; i++) lifeLabel[i].setVisible(false);
            lifeLabel[4].setVisible(true);
            shipLife = 1;
        }

        ship.setLife(shipLife);
        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        firePressed = false;
    }

    /**
     * Initialise the starting state of the entities (ship and aliens). Each
     * entitiy will be added to the overall list of entities in the game.
     */
    private void initEntities() {
        // create the player ship and place it roughly in the center of the screen
        if (Framework.character == 0)
        {
            ship = new ShipEntity(this,"images/ship.gif",370,550);

        }
        else if (Framework.character == 1)
        {
            ship = new ShipEntity(this,"images/ship2.png",370,550);

        }
        else if (Framework.character == 2)
        {
            ship = new ShipEntity(this,"images/ship3.png",370,550);

        }
        else if (Framework.character == 3)
        {
            ship = new ShipEntity(this,"images/ship4.png",370,550);

        }
        else if (Framework.character == 4)
        {
            ship = new ShipEntity(this,"images/ship5.png",370,550);

        }
        ship.setLife(shipLife);
        entities.add(ship);


        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        if (stageLevel < bossStageLevel) {
            alienCount = 0;
            //적(외계인) 생성 : 12x5 크기
            int alienRow, alienX;
            if (gameDifficulty == 0) {
                alienRow = 4;
                alienX = 6;
            }
            else if (gameDifficulty == 1) {
                alienRow = 5;
                alienX = 7;
            }
            else {
                alienRow = 6;
                alienX = 8;
            }


            for (int row = 0; row < alienRow+stageLevel; row++) {
                for (int x = 0; x < alienX+stageLevel; x++) {
                    Entity alien = new AlienEntity(this,100+(x*50),(50)+row*30);
                    entities.add(alien);
                    alienCount++;
                }
            }
        }
        else { //보스전
            alienCount = 0;

            Entity bossAlien = new BossAlienEntity(this, 100, 50);
            entities.add(bossAlien);
            //ObstacleEntity obstacle = new ObstacleEntity(this, "images/Obstacle.png", (int) (Math.random() * 750), 10);

            alienCount++;
        }


    }
    private void CreateItemEntities(){
        item=new ItemEntity(this,"images/item.gif");
        entities.add(item);

    }

    private void CreateObstacle(){

        obstacle=new ObstacleEntity(this,"images/obstacle.png",(int)(Math.random()*750),10);
        entities.add(obstacle);
    }


    /**
     * Notification from a game entity that the logic of the game
     * should be run at the next opportunity (normally as a result of some
     * game event)
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    /**
     * Remove an entity from the game. The entity removed will
     * no longer move or be drawn.
     *
     * @param entity The entity that should be removed
     */
    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    /**
     * Notification that the player has died.
     */
    public void notifyDeath() {
        message = "Oh no! They got you, try again?" + "  stage level : " + stageLevel;
        stageLevel = 0;
        waitingForKeyPress = true;
    }

    /**
     * Notification that the player has won since all the aliens
     * are dead.
     */
    public void notifyWin() {
        message = "Well done! You Win!" + "  stage level : " + stageLevel ;
        waitingForKeyPress = true;
        stageLevel++;
        if (stageLevel > bossStageLevel) {
            //쉬움
            if (gameDifficulty == 0){
                JOptionPane.showMessageDialog(null, "쉬움 클리어! ");
            }
            //보통
            else if (gameDifficulty == 1) {
                JOptionPane.showMessageDialog(null, "보통 클리어!");
            }
            //어려움
            else { //gameDifficulty ==2
                JOptionPane.showMessageDialog(null, "어려움 클리어!");
            }
        }
        //gameRunning = false;
    }

    /**
     * Notification that an alien has been killed
     */
    public void notifyAlienKilled() {
        // reduce the alient count, if there are none left, the player has won!
        alienCount--;
        if (user != null) {
            String encodedEmail = Base64.getEncoder().encodeToString(user.email.getBytes());

            user.gold++;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            DatabaseReference userRef = ref.child(encodedEmail);

            Map<String, Object> updates = new HashMap<>();
            updates.put("/" + encodedEmail + "/gold", user.gold);
            userRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
//						Log.d(TAG, "Data could not be saved: " + databaseError.getMessage());
                    } else {
//						Log.d(TAG, "Data saved successfully.");
                    }
                }
            });
            // update the UI with the new gold value
            SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(user.gold)));
        }
        else{
            getGoldCnt++;
            SwingUtilities.invokeLater(() -> getGoldLabel.setText(Integer.toString(getGoldCnt)));
        }
        if (alienCount <= 0) {
            notifyWin();
        }

        // if there are still some aliens left then they all need to get faster, so
        // speed up all the existing aliens
        for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }

    /**
     * Attempt to fire a shot from the player. Its called "try"
     * since we must first check that the player can fire at this
     * point, i.e. has he/she waited long enough between shots
     */
    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this,"images/shot.gif",ship.getX()+10,ship.getY()-30);
        shot.setMoveSpeed(shotSpeed);
        entities.add(shot);
        // 총알 발사 시 효과음 재생
        Music.shotAudio();
    }
    public void itemFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();


        for ( int i=0; i<5;i++){
            ShotEntity shot_item = new ShotEntity(this,"images/shot.gif",ship.getX()+(i*60)-60,ship.getY()-30);
            entities.add(shot_item);


        }



    }


    public void shotShip() {

        for (int i=0; i<3; i++) {
            BossShotEntity shot = new BossShotEntity(this,"images/stone_boss_shot.png",ship.getX()+(i*30-30),100);
            entities.add(shot);
        }

    }
    public void useItem(){



        int itemrandomnum=(int)(Math.random()*2)+1;

        if(itemrandomnum==1){
            firingInterval=250;
        }
        else if (itemrandomnum==2){
            itemact=true;
        }


    }

    public void resetItem(){
        firingInterval=500;
        itemact=false;
    }
    public void AddObstacle(){
        ObstacleEntity obstacle = new ObstacleEntity(this, "images/obstacle.png", 10, (int) (Math.random() * 450));
        entities.add(obstacle);

    }

    /**
     * The main game loop. This loop is running during all game
     * play as is responsible for the following activities:
     * <p>
     * - Working out the speed of the game loop to update moves
     * - Moving the game entities
     * - Drawing the screen contents (entities, text)
     * - Updating game events
     * - Checking Input
     * <p>
     */
    public static String formatTime(long delta) {
        long millis = delta % 1000;
        delta /= 1000;
        long seconds = delta % 60;
        delta /= 60;
        long minutes = delta % 60;
        String time = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);
        return time;
    }

    public void gameLoop() {

        long lastLoopTime = SystemTimer.getTime();
        long startTime = lastLoopTime;

        // keep looping round til the game ends

        while (true) {
            if (gameRunning) {
                // 게임 실행 중인 코드
                while (gameRunning) {
                    // work out how long its been since the last update, this
                    // will be used to calculate how far the entities should
                    // move this loop
                    long delta = SystemTimer.getTime() - lastLoopTime;
                    String formattedTime = formatTime(SystemTimer.getTime() - startTime);
//                    System.out.println("경과 시간 : " + formattedTime);

                    lastLoopTime = SystemTimer.getTime();

                    // update the frame counter
                    lastFpsTime += delta;
                    fps++;
                    timer++;
                    if(timer>10000){
                        timer=1;
                    }

                    // update our FPS counter if a second has passed since
                    // we last recorded
                    if (lastFpsTime >= 1000) {
                        container.setTitle(windowTitle+" (FPS: "+fps+")");
                        lastFpsTime = 0;
                        fps = 0;
                    }

                    // Get hold of a graphics context for the accelerated
                    // surface and blank it out
                    //배경색
                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0,0,800,600);
                    g.setColor(Color.white);
                    g.drawString("남은 적 수 "+String.valueOf(alienCount),10,30);

                    // cycle round asking each entity to move itself
                    //, 적 무리 만들고 움직이게 하기
                    if (!waitingForKeyPress) {
                        for (int i=0; i<entities.size(); i++) {
                            //i번째 entities 가져온다
                            Entity entity = (Entity) entities.get(i);
                            entity.move(delta);
                        }
                    }

                    // cycle round drawing all the entities we have in the game
                    for (int i=0;i<entities.size();i++) {
                        Entity entity = (Entity) entities.get(i);

                        entity.draw(g);
                    }

                    // brute force collisions, compare every entity against
                    // every other entity. If any of them collide notify
                    // both entities that the collision has occured
                    for (int p=0; p<entities.size(); p++) {
                        for (int s=p+1; s<entities.size(); s++) {
                            Entity me = (Entity) entities.get(p);
                            Entity him = (Entity) entities.get(s);

                            if (me.collidesWith(him)) {

                                me.collidedWith(him);
                                him.collidedWith(me);

                                if ((me instanceof ShipEntity  || him instanceof ShipEntity) &&
                                        (me instanceof AlienEntity || him instanceof AlienEntity
                                                || me instanceof BossAlienEntity || him instanceof BossAlienEntity
                                                || me instanceof BossShotEntity || him instanceof BossShotEntity)) {
                                    lifeLabel[4-ship.getLife()].setVisible(false);
                                }

                            }
                        }
                    }

                    // remove any entity that has been marked for clear up
                    entities.removeAll(removeList);
                    removeList.clear();

                    // if a game event has indicated that game logic should
                    // be resolved, cycle round every entity requesting that
                    // their personal logic should be considered.
                    if (logicRequiredThisLoop) {
                        for (int i=0;i<entities.size();i++) {
                            Entity entity = (Entity) entities.get(i);
                            entity.doLogic();
                        }

                        logicRequiredThisLoop = false;
                    }

                    // if we're waiting for an "any key" press then draw the
                    // current message
                    //아무키 누르는 거 대기 중(게임 시작 전, 게임 끝난 후)
                    if (waitingForKeyPress) {
                        g.setColor(Color.white);
                        g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
                        g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
                    }

                    // finally, we've completed drawing so clear up the graphics
                    // and flip the buffer over
                    g.dispose();
                    strategy.show();

                    // resolve the movement of the ship. First assume the ship
                    // isn't moving. If either cursor key is pressed then
                    // update the movement appropraitely
                    ship.setHorizontalMovement(0);
                    ship.setVerticalMovement(0);

                    if ((leftPressed) && (!rightPressed)) {
                        ship.setHorizontalMovement(-moveSpeed);
                    } else if ((rightPressed) && (!leftPressed)) {
                        ship.setHorizontalMovement(moveSpeed);
                    }

                    if ((upPressed)&&(!downPressed)) {
                        ship.setVerticalMovement(-moveSpeed);
                    }
                    else if ((downPressed)&&(!upPressed)) {
                        ship.setVerticalMovement(moveSpeed);
                    }

                    // if we're pressing fire, attempt to fire
                    if (firePressed) {
                        if(itemact){
                            itemFire();
                        }
                        else {
                            tryToFire();
                        }
                        if (stageLevel >= bossStageLevel) {
                            shotShip();
                        }

                    }
                    if(timer%200==0)
                    {
                        AddObstacle();
                    }



                    // we want each frame to take 10 milliseconds, to do this
                    // we've recorded when we started the frame. We add 10 milliseconds
                    // to this and then factor in the current time to give
                    // us our final value to wait for
                    SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
                }
            } else {
                // 스테이지 종료 후, 아이템 상점
                if (stageLevel <= bossStageLevel) {
                    drawStoreMenu();
                }
                else {
                    drawGameClear();
                }
            }
        }
    }

    public void drawGameClear() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);

        URL fontUrl = getClass().getResource("/font/Cafe24Danjunghae.ttf");

        Font font1 = null;
        Font font2 = null;
        try {
            assert fontUrl != null;
            font1 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 40);
            font2 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 20);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        g.setFont(font1);
        g.drawString("게임 결과",(800-g.getFontMetrics().stringWidth("게임 결과"))/2,150);

        g.setFont(font2);

        //클리어한 난이도
        if (gameDifficulty == 0)
        {
            g.drawString("난이도 : 쉬움",800-g.getFontMetrics().stringWidth("난이도 : 쉬움")/2, 200);

        }
        else if (gameDifficulty == 1) {
            g.drawString("난이도 : 보통",800-g.getFontMetrics().stringWidth("난이도 : 보통")/2, 200);

        }
        else {
            g.drawString("난이도 : 어려움",800-g.getFontMetrics().stringWidth("난이도 : 어려움")/2, 200);

        }

        //클리어하는데 걸린 시간


        //아이템 사용 종류와 개수
        g.drawString("총 구매 아이템 개수 : " + itemPurchaseCnt[4], 800-g.getFontMetrics().stringWidth("총 구매 아이템 개수 : " + itemPurchaseCnt[4])/2, 280);
        g.drawString("ship 속도 증가 : " + itemPurchaseCnt[0],800-g.getFontMetrics().stringWidth("ship 속도 증가 : " + itemPurchaseCnt[0])/2, 300);
        g.drawString("총알 속도 증가 : " + itemPurchaseCnt[1],800-g.getFontMetrics().stringWidth("난이도 : 보통")/2, 320);
        g.drawString("총알 발사 간격 감소 : " + itemPurchaseCnt[2],800-g.getFontMetrics().stringWidth("총알 발사 간격 감소 : " + itemPurchaseCnt[2])/2, 340);
        g.drawString("생명 추가 : " + itemPurchaseCnt[3],800-g.getFontMetrics().stringWidth("생명 추가 : " + itemPurchaseCnt[3])/2, 360);


        //현재 랭킹

    }

    public void drawStoreMenu() {
        // 이미지1을 배경으로 하는 화면 그리기
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);

        g.drawImage(getStoreBackgroundImage(),0,0,800,600,null);
        g.drawString("ship 속도", itemPurchaseBtn[0].getX() + itemPurchaseBtn[0].getWidth() / 2 - g.getFontMetrics().stringWidth("ship 속도")/2, 360);
        g.drawString("+20", itemPurchaseBtn[0].getX() + itemPurchaseBtn[0].getWidth() / 2 - g.getFontMetrics().stringWidth("+20")/2, 380);
        g.drawString("20코인", itemPurchaseBtn[0].getX() + itemPurchaseBtn[0].getWidth() / 2 - g.getFontMetrics().stringWidth("20코인")/2, 400);

        g.drawString("총알 속도", itemPurchaseBtn[1].getX() + itemPurchaseBtn[1].getWidth() / 2 - g.getFontMetrics().stringWidth("총알 속도")/2, 360);
        g.drawString("+5", itemPurchaseBtn[1].getX() + itemPurchaseBtn[1].getWidth() / 2 - g.getFontMetrics().stringWidth("+5")/2, 380);
        g.drawString("20코인", itemPurchaseBtn[1].getX() + itemPurchaseBtn[1].getWidth() / 2 - g.getFontMetrics().stringWidth("20코인")/2, 400);

        g.drawString("총알 발사 간격", itemPurchaseBtn[2].getX() + itemPurchaseBtn[2].getWidth() / 2 - g.getFontMetrics().stringWidth("총알 발사 간격")/2, 360);
        g.drawString("+10", itemPurchaseBtn[2].getX() + itemPurchaseBtn[2].getWidth() / 2 - g.getFontMetrics().stringWidth("+10")/2, 380);
        g.drawString("50코인",itemPurchaseBtn[2].getX() + itemPurchaseBtn[2].getWidth() / 2 - g.getFontMetrics().stringWidth("50코인")/2 , 400) ;

        g.drawString("ship 생명", itemPurchaseBtn[3].getX() + itemPurchaseBtn[3].getWidth() / 2 - g.getFontMetrics().stringWidth("ship 생명")/2, 360);
        g.drawString("+1", itemPurchaseBtn[3].getX() + itemPurchaseBtn[3].getWidth() / 2 - g.getFontMetrics().stringWidth("+1")/2, 380);
        g.drawString("200코인", itemPurchaseBtn[3].getX() + itemPurchaseBtn[3].getWidth() / 2 - g.getFontMetrics().stringWidth("200코인")/2, 400);

        g.drawString("스테이지 " + stageLevel + "클리어! 코인으로 아이템을 구매하세요!",(800-g.getFontMetrics().stringWidth("스테이지 " + stageLevel + "클리어! 코인으로 아이템을 구매하세요!"))/2,200);


        URL fontUrl = getClass().getResource("/font/Cafe24Danjunghae.ttf");

        Font font = null;
        try {
            assert fontUrl != null;
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 40);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        g.setFont(font);
        g.drawString("아이템 상점",(800-g.getFontMetrics().stringWidth("아이템 상점"))/2,150);





        // 버튼 그리기
        btnManager();

        g.dispose();
        strategy.show();
    }

    public BufferedImage getStoreBackgroundImage() {
        return storeBackgroundImage;
    }


    public String getBtnColor() {
        return btnColor;
    }

    /**
     * A class to handle keyboard input from the user. The class
     * handles both dynamic input during game play, i.e. left/right
     * and shoot, and more static type input (i.e. press any key to
     * continue)
     *
     * This has been implemented as an inner class more through
     * habbit then anything else. Its perfectly normal to implement
     * this as seperate class if slight less convienient.
     *
     * @author Kevin Glass
     */
    private class KeyInputHandler extends KeyAdapter {
        /** The number of key presses we've had while waiting for an "any key" press */
        private int pressCount = 1;

        /**
         * Notification from AWT that a key has been pressed. Note that
         * a key being pressed is equal to being pushed down but *NOT*
         * released. Thats where keyTyped() comes in.
         *
         * @param e The details of the key that was pressed
         */
        public void keyPressed(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "press"
            if (waitingForKeyPress) {
                return;
            }


            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
        }

        /**
         * Notification from AWT that a key has been released.
         *
         * @param e The details of the key that was released
         */
        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "released"
            if (waitingForKeyPress) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
        }

        /**
         * Notification from AWT that a key has been typed. Note that
         * typing a key means to both press and then release it.
         *
         * @param e The details of the key that was typed.
         */
        public void keyTyped(KeyEvent e) {
            // if we're waiting for a "any key" type then
            // check if we've recieved any recently. We may
            // have had a keyType() event from the user releasing
            // the shoot or move keys, hence the use of the "pressCount"
            // counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now recieved our key typed
                    // event we can mark it as such and start
                    // our new game
                    waitingForKeyPress = false;
                    if (stageLevel == 0) {
                        startGame();

                    }
                    else {
                        gameRunning = false;
                    }
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    public int getGameDifficulty() {
        return gameDifficulty;
    }

    /**
     * The entry point into the game. We'll simply create an
     * instance of class which will start the display and game
     * loop.
     *
     * @param argv The arguments that are passed into our game
     */
    public static void main(String[] argv) {
//		Game g = new Game();

        // Start the main game loop, note: this method will not
        // return until the game has finished running. Hence we are
        // using the actual main thread to run the game.

//		g.gameLoop();
        Window w = new Window();
    }
}