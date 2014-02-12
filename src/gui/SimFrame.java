package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class SimFrame extends JFrame {
	 private JPanel jPanel1, jPanel2,icon;
	 private JScrollPane jScrollPane1;

	public SimFrame() throws IOException {
		
		setSize(1366,768);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		

		jScrollPane1 = new javax.swing.JScrollPane();      
       
       
        //This will center
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //creating a border to highlight the JPanel areas
        Border outline = BorderFactory.createLineBorder(Color.black);

		JPanel pageAxisPanel = new SimPanel(jScrollPane1.getViewportBorderBounds().getHeight(),jScrollPane1.getViewportBorderBounds().getWidth());
        pageAxisPanel.setBorder(outline);
        pageAxisPanel.setLayout(new BoxLayout(pageAxisPanel, BoxLayout.PAGE_AXIS));
        
        //Add some buttons
     
        JPanel lineAxisPanel = new SidePanel();
        lineAxisPanel.setLayout(new BoxLayout(lineAxisPanel, BoxLayout.LINE_AXIS));
        lineAxisPanel.setBorder(outline);
        
//        icon = new SidePanel();
//        icon.setLayout(new BoxLayout(icon, BoxLayout.LINE_AXIS));
//        icon.setBorder(outline);
//		icon.add(makeImage("images/icon.png"));
       
       //add(icon,BorderLayout.SOUTH);
       add(new JScrollPane(pageAxisPanel),BorderLayout.NORTH);
       add(lineAxisPanel,BorderLayout.SOUTH);
       setVisible(true);
        
    }
    
    //All the buttons are following the same pattern
    //so create them all in one place.
   
	
	public static void main(String[] args) throws IOException {
		new SimFrame();

	}
	private JLabel makeImage(String im) throws IOException {
		BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		
    	return picLabel;
	}
	 private void addButton(Container parent, String name)
	    {
	        JButton but = new JButton(name);
	        but.setActionCommand(name);
	        parent.add(but);
	    }
	


}