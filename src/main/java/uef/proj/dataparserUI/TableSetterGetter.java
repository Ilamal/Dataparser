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
    CheckBox checkBox;
    CheckBox checkBox2;

    public TableSetterGetter(String name, TextField startDay, TextField endDay, CheckBox checkBox, CheckBox checkBox2) {
        this.name = name;
        this.startDay = startDay;
        this.endDay = endDay;
        this.checkBox = checkBox;
        this.checkBox2 = checkBox2;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public CheckBox getCheckBox2() {
        return checkBox2;
    }

    public void setCheckBox2(CheckBox checkBox2) {
        this.checkBox2 = checkBox2;
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
    
}
