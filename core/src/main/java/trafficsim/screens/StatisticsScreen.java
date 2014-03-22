package trafficsim.screens;


public class StatisticsScreen extends SuperScreen {
	
	
	public StatisticsScreen(Screens screens) {
		super(screens);
	}


	@Override
	public void render(float delta) {
		getWorldLayer().act(delta);
		getUILayer().act(delta);
		getWorldLayer().draw();
		getUILayer().draw();
	}


	@Override
	public void populateUILayer() {
		
	}


	@Override
	public void populateWorldLayer() {
		
	}

}
