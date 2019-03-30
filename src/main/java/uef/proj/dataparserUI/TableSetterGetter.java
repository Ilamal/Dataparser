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
    TextField startDay;
    TextField endDay;
    String name;
    String alias; 
    CheckBox cb_default;
    CheckBox cb_average;

    public TableSetterGetter(String name,String alias, TextField startDay, TextField endDay, CheckBox checkBox, CheckBox checkBox2) {
        this.name = name;
        this.alias = alias;
        this.startDay = startDay;
        this.endDay = endDay;
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

    public TextField getStartDay() {
        return startDay;
    }

    public void setStartDay(TextField startDay) {
        this.startDay = startDay;
    }

    public TextField getEndDay() {
        return endDay;
    }

    public void setEndDay(TextField endDay) {
        this.endDay = endDay;
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
