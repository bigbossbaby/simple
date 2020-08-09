import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class New_Game extends JPanel implements  ActionListener, KeyListener {
	private boolean play = false;
	private int scores = 0;
	
	int x =0;
	
	private int delay = 8;
	
	private Timer timer;

	
	private int totalBricks = 21;
 	
	private int playerX = 310;
	
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private  MapGenerator map;
	
	
	
	
	
	
	New_Game(){
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		
		 map = new MapGenerator(3, 7);
	
	}

	
	public void paint(Graphics g){
		//background
		g.setColor(Color.black);
		g.fillRect(1,  1 , 692,  592);
		
		//drwing map
		map.draw((Graphics2D) g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//scres
		g.setColor(Color.white);
		g.setFont(new Font("sherif", Font.BOLD, 28));
		g.drawString(String.valueOf(scores) , 590, 30);
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		if(ballPosY >= 570 || scores == 21) {
			play = false;
			ballYdir = 0;
			ballXdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("GAME OVER" , 150, 190);
			g.setFont(new Font("arial", Font.BOLD, 25));
			g.drawString("Restart: Enter", 150, 220);
		}
		
		//ball
		g.setColor(Color.yellow);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		g.dispose();
		
		
		
		
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if( keycode == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			}
			else moveRight();
		}
		if( keycode == KeyEvent.VK_LEFT) {
			if(playerX <= 10) {
				playerX = 10;
			}
			else moveLeft();
		}
		
		if(keycode == KeyEvent.VK_ENTER) {
		//	System.out.print("ok");
			if(!play) {
				play = true;
				ballPosX = 120;
				ballPosY = 350;
				ballYdir = 2;
				ballXdir = 2;
				playerX = 310;
				scores = 0;
				totalBricks = 21;
				map = new MapGenerator(3,7);
				
				repaint();
			}
		}
	}


	
	private void moveLeft() {
		// TODO Auto-generated method stub
		play = true;
		playerX -= 20;
		
	}


	private void moveRight() {
		// TODO Auto-generated method stub
		play = true;
		playerX += 20;
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		
		 if (play) {
			if(new Rectangle(ballPosX, ballPosY, 20,20).intersects(new Rectangle(playerX,550, 100, 8))) {
				ballYdir =-ballYdir;
			}
			
			a: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickx = j*map.brickwidth+80;
						int bricky = i*map.brickHeight+50;
						int brickHeight = map.brickHeight;
						int brickWidth = map.brickwidth;
						
						Rectangle rect = new Rectangle(brickx, bricky, brickHeight, brickWidth);
						Rectangle ballRec = new Rectangle(ballPosX, ballPosY, 20, 20) ;
							if(ballRec.intersects(rect)) {
								map. setBrickValue(0, i, j );
								totalBricks--;
								scores++;
								
								if(ballPosX +19 < rect.x || ballPosX+1 >= rect.x +rect.width ) {
									ballXdir =-ballXdir;	
							} else {
								ballYdir = -ballYdir;
							}
								break a;
						}
							
						
								
					}
					
				}
				
			}
			
			ballPosX += ballXdir;
			ballPosY += ballYdir;
			if(ballPosX<= 0 || ballPosX >= 670) {
				ballXdir = -ballXdir;	
			}
			if(ballPosY<= 0 ) {
				ballYdir = -ballYdir;	
			}
			
			
			
		}
		repaint();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) {
		
		
		New_Game game = new New_Game();
		JFrame window = new JFrame();
		window.setResizable(false);
		window.setTitle("Game Play");
		window.setBounds(10,  10,  700,  600);
		
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.green);
		window.add(game);
	}

}


class MapGenerator{
	public int map[][];
	public int brickwidth;
	public int brickHeight;
	
	MapGenerator(int row, int col){
		map = new int[row][col];
		for(int i = 0; i < map.length; i++) {
			for(int j =0; j <map[0].length; j++) {
				map[i][j]= 1;
			}
		}
		brickwidth = 540/col;
		brickHeight = 150/row;
	}
	void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j =0; j <map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(Color.white);
					g.fillRect(j*brickwidth+80, i*brickHeight+50, brickwidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j*brickwidth+80, i*brickHeight+50, brickwidth, brickHeight);
				}
			}
		
	}
	
}
	
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value ;
	}
}