package org.example;

public class Events {
    private String node;
    private String description;
    private double point;
    private int trueOrFalse;
    public Events(){
    }
    public Events(String nod,String desc,int trueOrFalse){
        this.setTrueOrFalse(trueOrFalse);
        this.node=nod;
        this.description=desc;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTrueOrFalse() {
        return trueOrFalse;
    }
    public void setTrueOrFalse(int trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }
}
