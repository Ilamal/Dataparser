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

    /**
     *
     */
    String name;

    /**
     *
     */
    String alias; 

    /**
     *
     */
    CheckBox cb_default;

    /**
     *
     */
    CheckBox cb_average;

    /**
     *
     * @param name
     * @param alias
     * @param checkBox
     * @param checkBox2
     */
    public TableSetterGetter(String name,String alias, CheckBox checkBox, CheckBox checkBox2) {
        this.name = name;
        this.alias = alias;       
        this.cb_default = checkBox;
        this.cb_average = checkBox2;
    }

    /**
     *
     * @return
     */
    public CheckBox getCheckBox() {
        return cb_default;
    }

    /**
     *
     * @param checkBox
     */
    public void setCheckBox(CheckBox checkBox) {
        this.cb_default = checkBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getCheckBox2() {
        return cb_average;
    }

    /**
     *
     * @param checkBox2
     */
    public void setCheckBox2(CheckBox checkBox2) {
        this.cb_average = checkBox2;
    }   

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
}
