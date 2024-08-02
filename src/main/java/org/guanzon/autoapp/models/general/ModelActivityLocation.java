/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author AutoGroup Programmers
 */
public class ModelActivityLocation {

    private SimpleStringProperty tblindexLocation01; // Row
    private SimpleStringProperty tblindexLocation02; // StreetName
    private SimpleStringProperty tblindexLocation03; // Town ID
    private SimpleStringProperty tblindexLocation04; // Town Name
    private SimpleStringProperty tblindexLocation05; // Establishment
    private SimpleStringProperty tblindexLocation06; // ZipCode
    private SimpleStringProperty tblindexLocation07; // ProvinceID
    private SimpleStringProperty tblindexLocation08; // Province Name
    private SimpleStringProperty tblindexLocation09; // Barangay ID
    private SimpleStringProperty tblindexLocation10; // Barangay Name

    public ModelActivityLocation(String tblindexLocation01,
            String tblindexLocation02,
            String tblindexLocation03,
            String tblindexLocation04,
            String tblindexLocation05,
            String tblindexLocation06,
            String tblindexLocation07,
            String tblindexLocation08,
            String tblindexLocation09,
            String tblindexLocation10) {
        this.tblindexLocation01 = new SimpleStringProperty(tblindexLocation01);
        this.tblindexLocation02 = new SimpleStringProperty(tblindexLocation02);
        this.tblindexLocation03 = new SimpleStringProperty(tblindexLocation03);
        this.tblindexLocation04 = new SimpleStringProperty(tblindexLocation04);
        this.tblindexLocation05 = new SimpleStringProperty(tblindexLocation05);
        this.tblindexLocation06 = new SimpleStringProperty(tblindexLocation06);
        this.tblindexLocation07 = new SimpleStringProperty(tblindexLocation07);
        this.tblindexLocation08 = new SimpleStringProperty(tblindexLocation08);
        this.tblindexLocation09 = new SimpleStringProperty(tblindexLocation09);
        this.tblindexLocation10 = new SimpleStringProperty(tblindexLocation10);
    }

    public String getTblindexLocation01() {
        return tblindexLocation01.get();
    }

    public void setTblindexLocation01(String tblindexLocation01) {
        this.tblindexLocation01.set(tblindexLocation01);
    }

    public String getTblindexLocation02() {
        return tblindexLocation02.get();
    }

    public void setTblindexLocation02(String tblindexLocation02) {
        this.tblindexLocation02.set(tblindexLocation02);
    }

    public String getTblindexLocation03() {
        return tblindexLocation03.get();
    }

    public void setTblindexLocation03(String tblindexLocation03) {
        this.tblindexLocation03.set(tblindexLocation03);
    }

    public String getTblindexLocation04() {
        return tblindexLocation04.get();
    }

    public void setTblindexLocation04(String tblindexLocation04) {
        this.tblindexLocation04.set(tblindexLocation04);
    }

    public String getTblindexLocation05() {
        return tblindexLocation05.get();
    }

    public void setTblindexLocation05(String tblindexLocation05) {
        this.tblindexLocation05.set(tblindexLocation05);
    }

    public String getTblindexLocation06() {
        return tblindexLocation06.get();
    }

    public void setTblindexLocation06(String tblindexLocation06) {
        this.tblindexLocation06.set(tblindexLocation06);
    }

    public String getTblindexLocation07() {
        return tblindexLocation07.get();
    }

    public void setTblindexLocation07(String tblindexLocation07) {
        this.tblindexLocation07.set(tblindexLocation07);
    }

    public String getTblindexLocation08() {
        return tblindexLocation08.get();
    }

    public void setTblindexLocation08(String tblindexLocation08) {
        this.tblindexLocation08.set(tblindexLocation08);
    }

    public String getTblindexLocation09() {
        return tblindexLocation09.get();
    }

    public void setTblindexLocation09(String tblindexLocation09) {
        this.tblindexLocation09.set(tblindexLocation09);
    }

    public String getTblindexLocation10() {
        return tblindexLocation10.get();
    }

    public void setTblindexLocation10(String tblindexLocation10) {
        this.tblindexLocation10.set(tblindexLocation10);
    }

}
