/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Date Created: 04-25-2023
 *
 * @author Arsiela
 */
public class ModelInquiryFollowUp {

    private SimpleStringProperty tblrowxx01; //Row
    private SimpleStringProperty tblindex01; //sRefNox
    private SimpleStringProperty tblindex02; //Follow up date
    private SimpleStringProperty tblindex03; //Next Follow up date
    private SimpleStringProperty tblindex04; //Next Follow up time
    private SimpleStringProperty tblindex05; //Medium
    private SimpleStringProperty tblindex06; //Platform
    private SimpleStringProperty tblindex07; //Remarks

    public ModelInquiryFollowUp(String tblrowxx01,
            String tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex04,
            String tblindex05,
            String tblindex06,
            String tblindex07
    ) {

        this.tblrowxx01 = new SimpleStringProperty(tblrowxx01);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
    }

    //Row
    public String getTblrowxx01() {
        return tblrowxx01.get();
    }

    public void setTblrowxx01(String tblrowxx01) {
        this.tblrowxx01.set(tblrowxx01);
    }

    //sTransNo
    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    //follow up date
    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    //next follow up date
    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    //medium
    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

    //Platform
    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    //Remarks
    public String getTblindex06() {
        return tblindex06.get();
    }

    public void setTblindex06(String tblindex06) {
        this.tblindex06.set(tblindex06);
    }

    //Remarks
    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

}
