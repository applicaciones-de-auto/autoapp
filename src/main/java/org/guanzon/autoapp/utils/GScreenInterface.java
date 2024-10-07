/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.utils;

import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author AutoGroup Programmers
 */
public interface GScreenInterface {

    void setGRider(GRider foValue);  //common

    void initCapitalizationFields(); //common

    void initTextFieldFocus();  //common

    void initTextKeyPressed();  //common

    void initButtonsClick();  //common

    void initComboBoxItems();

    void initFieldsAction();

    void initTextFieldsProperty();

    void clearTables();

    void clearFields();  //common

    void initFields(int fnValue);
}
