package practiceUCSDMaps;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import module6.EarthquakeMarker;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import Parsing.ParseFeed;

public class earthquakeCityMap extends PApplet {

	private static final boolean offline = false;
	
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LOW = 3;
	
	private final int red = color(233, 63, 51);
	private final int yellow = color(253, 247, 49);
	private final int blue = color(42, 79, 252);
	
	public static String mbTilesString =  "blankLight-1-3.mbtiles";
	
	private UnfoldingMap map;
	private List<Marker> countryMarkers;
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	
	
	private String earthquakeURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	private commonMarker lastSelected;
	private commonMarker lastClicked;
	
	public void setup(){
		size(950,600, OPENGL);
		
		if (offline){
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakeURL = "2.5_week.atom";
			}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		}
		
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// creating list of countries
		
		// A feature stores one or more locations, its type, and additional data properties.
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		//create markers for feature "countries".
		countryMarkers = MapUtils.createSimpleMarkers(countries); 
		
		
		// creating list of cities
		
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		
		for (Feature city: cities){
			//Markers are created with location.
			cityMarkers.add(new cityMarker(city));
		}
		
		// creating list of quakeMarkers
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakeURL);
		quakeMarkers = new ArrayList<Marker>();
		
		/*if (quakeMarkers.size() > 0){
		
			PointFeature f = quakeMarkers.get(0);
			Object magObj = f.getProperty("magnitude");
			Float mag = Float.parseFloat(magObj.toString());
		
			}
	*/
		for (PointFeature feature : earthquakes){
			if (isLand(feature)){
				quakeMarkers.add(new landQuakeMarker(feature));
			}
			else{
				quakeMarkers.add(new oceanQuakeMarker(feature));
			}
		}
	
		// adding markers to the map
		map.addMarkers(cityMarkers);
		map.addMarkers(quakeMarkers);
		
		sortAndPrint(200);
		
	}

	private SimplePointMarker createMarker(PointFeature earthquake) {
		SimplePointMarker m = new SimplePointMarker(earthquake.getLocation());
		Object magObj = earthquake.getProperty("magnitude");
        float mag = Float.parseFloat(magObj.toString());
        
        if (mag >=5.0){
        	
        	m.setColor(red);
        	m.setRadius(16);
        }
        else if (mag < 4.0){
        	
        	m.setColor(blue);
        	m.setRadius(4);
        }
        else {
            m.setColor(yellow);
        }
        
        
		return m;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}
	
	private void sortAndPrint(int numToPrint){
		earthquakeMarker[] quakes = new earthquakeMarker[quakeMarkers.size()];
		quakeMarkers.toArray(quakes);
		Arrays.sort(quakes);
		
		for (int i=0; i < numToPrint && i < quakes.length; i++){
			System.out.println("Sorted: " +quakes[i]);
		}
	}
	
	@Override
	public void mouseMoved(){

		//clear the last selection
		if (lastSelected !=null){
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		selectMarkerIfHover(cityMarkers);
		selectMarkerIfHover(quakeMarkers);
		
	}
	
	
	
	private void selectMarkerIfHover(List<Marker> markers) {
		if (lastSelected !=null){
			return;
		}
		
		for (Marker m : markers){
			commonMarker marker = (commonMarker) m;
			if (marker.isInside(map, mouseX, mouseY)){
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
		
	}
	
	@Override
	public void mouseClicked(){
		if (lastClicked !=null){
			unhideMakers();
			lastClicked = null;
		}
		else if (lastClicked == null){
			checkEarthquakeForClick();
			if (lastClicked == null){
				checkCitiesForClick();
			}
		} 
	} 

	private void checkCitiesForClick() {
		if (lastClicked !=null){
			return;
		}
		
		for (Marker marker : cityMarkers){
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)){
				lastClicked = (commonMarker)marker;
				// hide all other earthquake
				for (Marker mhide : cityMarkers){
					if (mhide !=lastClicked){
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : quakeMarkers){
					earthquakeMarker quakeMarker = (earthquakeMarker) mhide;
					if (quakeMarker.getDistanceTo(marker.getLocation()) > quakeMarker.threatCircle()){
						mhide.setHidden(true);
					}
					
				}
				return;
			}
		}
		
	}

	private void checkEarthquakeForClick() {
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : quakeMarkers) {
			earthquakeMarker marker = (earthquakeMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : quakeMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : cityMarkers) {
					if (mhide.getDistanceTo(marker.getLocation()) 
							> marker.threatCircle()) {
						mhide.setHidden(true);
					}
				}
				return;
			}
		}
		
	}

	private void unhideMakers() {
		for (Marker marker : cityMarkers){
			marker.setHidden(false);
		}
		for (Marker marker : quakeMarkers){
			marker.setHidden(false);
		}
		
	}

	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(color(232, 255, 255));
        rect(25, 50, 150, 250);
        
        fill(color(10, 10, 10));
        textSize(16);
        text("Earthquake Key", 40, 80);
        textSize(12);
        text("(In Richter scale)", 45, 100);
        
        textSize(15);
        text(">=5.0", 82, 145);
        text(">=4.0", 82, 205);
        text("<4.0", 82, 265);
        
        fill(red);
        ellipse(60, 140, 27, 27);
        
        fill(yellow);
        ellipse(60, 200, 20, 20);
        
        fill(blue);
        ellipse(60, 260, 13, 13);
	
	}
	
	private boolean isLand(PointFeature earthquake){
		for (Marker marker : countryMarkers){
			if (isInCountry(earthquake, marker)){
				return true;
			}
		}
		return false;
		
	}

	private boolean isInCountry(PointFeature earthquake, Marker country) {
		Location checkLoc = earthquake.getLocation();
		
		if(country.getClass() == MultiMarker.class){
			for (Marker marker : ((MultiMarker)country).getMarkers()){
				if (((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)){
					earthquake.addProperty("country", earthquake.getProperty("name"));
					return true;
				}
			}
		}
		return false;
	}
}

