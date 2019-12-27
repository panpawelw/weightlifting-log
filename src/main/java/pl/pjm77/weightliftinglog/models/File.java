package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;

public class File implements Serializable {
    private String filename;
    private String data;

    public File() {
    }

    public File(String filename, String data) {
        this.filename = filename;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
