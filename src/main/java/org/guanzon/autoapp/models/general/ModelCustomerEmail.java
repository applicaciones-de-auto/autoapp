/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;

/**
 * Date: 03-07-2023
 *
 * @author Arsiela
 */
public class ModelCustomerEmail {

    private SimpleStringProperty emadindex01;
    private SimpleStringProperty emadindex02;
    private SimpleStringProperty emadindex03;
    private SimpleStringProperty emadindex04;
    private SimpleStringProperty emadindex05;

    public ModelCustomerEmail(String emadindex01,
            String emadindex02,
            String emadindex03,
            String emadindex04,
            String emadindex05
    ) {

        this.emadindex01 = new SimpleStringProperty(emadindex01);
        this.emadindex02 = new SimpleStringProperty(emadindex02);
        this.emadindex03 = new SimpleStringProperty(emadindex03);
        this.emadindex04 = new SimpleStringProperty(emadindex04);
        this.emadindex05 = new SimpleStringProperty(emadindex05);
    }

    public String getEmadindex01() {
        return emadindex01.get();
    }

    public void setEmadindex01(String emadindex01) {
        this.emadindex01.set(emadindex01);
    }

    public String getEmadindex02() {
        return emadindex02.get();
    }

    public void setEmadindex02(String emadindex02) {
        this.emadindex02.set(emadindex02);
    }

    public String getEmadindex03() {
        return emadindex03.get();
    }

    public void setEmadindex03(String emadindex03) {
        this.emadindex03.set(emadindex03);
    }

    public String getEmadindex04() {
        return emadindex04.get();
    }

    public void setEmadindex04(String emadindex04) {
        this.emadindex04.set(emadindex04);
    }

    public String getEmadindex05() {
        return emadindex05.get();
    }

    public void setEmadindex05(String emadindex05) {
        this.emadindex05.set(emadindex05);
    }

}
