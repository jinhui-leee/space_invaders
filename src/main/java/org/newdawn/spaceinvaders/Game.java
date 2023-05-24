package org.newdawn.spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
import org.newdawn.spaceinvaders.entity.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Game extends Canvas implements ActionListener, WindowListener
{
    private final BufferStrategy strategy;
    int gameTimer;

    private JLabel[] lifeLabel;

    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;

    /** The list of all the entities that exist in our game */
    private final ArrayList entities = new ArrayList();

    /** The list of entities that need to be removed from the game this loop */
    private final ArrayList removeList = new ArrayList();

    /** The entity representing the player */
    private ShipEntity shipEntity;
    private Entity itemEntity;
    private Entity obstacleEntity;

    private boolean isItemActivated = false;
    private boolean isItem2Activated = false;
    private boolean isItem3Activated = false;




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

    /** The game window that we'll update with the frame count */
    private final JFrame container;

    /**스테이지 레벨*/
    private int stageLevel = 0;

    private final int bossStageLevel = 5;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    /** 글자 크기 */
    static Font font_basic_bold_size_14 = new Font("맑은 고딕",Font.BOLD,14);


    private  JButton audioBtn;

    private  Music backgroundMusic;

    private  ImageIcon changeIconAudioOff;
    private ImageIcon changeIconAudioOn;

    public ImageIcon changeGoldIcon;

    private static User user;

    /** 골드 초기값 = 0 */
    private int goldCnt = 0;
    private JLabel goldLabel;

    /** 경과 시간 초기값 = 0 */
    private JLabel timeLabel;
    private String timeString = "";

    private String totalClearTime ="";
    private Integer totalClearTimeInt = 0;

    private final int gameDifficulty;

    private final BufferedImage backgroundImage;

    private JButton []itemPurchaseBtn;

    private int shotSpeed = -330;

    //총알 발사 간격 아이템 사용 이후, 원래 총알 간격으로 돌아가기 위한 총알 간격
    private long defaultFiringInterval = 500;

    private JLabel[] itemStoreLabel;

    private int shipEntityLife;

    private int []itemPurchaseCnt;

    private String themeColor;



    /**
     * Construct our game and set it running.
     */
    public Game(int gameDifficulty, User user, String themeColor, BufferedImage image) {
        this.gameDifficulty = gameDifficulty;
        if(user != null) Game.user = user;
        this.themeColor = themeColor;
        this.backgroundImage = image;

        container = new JFrame("Space Invaders 102");
        setBounds(0,0,800,600);
        container.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);

        loadContent();

        addWindowListener(this);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addKeyListener(new KeyInputHandler());
        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        initEntities();
    }

    public void loadContent() {

        JPanel panel = (JPanel) container.getContentPane();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800,600));

        JLabel userNameLabel;

        if (user!=null){
            // 로그인한 사용자 표시 (로그인 시 사용한 ID를 통해 파이어베이스에서 사용자 정보 가져옴)
            userNameLabel = new JLabel(user.getName());
            //int nameLength = user.getName().length();

            // 획득 골드 숫자 표시
            goldLabel = new JLabel(user.getGold().toString());

        }

        else{
            userNameLabel = new JLabel("게스트");

            // 획득 골드 숫자 표시
            goldLabel = new JLabel(String.valueOf(goldCnt));

        }
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        userNameLabel.setBounds(120,20,60,25);

        panel.add(userNameLabel);

        userNameLabel.setFont(font_basic_bold_size_14);

        goldLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        Color goldBGC = new Color(210, 216, 217);
        goldLabel.setBackground(goldBGC);
        goldLabel.setBounds(685,25,50,20);
        panel.add(goldLabel);


        // Music 객체 받아오고 재생
        backgroundMusic = new Music();
        backgroundMusic.playMusic();

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

        this.changeGoldIcon = changeIconGetGold;

        JLabel imageGetGoldLabel = new JLabel(changeIconGetGold);
        imageGetGoldLabel.setBounds(655,25,20,20);
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
            itemPurchaseBtn[i].setBackground(Color.decode(themeColor));
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

        // 경과 시간 표시
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(font_basic_bold_size_14);
        timeLabel.setBounds(20,20,70,25);
        panel.add(timeLabel);


        itemPurchaseCnt = new int[5];
        Arrays.fill(itemPurchaseCnt, 0);

        panel.add(this);

    }

    // 음악 재생 및 정지
    public void actionPerformed(ActionEvent e) {
        // 오디오 버튼 클릭 시 이미지 변경 및 소리 조절
        if (e.getSource() == audioBtn) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stopMusic();
                audioBtn.setIcon(this.changeIconAudioOn);
            } else {
                backgroundMusic.playMusic();
                audioBtn.setIcon(this.changeIconAudioOff);

            }
        }
        //아이템 구매 : ship +20 속도 증가, 20원
        else if (e.getSource() == itemPurchaseBtn[0]) {

            if (goldCnt >= 20) {
                goldCnt -= 20;
                SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(goldCnt)));
                moveSpeed += 20;
                itemPurchaseCnt[0]++;
                itemPurchaseCnt[4]++;

                if (user != null) {
                    user.setGold(goldCnt);
                }
            }
            else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");


        }
        //아이템 구매 : 총알 속도 -5 증가, 20원
        else if (e.getSource() == itemPurchaseBtn[1]) {

            if (goldCnt >= 20) {
                goldCnt -= 20;
                SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(goldCnt)));
                shotSpeed -= 5;
                itemPurchaseCnt[1]++;
                itemPurchaseCnt[4]++;

                if (user != null) {
                    user.setGold(goldCnt);
                }
            }
            else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
        }

        //아이템 구매 : 총알 발사 간격 -10, 50원
        else if (e.getSource() == itemPurchaseBtn[2]) {
            if (goldCnt >= 50) {
                goldCnt -= 50;
                SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(goldCnt)));
                firingInterval-=10;
                itemPurchaseCnt[2]++;
                itemPurchaseCnt[4]++;

                defaultFiringInterval = firingInterval;

                if (user != null) {
                    user.setGold(goldCnt);
                }
            }
            else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");

        }
        //아이템 구매 : 생명 +1, 200원
        else if (e.getSource() == itemPurchaseBtn[3]) {
            if (shipEntity.getShipLife() >= 5) {
                JOptionPane.showMessageDialog(null, "생명은 최대 5개입니다.");
            }
            else {
                if (goldCnt >= 200) {
                    goldCnt -= 200;
                    SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(goldCnt)));
                    shipEntityLife = shipEntity.getShipLife()+1;
                    shipEntity.setShipLife(shipEntityLife);
                    itemPurchaseCnt[3]++;
                    itemPurchaseCnt[4]++;

                    lifeLabel[5- shipEntity.getShipLife()].setVisible(true);

                    if (user != null) {
                        user.setGold(goldCnt);
                    }
                }
                else JOptionPane.showMessageDialog(null, "코인 부족으로 구매할 수 없습니다.");
            }

        }
        //다음 스테이지 넘어가기
        else if (e.getSource() == itemPurchaseBtn[4]) {
            gameRunning = true;
            manageComponent();
            startGame();
        }
    }
    public void manageComponent() {
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
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stopMusic();
        }

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


    private void startGame() {
        // clear out any existing entities and intialise a new set
        entities.clear();
        initEntities();
        createItemEntities();
        resetItem();

        if (stageLevel == 0) {
            for (int i = 0; i < 4; i++) lifeLabel[i].setVisible(false);
            lifeLabel[4].setVisible(true);
            shipEntityLife = 1;
        }

        shipEntity.setShipLife(shipEntityLife);
        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        firePressed = false;

    }


    private void initEntities() {
        // create the player ship and place it roughly in the center of the screen
        if (Framework.character == 0)
        {
            shipEntity = new ShipEntity(this,"images/ship.gif",370,550);

        }
        else if (Framework.character == 1)
        {
            shipEntity = new ShipEntity(this,"images/ship2.png",370,550);

        }
        else if (Framework.character == 2)
        {
            shipEntity = new ShipEntity(this,"images/ship3.png",370,550);

        }
        else if (Framework.character == 3)
        {
            shipEntity = new ShipEntity(this,"images/ship4.png",370,550);

        }
        else if (Framework.character == 4)
        {
            shipEntity = new ShipEntity(this,"images/ship5.png",370,550);

        }
        shipEntity.setShipLife(shipEntityLife);
        entities.add(shipEntity);


        if (stageLevel < bossStageLevel) {
            alienCount = 0;
            //적(외계인) 생성
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


            for (int row = 0; row < alienRow + stageLevel; row++) {
                for (int x = 0; x < alienX + stageLevel; x++) {
                    Entity alien = new AlienEntity(this, 100 + (x * 50), (50) + row * 30);
                    entities.add(alien);
                    alienCount++;
                }
            }
        }
        else { //보스전
            alienCount = 0;

            Entity bossAlien = new BossAlienEntity(this, 100, 50);
            entities.add(bossAlien);

            alienCount++;
        }
    }
    private void createItemEntities(){
        itemEntity =new ItemEntity(this,"images/item.gif");
        entities.add(itemEntity);

    }




    public void updateLogic() {
        logicRequiredThisLoop = true;
    }


    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }


    public void notifyDeath() {
        message = "Oh no! They got you, try again? " + " stage level : " + (stageLevel+1);
        stageLevel = 0;
        waitingForKeyPress = true;

    }

    // String 형으로 된 시간을 Int 형으로 변환
    public int convertTimeStringToInt(String timeStr) {
        String[] tokens = timeStr.split(":");
        int minutes = Integer.parseInt(tokens[0]);
        int seconds = Integer.parseInt(tokens[1]);
        int millis = Integer.parseInt(tokens[2]);
        int timeInt = minutes * 60 * 1000 + seconds * 1000 + millis;
        return timeInt;
    }
    // Int 형으로 된 시간을 String 형으로 변환
    public String convertTimeIntToString(int timeInt) {
        int minutes = timeInt / 1000 / 60;
        int seconds = (timeInt / 1000) % 60;
        int millis = timeInt % 1000;
        String timeStr = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);
        return timeStr;
    }

    /**
     * Notification that the player has won since all the aliens
     * are dead.
     */
    public void notifyWin() {
        message = "Well done! You Win!" + "  stage level : " + (stageLevel+1)+" clear time : " + timeString;
        // 새로운 기록을 누적값에 추가
        Integer timeInt = convertTimeStringToInt(timeString);
        totalClearTimeInt += timeInt;
        totalClearTime = convertTimeIntToString(totalClearTimeInt);

        waitingForKeyPress = true;
        stageLevel++;


//        gameRunning = false;
    }

    /**
     * Notification that an alien has been killed
     */
    public void notifyAlienKilled() {
        // reduce the alien count, if there are none left, the player has won!
        alienCount--;
        if (user != null) {
            String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().getBytes());
            // 몬스터 죽일 때마다 골드 1 증가
            user.setGold(user.getGold()+1);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            DatabaseReference userRef = ref.child("Users").child(encodedEmail);

            Map<String, Object> updates = new HashMap<>();
            updates.put("/" + encodedEmail + "/gold", user.getGold());

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
            SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(user.getGold())));
        }
        else{
            goldCnt++;
            SwingUtilities.invokeLater(() -> goldLabel.setText(Integer.toString(goldCnt)));
        }
        if (alienCount <= 0) {
            notifyWin();
        }


        for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }


    public void tryToFire() {
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this,"images/shot.gif", shipEntity.getX()+10, shipEntity.getY()-30);
        entities.add(shot);
        // 총알 발사 시 효과음 재생
        Music.playShotAudio();
    }

    public void fireItem() {
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        lastFire = System.currentTimeMillis();


        for (int i=0; i<5;i++){
            ShotEntity shot_item = new ShotEntity(this,"images/shot.gif", shipEntity.getX()+(i*60)-60, shipEntity.getY()-30);
            entities.add(shot_item);
            Music.playShotAudio();

        }



    }
    public void fireItem2() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this,"images/shot2.png", shipEntity.getX()+10, shipEntity.getY()-30);

        int itemShot2Speed = -1000;
        shot.setMoveSpeed(itemShot2Speed);
        entities.add(shot);
        // 총알 발사 시 효과음 재생
        Music.playShotAudio();
    }

    public void shootShip() {

        for (int i=0; i<3; i++) {
            BossShotEntity shot = new BossShotEntity(this,"images/stone_boss_shot.png", shipEntity.getX()+(i*30-30),100);
            entities.add(shot);
        }
    }
    public void useItem(){


        int itemRandomNum=(int)(Math.random()*3)+1;
        if (itemRandomNum==1){
            isItemActivated =true;
        }
        else if(itemRandomNum==2){
            firingInterval-=250;
            isItem2Activated =true;
        }
        else if(itemRandomNum==3){
            isItem3Activated = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isItem3Activated = false;}
            }, 3000); //3초간 무적
        }
    }

    public void resetItem(){
        firingInterval = defaultFiringInterval;
        isItemActivated = false;
        isItem2Activated = false;
        isItem3Activated = false;
    }
    public void addObstacle(){
        ObstacleEntity obstacle = new ObstacleEntity(this, "images/obstacle.png", 10, (int) (Math.random() * 450));
        entities.add(obstacle);

    }



    public void gameLoop() {
        long lastLoopTime = SystemTimer.getTime();
        long startTime = lastLoopTime;
        long elapsedTime=0;


        while (true) {
            if (gameRunning) {
                while (gameRunning) {
                    long delta = SystemTimer.getTime() - lastLoopTime;
                    elapsedTime+=delta;
                    lastLoopTime = SystemTimer.getTime();

                    int minutes=(int)(elapsedTime/1000)/60;
                    int seconds=(int)(elapsedTime/1000)%60;
                    int millis = (int)(elapsedTime % 1000);

                    timeString = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);

                    SwingUtilities.invokeLater(() -> timeLabel.setText(timeString));

                    // update the frame counter
                    lastFpsTime += delta;
                    fps++;
                    gameTimer++;
                    if(gameTimer >10000){
                        gameTimer =1;
                    }

                    // update our FPS counter if a second has passed since
                    // we last recorded
                    if (lastFpsTime >= 1000) {
                        /* The normal title of the game window */
                        String windowTitle = "Space Invaders 102";
                        container.setTitle(windowTitle +" (FPS: "+fps+")");
                        lastFpsTime = 0;
                        fps = 0;
                    }


                    //배경색
                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0,0,800,600);
                    g.setColor(Color.white);
                    String messageEnemyCnt = "남은 적 수 " + String.valueOf(alienCount);
                    int stringWidth = g.getFontMetrics().stringWidth(messageEnemyCnt);
                    g.drawString(messageEnemyCnt, (getWidth() - stringWidth) / 2, 30);

                    if(isItem3Activated)
                    {
                        g.setColor(Color.blue);
                        Font font = new Font("Arial", Font.BOLD, 70);
                        g.setFont(font);
                        g.drawString("GOD MODE",(800-g.getFontMetrics().stringWidth("GOD MODE"))/2,300);                    }


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
                                    lifeLabel[4- shipEntity.getShipLife()].setVisible(false);
                                }

                            }
                        }
                    }

                    entities.removeAll(removeList);
                    removeList.clear();

                    if (logicRequiredThisLoop) {
                        for (int i=0;i<entities.size();i++) {
                            Entity entity = (Entity) entities.get(i);
                            entity.doLogic();
                        }

                        logicRequiredThisLoop = false;
                    }


                    if (waitingForKeyPress) {
                        elapsedTime=0;
                        g.setColor(Color.white);
                        g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
                        g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
                    }

                    g.dispose();
                    strategy.show();

                    shipEntity.setHorizontalMovement(0);
                    shipEntity.setVerticalMovement(0);

                    if ((leftPressed) && (!rightPressed)) {
                        shipEntity.setHorizontalMovement(-moveSpeed);
                    } else if ((rightPressed) && (!leftPressed)) {
                        shipEntity.setHorizontalMovement(moveSpeed);
                    }

                    if ((upPressed)&&(!downPressed)) {
                        shipEntity.setVerticalMovement(-moveSpeed);
                    }
                    else if ((downPressed)&&(!upPressed)) {
                        shipEntity.setVerticalMovement(moveSpeed);
                    }

                    if (firePressed) {
                        if(isItemActivated){
                            fireItem();
                        } else if (isItem2Activated) {
                            fireItem2();

                        } else {
                            tryToFire();
                        }
                        if (stageLevel >= bossStageLevel && !waitingForKeyPress) {
                            shootShip();
                        }

                    }
                    if(gameTimer %200==0)
                    {
                        addObstacle();
                    }

                    SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
                }
            }
            else {
                elapsedTime=0;
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

        g.drawImage(getBackgroundImage(),0,0,800,600,null);

        g.setColor(Color.white);

        URL fontUrl = getClass().getResource("/font/Cafe24Danjunghae.ttf");

        Font font1 = null;
        Font font2 = null;
        Font font3 = null;
        Font font4 = null;
        try {
            assert fontUrl != null;
            font1 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 40);
            font2 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 20);
            font3 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 14);
            font4 = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(Font.BOLD, 30);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        g.setFont(font1);
        g.drawString("게임 결과",(800-g.getFontMetrics().stringWidth("게임 결과"))/2,160);

        g.setFont(font2);

        if (user != null) {
            g.drawString("<" + user.getName() + ">", (800-g.getFontMetrics().stringWidth("<" + user.getName() + ">"))/2, 200);
        }
        else {
            g.drawString("<#게스트>", (800-g.getFontMetrics().stringWidth("<#게스트>"))/2, 200);

        }

        g.setFont(font3);
        //클리어한 난이도
        if (gameDifficulty == 0)
        {
            g.drawString("난이도 : 쉬움",(800-g.getFontMetrics().stringWidth("난이도 : 쉬움"))/2, 220);

        }
        else if (gameDifficulty == 1) {
            g.drawString("난이도 : 보통",(800-g.getFontMetrics().stringWidth("난이도 : 보통"))/2, 220);

        }
        else {
            g.drawString("난이도 : 어려움",(800-g.getFontMetrics().stringWidth("난이도 : 어려움"))/2, 220);

        }


        //아이템 사용 종류와 개수
        g.drawString("총 구매 아이템 개수 : " + itemPurchaseCnt[4], (800-g.getFontMetrics().stringWidth("총 구매 아이템 개수 : " + itemPurchaseCnt[4]))/2, 250);

        g.drawString("ship 속도 증가 : " + itemPurchaseCnt[0] + "  총알 속도 증가 : " + itemPurchaseCnt[1],
                (800-g.getFontMetrics().stringWidth("ship 속도 증가 : " + itemPurchaseCnt[0] + "  총알 속도 증가 : " + itemPurchaseCnt[1]))/2, 290);

        g.drawString("총알 발사 간격 감소 : " + itemPurchaseCnt[2] + "  생명 추가 : " + itemPurchaseCnt[3],
                (800-g.getFontMetrics().stringWidth("총알 발사 간격 감소 : " + itemPurchaseCnt[2] + "  생명 추가 : " + itemPurchaseCnt[3]))/2, 310);


        g.setFont(font4);
        //사용자의 경우 기존 점수와 비교
        if(user!=null) {
            // 기존 user 점수 = bestTime
            final String bestTime = user.getBestTime();
            int bestTimeInt = convertTimeStringToInt(bestTime);
            totalClearTimeInt = convertTimeStringToInt(totalClearTime);

            // Best Time 갱신인 경우 사용자 DB bestTime에 저장
            if (totalClearTimeInt < bestTimeInt) {
                g.drawString("Best Time 갱신 !!", (800 - g.getFontMetrics().stringWidth("Best Time 갱신 !!")) / 2, 360);
                g.drawString("기존 최종 클리어 시간 : " + bestTime, (800 - g.getFontMetrics().stringWidth("기존 최종 클리어 시간 : 00:00:00")) / 2, 410);
                g.drawString("최종 클리어 시간 : " + totalClearTime, (800 - g.getFontMetrics().stringWidth("최종 클리어 시간 : 00:00:00")) / 2, 460);

                String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().getBytes());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                DatabaseReference userRef = ref.child("Users").child(encodedEmail);

                Map<String, Object> updates = new HashMap<>();
                updates.put("/" + encodedEmail + "/bestTime", totalClearTime);

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
            }
            else{
                g.drawString("기존 최종 클리어 시간 : " + bestTime, (800 - g.getFontMetrics().stringWidth("기존 최종 클리어 시간 : 00:00:00")) / 2, 410);
                g.drawString("최종 클리어 시간 : " + totalClearTime, (800 - g.getFontMetrics().stringWidth("최종 클리어 시간 : 00:00:00")) / 2, 460);
            }
        }
        // 게스트는 최종 클리어 시간만 띄움
        else{
            g.drawString("최종 클리어 시간 : " + totalClearTime, (800 - g.getFontMetrics().stringWidth("최종 클리어 시간 : 00:00:00")) / 2, 460);
        }

        g.dispose();
        strategy.show();

    }

    public void drawStoreMenu() {
        // 이미지1을 배경으로 하는 화면 그리기
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.white);

        g.drawImage(getBackgroundImage(),0,0,800,600,null);
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

        manageComponent();

        g.dispose();
        strategy.show();
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }



    private class KeyInputHandler extends KeyAdapter {
        private int pressCount = 1;

        public void keyPressed(KeyEvent e) {
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

        public void keyReleased(KeyEvent e) {
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


        public void keyTyped(KeyEvent e) {

            if (waitingForKeyPress) {
                if (pressCount == 1) {

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

            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    public boolean isItem3Activated() {
        return isItem3Activated;
    }


    public int getGameDifficulty() {
        return gameDifficulty;
    }

    public static void main(String[] argv) {

        new Window();
    }
}