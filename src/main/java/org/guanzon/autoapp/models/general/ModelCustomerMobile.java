/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 *
 * @author AutoGroup Programmers
 */
public class ModelCustomerMobile {

    private SimpleStringProperty contindex01;
    private SimpleStringProperty contindex02;
    private SimpleStringProperty contindex03;
    private SimpleStringProperty contindex04;
    private SimpleStringProperty contindex05;
    private SimpleStringProperty contindex06;
    private SimpleStringProperty contindex07;

    public ModelCustomerMobile(String contindex01,
            String contindex02,
            String contindex03,
            String contindex04,
            String contindex05,
            String contindex06,
            String contindex07
    ) {

        this.contindex01 = new SimpleStringProperty(contindex01);
        this.contindex02 = new SimpleStringProperty(contindex02);
        this.contindex03 = new SimpleStringProperty(contindex03);
        this.contindex04 = new SimpleStringProperty(contindex04);
        this.contindex05 = new SimpleStringProperty(contindex05);
        this.contindex06 = new SimpleStringProperty(contindex06);
        this.contindex07 = new SimpleStringProperty(contindex07);
    }

    public String getContindex01() {
        return contindex01.get();
    }

    public void setContindex01(String contindex01) {
        this.contindex01.set(contindex01);
    }

    public String getContindex02() {
        return contindex02.get();
    }

    public void setContindex02(String contindex02) {
        this.contindex02.set(contindex02);
    }

    public String getContindex03() {
        return contindex03.get();
    }

    public void setContindex03(String contindex03) {
        this.contindex03.set(contindex03);
    }

    public String getContindex04() {
        return contindex04.get();
    }

    public void setContindex04(String contindex04) {
        this.contindex04.set(contindex04);
    }

    public String getContindex05() {
        return contindex05.get();
    }

    public void setContindex05(String contindex05) {
        this.contindex05.set(contindex05);
    }

    public String getContindex06() {
        return contindex06.get();
    }

    public void setContindex06(String contindex06) {
        this.contindex06.set(contindex06);
    }

    public String getContindex07() {
        return contindex07.get();
    }

    public void setContindex07(String contindex07) {
        this.contindex07.set(contindex07);
    }

}
