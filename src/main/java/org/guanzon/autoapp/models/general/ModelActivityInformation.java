///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author
 */
public class ModelActivityInformation {

    private SimpleStringProperty tblindexID;//sActvtyID
    private SimpleStringProperty tblindex01;//Activity No.
    private SimpleStringProperty tblindex02;//From
    private SimpleStringProperty tblindex03;//To
    private SimpleStringProperty tblindex04;//Activity Type
    private SimpleStringProperty tblindex05;//Source
    private SimpleStringProperty tblindex06;//Activity Title
    private SimpleStringProperty tblindex07;//Description
    private SimpleStringProperty tblindex08;//Logistic Remarks
    private SimpleStringProperty tblindex09;//Activity Remarks
    private SimpleStringProperty tblindex10;//Department in charge
    private SimpleStringProperty tblindex11;//Person in charge
    private SimpleStringProperty tblindex12;//Branch in charge
    private SimpleStringProperty tblindex13;//Branch
    private SimpleStringProperty tblindex14;//No. of Target Clients
    private SimpleStringProperty tblindex15;//Total Event Budget
    private SimpleStringProperty tblindex16;//Province
    private SimpleStringProperty tblindex17;//Street
    private SimpleStringProperty tblindex18;//Establishment

    public ModelActivityInformation(String tblindexID,
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
            String tblindex13,
            String tblindex14,
            String tblindex15,
            String tblindex16,
            String tblindex17,
            String tblindex18) {
        this.tblindexID = new SimpleStringProperty(tblindexID);
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
        this.tblindex13 = new SimpleStringProperty(tblindex12);
        this.tblindex14 = new SimpleStringProperty(tblindex13);
        this.tblindex15 = new SimpleStringProperty(tblindex14);
        this.tblindex16 = new SimpleStringProperty(tblindex15);
        this.tblindex17 = new SimpleStringProperty(tblindex16);
        this.tblindex18 = new SimpleStringProperty(tblindex17);

    }

    public String getTblindexID() {
        return tblindexID.get();
    }

    public void setTblindexID(String tblindexID) {
        this.tblindexID.set(tblindexID);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

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

    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

    public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }

    public String getTblindex10() {
        return tblindex10.get();
    }

    public void setTblindex10(String tblindex10) {
        this.tblindex10.set(tblindex10);
    }

    public String getTblindex11() {
        return tblindex11.get();
    }

    public void setTblindex11(String tblindex11) {
        this.tblindex11.set(tblindex11);
    }

    public String getTblindex12() {
        return tblindex12.get();
    }

    public void setTblindex12(String tblindex12) {
        this.tblindex12.set(tblindex12);
    }

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }

    public String getTblindex14() {
        return tblindex14.get();
    }

    public void setTblindex14(String tblindex14) {
        this.tblindex14.set(tblindex14);
    }

    public String getTblindex15() {
        return tblindex15.get();
    }

    public void setTblindex15(String tblindex15) {
        this.tblindex15.set(tblindex15);
    }

    public String getTblindex16() {
        return tblindex16.get();
    }

    public void setTblindex16(String tblindex16) {
        this.tblindex16.set(tblindex16);
    }

    public String getTblindex17() {
        return tblindex17.get();
    }

    public void setTblindex17(String tblindex17) {
        this.tblindex17.set(tblindex17);
    }

    public String getTblindex18() {
        return tblindex18.get();
    }

    public void setTblindex18(String tblindex18) {
        this.tblindex18.set(tblindex18);
    }
}
