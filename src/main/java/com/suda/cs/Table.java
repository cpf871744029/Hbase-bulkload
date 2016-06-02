package com.suda.cs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 2016/6/1.
 */
public class Table {
    private String name;
    private String rwokey;
    private List<Family> families = new ArrayList<Family>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRwokey() {
        return rwokey;
    }

    public void setRwokey(String rwokey) {
        this.rwokey = rwokey;
    }

    public List<Family> getFamilies() {
        return families;
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }

    @Override
    public String toString() {
        return "name:\t" + this.name + "\n" +
                "rowkey:\t" + this.rwokey + "\n" +
                families.toString();
    }
}
class Family{
    private String name;
    private List<String> qualifies = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQualifies() {
        return qualifies;
    }

    public void setQualifies(List<String> qualifies) {
        this.qualifies = qualifies;
    }

    @Override
    public String toString() {
        return "Family{" +
                "name='" + name + '\'' +
                ", qualifies=" + qualifies.toString() +
                '}';
    }
}
