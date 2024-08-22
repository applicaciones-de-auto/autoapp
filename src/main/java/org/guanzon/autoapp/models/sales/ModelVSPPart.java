/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class ModelVSPPart {

    private SimpleStringProperty tblindex01_part;
    private CheckBox select;
    private SimpleStringProperty tblindex02_part;
    private SimpleStringProperty tblindex03_part;
    private SimpleStringProperty tblindex04_part;
    private SimpleStringProperty tblindex05_part;
    private SimpleStringProperty tblindex06_part;
    private SimpleStringProperty tblindex07_part;
    private SimpleStringProperty tblindex08_part;
    private SimpleStringProperty tblindex09_part;
    private SimpleStringProperty tblindex10_part;
    private SimpleStringProperty tblindex11_part;
    private SimpleStringProperty tblindex12_part;
    private SimpleStringProperty tblindex13_part;
    private SimpleBooleanProperty tblindex14_part;
    private CheckBox FreeOrNot;

    public ModelVSPPart(String tblindex01_part,
            String tblindex02_part,
            String tblindex03_part,
            String tblindex04_part,
            String tblindex05_part,
            String tblindex06_part,
            String tblindex07_part,
            String tblindex08_part,
            String tblindex09_part,
            String tblindex10_part,
            String tblindex11_part,
            String tblindex12_part,
            String tblindex13_part,
            boolean tblindex14_part
    ) {
        this.tblindex01_part = new SimpleStringProperty(tblindex01_part);
        this.select = new CheckBox();
        this.tblindex02_part = new SimpleStringProperty(tblindex02_part);
        this.tblindex03_part = new SimpleStringProperty(tblindex03_part);
        this.tblindex04_part = new SimpleStringProperty(tblindex04_part);
        this.tblindex05_part = new SimpleStringProperty(tblindex05_part);
        this.tblindex06_part = new SimpleStringProperty(tblindex06_part);
        this.tblindex07_part = new SimpleStringProperty(tblindex07_part);
        this.tblindex08_part = new SimpleStringProperty(tblindex08_part);
        this.tblindex09_part = new SimpleStringProperty(tblindex09_part);
        this.tblindex10_part = new SimpleStringProperty(tblindex10_part);
        this.tblindex11_part = new SimpleStringProperty(tblindex11_part);
        this.tblindex12_part = new SimpleStringProperty(tblindex12_part);
        this.tblindex13_part = new SimpleStringProperty(tblindex13_part);
        this.FreeOrNot = new CheckBox();
        this.tblindex14_part = new SimpleBooleanProperty(tblindex14_part);
        this.FreeOrNot.setSelected(tblindex14_part);
        FreeOrNot.setDisable(true);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public CheckBox getFreeOrNot() {
        return FreeOrNot;
    }

    public void setFreeOrNot(CheckBox FreeOrNot) {
        this.FreeOrNot = FreeOrNot;
    }

    public SimpleStringProperty getTblindex01_part() {
        return tblindex01_part;
    }

    public void setTblindex01_part(String tblindex01_part) {
        this.tblindex01_part.set(tblindex01_part);
    }

    public String getTblindex02_part() {
        return tblindex02_part.get();
    }

    public void setTblindex02_part(String tblindex02_part) {
        this.tblindex02_part.set(tblindex02_part);
    }

    public String getTblindex03_part() {
        return tblindex03_part.get();
    }

    public void setTblindex03_part(String tblindex03_part) {
        this.tblindex03_part.set(tblindex03_part);
    }

    public String getTblindex04_part() {
        return tblindex04_part.get();
    }

    public void setTblindex04_part(String tblindex04_part) {
        this.tblindex04_part.set(tblindex04_part);
    }

    public String getTblindex05_part() {
        return tblindex05_part.get();
    }

    public void setTblindex05_part(String tblindex05_part) {
        this.tblindex05_part.set(tblindex05_part);
    }

    public String getTblindex06_part() {
        return tblindex06_part.get();
    }

    public void setTblindex06_part(String tblindex06_part) {
        this.tblindex06_part.set(tblindex06_part);
    }

    public String getTblindex07_part() {
        return tblindex07_part.get();
    }

    public void setTblindex07_part(String tblindex07_part) {
        this.tblindex07_part.set(tblindex07_part);
    }

    public String getTblindex08_part() {
        return tblindex08_part.get();
    }

    public void setTblindex08_part(String tblindex08_part) {
        this.tblindex08_part.set(tblindex08_part);
    }

    public String getTblindex09_part() {
        return tblindex09_part.get();
    }

    public void setTblindex09_part(String tblindex09_part) {
        this.tblindex09_part.set(tblindex09_part);
    }

    public String getTblindex10_part() {
        return tblindex10_part.get();
    }

    public void setTblindex10_part(String tblindex10_part) {
        this.tblindex10_part.set(tblindex10_part);
    }

    public String getTblindex11_part() {
        return tblindex11_part.get();
    }

    public void setTblindex11_part(String tblindex11_part) {
        this.tblindex11_part.set(tblindex11_part);
    }

    public String getTblindex12_part() {
        return tblindex12_part.get();
    }

    public void setTblindex12_part(String tblindex12_part) {
        this.tblindex12_part.set(tblindex12_part);
    }

    public String getTblindex13_part() {
        return tblindex13_part.get();
    }

    public void setTblindex13_part(String tblindex13_part) {
        this.tblindex13_part.set(tblindex13_part);
    }

    public boolean isTblindex14_Part() {
        return tblindex14_part.get();
    }

    public void setTblindex14_Part(boolean tblindex14_part) {
        this.tblindex14_part.set(tblindex14_part);
    }

}
