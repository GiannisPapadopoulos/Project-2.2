package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class UsefulMethods {

	
	public static Image loadImage(String path){
		Texture texture = new Texture(path);
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());    
        
        return new Image(region);
      
		
	}
	
	public static Image loadImage(String path, int percent){
		Texture texture = new Texture(path);
        TextureRegion region = new TextureRegion(texture, 0, 0, (int) texture.getWidth()*percent/100,  (int) texture.getHeight()*percent/100);          

        return new Image(region);
      
		
	}
	
	public static Image resizeImage(Image img, int percent){
		img.scale(percent);
		return img;
		
	}

}
