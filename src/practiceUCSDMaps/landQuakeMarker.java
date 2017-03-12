package practiceUCSDMaps;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

public class landQuakeMarker extends earthquakeMarker {
	
	
	public landQuakeMarker (PointFeature quake){
		super(quake);
		IsOnLand = true;
	}
	
	@Override 
	public void drawEquarthquakes (PGraphics pg, float x, float y){
		float size = 2f * this.getMagnitude();
		pg.ellipse(x, y, size, size);
	}
	
	public String getCountry (){
		return (String) getProperty("country");
	}
	
	

}
