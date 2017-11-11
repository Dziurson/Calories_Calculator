package pl.edu.agh.student.calcalc.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.StringHelper;

import static android.os.Environment.getExternalStorageDirectory;

public class GpxFileSerializer {

    private XmlSerializer serializer;
    private FileOutputStream fileOutputStream;
    private File gpxFileDir;
    private boolean isGpxFileOpened = false;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private OutputFileFormat currentExtension;
    private Context context;

    public  GpxFileSerializer(Context context) {
        this.context = context;
        serializer = Xml.newSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        gpxFileDir = new File(getExternalStorageDirectory().getPath() + UserSettings.userFileDirectory);
        if(!gpxFileDir.exists()) {
            gpxFileDir.mkdirs();
        }
    }

    public boolean start(String filename, OutputFileFormat extension) {
        try {
            fileOutputStream = new FileOutputStream(StringHelper.concat(gpxFileDir.toString(),"/",filename,context.getString(extension.getStringResourceId())),true);
            currentExtension = extension;
            serializer.setOutput(fileOutputStream, "UTF-8");
            startDocument();
            isGpxFileOpened = true;
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean stop() {
        try {
            endDocument();
            serializer.flush();
            fileOutputStream.close();
            isGpxFileOpened = false;
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean isStarted() {
        return isGpxFileOpened;
    }

    public void addNewPoint(Location location) {
        try {
            if(isGpxFileOpened) {
                appendNewPoint(location);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startDocument() throws IOException{
        switch(currentExtension) {
            case GPX:
                serializer.startDocument(null, true);
                serializer.startTag(null,"gpx").attribute(null,"version","1.0");
                serializer.startTag(null,"trk");
                break;
            case KML:
                serializer.startDocument(null,null);
                serializer.startTag(null,"kml").attribute(null,"xmlns","http://www.opengis.net/kml/2.2");
                serializer.startTag(null,"Placemark");
                serializer.startTag(null,"LineString");
                serializer.startTag(null,"coordinates");
                break;
        }
    }

    private void appendNewPoint(Location location) throws IOException{
        switch (currentExtension) {
            case GPX:
                serializer.startTag(null, "trkpt").attribute(null, "lat", ((Double) location.getLatitude()).toString()).attribute(null, "lon", ((Double) location.getLongitude()).toString());
                serializer.startTag(null, "ele");
                serializer.text(((Double) location.getAltitude()).toString());
                serializer.endTag(null, "ele");
                serializer.startTag(null, "time");
                serializer.text(dateFormat.format(new Date(location.getTime())));
                serializer.endTag(null, "time");
                serializer.endTag(null, "trkpt");
                break;
            case KML:
                //TODO:Change to placemarks in future
                serializer.text(StringHelper.concat(((Double) location.getLatitude()).toString(),",",((Double) location.getLongitude()).toString(),",",((Double) location.getAltitude()).toString(),"\r\n"));
                break;
        }
    }

    private void endDocument() throws IOException {
        switch (currentExtension) {
            case GPX:
                serializer.endTag(null,"trk");
                serializer.endTag(null,"gpx");
                serializer.endDocument();
                break;
            case KML:
                serializer.endTag(null,"coordinates");
                serializer.endTag(null,"LineString");
                serializer.endTag(null,"Placemark");
                serializer.endTag(null,"kml");
                serializer.endDocument();
                break;
        }
    }
}
