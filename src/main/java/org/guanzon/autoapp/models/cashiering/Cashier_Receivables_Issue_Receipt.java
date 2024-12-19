/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.cashiering;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author John Dave
 */
public class Cashier_Receivables_Issue_Receipt {

    private SimpleStringProperty tblindex01;
    private SimpleStringProperty tblindex02; // formtype
    private SimpleStringProperty tblindex03; // sino
    private SimpleStringProperty tblindex04; // si date
    private SimpleStringProperty tblindex05; // sitotal

    public Cashier_Receivables_Issue_Receipt(String tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex04,
            String tblindex05) {
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
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

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindex04() {
        return tblindex04.get();
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
}
