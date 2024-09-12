/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Labor {

    private SimpleStringProperty tblindex01_labor;
    private CheckBox select;
    private SimpleStringProperty tblindex02_labor;
    private SimpleStringProperty tblindex03_labor;
    private SimpleStringProperty tblindex04_labor;
    private SimpleStringProperty tblindex05_labor;
    private SimpleStringProperty tblindex06_labor;
    private SimpleStringProperty tblindex07_labor;
    private SimpleStringProperty tblindex07_labor_Rust;
    private SimpleStringProperty tblindex07_labor_Perma;
    private SimpleStringProperty tblindex07_labor_Under;
    private SimpleStringProperty tblindex07_labor_Tint;
    private SimpleStringProperty tblindex08_labor;
    private SimpleStringProperty tblindex09_labor;
    private SimpleStringProperty tblindex10_labor;
    private CheckBox addOrNot;
    private SimpleBooleanProperty tblindex11_labor;
    private SimpleBooleanProperty tblindex12_labor;
    private CheckBox FreeOrNot;

    public Labor(String tblindex01_labor,
            String tblindex02_labor,
            String tblindex03_labor,
            String tblindex04_labor,
            String tblindex05_labor,
            String tblindex06_labor,
            String tblindex07_labor,
            String tblindex07_labor_Rust,
            String tblindex07_labor_Perma,
            String tblindex07_labor_Under,
            String tblindex07_labor_Tint,
            String tblindex08_labor,
            String tblindex09_labor,
            String tblindex10_labor,
            boolean tblindex11_labor,
            boolean tblindex12_labor
    ) {
        this.tblindex01_labor = new SimpleStringProperty(tblindex01_labor);
        this.select = new CheckBox();
        this.tblindex02_labor = new SimpleStringProperty(tblindex02_labor);
        this.tblindex03_labor = new SimpleStringProperty(tblindex03_labor);
        this.tblindex04_labor = new SimpleStringProperty(tblindex04_labor);
        this.tblindex05_labor = new SimpleStringProperty(tblindex05_labor);
        this.tblindex06_labor = new SimpleStringProperty(tblindex06_labor);
        this.tblindex07_labor = new SimpleStringProperty(tblindex07_labor);
        this.tblindex07_labor_Rust = new SimpleStringProperty(tblindex07_labor_Rust);
        this.tblindex07_labor_Perma = new SimpleStringProperty(tblindex07_labor_Perma);
        this.tblindex07_labor_Under = new SimpleStringProperty(tblindex07_labor_Under);
        this.tblindex07_labor_Tint = new SimpleStringProperty(tblindex07_labor_Tint);
        this.tblindex08_labor = new SimpleStringProperty(tblindex08_labor);
        this.tblindex09_labor = new SimpleStringProperty(tblindex09_labor);
        this.tblindex10_labor = new SimpleStringProperty(tblindex10_labor);
        this.addOrNot = new CheckBox();
        this.tblindex11_labor = new SimpleBooleanProperty(tblindex11_labor);
        this.addOrNot.setSelected(tblindex11_labor);
        addOrNot.setDisable(true);
        this.FreeOrNot = new CheckBox();
        this.tblindex12_labor = new SimpleBooleanProperty(tblindex12_labor);
        this.FreeOrNot.setSelected(tblindex12_labor);
        FreeOrNot.setDisable(true);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public CheckBox getAddOrNot() {
        return addOrNot;
    }

    public void setAddOrNot(CheckBox addOrNot) {
        this.addOrNot = addOrNot;
    }

    public boolean isTblindex11_Labor() {
        return tblindex11_labor.get();
    }

    public void setTblindex11_Labor(boolean tblindex11_labor) {
        this.tblindex11_labor.set(tblindex11_labor);
    }

    public BooleanProperty selectedProperty() {
        return tblindex11_labor;
    }

    public CheckBox getFreeOrNot() {
        return FreeOrNot;
    }

    public void setFreeOrNot(CheckBox FreeOrNot) {
        this.FreeOrNot = FreeOrNot;
    }

    public boolean isTblindex12_Labor() {
        return tblindex12_labor.get();
    }

    public void setTblindex12_Labor(boolean tblindex12_labor) {
        this.tblindex12_labor.set(tblindex12_labor);
    }

    public BooleanProperty selectedProperty12_Labor() {
        return tblindex12_labor;
    }

    public String laborProperty() {
        return tblindex04_labor.get();
    }

    public String getTblindex01_labor() {
        return tblindex01_labor.get();
    }

    public void setTblindex01_labor(String tblindex01_labor) {
        this.tblindex01_labor.set(tblindex01_labor);
    }

    public String getTblindex02_labor() {
        return tblindex02_labor.get();
    }

    public void setTblindex02_labor(String tblindex02_labor) {
        this.tblindex02_labor.set(tblindex02_labor);
    }

    public String getTblindex03_labor() {
        return tblindex03_labor.get();
    }

    public void setTblindex03_labor(String tblindex03_labor) {
        this.tblindex03_labor.set(tblindex03_labor);
    }

    public String getTblindex04_labor() {
        return tblindex04_labor.get();
    }

    public void setTblindex04_labor(String tblindex04_labor) {
        this.tblindex04_labor.set(tblindex04_labor);
    }

    public String getTblindex05_labor() {
        return tblindex05_labor.get();
    }

    public void setTblindex05_labor(String tblindex05_labor) {
        this.tblindex05_labor.set(tblindex05_labor);
    }

    public String getTblindex06_labor() {
        return tblindex06_labor.get();
    }

    public void setTblindex06_labor(String tblindex06_labor) {
        this.tblindex06_labor.set(tblindex06_labor);
    }

    public String getTblindex07_labor() {
        return tblindex07_labor.get();
    }

    public void setTblindex07_labor(String tblindex07_labor) {
        this.tblindex07_labor.set(tblindex07_labor);
    }

    public String getTblindex07_labor_Rust() {
        return tblindex07_labor_Rust.get();
    }

    public void setTblindex07_labor_Rust(String tblindex07_labor_Rust) {
        this.tblindex07_labor_Rust.set(tblindex07_labor_Rust);
    }

    public String getTblindex07_labor_Perma() {
        return tblindex07_labor_Perma.get();
    }

    public void setTblindex07_labor_Perma(String tblindex07_labor_Perma) {
        this.tblindex07_labor_Perma.set(tblindex07_labor_Perma);
    }

    public String getTblindex07_labor_Under() {
        return tblindex07_labor_Under.get();
    }

    public void setTblindex07_labor_Under(String tblindex07_labor_Under) {
        this.tblindex07_labor_Under.set(tblindex07_labor_Under);
    }

    public String getTblindex07_labor_Tint() {
        return tblindex07_labor_Tint.get();
    }

    public void setTblindex07_labor_Tint(String tblindex07_labor_Tint) {
        this.tblindex07_labor_Tint.set(tblindex07_labor_Tint);
    }

    public String getTblindex08_labor() {
        return tblindex08_labor.get();
    }

    public void setTblindex08_labor(String tblindex08_labor) {
        this.tblindex08_labor.set(tblindex08_labor);
    }

    public String getTblindex09_labor() {
        return tblindex09_labor.get();
    }

    public void setTblindex09_labor(String tblindex09_labor) {
        this.tblindex09_labor.set(tblindex09_labor);
    }

    public String getTblindex10_labor() {
        return tblindex10_labor.get();
    }

    public void setTblindex10_labor(String tblindex10_labor) {
        this.tblindex10_labor.set(tblindex10_labor);
    }

}
