/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class InquiryRequirements {

    private SimpleBooleanProperty tblindex01; //Check box
    private SimpleStringProperty tblindex02; //Requirements
    private SimpleStringProperty tblindex03; //Received By
    private SimpleStringProperty tblindex04; //Received Date
    private SimpleStringProperty tblindex05; //Requirements code
    private SimpleStringProperty tblindex06; //Requirements code

    public InquiryRequirements(boolean tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex04, String tblindex05,
            String tblindex06
    ) {
        this.tblindex01 = new SimpleBooleanProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
    }

    //Check box
    public boolean isTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(boolean tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public BooleanProperty selectedProperty() {
        return tblindex01;
    }

    //Requirements
    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    //Received By
    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    //Received Date
    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }
    //Received Date

    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    public String getTblindex06() {
        return tblindex06.get();
    }

    public void setTblindex06(String tblindex06) {
        this.tblindex06.set(tblindex06);
    }
}
