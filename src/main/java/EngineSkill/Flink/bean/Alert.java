package EngineSkill.Flink.bean;

public class Alert {
    private long eventid;
    private String title;
    private String text;
    private int status;//2:主要，3：次要，4：警告，6：清除
    private String ipv4;

    public long getEventid() {
        return eventid;
    }

    public void setEventid(long eventid) {
        this.eventid = eventid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public Alert(long eventid, String title, String text, String ipv4) {
        this.eventid = eventid;
        this.title = title;
        this.text = text;
        this.ipv4 = ipv4;
    }

    @Override
    public String toString() {
        return String.format("Alert{" +
                "eventid=%s," +
                "title=%s," +
                "text=%s," +
                "status=%s," +
                "ipv4=%s}", eventid,title,text,status,ipv4) ;
    }
}
