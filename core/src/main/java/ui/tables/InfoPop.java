package ui.tables;

import lombok.AllArgsConstructor;
import trafficsim.components.DataComponent;
import trafficsim.components.PhysicsBodyComponent;
import utils.Assets;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

@AllArgsConstructor
public class InfoPop {
	
	private Entity entityToRender;
	private TextButton popUp;
	private SpriteBatch batch;
	
	public InfoPop(SpriteBatch batch)
	{
		this.batch = batch;
		entityToRender = null;
		popUp = new TextButton(null, Assets.skin);
	}
	
	public void render()
	{
		
		if (entityToRender != null)
		{
			
			// Draw entity info
			float totalDistance = entityToRender.getComponent(DataComponent.class).getTotalDistance();
			float totalTime = entityToRender.getComponent(DataComponent.class).getTotalTime();
			Vector2 position = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			//float acceleration = entityToRender.getComponent(PhysicsBodyComponent.class).getPosition();
			ButtonStyle style = new ButtonStyle();
			
			popUp.setText((int)totalDistance + "m\n" + (int)totalTime + "s\n");
			popUp.setPosition(position.x, position.y);
			
			popUp.getLabel().setFontScale(0.5f);
			
			popUp.setSize(2,40);
			popUp.setChecked(true);
			batch.begin();
			popUp.draw(batch, (float) 0.75); // Drawing pop up on the main batch
			batch.end();
			
		}
	}
	
	public void setEntityToRender(Entity clickedEntity)
	{
//		if (clickedEntity.equals(entityToRender))
//		{
//			entityToRender = null;
//		}
//		else
			entityToRender = clickedEntity;
	}

}
