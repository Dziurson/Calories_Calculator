package pl.edu.agh.student.calcalc.utilities;

import android.location.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jakub on 30.12.2017.
 */

public class FileParser {
    public static LinkedList<Location> parseGpxFile(File file) throws XmlPullParserException, IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        boolean isNextElevation = false;
        boolean isNextTime = false;
        XmlPullParserFactory xmlParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlParserFactory.newPullParser();
        FileReader fileReader = new FileReader(file);
        parser.setInput(fileReader);
        int event = parser.getEventType();
        Location location = null;
        LinkedList<Location> result = new LinkedList<>();
        while(event != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();
            if(isNextElevation) {
                isNextElevation = false;
                location.setAltitude(Double.parseDouble(parser.getText()));
            }
            if(isNextTime) {
                isNextTime = false;
                location.setTime(dateFormat.parse(parser.getText()).getTime());
            }
            switch (event) {
                case XmlPullParser.START_TAG:
                    if(tag.compareToIgnoreCase("trkpt") == 0) {
                        location = new Location("");
                        location.setLatitude(Double.parseDouble(parser.getAttributeValue(null,"lat")));
                        location.setLongitude(Double.parseDouble(parser.getAttributeValue(null,"lon")));
                    }
                    if(tag.compareToIgnoreCase("ele") == 0) {
                        isNextElevation = true;
                    }
                    if(tag.compareToIgnoreCase("time") == 0) {
                        isNextTime = true;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(tag.compareToIgnoreCase("trkpt") == 0) {
                        result.add(location);
                    }
                    break;
            }
            event = parser.next();
        }
        return result;
    }

    public static LinkedList<Location> parseKmlFile(File file) throws XmlPullParserException, IOException{
        XmlPullParserFactory xmlParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlParserFactory.newPullParser();
        boolean startParsing = false;
        FileReader fileReader = new FileReader(file);
        parser.setInput(fileReader);
        int event = parser.getEventType();
        Location location = null;
        LinkedList<Location> result = new LinkedList<>();
        while(event != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if(tag.compareToIgnoreCase("coordinates") == 0) {
                        startParsing = true;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(tag.compareToIgnoreCase("coordinates") == 0) {
                        startParsing = false;
                    }
                    break;
            }
            if(tag == null && startParsing) {
                String parts[] = parser.getText().split("\n|\r|\n\r");
                String locParts[];
                for (String locString : parts) {
                    locParts = locString.split(",");
                    if (locParts.length == 3) {
                        location = new Location("");
                        location.setLatitude(Double.parseDouble(locParts[0]));
                        location.setLongitude(Double.parseDouble(locParts[1]));
                        location.setAltitude(Double.parseDouble(locParts[2]));
                        result.add(location);
                    }
                }
            }
            event = parser.next();
        }
        return result;
    }
}
