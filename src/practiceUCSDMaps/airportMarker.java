package practiceUCSDMaps;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PGraphics;

public class airportMarker extends commonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public airportMarker (Feature city){
		super(((PointFeature)city).getLocation(), city.getProperties());
		
	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y){
		pg.fill(11);
		pg.ellipse(x, y, 5, 5);
	}
	
	@Override
	public void showTitle(PGraphics pg, float x, float y){
		
	}
	
}
