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
public class ModelActivityVehicle {

    private SimpleStringProperty tblindexVhcl01;
    private CheckBox select;
    private SimpleStringProperty tblindexVhcl02;//sSerialID
    private SimpleStringProperty tblindexVhcl03; //sDescript
    private SimpleStringProperty tblindexVhcl04; //sCSNoxxxx

    public ModelActivityVehicle(String tblindexVhcl01, String tblindexVhcl02, String tblindexVhcl03, String tblindexVhcl04) {
        this.tblindexVhcl01 = new SimpleStringProperty(tblindexVhcl01);
        this.select = new CheckBox();
        this.tblindexVhcl02 = new SimpleStringProperty(tblindexVhcl02);
        this.tblindexVhcl03 = new SimpleStringProperty(tblindexVhcl03);
        this.tblindexVhcl04 = new SimpleStringProperty(tblindexVhcl04);
    }

    public String getTblIndexVhcl01() {
        return tblindexVhcl01.get();
    }

    public void setTblIndexVhcl01(String tblindexVchl01) {
        this.tblindexVhcl01.set(tblindexVchl01);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblIndexVhcl02() {
        return tblindexVhcl02.get();
    }

    public void setTblIndexVhcl02(String tblindexVchl02) {
        this.tblindexVhcl02.set(tblindexVchl02);
    }

    public String getTblIndexVchl03() {
        return tblindexVhcl03.get();
    }

    public void setTblIndexVchl03(String tblindexVchl03) {
        this.tblindexVhcl03.set(tblindexVchl03);
    }

    public String getTblIndexVchl04() {
        return tblindexVhcl04.get();
    }

    public void setTblIndexVchl04(String tblindexVchl04) {
        this.tblindexVhcl04.set(tblindexVchl04);
    }

}
