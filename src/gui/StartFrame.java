package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartFrame extends JFrame {
	

public StartFrame() throws IOException{
	
	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	setSize(500,500);
	
	setLayout(null);
	setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("images/startframe3.png")))));
	JButton button = new JButton(); 
	button.setBounds(150, 390, 185, 50);
	add(button);
	pack();
	
	setVisible(true);
	
	
	button.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ae){
        
        
		try {
			new SimFrame();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispose();
		
    }
});
	
	
}
public static void main(String[] args) throws IOException {
	StartFrame x = new StartFrame();
	x.repaint();

}
}