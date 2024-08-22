/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.parts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author AutoGroup Progrmamers
 */
public class ModelItemEntryModelYear {

    private SimpleStringProperty tblindexModel01; //row
    private SimpleStringProperty tblindexModel02; // make id
    private SimpleStringProperty tblindexModel03; // make name
    private CheckBox select; //checkbox
    private SimpleStringProperty tblindexModel04; // model id
    private SimpleStringProperty tblindexModel05; // model name
    private SimpleStringProperty tblindexModel06; // year
    private SimpleStringProperty tblindexModel07;
    private SimpleStringProperty tblindexModel08; //ModeCode

    public ModelItemEntryModelYear(String tblindexModel01,
            String tblindexModel02,
            String tblindexModel03,
            String tblindexModel04,
            String tblindexModel05,
            String tblindexModel06,
            String tblindexModel07,
            String tblindexModel08) {
        this.tblindexModel01 = new SimpleStringProperty(tblindexModel01);
        this.tblindexModel02 = new SimpleStringProperty(tblindexModel02);
        this.tblindexModel03 = new SimpleStringProperty(tblindexModel03);
        this.select = new CheckBox();
        this.tblindexModel04 = new SimpleStringProperty(tblindexModel04);
        this.tblindexModel05 = new SimpleStringProperty(tblindexModel05);
        this.tblindexModel06 = new SimpleStringProperty(tblindexModel06);
        this.tblindexModel07 = new SimpleStringProperty(tblindexModel07);
        this.tblindexModel08 = new SimpleStringProperty(tblindexModel08);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindexModel01() {
        return tblindexModel01.get();
    }

    public void setTblindexModel01(String tblindexModel01) {
        this.tblindexModel01.set(tblindexModel01);
    }

    public String getTblindexModel02() {
        return tblindexModel02.get();
    }

    public void setTblindexModel02(String tblindexModel02) {
        this.tblindexModel02.set(tblindexModel02);
    }

    public String getTblindexModel03() {
        return tblindexModel03.get();
    }

    public void setTblindexModel03(String tblindexModel03) {
        this.tblindexModel03.set(tblindexModel03);
    }

    public String getTblindexModel04() {
        return tblindexModel04.get();
    }

    public void setTblindexModel04(String tblindexModel04) {
        this.tblindexModel04.set(tblindexModel04);
    }

    public String getTblindexModel05() {
        return tblindexModel05.get();
    }

    public void setTblindexModel05(String tblindexModel05) {
        this.tblindexModel05.set(tblindexModel05);
    }

    public String getTblindexModel06() {
        return tblindexModel06.get();
    }

    public void setTblindexModel06(String tblindexModel06) {
        this.tblindexModel06.set(tblindexModel06);
    }

    public String getTblindexModel07() {
        return tblindexModel07.get();
    }

    public void setTblindexModel07(String tblindexModel07) {
        this.tblindexModel07.set(tblindexModel07);
    }

    public String getTblindexModel08() {
        return tblindexModel08.get();
    }

    public void setTblindexModel08(String tblindexModel08) {
        this.tblindexModel08.set(tblindexModel08);
    }
}
