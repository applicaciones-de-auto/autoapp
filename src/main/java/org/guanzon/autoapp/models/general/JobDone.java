/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.models.general;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author AutoGroup Programmers
 */
public class JobDone {

    private SimpleStringProperty tblindex01_done;
    private CheckBox select;
    private SimpleStringProperty tblindex02_done;
    private SimpleStringProperty tblindex03_done;
    private SimpleStringProperty tblindex04_done;
    private SimpleStringProperty tblindex05_done;
    private SimpleStringProperty tblindex06_done;
    private SimpleStringProperty tblindex07_done;
    private SimpleStringProperty tblindex08_done;

    public JobDone(String tblindex01_done,
            String tblindex02_done,
            String tblindex03_done,
            String tblindex04_done,
            String tblindex05_done,
            String tblindex06_done,
            String tblindex07_done,
            String tblindex08_done
    ) {
        this.tblindex01_done = new SimpleStringProperty(tblindex01_done);
        this.select = new CheckBox();
        this.tblindex02_done = new SimpleStringProperty(tblindex02_done);
        this.tblindex03_done = new SimpleStringProperty(tblindex03_done);
        this.tblindex04_done = new SimpleStringProperty(tblindex04_done);
        this.tblindex05_done = new SimpleStringProperty(tblindex05_done);
        this.tblindex06_done = new SimpleStringProperty(tblindex06_done);
        this.tblindex07_done = new SimpleStringProperty(tblindex07_done);
        this.tblindex08_done = new SimpleStringProperty(tblindex08_done);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex01_done() {
        return tblindex01_done.get();
    }

    public void setTblindex01_done(String tblindex01_done) {
        this.tblindex01_done.set(tblindex01_done);
    }

    public String getTblindex02_done() {
        return tblindex02_done.get();
    }

    public void setTblindex02_done(String tblindex02_done) {
        this.tblindex02_done.set(tblindex02_done);
    }

    public String getTblindex03_done() {
        return tblindex03_done.get();
    }

    public void setTblindex03_done(String tblindex03_done) {
        this.tblindex03_done.set(tblindex03_done);
    }

    public String getTblindex04_done() {
        return tblindex04_done.get();
    }

    public void setTblindex04_done(String tblindex04_done) {
        this.tblindex04_done.set(tblindex04_done);
    }

    public String getTblindex05_done() {
        return tblindex05_done.get();
    }

    public void setTblindex05_done(String tblindex05_done) {
        this.tblindex05_done.set(tblindex05_done);
    }

    public String getTblindex06_done() {
        return tblindex06_done.get();
    }

    public void setTblindex06_done(String tblindex06_done) {
        this.tblindex06_done.set(tblindex06_done);
    }

    public String getTblindex07_done() {
        return tblindex07_done.get();
    }

    public void setTblindex07_done(String tblindex07_done) {
        this.tblindex07_done.set(tblindex07_done);
    }

    public String getTblindex08_done() {
        return tblindex08_done.get();
    }

    public void setTblindex08_done(String tblindex08_done) {
        this.tblindex08_done.set(tblindex08_done);
    }
}
