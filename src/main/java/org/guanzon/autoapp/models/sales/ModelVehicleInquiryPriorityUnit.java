/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.sales;

import javafx.beans.property.SimpleStringProperty;

/**
 * Date Created: 04-18-2023
 * @author Arsiela
 */
public class ModelVehicleInquiryPriorityUnit {
     private SimpleStringProperty tblindex01; //Row
     private SimpleStringProperty tblindex02; //Vehicle Description
     
     ModelVehicleInquiryPriorityUnit( String tblindex01,
                         String tblindex02
                         ){
     
          this.tblindex01 = new SimpleStringProperty(tblindex01);
          this.tblindex02 = new SimpleStringProperty(tblindex02);
     }
     
     //Row
     public String getTblindex01(){return tblindex01.get();}
     public void setTblindex01(String tblindex01){this.tblindex01.set(tblindex01);}
     //Vehicle Description
     public String getTblindex02(){return tblindex02.get();}
     public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
}
