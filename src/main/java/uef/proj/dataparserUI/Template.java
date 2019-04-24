/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import java.io.Serializable;

/**
 *
 * @author Ilamal
 */
public class Template implements Serializable {
    
    String heading;
    String alias;
    
    public Template() {
    }

    public Template(String heading, String alias) {
        this.heading = heading;
        this.alias = alias;
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
    
    
    
    
}
