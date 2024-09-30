/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.interfaces;

import java.awt.Component;
import javafx.event.ActionEvent;
import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author AutoGroup Programmers
 */
public interface GPrintInterface {

    void setGRider(GRider foValue);

    void setTransNo(String fsValue);

    void handleButtonAction(ActionEvent event);

    void hideReport();

    void generateReport();

    void loadReport();

    void showReport();

    void findAndHideButton(Component foComponent, String fsButtonText);
}
