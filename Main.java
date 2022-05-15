import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		//creating frame to contain game
		JFrame obj=new JFrame();
		Gameplay gamePlay = new Gameplay();
		
		//specs for frame
		obj.setBounds(20, 20, 700, 600);
		obj.setTitle("Cassie's Brick Breaker");		
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gamePlay);
                obj.setVisible(true);
		
	}

}