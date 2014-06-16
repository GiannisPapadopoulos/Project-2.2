package trafficsim.systems;

import static functions.MovementFunctions.getRoad;
import static trafficsim.TrafficSimConstants.*;
import static utils.EntityRetrievalUtils.getEntity;
import trafficsim.TrafficSimWorld;
import trafficsim.callbacks.FindBodyQueryCallback;
import trafficsim.components.PhysicsBodyComponent;
import trafficsim.data.DataGatherer;
import trafficsim.roads.NavigationObject;
import trafficsim.screens.SimulationScreen;
import trafficsim.screens.SuperScreen;
import utils.ExportData;

import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import editor.MousePosition.Coordinates;
import graph.Edge;
import graph.EntityIdentificationData;
import graph.EntityIdentificationData.ElementType;

/** System to control the camera and other input commands we might define */
public class InputSystem extends VoidEntitySystem implements InputProcessor {

	private SuperScreen superScreen;
	private OrthographicCamera camera;

	private static final double ZOOMING_FACTOR1 = 0.05f;
	private static final double ZOOMING_FACTOR2 = .5f;
	private static final float TRANSLATION_FACTOR = 0.15f;

	protected float previousDragX;
	protected float previousDragY;

	private boolean printCoordinates = false;

	public InputSystem(SuperScreen superScreen) {
		this.superScreen = superScreen;
		this.camera = superScreen.getCamera();
	}

	@Override
	protected void processSystem() {

	}

	@Override
	public boolean keyDown(int keycode) {
		DataGatherer dataGatherer = ((TrafficSimWorld) world).getDataGatherer();
		if (keycode == Keys.C) {
			System.out.println("Total time running " + TIMER.getTime() / 1000.0 + " cars spawned "
								+ world.getSystem(MovementSystem.class).getTotalCars());
			System.out.println(dataGatherer);
			return true;
		}
		else if (keycode == Keys.P) {
			SimulationScreen screen = superScreen.getScreens().getSimulationScreen();
			if (screen != null) {
				// Toggle paused
				screen.setPaused(!screen.isPaused());
			}
		}
		else if (keycode == Keys.E) {
			ExportData.writeToFile(dataGatherer, "data/simulationData");
			System.out.println(dataGatherer.getAverageDistanceTravelled().size() + " cars have reached destination");
		}
		else if (keycode == Keys.R) {
			printCoordinates = !printCoordinates;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		previousDragX = screenX;
		previousDragY = screenY;
		if (button == Buttons.RIGHT) {
			superScreen.getMousePosition().update(camera);
			World box2dWorld = ((TrafficSimWorld) world).getBox2dWorld();
			Coordinates coords = superScreen.getMousePosition().getAbsolute();

			float boxSize = 1f;

			FindBodyQueryCallback callBack = new FindBodyQueryCallback(new Vector2(coords.getX(), coords.getY()));
			box2dWorld.QueryAABB(	callBack, coords.getX() - boxSize, coords.getY() - boxSize,
									coords.getX() + boxSize,
									coords.getY() + boxSize);
			if (callBack.foundSomething()) {
				// int entityID = callBack.getClosestId();
				Entity entity = getEntity(world, callBack.getIdData());
				PhysicsBodyComponent physComp = entity.getComponent(PhysicsBodyComponent.class);
				if (physComp != null && physComp.getUserData().getClass() == EntityIdentificationData.class) {
					EntityIdentificationData graphData = (EntityIdentificationData) physComp.getUserData();
					if (graphData.getType() == ElementType.EDGE) {
						Edge<NavigationObject> edge = ((TrafficSimWorld) world).getGraph().getEdge(graphData.getID());
						if (DEBUG_CLICKS)
							System.out.println(getRoad(edge).getDirection());
					}
				}
				if (DEBUG_CLICKS)
					System.out.println("Found something " + entity + " id " + callBack.getIdData().getID() + " "
										+ physComp.getPosition() + "\n");
				superScreen.getScreens().getSimulationScreen().getPop().setEntityToRender(entity);
				superScreen.getScreens().getSimulationScreen().getFocus().setEntityToRender(entity);
			}
		}
		else {
			superScreen.getMousePosition().update(camera);
			Coordinates coords = superScreen.getMousePosition().getAbsolute();
			if (printCoordinates)
				System.out.println("x " + coords.getX() + " y " + coords.getY());
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		camera.unproject(vec, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		// System.out.println(screenX + " " + screenY + " v " + vec);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.translate((previousDragX - screenX) * TRANSLATION_FACTOR,
				-(previousDragY - screenY) * TRANSLATION_FACTOR);
		previousDragX = screenX;
		previousDragY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		double ZOOMING_FACTOR = camera.zoom <= 1 ? ZOOMING_FACTOR1
				: ZOOMING_FACTOR2;
		if (camera.zoom + amount * ZOOMING_FACTOR > 0.05) {
			camera.zoom += amount * ZOOMING_FACTOR;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

}
