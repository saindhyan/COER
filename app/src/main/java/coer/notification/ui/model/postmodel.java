package coer.notification.ui.model;

public class postmodel {
    private String postid;
    private String postimage;
    private String postedbny;
    private String caption;
    private long dt;

    public postmodel() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getPostedbny() {
        return postedbny;
    }

    public void setPostedbny(String postedbny) {
        this.postedbny = postedbny;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public postmodel(String postid, String postimage, String postedbny, String caption, long dt) {
        this.postid = postid;
        this.postimage = postimage;
        this.postedbny = postedbny;
        this.caption = caption;
        this.dt = dt;
    }
}
