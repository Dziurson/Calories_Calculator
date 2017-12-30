package pl.edu.agh.student.calcalc.utilities;

import android.location.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jakub on 30.12.2017.
 */

public class FileParser {
    public static ArrayList<Location> parseGpxFile(File file) throws XmlPullParserException, IOException {
        XmlPullParserFactory xmlParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlParserFactory.newPullParser();
        FileReader fileReader = new FileReader(file);
        parser.setInput(fileReader);
        int event = parser.getEventType();
        while(event != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = parser.next();
        }
        return new ArrayList<>();
    }

    public static ArrayList<Location> parseKmlFile(File file) {
        return new ArrayList<>();
    }
}
