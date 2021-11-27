package application;

public class SimulatorController {
	
	Simulator simulator;
	Renderer renderer;
	
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	
	public void pause() {
		simulator.pause();
	}
	
	public void play() {
		simulator.unpause();
	}

	public void playOrPause() {
		simulator.playOrPause();
	}
	

	public void zoomIn() {
		renderer.zoomIn();
	}
	
	public void zoomOut() {
		renderer.zoomOut();
	}
}
