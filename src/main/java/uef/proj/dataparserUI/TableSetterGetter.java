/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import javafx.scene.control.CheckBox;

/**
 *
 * @author mikkonyg
 */
public class TableSetterGetter {
    int id;
    String name;
    CheckBox checkBox;

    public TableSetterGetter(int id, String name, CheckBox checkBox) {
        this.id = id;
        this.name = name;
        this.checkBox = checkBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
    
    
}
