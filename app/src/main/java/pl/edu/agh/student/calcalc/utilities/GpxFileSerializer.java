package pl.edu.agh.student.calcalc.utilities;

import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import static android.os.Environment.getExternalStorageDirectory;

public class GpxFileSerializer {

    private XmlSerializer serializer;
    private FileOutputStream fileOutputStream;
    private File gpxFileDir;

    public  GpxFileSerializer() {
        serializer = Xml.newSerializer();
        gpxFileDir = new File(getExternalStorageDirectory().getPath() + UserSettings.userFileDirectory);
        if(!gpxFileDir.exists()) {
            gpxFileDir.mkdirs();
        }
    }

    public boolean start(String filename) {
        try {
            fileOutputStream = new FileOutputStream(gpxFileDir + "/" + filename + ".gpx", true);
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument(null, true);
            serializer.startTag(null,"gpx").attribute(null,"version","1.0");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean stop() {
        try {
            serializer.endTag(null,"gpx");
            serializer.endDocument();
            serializer.flush();
            fileOutputStream.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
