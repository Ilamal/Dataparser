/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 *
 * @author mikkonyg
 */
public class TableSetterGetter {  
    String name;
    String alias; 
    CheckBox cb_default;
    CheckBox cb_average;

    public TableSetterGetter(String name,String alias, CheckBox checkBox, CheckBox checkBox2) {
        this.name = name;
        this.alias = alias;       
        this.cb_default = checkBox;
        this.cb_average = checkBox2;
    }

    public CheckBox getCheckBox() {
        return cb_default;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.cb_default = checkBox;
    }

    public CheckBox getCheckBox2() {
        return cb_average;
    }

    public void setCheckBox2(CheckBox checkBox2) {
        this.cb_average = checkBox2;
    }   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
      public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
}
