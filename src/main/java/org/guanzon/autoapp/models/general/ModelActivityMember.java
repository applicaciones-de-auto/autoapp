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
public class ModelActivityMember {

    private SimpleStringProperty tblindexMem01;
    private CheckBox select;
    private SimpleStringProperty tblindexMem02; //sDeptIDxx
    private SimpleStringProperty tblindexMem03; //sDeptName
    private SimpleStringProperty tblindexMem04; //sEmployID
    private SimpleStringProperty tblindexMem05; //sCompnyNm

    public ModelActivityMember(String tblindexMem01, String tblindexMem02, String tblindexMem03, String tblindexMem04, String tblindexMem05) {
        this.tblindexMem01 = new SimpleStringProperty(tblindexMem01);
        this.select = new CheckBox();
        this.tblindexMem02 = new SimpleStringProperty(tblindexMem02);
        this.tblindexMem03 = new SimpleStringProperty(tblindexMem03);
        this.tblindexMem04 = new SimpleStringProperty(tblindexMem04);
        this.tblindexMem05 = new SimpleStringProperty(tblindexMem05);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindexMem01() {
        return tblindexMem01.get();
    }

    public void setTblindexMem01(String tblindexMem01) {
        this.tblindexMem01.set(tblindexMem01);
    }

    public String getTblindexMem02() {
        return tblindexMem02.get();
    }

    public void setTblindexMem02(String tblindexMem02) {
        this.tblindexMem02.set(tblindexMem02);
    }

    public String getTblindexMem03() {
        return tblindexMem03.get();
    }

    public void setTblindex14(String tblindexMem03) {
        this.tblindexMem03.set(tblindexMem03);
    }

    public String getTblindexMem04() {
        return tblindexMem04.get();
    }

    public void setTblindexMem04(String tblindex25) {
        this.tblindexMem04.set(tblindex25);
    }

    public String getTblindexMem05() {
        return tblindexMem05.get();
    }

    public void setTblindexMem05(String tblindexMem05) {
        this.tblindexMem05.set(tblindexMem05);
    }

}
