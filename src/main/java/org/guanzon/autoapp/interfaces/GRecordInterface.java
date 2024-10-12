/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.interfaces;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author AutoGroup Programmers
 */
public interface GRecordInterface {

    void setGRider(GRider foValue);

    void initCapitalizationFields();

    boolean loadMasterFields();

    void initPatternFields();

    void initLimiterFields();

    void initTextFieldFocus();

    void initTextKeyPressed();

    void txtField_KeyPressed(KeyEvent event);

    void textArea_KeyPressed(KeyEvent event);

    void initButtonsClick();

    void handleButtonAction(ActionEvent event);

    void initComboBoxItems();

    void initFieldsAction();

    void initTextFieldsProperty();

    void clearTables();

    void clearFields();

    void initFields(int fnValue);
}
