package practiceUCSDMaps;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.data.Feature;

public class cityMarker extends SimplePointMarker {
	
	public cityMarker (Location location){
		
		super (location);
	}
	
	public cityMarker (Feature city){
		
		super(((PointFeature) city).getLocation(), city.getProperties());
	}
	
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		pg.fill(255, 0, 0);
		pg.triangle(x, y, x+5f, y-8.25f, x+10, y);
		
		// reset to previous styling
		pg.popStyle();
		
	}
	
	public String getCountry(){
		return getStringProperty("country");
	}
	
	public String getCity(){
		return getStringProperty("name");
	}
	
	public float getPopulation(){
		return Float.parseFloat(getStringProperty("population"));
	}
}
