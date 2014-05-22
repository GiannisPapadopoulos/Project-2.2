package ui.tables;

import lombok.AllArgsConstructor;
import trafficsim.components.DataComponent;
import trafficsim.components.PhysicsBodyComponent;
import utils.Assets;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

@AllArgsConstructor
public class InfoPop {
	
	private Entity entityToRender;
	private Label popUp;
	private SpriteBatch batch;
	
	private Drawable glow ;
	private ShapeRenderer shapeRenderer;
	public InfoPop(SpriteBatch batch)
	{
		this.batch = batch;
		entityToRender = null;
		popUp = new Label(null, Assets.skin);
		TextureAtlas atlas = new TextureAtlas("assets/packed-textures/ui-textures.pack");
		glow = new TextureRegionDrawable( atlas.findRegion("car glow"));
		
		
	}
	
	public void render()
	{
		
		if (entityToRender != null)
		{
			
			// Draw entity info
			float totalDistance = entityToRender.getComponent(DataComponent.class).getAverageSpeed();
			float totalTime = entityToRender.getComponent(DataComponent.class).getTotalTime();
			Vector2 position = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			Vector2 speed = entityToRender.getComponent(PhysicsBodyComponent.class).getLinearVelocity();
			//float acceleration = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			float timeSpentOnTrafficLights = entityToRender.getComponent(DataComponent.class).getTimeSpentOnTrafficLights();
		
			Texture texture = new Texture(Gdx.files.internal("assets/m2_0.png"), true); // true enables mipmaps
			texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear); // linear filtering in nearest mipmap image
			
			BitmapFont font = new BitmapFont(Gdx.files.internal("assets/m2.fnt"), new TextureRegion(texture), false);
		
			popUp.setText((int)timeSpentOnTrafficLights  + "s\n" + (int)Math.max(Math.abs(speed.x), Math.abs(speed.y)) + "km/h");
			popUp.setPosition(position.x+5f, position.y-19f);
			
//			float a= popUp.getFontScaleX();
//			float b = popUp.getFontScaleY();
			
			LabelStyle style = new LabelStyle();
			style.font = font;
			
			popUp.setStyle(style);
			//batch.getProjectionMatrix().setToOrtho2D(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.begin();
		 
			glow.draw(batch, position.x-4f, position.y-4f, 8,8);
			//popUp.setSize(40,40);
			popUp.setFontScale(0.22f);//
			popUp.draw(batch, 1f); // Drawing pop up on the main batch
			batch.end();
			
		}
	}
	
	public void setEntityToRender(Entity clickedEntity)
	{
		if (clickedEntity.equals(entityToRender))
		{
			entityToRender = null;
		}
		else
			entityToRender = clickedEntity;
	}

}
