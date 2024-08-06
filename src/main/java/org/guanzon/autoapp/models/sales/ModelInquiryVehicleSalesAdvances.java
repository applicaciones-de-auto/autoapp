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
public class ModelInquiryVehicleSalesAdvances {

    private SimpleBooleanProperty tblcheck01; //Check box
    private SimpleStringProperty tblindex01; //Row
    private SimpleStringProperty tblindex02; //Slip Date
    private SimpleStringProperty tblindex03; //Slip Type
    private SimpleStringProperty tblindex04; //Slip No
    private SimpleStringProperty tblindex05; //Slip Amount
    private SimpleStringProperty tblindex06; //Slip Status
    private SimpleStringProperty tblindex07; //Remarks
    private SimpleStringProperty tblindex08; //Approved By
    private SimpleStringProperty tblindex09; //Approved Date
    private SimpleStringProperty tblindex10; //rsvcode	sTransNox
    private SimpleStringProperty tblindex11; //clientname	sCompnyNm
    private SimpleStringProperty tblindex12; //SE Name sSeName
    private SimpleStringProperty tblindex13; //unit description sDescript

    public ModelInquiryVehicleSalesAdvances(Boolean tblcheck01,
            String tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex04,
            String tblindex05,
            String tblindex06,
            String tblindex07,
            String tblindex08,
            String tblindex09,
            String tblindex10,
            String tblindex11,
            String tblindex12,
            String tblindex13
    ) {

        this.tblcheck01 = new SimpleBooleanProperty(tblcheck01);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex10 = new SimpleStringProperty(tblindex10);
        this.tblindex11 = new SimpleStringProperty(tblindex11);
        this.tblindex12 = new SimpleStringProperty(tblindex12);
        this.tblindex13 = new SimpleStringProperty(tblindex13);
    }
    //Check box

    public boolean isTblcheck01() {
        return tblcheck01.get();
    }

    public void setTblcheck01(boolean tblcheck01) {
        this.tblcheck01.set(tblcheck01);
    }

    public BooleanProperty selectedProperty() {
        return tblcheck01;
    }

    //Row
    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }
    //Slip Date

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }
    //Slip Type

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }
    //Slip No

    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }
    //Slip Amount

    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }
    //Slip Status

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
    //Approved By

    public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }
    //Approved Date

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }
    //RSVCODE

    public String getTblindex10() {
        return tblindex10.get();
    }

    public void setTblindex10(String tblindex10) {
        this.tblindex10.set(tblindex10);
    }
    //Custname

    public String getTblindex11() {
        return tblindex11.get();
    }

    public void setTblindex11(String tblindex11) {
        this.tblindex11.set(tblindex11);
    }
    //SE sSeName

    public String getTblindex12() {
        return tblindex12.get();
    }

    public void setTblindex12(String tblindex12) {
        this.tblindex12.set(tblindex12);
    }
    //Unit Description

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }
}
