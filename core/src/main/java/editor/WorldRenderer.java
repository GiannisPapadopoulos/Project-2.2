package editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	private EditorData editorData;
	private OrthographicCamera cam;

	private ShapeRenderer gridRenderer;

	public WorldRenderer(EditorData editorData) {
		this.editorData = editorData;
		this.cam = new OrthographicCamera(1500, 800);
		this.cam.position.set(0, 0, 0);
		this.cam.update();
		this.gridRenderer = new ShapeRenderer();
	}

	public void render() {
		gridRenderer.setProjectionMatrix(cam.combined);
		gridRenderer.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(1);
		gridRenderer.setColor(Color.GREEN);

		for (int i = 0; i < 2; i++) {
			float start_x = 0;
			float start_y = 0;
			float end_x = (int) (Math.random() * 100);
			float end_y = (int) (Math.random() * 100);
			gridRenderer.line(start_x, start_y, end_x, end_y);
			start_x = 0.1f;
			start_y = 0;
			end_x = (int) (Math.random() * 10);
			end_y = (int) (Math.random() * 10);
			gridRenderer.line(start_x, start_y, end_x, end_y);
		}
		gridRenderer.end();
	}

}
