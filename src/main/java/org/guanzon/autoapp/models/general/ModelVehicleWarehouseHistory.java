/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class ModelVehicleWarehouseHistory {

    private SimpleStringProperty tblViewVhclWrhHsty01;
    private SimpleStringProperty tblViewVhclWrhHsty02;
    private SimpleStringProperty tblViewVhclWrhHsty03;
    private SimpleStringProperty tblViewVhclWrhHsty04;
    private SimpleStringProperty tblViewVhclWrhHsty05;

    public ModelVehicleWarehouseHistory(String tblViewVhclWrhHsty01,
            String tblViewVhclWrhHsty02,
            String tblViewVhclWrhHsty03,
            String tblViewVhclWrhHsty04,
            String tblViewVhclWrhHsty05
    ) {
        this.tblViewVhclWrhHsty01 = new SimpleStringProperty(tblViewVhclWrhHsty01);
        this.tblViewVhclWrhHsty02 = new SimpleStringProperty(tblViewVhclWrhHsty02);
        this.tblViewVhclWrhHsty03 = new SimpleStringProperty(tblViewVhclWrhHsty03);
        this.tblViewVhclWrhHsty04 = new SimpleStringProperty(tblViewVhclWrhHsty04);
        this.tblViewVhclWrhHsty05 = new SimpleStringProperty(tblViewVhclWrhHsty05);
    }

    public String getTblViewVhclWrhHsty01() {
        return tblViewVhclWrhHsty01.get();
    }

    public void setTblViewVhclWrhHsty01(String tblViewVhclWrhHsty01) {
        this.tblViewVhclWrhHsty01.set(tblViewVhclWrhHsty01);
    }

    public String getTblViewVhclWrhHsty02() {
        return tblViewVhclWrhHsty02.get();
    }

    public void setTblViewVhclWrhHsty02(String tblViewVhclWrhHsty02) {
        this.tblViewVhclWrhHsty02.set(tblViewVhclWrhHsty02);
    }

    public String getTblViewVhclWrhHsty03() {
        return tblViewVhclWrhHsty03.get();
    }

    public void setTblViewVhclWrhHsty03(String tblViewVhclWrhHsty03) {
        this.tblViewVhclWrhHsty03.set(tblViewVhclWrhHsty03);
    }

    public String getTblViewVhclWrhHsty04() {
        return tblViewVhclWrhHsty04.get();
    }

    public void setTblViewVhclWrhHsty04(String tblViewVhclWrhHsty04) {
        this.tblViewVhclWrhHsty04.set(tblViewVhclWrhHsty04);
    }

    public String getTblViewVhclWrhHsty05() {
        return tblViewVhclWrhHsty05.get();
    }

    public void setTblViewVhclWrhHsty05(String tblViewVhclWrhHsty05) {
        this.tblViewVhclWrhHsty05.set(tblViewVhclWrhHsty05);
    }
}
