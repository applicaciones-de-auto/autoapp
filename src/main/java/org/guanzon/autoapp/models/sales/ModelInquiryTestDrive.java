/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.sales;

import org.guanzon.autoapp.models.general.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author AutoGroup Programmers
 */
public class ModelInquiryTestDrive {

    private SimpleStringProperty tblindex01;
    private CheckBox select;
    private SimpleStringProperty tblindex02; //TestDrive Model

    public ModelInquiryTestDrive(String tblindex01, String tblindex02) {
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.select = new CheckBox();
        this.tblindex02 = new SimpleStringProperty(tblindex02);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

}
