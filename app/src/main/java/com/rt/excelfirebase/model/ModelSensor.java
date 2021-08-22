package com.rt.excelfirebase.model;

public class ModelSensor {

    String pagi,siang,sore,malam;

    public ModelSensor() {
    }

    public String getPagi() {
        return pagi;
    }

    public void setPagi(String pagi) {
        this.pagi = pagi;
    }

    public String getSiang() {
        return siang;
    }

    public void setSiang(String siang) {
        this.siang = siang;
    }

    public String getSore() {
        return sore;
    }

    public void setSore(String sore) {
        this.sore = sore;
    }

    public String getMalam() {
        return malam;
    }

    public void setMalam(String malam) {
        this.malam = malam;
    }

    public ModelSensor(String pagi, String siang, String sore, String malam) {
        this.pagi = pagi;
        this.siang = siang;
        this.sore = sore;
        this.malam = malam;
    }
}
