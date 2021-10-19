package EngineSkill.Drools.Entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Alert {
    private static volatile AtomicInteger ID = new AtomicInteger(0);
    private int eventid;
    private String title;//告警标题
    private String text;//告警内容
    private int alertStatus;//告警级别<02.drl:主要，3：次要，4：警告，6：清除>
    private String moIP;//告警所在ip地址


    public Alert(String title,String text,int alertStatus,String moIP){
        this.eventid = ID.getAndIncrement();
        this.title = title;
        this.text = text;
        this.alertStatus = alertStatus;
        this.moIP = moIP;
    }


    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
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

    public int getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(int alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getMoIP() {
        return moIP;
    }

    public void setMoIP(String moIP) {
        this.moIP = moIP;
    }

    public String toString(){
        return String.format("Alert{id=%s,title=%s,text=%s,alertStatus=%d,moIP=%s}",
                eventid,title,text,alertStatus,moIP);
    }


}
