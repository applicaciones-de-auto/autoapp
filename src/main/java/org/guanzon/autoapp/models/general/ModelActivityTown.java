/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author
 */
public class ModelActivityTown {

    private SimpleStringProperty tblindexCity01;
    private CheckBox select;
    private SimpleStringProperty tblindexCity02; //sTownIDxx
    private SimpleStringProperty tblindexCity03; //

    public ModelActivityTown(String tblindexCity01, String tblindexCity02, String tblindexCity03) {
        this.tblindexCity01 = new SimpleStringProperty(tblindexCity01);
        this.select = new CheckBox();
        this.tblindexCity02 = new SimpleStringProperty(tblindexCity02);
        this.tblindexCity03 = new SimpleStringProperty(tblindexCity03);
    }

    public String getTblindexCity01() {
        return tblindexCity01.get();
    }

    public void setTblindexCity01(String tblindexCity01) {
        this.tblindexCity01.set(tblindexCity01);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindexCity02() {
        return tblindexCity02.get();
    }

    public void setTblindexCity02(String tblindexCity02) {
        this.tblindexCity02.set(tblindexCity02);
    }

    public String getTblindexCity03() {
        return tblindexCity03.get();
    }

    public void setTblindexCity03(String tblindexCity03) {
        this.tblindexCity03.set(tblindexCity03);
    }

}
