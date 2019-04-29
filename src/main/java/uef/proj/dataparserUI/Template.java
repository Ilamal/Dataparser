/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import java.io.Serializable;

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård, Toni Takkinen
 * @version 1.0
 * Changed to similar Object to HeaderInfo
 */
public class Template implements Serializable {
    
    /**
     *
     */
    String heading;

    /**
     *
     */
    String alias;

    /**
     *
     */
    boolean normal;

    /**
     *
     */
    boolean avg;
    
    /**
     *
     */
    public Template() {
    }

    /**
     *
     * @param heading String variable for Template object.
     * @param alias String variable for Template object.
     * @param normal boolean variable for Template object.
     * @param avg boolean variable for Template object.
     */
    public Template(String heading, String alias, boolean normal, boolean avg) {
        this.heading = heading;
        this.alias = alias;
        this.normal = normal;
        this.avg = avg;
    }    

    /**
     *
     * @return returns heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     *
     * @param heading heading for Template object.
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     *
     * @return returns alias.
     */
    public String getAlias() {
        return alias;
    } 

    /**
     *
     * @param alias alias for Template.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     *
     * @return returns boolean normal.
     */
    public boolean isNormal() {
        return normal;
    }

    /**
     *
     * @param normal boolean value for normal.
     */
    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    /**
     *
     * @return returns value for avg.
     */
    public boolean isAvg() {
        return avg;
    }

    /**
     *
     * @param avg avg for Template.
     */
    public void setAvg(boolean avg) {
        this.avg = avg;
    }
    
    
}
