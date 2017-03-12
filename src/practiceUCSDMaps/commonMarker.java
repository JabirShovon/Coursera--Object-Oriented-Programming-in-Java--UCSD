package practiceUCSDMaps;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public abstract class commonMarker extends SimplePointMarker {

	public boolean clicked = false;
	public commonMarker(Location location) {
		super (location);
	}
	
	public commonMarker (Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties){
		super(location, properties);
	}
	
	public boolean getClicked(){
		return clicked;
	}
	
	public void setClicked(boolean state){
		clicked  = state;
	}
	
	public void draw(processing.core.PGraphics pg, float x, float y){
		if (!hidden){
			drawMarker(pg, x, y);
			if (selected){
				showTitle(pg, x, y);
			}
		}
	}
	
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
}
