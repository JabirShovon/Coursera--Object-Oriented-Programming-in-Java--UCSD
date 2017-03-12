package Parsing;

import java.util.*;
import de.fhpotsdam.*;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.*;
import processing.data.*;

public class ParseFeed {

	
	public static List<PointFeature> parseEarthquake (PApplet p, String filename){
		XML rss = p.loadXML(filename);
	    
		List<PointFeature> features = new ArrayList<PointFeature>();
		
		XML[] itemXML = rss.getChildren("entry");
		
		PointFeature point;
		
		for (int i=0; i<itemXML.length; i++){
			
			Location location = getLocationFromPoint(itemXML[i]);
			
			if (location != null){
				
				point = new PointFeature(location);
				features.add(point);
				}
			else {
				
				continue;
			}
			
			String strVal = getStringValue(itemXML[i],"title");
			
			if (strVal != null){
				
				point.putProperty("title", strVal);
				point.putProperty("Magnitude", Float.parseFloat(strVal.substring(2, 5)));
			}
			
			float depthVal = getFloatVal(itemXML[i], "georss:elev");
			int interVal = (int)(depthVal/100);
			depthVal = (float) interVal/10;
			
			point.putProperty("depth", Math.abs(depthVal));
			
			
			XML[] catXML = itemXML[i].getChildren("category");
			
			for (int c = 0; c< catXML.length; c++){
				
				String label = catXML[c].getString("label");
				if ("Age".equals(label)){
					
					String ageStr = catXML[c].getString("term");
					point.putProperty("age", ageStr);
				}
				
			}
		}
		
		return features;
	}

	private static String getStringValue(XML xml, String string) {
		String str = null;
		XML strXML = xml.getChild(string);
		
		// check if node exists and has content
		if (strXML != null && strXML.getContent() != null) {
			str = strXML.getContent();
		}
		
		return str;
	}

	private static float getFloatVal(XML xml, String string) {
		return Float.parseFloat(getStringValue(xml, string));
	}

	private static Location getLocationFromPoint(XML xml) {
        
		Location loc = null;
		XML pointXML = xml.getChild("georss:point"); /*getchild is used instead of getString because 
	 getString is a method of String type while getChild is the method of XML type object.
		*/
		
		if (pointXML != null && pointXML.getContent()!=null){
			
			String content = pointXML.getContent();
			String[] latlon = content.split(" ");
			float lat = Float.valueOf(latlon[0]);
			float lon = Float.valueOf(latlon[1]);
			
			loc = new Location (lat, lon);
			
		}
		
		
		return loc;
	}
	
	
	public static List<PointFeature> parseAirports (PApplet p, String filename){
		
		List<PointFeature> features = new ArrayList<PointFeature>();
		
		int i=0;
		
		String[] rows = p.loadStrings(filename);
		
		for (String row : rows){
			
			String[] columns = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			
			float lan =  Float.parseFloat(columns[6]);
			float lon =  Float.parseFloat(columns[7]);
			
			Location loc = new Location(lan,lon);
			PointFeature point = new PointFeature(loc);
			
			point.setId(columns[0]);
			point.addProperty("name", columns[1]);
			point.putProperty("cityName", columns[2]);
			point.putProperty("country", columns[3]);
			
			if (!columns[4].equals("")){
				
				point.putProperty("code", columns[4]);
			}
				else if (!columns[5].equals("")){
					point.putProperty("code", columns[5]);
				}
				
				point.putProperty("altitude", columns[8+i]);
			
			
		}
		
		return features;
	}
	
	
}
