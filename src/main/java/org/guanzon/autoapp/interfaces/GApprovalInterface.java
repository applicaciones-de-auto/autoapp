package org.guanzon.autoapp.interfaces;

import javafx.event.ActionEvent;
import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author AutoGroup Programmers
 */
public interface GApprovalInterface {

    void setGRider(GRider foValue);

    void loadTable();

    void initLoadTable();

    void handleButtonAction(ActionEvent event);

    void initFields();
}
