package ui.tables;

import lombok.AllArgsConstructor;
import lombok.Setter;
import trafficsim.components.DataComponent;
import trafficsim.components.MaxSpeedComponent;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.components.RouteComponent;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

	
	private Drawable glow, destination, start ;
	private ShapeRenderer shapeRenderer;
	public InfoPop(SpriteBatch batch)
	{
		this.batch = batch;
		entityToRender = null;
		popUp = new Label(null, Assets.skin);
		TextureAtlas atlas = new TextureAtlas("assets/packed-textures/ui-textures.pack");
		glow = new TextureRegionDrawable( atlas.findRegion("car glow"));
		start = new TextureRegionDrawable(atlas.findRegion("target"));
		destination = new TextureRegionDrawable(atlas.findRegion("target"));
		
		
	}
	
	public void render()
	{
		
		if (entityToRender != null && entityToRender.isActive() && isCar(entityToRender))
		{
			
			// Draw entity info
			float totalDistance = entityToRender.getComponent(DataComponent.class).getAverageSpeed();
			float totalTime = entityToRender.getComponent(DataComponent.class).getTotalTime();
			Vector2 position = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			Vector2 speed = entityToRender.getComponent(PhysicsBodyComponent.class).getLinearVelocity();
			//float acceleration = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			float timeSpentOnTrafficLights = entityToRender.getComponent(DataComponent.class).getTimeSpentOnTrafficLights();
			
			
			/** distance left*/
//			float d2t = entityToRender.getComponent(DataComponent.class).
			
			// entityToRender.getComponent(RouteComponent.class).getTarget().getData().getPointC();
		
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
			
			drawPath(entityToRender);
		 
			destination.draw(	batch,
								entityToRender.getComponent(RouteComponent.class).getTarget().getData().getPosition().x - 4,
								entityToRender.getComponent(RouteComponent.class).getTarget().getData().getPosition().y - 7,
									 8	, 8);
			
			destination.draw(	batch,
								entityToRender.getComponent(RouteComponent.class).getSource().getData().getPosition().x - 4,
								entityToRender.getComponent(RouteComponent.class).getSource().getData().getPosition().y - 7,
									8	, 8);
			glow.draw(batch, position.x-4f, position.y-4f, 8,8);
			//popUp.setSize(40,40);
			popUp.setFontScale(0.22f);//
			popUp.draw(batch, 1f); // Drawing pop up on the main batch
			batch.end();
			
		}
	}
	
	private boolean isCar(Entity entityToRender) {
		return entityToRender.getComponent(MaxSpeedComponent.class) != null;
	}

	public void drawPath(Entity entity){
		
		RouteComponent entityRoute = entity.getComponent(RouteComponent.class);
		if (entityRoute == null || !entityRoute.isSet() ||
				entityRoute.getRoute() == null) {
			return;
		}
		
		 ShapeRenderer sr = new ShapeRenderer();
		 
		 sr.setProjectionMatrix(batch.getProjectionMatrix());

	        sr.begin(ShapeType.Line);
	        sr.setColor(Color.RED);
	        
	       // System.out.println("waypoint size: " + entityRoute.getWayPoints().size());
	        
	        //entityRoute.getPath()entit
	        
	        for(int i=0;i<entityRoute.getAllVertices().size()-1;i++){
	        	
	        	sr.line(entityRoute.getAllVertices().get(i),entityRoute.getAllVertices().get(i+1));
	        	
	        }
	        		
	sr.end();	
		
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
