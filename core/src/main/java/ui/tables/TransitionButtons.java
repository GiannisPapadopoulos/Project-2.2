package ui.tables;

import java.util.ArrayList;

import lombok.Getter;
import lombok.val;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TransitionButtons extends Table{
	
	@Getter
	private ArrayList<Button> buttons;
	private Button panelicon, play, edit;

	private TextureAtlas atlas;
	
public TransitionButtons(){
	buttons = new ArrayList<Button>();
	
	atlas = new TextureAtlas("assets/packed-textures/textures.pack");

	panelicon = createButton("panelicon");
	play = createButton("play");
	edit = createButton("editor");
	
	panelicon.setName("Simulation");
	play.setName("TimeControl");
	edit.setName("Editor");
	
	buttons.add(panelicon);
	buttons.add(play);
	buttons.add(edit);
	
	for (val button : buttons){	
		add(button).size(100, 100);	
	}	
	
}




private Button createButton(String name){

	Drawable up = new TextureRegionDrawable( atlas.findRegion(name + "_normal"));
	Drawable hover = new TextureRegionDrawable( atlas.findRegion(name + "_pressed"));
	
	ButtonStyle style = new ButtonStyle();
	
	style.over = hover;
	style.up = up;

	return new Button(style);
	
	
}





}
