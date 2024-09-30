/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.interfaces;

import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author AutoGroup Programmers
 */
public interface GRecordInterface {

    void setGRider(GRider foValue);

    void initCapitalizationFields();

    void initTextFieldFocus();

    void initTextKeyPressed();

    void initButtonsClick();

    void initComboBoxItems();

    void initFieldsAction();

    void initTextFieldsProperty();

    void clearTables();

    void clearFields();

    void initFields(int fnValue);
}
