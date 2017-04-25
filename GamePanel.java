import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics.*;

public class GamePanel extends JPanel{
	ImageIcon background;

	public GamePanel(){
		this.setLayout(null);
		background = new ImageIcon("background.jpg");
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(background.getImage(),0,0,950,550,this);
	}
}