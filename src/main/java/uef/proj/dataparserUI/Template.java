/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import java.io.Serializable;

/**
 *
 * muutettu samanlaiseksi kuin HeaderInfo.java
 */
public class Template implements Serializable {
    
    String heading;
    String alias;
    boolean normal;
    boolean avg;
    
     public Template() {
    }

    public Template(String heading, String alias, boolean normal, boolean avg) {
        this.heading = heading;
        this.alias = alias;
        this.normal = normal;
        this.avg = avg;
    }    

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getAlias() {
        return alias;
    } 

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public boolean isAvg() {
        return avg;
    }

    public void setAvg(boolean avg) {
        this.avg = avg;
    }
    
    
}
