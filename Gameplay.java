import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;


public class Gameplay extends JPanel implements KeyListener, ActionListener 
{
	// game paused to start
	private boolean play = false;
	private int score = 0;
	
	// amount of bricks game starts with
	private int totalBricks = 48;
	
	// how fast the ball moves
	private Timer timer;
	private int delay = 8;
	
	// starting position of slider
	private int paddle = 310;
	
	// starting position of ball, random direction to change gameplay
	Random random = new Random();
	int n = random.nextInt(2+1-2) - 2;  
	private int ballXdir = n;
	private int ballYdir = -2;
	private int ballposX = 120;
	private int ballposY = 350;
	
	
	// call map
	private MapGenerator map;
	
	public Gameplay()
	{	
		// create objects for gameplay
		map = new MapGenerator(4, 12);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{    		
		// setting background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// drawing the map
		map.draw((Graphics2D) g);
		
		// color and boundaries for 3 borders
		g.setColor(Color.white);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// color and position of score		
		g.setColor(Color.green);
		g.setFont(new Font("dialog",Font.BOLD, 25));
		g.drawString(""+score, 590,30);
		
		// color and position of the paddle
		g.setColor(Color.cyan);
		g.fillRect(paddle, 550, 100, 8);
		
		// color and position of the ball
		g.setColor(Color.white);
		g.fillOval(ballposX, ballposY, 30, 30);
	
		// what happens if you win the game
		if(totalBricks <= 0)
		{
			 play = false;
             ballXdir = 0;
     		 ballYdir = 0;
             g.setColor(Color.WHITE);
             g.setFont(new Font("dialog",Font.BOLD, 30));
             g.drawString("Yay! You won the game!", 190,300);
             
             g.setColor(Color.RED);
             g.setFont(new Font("dialog",Font.BOLD, 20));           
             g.drawString("Please press [Enter] to Restart", 200,350);  
		}
		
		// what happens if you lose the game
		if(ballposY > 570)
        {
			 play = false;
             ballXdir = 0;
     		 ballYdir = 0;
             g.setColor(Color.WHITE);
             g.setFont(new Font("dialog",Font.BOLD, 30));
             g.drawString("Game Over! Try Again! ", 190,300);
             
             g.setColor(Color.RED);
             g.setFont(new Font("dialog",Font.BOLD, 20));           
             g.drawString("Please press [Enter] to Restart", 200,350);        
        }
		
		g.dispose();
	}	

	public void keyPressed(KeyEvent e) 
	{
		
		// move paddle to the right, within bounds
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{        
			if(paddle >= 600)
			{
				paddle = 600;
			}
			else
			{
				moveRight();
			}
        }
		
		// move paddle to the left, within bounds
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{          
			if(paddle < 10)
			{
				paddle = 10;
			}
			else
			{
				moveLeft();
			}
        }	
		
		// start game over if enter is pressed
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if(!play)
			{
				play = true;
				ballposX = 120;
				ballposY = 350;
				
				Random random = new Random();
				int n = random.nextInt(2+1-2) - 2;  
				ballXdir = n;
				ballYdir = -2;
				
				paddle = 310;
				score = 0;
				totalBricks = 48;
				map = new MapGenerator(4, 12);
				
				repaint();
			}
        }		
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	// how far the paddle moves when key is pressed
	public void moveRight()
	{
		play = true;
		paddle+=20;	
	}
	
	public void moveLeft()
	{
		play = true;
		paddle-=20;	 	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
		timer.start();
		if(play)
		{	
			// detecting ball collisions with boundaries
			if(new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(paddle, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = -2;
			}
			else if(new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(paddle + 70, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
			else if(new Rectangle(ballposX, ballposY, 30, 30).intersects(new Rectangle(paddle + 30, 550, 40, 8)))
			{
				ballYdir = -ballYdir;
			}
			
			// removing balls once collided with
			A: for(int i = 0; i<map.map.length; i++)
			{
				for(int j =0; j<map.map[0].length; j++)
				{				
					if(map.map[i][j] > 0)
					{
						
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 30, 30);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{	
							// addding up the score
							map.setBrickValue(0, i, j);
							score+=5;	
							totalBricks--;
							
							// ball hitting the sides of bricks
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// ball hitting top and bottom of bricks
							else
							{
								ballYdir = -ballYdir;				
							}
							
							break A;
						}
					}
				}
			}
			
			// how the ball moves
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}		
			
			repaint();		
		}
	}
}