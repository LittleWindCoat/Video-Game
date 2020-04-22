import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.*;
import java.awt.MouseInfo;
import java.awt.Point;

public class game extends JPanel{

	protected int frameWidth = getWidth();
	protected int frameHeight = getHeight();
	protected int ballRadius = 20;
	protected int ballLocX = 0;
	protected int ballLocY = 0;
	protected int ballLocX2 = getWidth()-ballRadius;
	protected int ballLocY2 = 0;
	protected int paddleWidth = 80;
	protected int paddleThickness = 20; 
	protected int paddleLocX = 0;
	protected int paddleLocY = 0;

	protected int v0 = 15; // change velocity
	protected int t = 0; // arbitary >> used in equation 
	protected int tx =0;
	protected double theta = (Math.PI/5 ); // in radian default >> can be changed

	protected int numLife = 3; // initialy life
	protected int countdown_time = 60 ; // in sec
	protected int score = 0;
	protected double g = 0.098*3;

	protected int curDirectionX = 1;
	protected int curDirectionY = 1;
	protected int direction=0;

	protected int vx = (int) (v0 * Math.cos(theta));
	protected int vy = (int) (v0 * Math.sin(theta) - g*t);

	protected int vx0 = v0;
	protected int vy0 = v0;
	protected int timeBar=300;
	protected int levelChange, lifeScreen;
	protected int x;
	protected Point loc;
	boolean leftright = false, gameEnd=false;
	int locY;
	protected BufferedImage image = null;

	protected int life = 2;
    
	@Override
	public void paintComponent(Graphics g) {


		try{
			if(life<0){
				System.out.println("game is over");
				gameEnd=true;
				image = ImageIO.read(new File("gallery_25428_6_11909.jpg"));
			}else if(levelChange==0&&lifeScreen==0)
				image = ImageIO.read(new File("51b4a2589ad4254981.jpg"));
			else if(levelChange==1&&lifeScreen==0) {
				image = ImageIO.read(new File("09410592.jpg"));
			}else if(lifeScreen==1){
				image = ImageIO.read(new File("maxresdefault-1.jpg"));
			} 
			//image = ImageIO.read(new File("09410592.jpg"));
		}catch (IOException e){
			System.out.println("image not found");
		}
		
			g.setColor(Color.RED);
			g.drawImage(image, 0, 0, null);
			if(gameEnd==false){
			if (life == 2){
				g.fillOval(10, 10, 20, 20);
				g.fillOval(80, 10, 20, 20);
				//System.out.println("This happened");
			}else if (life == 1){
				g.fillOval(10, 10, 20, 20);

				//System.out.println("This happened");
			}


			g.fillOval(ballLocX, ballLocY, ballRadius, ballRadius);

			// move Paddle
			loc = MouseInfo.getPointerInfo().getLocation();
			locY = getHeight() - 30;
			g.fillRect(loc.x,locY, paddleWidth, paddleThickness);
			g.setColor(Color.WHITE);
			g.fillRect(getWidth()-300, 50, timeBar, 10);
			if(levelChange==1){
				g.setColor(Color.PINK);
				g.fillRect(x, 200, 300, 20);
			}
		}
	}	

	protected Timer timer;

	public game() {
		addKeyListener(new TimerCallback());
		setFocusable(true);
		timer = new Timer(100, new TimerCallback()); // 100 ms = 0.1 sec
		timer.start();	
	}

	public void nextLevel(){
		timer.stop();
		//levelChange=1;
	}

	public boolean end(){
		timer.stop();
		timer.start();
		return gameEnd==true;
	}

	public boolean lossLife(int life){
		timer.stop();
		lifeScreen=1;
		ballLocX=0;
		ballLocY=0;
		return true;

	}
	protected class TimerCallback implements ActionListener, KeyListener{

		public void actionPerformed(ActionEvent e) {
			if(gameEnd==false){
				//System.out.println("Ball location: " + (ballLocY-ballRadius));
				//	System.out.println("Paddle location: " + (locY));
				if (life <= 0) {
					//Ending 

				}
				if(timeBar<0){
					nextLevel();
				}
				if(levelChange==1){
					if(leftright==false){
						if(x>getWidth()-320)
							leftright=true;
						x+=89;
					}else if(leftright==true){
						if(x<20)
							leftright=false;
						x-=34;
					}
				}

				if(direction==0){
					if(levelChange==1){
						//System.out.println(" ballLocY: " +ballLocY+", LocY: "+locY+"");
						ballLocX+=30;
						ballLocY+=30;
					}else{
						ballLocX+=30;
						ballLocY+=30;
					}

					if(ballLocY+ballRadius==200&&levelChange==1){
						for(int i=0; i<320; i++){
							if((ballLocX)==x+i){
								direction=3;
								break;
							}
						}
					}
					if(ballLocY+ballRadius>=getHeight()-50){
						System.out.println("touched the paddle");	
						for(int i=0; i<paddleWidth; i++){
							if((ballLocX)==loc.x+i){
								direction=3;
								break;
							}
						}
					}

					if(ballLocX>=getWidth()){
						direction=1;
					}else if(ballLocY>=getHeight()){
						if (life <=0){
							end();

						}
						life --;
						System.out.println("lost a life: " +life);
						lossLife(life);

						//direction=3;
					}

				}else if(direction==1){
					//System.out.println(" Direction 1, ballLocX: " +ballLocX);
					if(levelChange==1){
						System.out.println(" ballLocY: " +ballLocY+", LocY: "+locY+"");
						ballLocX-=30;
						ballLocY+=50;
					}else{
						ballLocX-=30;
						ballLocY+=50;
					}
					if(ballLocY+ballRadius==200&&levelChange==1){
						for(int i=0; i<320; i++){
							if((ballLocX)==x+i){
								direction=2;
								break;
							}
						}
					}
					if(ballLocY+ballRadius>=getHeight()-50){
						System.out.println("touched the paddle");
						for(int i=0; i<paddleWidth; i++){
							if((ballLocX)==loc.x+i){
								direction=2;
								break;
							}
						}
					}

					if(ballLocY>=getHeight()){
						if (life <=0){
							end();

						}
						life -= 1;
						System.out.println("lost a life: " +life);
						lossLife(life);

					}else if(ballLocY<=0){
						direction = 2;
					}else if(ballLocX<=0){
						direction = 0;
					}
				}else if(direction==2){
					//System.out.println(" Directio 2, ballLocX: " +ballLocX);
					if(levelChange==1){
						//System.out.println(" ballLocY: " +ballLocY+", LocY: "+locY+"");
						ballLocX-=30;
						ballLocY-=30;
					}else{
						ballLocX-=30;
						ballLocY-=30;
					};
					if(ballLocY+ballRadius==220&&levelChange==1){
						for(int i=0; i<320; i++){
							if((ballLocX)==x+i){
								direction=1;
								break;
							}
						}
					}
					if(ballLocX<=0){
						System.out.println("direction 3");
						direction = 3; 
					}else if(ballLocY<=0){
						direction=1;
					}
				}else if(direction==3){
					//System.out.println(" Direction 3, ballLocX: " +ballLocX);
					if(levelChange==1){
						//System.out.println(" ballLocY: " +ballLocY+", LocY: "+locY+"");
						ballLocX+=30;
						ballLocY-=30;
					}else{
						ballLocX+=30;
						ballLocY-=30;
					}
					if(ballLocY+ballRadius==220&&levelChange==1){
						for(int i=0; i<320; i++){
							if((ballLocX)==x+i){
								direction=0;
								break;
							}
						}
					}
					if(ballLocX>=getWidth()){
						direction=2;
					}else if(ballLocY<=0){
						direction=0;
					}
				}
				//			System.out.println("CurDirectionY: " + curDirectionY);
				//			//t++;
				//			//ballLocX += 1;
				////			if(curDirectionY ==1)
				////				t++;
				////			else if (curDirectionY == -1)
				////				t--;
				////			
				//			if(curDirectionX== 1){
				//				tx++;
				//				t++;
				//			}
				//				//v0++;
				//				//theta++;
				//			else if(curDirectionX== -1){
				//				tx--;
				//				t++;
				//				
				//				//theta--;
				//			}else if(curDirectionX==-1&&curDirectionY==1){
				//				tx--;
				//				t--;
				//			}
				//			// update vx, vy
				//			vx = (int) (vx0 * Math.cos(theta));
				//			vy = (int) (vy0 * Math.sin(theta) - g*t);
				//
				//			//
				//			if (ballLocX <= getWidth()){
				//				System.out.println("this is happen, ballLocX: "+ballLocX+"");
				//				//System.out.println("this is happen, ballLocY: "+ballLocY+"");
				//				ballLocX = (int) (vx0 * Math.cos(theta)*tx);
				//				if(ballLocX==loc.x)
				//					vy0++;
				//			}
				////			}else if(ballLocX >= getWidth()){
				////				ballLocX-=10;
				////				//curDirectionY=1;
				////				//curDirectionX=-1;
				////			}else if(ballLocY>=getHeight()&&curDirectionX==-1){
				////				ballLocY-=10;
				////				ballLocX-=10;
				////			}else if(ballLocX<=0){
				////				System.out.println("ballLoX is < 0");
				////				//t++;
				//			//}
				//			//System.out.println("this is happen");
				//			//ballLocX = (int) (getWidth() - vx0 * Math.cos(theta)*t);
				//
				//
				//			System.out.println("time " + t);
				//			System.out.println("time x " + tx);
				//			System.out.println("width: " + getWidth());
				//			System.out.println("loc x: " + ballLocX);
				//			System.out.println("v x: " + vx);
				//			System.out.println("v y: " + vy);
				//
				//			ballLocY = (int)(getHeight() - vy0 * Math.sin(theta)*t + 0.5*g*Math.pow(t, 2));
				//			System.out.println("this is happen, ballLocY: "+ballLocY+"");
				//
				//			//// change direction if bounces the frame
				//
				//			// bounce right side
				//			int ballRightEnd = ballLocX + ballRadius;
				//			int ballRightEnd2 = ballLocY + ballRadius;
				//			if (ballRightEnd >= getWidth()) {
				//				System.out.println("Ball hit right edged");
				//				curDirectionX= -1;
				//				//vx0 = vx;
				//				//vy0 = vy;
				//				//t = 3;
				//				tx--;
				//				t--;
				//				if(ballRightEnd2>=getHeight()){
				//					t--;
				//					tx++;
				//				}
				//				System.out.println("time changed");
				//				//ballLocX = (int)  (getWidth() - v0 * Math.cos(theta)*t);
				//				//ballLocY = (int)(getHeight() - v0 * Math.sin(theta)*t + 0.5*g*Math.pow(t, 2));				
				//			} else if(ballRightEnd <= 0){
				//				curDirectionX= 1;
				//				t++;
				//				
				//			}
				//			
				//			
				//			loc = MouseInfo.getPointerInfo().getLocation();
				//			int locY = getHeight() - 30;
				//			
				//			 //change direction if bounces the paddle
				//			int ballLeftEnd = ballLocX;
				////			if (ballLocY + ballRadius >= getHeight() - 30 && ballLeftEnd >= loc.x && ballRightEnd<= loc.x + paddleWidth) {
				////				System.out.println("This happened!!!!!!!!!!!!!!");
				////				System.out.println("paddlelocX      "+loc.x);
				////				System.out.println("paddlelocX + paddleWidth      "+ (loc.x+paddleWidth));
				////				System.out.println("ballLeftEnd is      "+ballLeftEnd);
				////				System.out.println("ballRightEnd is      "+ballRightEnd);
				////				
				////				curDirectionY -= 1;
				////				t--;
				//				
				//				if(ballLocY+20==getHeight())
				//					//ballLocY=-1;
				//					t--;
				////					for(int i = 0; i<80; i++){
				////						if(ballLocX==(paddleWidth+i)){
				////							curDirectionY=-1;
				////					}
				////				}
				////				if(vy0 >= 0){
				////					System.out.println("                 "+vy0);
				////					
				////					vy0 = -vy0;
				////				}
				//			//}
				timeBar-=2;
				repaint();
			}

		}

		@Override
		public void keyTyped(KeyEvent e) {	
		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getKeyCode());	
			if(timeBar<0&&e.getKeyCode()==32){
				timeBar=389;
				levelChange=1;
				timer.start();
			}else if(lossLife(life)==true&&e.getKeyCode()==32){
				lifeScreen=0;
				timer.start();
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}
	}
	public static void main(String[] args) {

		JFrame frame = new JFrame("game");
		game canvas = new game();
		frame.add(canvas);
		frame.setSize(1500, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setVisible(true);
	}
}