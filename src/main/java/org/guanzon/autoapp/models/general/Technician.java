/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author AutoGroup Programmers
 */
public class Technician {

    private SimpleStringProperty tblindex01_tech;
    private CheckBox select;
    private SimpleStringProperty tblindex02_tech;
    private SimpleStringProperty tblindex03_tech;
    private SimpleStringProperty tblindex04_tech;

    public Technician(String tblindex01_tech,
            String tblindex02_tech,
            String tblindex03_tech,
            String tblindex04_tech) {
        this.tblindex01_tech = new SimpleStringProperty(tblindex01_tech);
        this.tblindex02_tech = new SimpleStringProperty(tblindex02_tech);
        this.tblindex03_tech = new SimpleStringProperty(tblindex03_tech);
        this.tblindex04_tech = new SimpleStringProperty(tblindex04_tech);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex01_tech() {
        return tblindex01_tech.get();
    }

    public void setTblindex01_tech(String tblindex01_tech) {
        this.tblindex01_tech.set(tblindex01_tech);
    }

    public String getTblindex02_tech() {
        return tblindex02_tech.get();
    }

    public void setTblindex02_tech(String tblindex02_tech) {
        this.tblindex02_tech.set(tblindex02_tech);
    }

    public String getTblindex03_tech() {
        return tblindex03_tech.get();
    }

    public void setTblindex03_tech(String tblindex03_tech) {
        this.tblindex03_tech.set(tblindex03_tech);
    }

    public String getTblindex04_tech() {
        return tblindex04_tech.get();
    }

    public void setTblindex04_tech(String tblindex04_tech) {
        this.tblindex04_tech.set(tblindex04_tech);
    }
}
