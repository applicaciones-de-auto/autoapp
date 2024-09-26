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
public class VehicleComponents {

    private SimpleStringProperty tblindex01_vc;
    private CheckBox select;
    private SimpleStringProperty tblindex02_vc;
    private SimpleStringProperty tblindex03_vc;

    public VehicleComponents(String tblindex01_vc,
            String tblindex02_vc,
            String tblindex03_vc
    ) {
        this.tblindex01_vc = new SimpleStringProperty(tblindex01_vc);
        this.select = new CheckBox();
        this.tblindex02_vc = new SimpleStringProperty(tblindex02_vc);
        this.tblindex03_vc = new SimpleStringProperty(tblindex03_vc);
    }

    public String getTblindex01_vc() {
        return tblindex01_vc.get();
    }

    public void setTblindex01_vc(String tblindex01_vc) {
        this.tblindex01_vc.set(tblindex01_vc);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex02_vc() {
        return tblindex02_vc.get();
    }

    public void setTblindex02_vc(String tblindex02_vc) {
        this.tblindex02_vc.set(tblindex02_vc);
    }

    public String getTblindex03_vc() {
        return tblindex03_vc.get();
    }

    public void setTblindex03_vc(String tblindex03_vc) {
        this.tblindex03_vc.set(tblindex03_vc);
    }

}
