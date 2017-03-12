package practiceUCSDMaps;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

public class oceanQuakeMarker extends earthquakeMarker {

	public oceanQuakeMarker (PointFeature quake){
		
		super (quake);
		IsOnLand =  false;
	}
	
	@Override 
	public void drawEquarthquakes (PGraphics pg, float x, float y){
		float size = 2f * this.getMagnitude();
		pg.rect(x-size/2, y-size/2, size, size);
	}
	
}
