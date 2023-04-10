package coer.notification;

public class model {
    String filename,fileurl,date;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public model(String filename, String fileurl ) {
        this.filename = filename;
        this.fileurl = fileurl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public model(String date) {
        this.date = date;
    }

    public model() {
    }
}
