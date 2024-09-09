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
public class VehicleOwnerHistory {

    private SimpleStringProperty tblViewVhclOwnHsty01;
    private SimpleStringProperty tblViewVhclOwnHsty02;
    private SimpleStringProperty tblViewVhclOwnHsty03;
    private SimpleStringProperty tblViewVhclOwnHsty04;
    private SimpleStringProperty tblViewVhclOwnHsty05;

    public VehicleOwnerHistory(String tblViewVhclOwnHsty01,
            String tblViewVhclOwnHsty02,
            String tblViewVhclOwnHsty03,
            String tblViewVhclOwnHsty04,
            String tblViewVhclOwnHsty05
    ) {
        this.tblViewVhclOwnHsty01 = new SimpleStringProperty(tblViewVhclOwnHsty01);
        this.tblViewVhclOwnHsty02 = new SimpleStringProperty(tblViewVhclOwnHsty02);
        this.tblViewVhclOwnHsty03 = new SimpleStringProperty(tblViewVhclOwnHsty03);
        this.tblViewVhclOwnHsty04 = new SimpleStringProperty(tblViewVhclOwnHsty04);
        this.tblViewVhclOwnHsty05 = new SimpleStringProperty(tblViewVhclOwnHsty05);
    }

    public String getTblViewVhclOwnHsty01() {
        return tblViewVhclOwnHsty01.get();
    }

    public void setTblViewVhclOwnHsty01(String tblViewVhclOwnHsty01) {
        this.tblViewVhclOwnHsty01.set(tblViewVhclOwnHsty01);
    }

    public String getTblViewVhclOwnHsty02() {
        return tblViewVhclOwnHsty02.get();
    }

    public void setTblViewVhclOwnHsty02(String tblViewVhclOwnHsty02) {
        this.tblViewVhclOwnHsty02.set(tblViewVhclOwnHsty02);
    }

    public String getTblViewVhclOwnHsty03() {
        return tblViewVhclOwnHsty03.get();
    }

    public void setTblViewVhclOwnHsty03(String tblViewVhclOwnHsty03) {
        this.tblViewVhclOwnHsty03.set(tblViewVhclOwnHsty03);
    }

    public String getTblViewVhclOwnHsty04() {
        return tblViewVhclOwnHsty04.get();
    }

    public void setTblViewVhclOwnHsty04(String tblViewVhclOwnHsty04) {
        this.tblViewVhclOwnHsty04.set(tblViewVhclOwnHsty04);
    }

    public String getTblViewVhclOwnHsty05() {
        return tblViewVhclOwnHsty05.get();
    }

    public void setTblViewVhclOwnHsty05(String tblViewVhclOwnHsty05) {
        this.tblViewVhclOwnHsty05.set(tblViewVhclOwnHsty05);
    }
}
