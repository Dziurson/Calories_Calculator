package pl.edu.agh.student.calcalc.enums;

public enum OutputFileFormat {
    KML("kml"),
    GPX("gpx");

    private final String fileExtension;

    OutputFileFormat(String fileExtension_) {
        fileExtension = fileExtension_;
    }

    public String getExtensionWithDot() {
        return "." + fileExtension;
    }
}
