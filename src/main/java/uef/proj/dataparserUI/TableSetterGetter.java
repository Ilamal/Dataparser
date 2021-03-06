/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uef.proj.dataparserUI;

import javafx.scene.control.CheckBox;

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård,
 * Toni Takkinen
 * @version 1.0
 */
public class TableSetterGetter {

    String name;

    String alias;

    CheckBox cb_default;

    CheckBox cb_average;

    /**
     *
     * @param name The heading (generated from the user source file)
     * @param alias Similar to heading, custom heading which user can edit
     * @param checkBox Does user want default values (similar to source file)
     * @param checkBox2 Does user want averages generated from source file data
     */
    public TableSetterGetter(String name, String alias, CheckBox checkBox, CheckBox checkBox2) {
        this.name = name;
        this.alias = alias;
        this.cb_default = checkBox;
        this.cb_average = checkBox2;
    }

    /**
     *
     * @return Boolean value of first checkbox
     */
    public CheckBox getCheckBox() {
        return cb_default;
    }

    /**
     *
     * @param checkBox Boolean value of first CheckBox (Default)
     */
    public void setCheckBox(CheckBox checkBox) {
        this.cb_default = checkBox;
    }

    /**
     *
     * @return Boolean value of second CheckBox (Average)
     */
    public CheckBox getCheckBox2() {
        return cb_average;
    }

    /**
     *
     * @param checkBox2 Boolean value of second CheckBox (Average)
     */
    public void setCheckBox2(CheckBox checkBox2) {
        this.cb_average = checkBox2;
    }

    /**
     *
     * @return String of heading
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name String value of heading
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The custom heading (Edited by user)
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @param alias String value of custom heading (Edited by user)
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
