package editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	private EditorData data;
	private OrthographicCamera cam;
	
	private static final int CELL_SIZE = 20;

	private ShapeRenderer gridRenderer;

	public WorldRenderer(EditorData data) {
		this.data = data;
		this.cam = new OrthographicCamera(1500, 800);
		this.cam.position.set(0, 0, 0);
		this.cam.update();
		this.gridRenderer = new ShapeRenderer();
	}

	public void render(OrthographicCamera cam) {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(1);
		gridRenderer.setColor(Color.GREEN);

		for(int i=0;i<data.getHeight()+1;i++)
			gridRenderer.line(0, i*CELL_SIZE,data.getWidth()*CELL_SIZE,i*CELL_SIZE);
			
		for(int j=0;j<data.getWidth()+1;j++)
			gridRenderer.line(j*CELL_SIZE,0,j*CELL_SIZE,data.getHeight()*CELL_SIZE);
				
		

		gridRenderer.end();
	}

}
