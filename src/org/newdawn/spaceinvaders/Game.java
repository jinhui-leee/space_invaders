package org.newdawn.spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.*;

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
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;

	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;

	/** The list of all the entities that exist in our game */
	private ArrayList entities = new ArrayList();

	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();

	/** The entity representing the player */
	private Entity ship;

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
	private String windowTitle = "Space Invaders 102";

	/** The game window that we'll update with the frame count */
	private JFrame container;

	/**스테이지 레벨*/
	private int stageLevel = 0;

	private int bossStageLevel = 1;

	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = toolkit.getScreenSize();

	private JButton audioBtn;

	private Music music;

	private ImageIcon changeIconAudioOff;
	private ImageIcon changeIconAudioOn;

	long startTime;

	long timeRecord;




	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain our game
		container = new JFrame("Space Invaders 102");

		// get hold the content of the frame and set up the resolution of the game
		//JPanel
		JPanel panel = (JPanel) container.getContentPane();

		//panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(800,600));

		// setup our canvas size and put it into the content of the frame 절대 위치,크기 조정
		setBounds(0,0,800,600);
		container.setLocation(screenSize.width/2 - 400, screenSize.height/2 - 300);

		// Music 객체 받아오고 재생
		music = new Music();
		music.playMusic();

		// 음악 재생 및 정지
		// 이미지 로드
		ImageIcon audioOn = new ImageIcon("src/sprites/audioOn.png");
		ImageIcon audioOff = new ImageIcon("src/sprites/audioOff.png");
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

		initEntities();
	}

	// 음악 재생 및 정지
	public void actionPerformed(ActionEvent e) {
		// 오디오 버튼 클릭 시 이미지 변경 및 소리 조절
		if (e.getSource() == audioBtn) {
			if (music.isPlaying()) {
				music.stopMusic();
				System.out.print(music.isPlaying());
				audioBtn.setIcon(this.changeIconAudioOn);
				audioBtn.setFocusable(false);
			} else {
				music.playMusic();
				System.out.print(music.isPlaying());
				audioBtn.setIcon(this.changeIconAudioOff);
				audioBtn.setFocusable(false);

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
			ship = new ShipEntity(this,"sprites/ship.gif",370,550);

		}
		else if (Framework.character == 1)
		{
			ship = new ShipEntity(this,"sprites/ship2.png",370,550);

		}
		else if (Framework.character == 2)
		{
			ship = new ShipEntity(this,"sprites/ship3.png",370,550);

		}
		else if (Framework.character == 3)
		{
			ship = new ShipEntity(this,"sprites/ship4.png",370,550);

		}
		else if (Framework.character == 4)
		{
			ship = new ShipEntity(this,"sprites/ship5.png",370,550);

		}
		entities.add(ship);

		// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		if (stageLevel < bossStageLevel) {
			alienCount = 0;
			//적(외계인) 생성 : 12x5 크기
			for (int row = 0; row < 3+stageLevel; row++) {
				for (int x = 0; x < 5+stageLevel; x++) {
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

			alienCount++;
		}

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
		timeRecord = SystemTimer.getTime();
		timeRecord = (timeRecord - startTime)/1000;
		message = "Oh no! They got you, try again?" + " stage level : " + stageLevel + " time record : " + timeRecord + " s";
		stageLevel = 0;
		waitingForKeyPress = true;
	}

	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!\n" + "stage level : " + stageLevel + "time record : " ;
		waitingForKeyPress = true;
		stageLevel++;
	}

	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;

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
		ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
		entities.add(shot);
		// 총알 발사 시 효과음 재생
		Music.shotAudio();
	}

	public void shotShip() {

		for (int i=0; i<3; i++) {
			BossShotEntity shot = new BossShotEntity(this,"sprites/stone_boss_shot.png",ship.getX()+(i*30-30),100);
			entities.add(shot);
		}

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
	public void gameLoop() {

		startTime = SystemTimer.getTime();
		long lastLoopTime = SystemTimer.getTime();

		// keep looping round til the game ends

		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();

			// update the frame counter
			lastFpsTime += delta;
			fps++;

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
				tryToFire();

				if (stageLevel >= bossStageLevel && !waitingForKeyPress) {
					shotShip();
				}

			}


			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give
			// us our final value to wait for
			SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		}







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
					startGame();
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



	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 *
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
//		Game g = new Game();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.

//		g.gameLoop();
		Window w = new Window();
	}
}
