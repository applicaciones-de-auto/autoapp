/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author AutoGroup Programmers
 */
public class VSPReservationInquirers {

    private SimpleStringProperty tblindex01_reservation;
    private CheckBox select;
    private SimpleStringProperty tblindex02_reservation;
    private SimpleStringProperty tblindex03_reservation;
    private SimpleStringProperty tblindex04_reservation;
    private SimpleStringProperty tblindex05_reservation;
    private SimpleStringProperty tblindex06_reservation;
    private SimpleStringProperty tblindex07_reservation;
    private SimpleStringProperty tblindex08_reservation;

    public VSPReservationInquirers(String tblindex01_reservation,
            String tblindex02_reservation,
            String tblindex03_reservation,
            String tblindex04_reservation,
            String tblindex05_reservation,
            String tblindex06_reservation,
            String tblindex07_reservation,
            String tblindex08_reservation) {
        this.tblindex01_reservation = new SimpleStringProperty(tblindex01_reservation);
        this.select = new CheckBox();
        this.tblindex02_reservation = new SimpleStringProperty(tblindex02_reservation);
        this.tblindex03_reservation = new SimpleStringProperty(tblindex03_reservation);
        this.tblindex04_reservation = new SimpleStringProperty(tblindex04_reservation);
        this.tblindex05_reservation = new SimpleStringProperty(tblindex05_reservation);
        this.tblindex06_reservation = new SimpleStringProperty(tblindex06_reservation);
        this.tblindex07_reservation = new SimpleStringProperty(tblindex07_reservation);
        this.tblindex08_reservation = new SimpleStringProperty(tblindex08_reservation);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex01_reservation() {
        return tblindex01_reservation.get();
    }

    public void setTblindex01_reservation(String tblindex01_reservation) {
        this.tblindex01_reservation.set(tblindex01_reservation);
    }

    public String getTblindex02_reservation() {
        return tblindex02_reservation.get();
    }

    public void setTblindex02_reservation(String tblindex02_reservation) {
        this.tblindex02_reservation.set(tblindex02_reservation);
    }

    public String getTblindex03_reservation() {
        return tblindex03_reservation.get();
    }

    public void setTblindex03_reservation(String tblindex03_reservation) {
        this.tblindex03_reservation.set(tblindex03_reservation);
    }

    public String getTblindex04_reservation() {
        return tblindex04_reservation.get();
    }

    public void setTblindex04_reservation(String tblindex04_reservation) {
        this.tblindex04_reservation.set(tblindex04_reservation);
    }

    public String getTblindex05_reservation() {
        return tblindex05_reservation.get();
    }

    public void setTblindex05_reservation(String tblindex05_reservation) {
        this.tblindex05_reservation.set(tblindex05_reservation);
    }

    public String getTblindex06_reservation() {
        return tblindex06_reservation.get();
    }

    public void setTblindex06_reservation(String tblindex06_reservation) {
        this.tblindex06_reservation.set(tblindex06_reservation);
    }

    public String getTblindex07_reservation() {
        return tblindex07_reservation.get();
    }

    public void setTblindex07_reservation(String tblindex07_reservation) {
        this.tblindex07_reservation.set(tblindex07_reservation);
    }

    public String getTblindex08_reservation() {
        return tblindex08_reservation.get();
    }

    public void setTblindex08_reservation(String tblindex08_reservation) {
        this.tblindex08_reservation.set(tblindex08_reservation);
    }
}
