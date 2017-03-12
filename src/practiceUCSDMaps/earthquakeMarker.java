package practiceUCSDMaps;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.data.*;



public abstract class earthquakeMarker extends SimplePointMarker {
	
	
	protected boolean IsOnLand;
	protected float radius;
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;
	
	public abstract void drawEquarthquakes (PGraphics pg, float x, float y);

	// Constructor
	public earthquakeMarker (PointFeature feature){
		super (feature.getLocation());
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude);
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
	}

	
	public void draw(PGraphics pg, float x, float y){
		pg.pushStyle();
		colorDetermine(pg);
		drawEquarthquakes (pg, x, y);
		pg.popStyle();
	}
	
	private void colorDetermine(PGraphics pg) {
		float depth = this.getDepth();

		//System.out.print("depth: " + depth);
		if( depth > 0 && depth < 70 ) {
			//this.setColor( pg.color(252,252,5) );
			pg.fill(252, 252, 5); //green
			//System.out.println(", color: yellow" );

		}
		else if( depth > 70 && depth < 300 ) {
			//this.setColor( pg.color(0, 0, 255));
			pg.fill(0, 0, 255); //blue
			//System.out.println(", color: blue" );
		}
		else {
			pg.fill(255, 0, 0); //red
			//System.out.println(", color: red" );
			//this.setColor( pg.color(255, 0, 0));
		}
		
	}


	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public boolean IsOnLand(){
		return IsOnLand;
	}
	
	
}
