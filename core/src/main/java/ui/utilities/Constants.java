package ui.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Constants {
	
	//public static  String TITLE = "TrafficSimulator";
	
	
	public static final Color background = new Color(0.7f, 0.7f, 0.7f, 255);

	
	static TextureAtlas buttonPack = new TextureAtlas("ui/button.pack");
	public static Skin skin = new Skin(buttonPack);

	public static BitmapFont white = new BitmapFont(Gdx.files.internal("font/white.fnt"), false);
	//public static BitmapFont smaller = new BitmapFont(Gdx.files.internal("font/smaller.fnt"), false);
	public static BitmapFont black = new BitmapFont(Gdx.files.internal("font/black.fnt"), false);
	
//	static TextureAtlas atlas = new TextureAtlas("skin/Holo-dark-hdpi.json");
//	public static Skin oSkin = new Skin(atlas);

	}