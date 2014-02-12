

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SimPanel extends JPanel implements MouseWheelListener{
    Dimension area;
    private Dimension preferredSize= new Dimension(400,400);
    double height,width;
    double scale=0.5;
    private String goat = "images/hono.jpg";
   
    public SimPanel(double h, double w) throws IOException {       
        height=h;
        width=w;
        addMouseWheelListener(this);
        
      
        
    }
    private Image makeImage(String im) throws IOException {
    	Image myPicture = ImageIO.read(new File(im));
    	
    	return myPicture;

		
	}
	protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D)g.create();
        g2d.translate(width/2, height/2);
        g2d.scale(scale, scale);
        g2d.translate(-width/2, -height/2);
        g2d.setColor(Color.GREEN);
        
        try {
			g2d.drawImage(  makeImage(goat), 0, 0, null);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
        
        area=new Dimension((int)width,(int)height);     
      
        g2d.dispose();
    }
 
    public Dimension getPreferredSize(){
        return preferredSize;
    }
    private void updatePreferredSize(int n, Point p) {
        double d = (double) n * 1.08;
        d = (n > 0) ? 1 / d : -d;

        int w = (int) (getWidth() * d);
        int h = (int) (getHeight() * d);
        preferredSize.setSize(w, h);

        int offX = (int)(p.x * d) - p.x;
        int offY = (int)(p.y * d) - p.y;
        setLocation(getLocation().x-offX,getLocation().y-offY);

        getParent().doLayout();
    }
    
 
    public void mouseWheelMoved(MouseWheelEvent e) {
    	
    	updatePreferredSize(e.getWheelRotation(), e.getPoint());
    	
        if(e.getWheelRotation()<0){
        	
        		
          scale=scale>=1.0?1.0:scale+0.05;
         // scale +=1; //0.05;
        	
      }
      else if(e.getWheelRotation()>0){
    		
         scale=scale<=0.1?0.1:scale-0.05;
       //   scale -=1;// -0.05;
      }
        revalidate();
        repaint();
    }
}