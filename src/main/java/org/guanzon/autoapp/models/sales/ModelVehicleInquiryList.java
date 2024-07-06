/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.models.sales;

import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * DATE CREATED: 04-17-2023
 * @author Arsiela
 */
public class ModelVehicleInquiryList {
     private SimpleStringProperty tbllistindex01; //Row
     private SimpleStringProperty tbllistindex02; //Inquiry Date
     private SimpleStringProperty tbllistindex03; //Customer Name
     private SimpleStringProperty tbllistindex04; //Inquiry Status
//     private SimpleStringProperty tbllistindex05; //Sales Excutive
//     private SimpleStringProperty tbllistindex06; //Reserve slip no
     
     private SimpleStringProperty tblcinqindex01; //sTransNox
     private SimpleStringProperty tblcinqindex02; //sBranchCD
     private SimpleStringProperty tblcinqindex03; //dTransact
     private SimpleStringProperty tblcinqindex04; //sEmployID
     private SimpleStringProperty tblcinqindex05; //cIsVhclNw
     private SimpleStringProperty tblcinqindex06; //sVhclIDxx
     private SimpleStringProperty tblcinqindex07; //sClientID
     private SimpleStringProperty tblcinqindex08; //sRemarksx
     private SimpleStringProperty tblcinqindex09; //sAgentIDx
     private SimpleStringProperty tblcinqindex10; //dTargetDt
     //private SimpleObjectProperty<Date> tblcinqindex10;
     private SimpleStringProperty tblcinqindex11; //cIntrstLv
     private SimpleStringProperty tblcinqindex12 ; // sSourceCD
     private SimpleStringProperty tblcinqindex13 ; // sSourceNo
     private SimpleStringProperty tblcinqindex14 ; // sTestModl
     private SimpleStringProperty tblcinqindex15 ; // sActvtyID
     //private SimpleStringProperty tblcinqindex16 ; // dLastUpdt
     private SimpleStringProperty tblcinqindex17 ; // nReserved
     private SimpleStringProperty tblcinqindex18 ; // nRsrvTotl
     //private SimpleStringProperty tblcinqindex19 ; // sLockedBy
     //private SimpleStringProperty tblcinqindex20 ; // sLockedDt
     private SimpleStringProperty tblcinqindex21 ; // sApproved
     //private SimpleStringProperty tblcinqindex22 ; // sSerialID
     //private SimpleStringProperty tblcinqindex23 ; // sInqryCde
     private SimpleStringProperty tblcinqindex24 ; // cTranStat
     //private SimpleStringProperty tblcinqindex25 ; // sEntryByx
     //private SimpleStringProperty tblcinqindex26 ; // dEntryDte
     //private SimpleStringProperty tblcinqindex27 ; // sModified
    // private SimpleStringProperty tblcinqindex28 ; // dModified, dTimeStmp
     private SimpleStringProperty tblcinqindex29 ; // sCompnyNm
     private SimpleStringProperty tblcinqindex30 ; // sMobileNo
     private SimpleStringProperty tblcinqindex31 ; //sAccountx
     private SimpleStringProperty tblcinqindex32 ; //sEmailAdd
     private SimpleStringProperty tblcinqindex33 ; //sAddressx
     private SimpleStringProperty tblcinqindex34 ; //sSalesExe
     private SimpleStringProperty tblcinqindex35 ; //sSalesAgn
     ModelVehicleInquiryList(   String tbllistindex01,
                         String tbllistindex02,
                         String tbllistindex03,
                         String tbllistindex04,
//                         String tbllistindex05,
//                         String tbllistindex06,
                         
                         String tblcinqindex01,
                         String tblcinqindex02,
                         String tblcinqindex03,
                         String tblcinqindex04,
                         String tblcinqindex05,
                         String tblcinqindex06,
                         String tblcinqindex07,
                         String tblcinqindex08,
                         String tblcinqindex09,
                         String tblcinqindex10,
                         //Date tblcinqindex10,
                         String tblcinqindex11,
                         String tblcinqindex12 ,
                         String tblcinqindex13 ,
                         String tblcinqindex14 ,
                         String tblcinqindex15 ,
                         //String tblcinqindex16 ,
                         String tblcinqindex17 ,
                         String tblcinqindex18 ,
//                         String tblcinqindex19 ,
//                         String tblcinqindex20 ,
                         String tblcinqindex21 ,
//                         String tblcinqindex22 ,
//                         String tblcinqindex23 ,
                         String tblcinqindex24,
//                         String tblcinqindex25 ,
//                         String tblcinqindex26 ,
//                         String tblcinqindex27 ,
//                         String tblcinqindex28 ,
                         String tblcinqindex29 ,
                         String tblcinqindex30,
                         String tblcinqindex31,
                         String tblcinqindex32,
                         String tblcinqindex33,
                         String tblcinqindex34,
                         String tblcinqindex35
                         ){
     
          this.tbllistindex01 = new SimpleStringProperty(tbllistindex01);
          this.tbllistindex02 = new SimpleStringProperty(tbllistindex02);
          this.tbllistindex03 = new SimpleStringProperty(tbllistindex03);
          this.tbllistindex04 = new SimpleStringProperty(tbllistindex04);
//          this.tbllistindex05 = new SimpleStringProperty(tbllistindex05);
//          this.tbllistindex06 = new SimpleStringProperty(tbllistindex06);
          
          
          this.tblcinqindex01 = new SimpleStringProperty(tblcinqindex01);
          this.tblcinqindex02 = new SimpleStringProperty(tblcinqindex02);
          this.tblcinqindex03 = new SimpleStringProperty(tblcinqindex03);
          this.tblcinqindex04 = new SimpleStringProperty(tblcinqindex04);
          this.tblcinqindex05 = new SimpleStringProperty(tblcinqindex05);
          this.tblcinqindex06 = new SimpleStringProperty(tblcinqindex06);
          this.tblcinqindex07 = new SimpleStringProperty(tblcinqindex07);
          this.tblcinqindex08 = new SimpleStringProperty(tblcinqindex08);
          this.tblcinqindex09 = new SimpleStringProperty(tblcinqindex09);
          this.tblcinqindex10 = new SimpleStringProperty(tblcinqindex10);
          //this.tblcinqindex10 = new SimpleObjectProperty<Date>(tblcinqindex10);
          this.tblcinqindex11 = new SimpleStringProperty(tblcinqindex11);
          this.tblcinqindex12 = new SimpleStringProperty( tblcinqindex12 );
          this.tblcinqindex13 = new SimpleStringProperty( tblcinqindex13 );
          this.tblcinqindex14 = new SimpleStringProperty( tblcinqindex14 );
          this.tblcinqindex15 = new SimpleStringProperty( tblcinqindex15 );
          //this.tblcinqindex16 = new SimpleStringProperty( tblcinqindex16 );
          this.tblcinqindex17 = new SimpleStringProperty( tblcinqindex17 );
          this.tblcinqindex18 = new SimpleStringProperty( tblcinqindex18 );
          //this.tblcinqindex19 = new SimpleStringProperty( tblcinqindex19 );
          //this.tblcinqindex20 = new SimpleStringProperty( tblcinqindex20 );
          this.tblcinqindex21 = new SimpleStringProperty( tblcinqindex21 );
          //this.tblcinqindex22 = new SimpleStringProperty( tblcinqindex22 );
          //this.tblcinqindex23 = new SimpleStringProperty( tblcinqindex23 );
          this.tblcinqindex24 = new SimpleStringProperty( tblcinqindex24 );
          //this.tblcinqindex25 = new SimpleStringProperty( tblcinqindex25 );
          //this.tblcinqindex26 = new SimpleStringProperty( tblcinqindex26 );
          //this.tblcinqindex27 = new SimpleStringProperty( tblcinqindex27 );
          //this.tblcinqindex28 = new SimpleStringProperty( tblcinqindex28 );
          this.tblcinqindex29 = new SimpleStringProperty( tblcinqindex29 );
          this.tblcinqindex30 = new SimpleStringProperty( tblcinqindex30 );
          this.tblcinqindex31 = new SimpleStringProperty( tblcinqindex31 );
          this.tblcinqindex32 = new SimpleStringProperty( tblcinqindex32 );
          this.tblcinqindex33 = new SimpleStringProperty( tblcinqindex33 );
          this.tblcinqindex34 = new SimpleStringProperty( tblcinqindex34 );
          this.tblcinqindex35 = new SimpleStringProperty( tblcinqindex35 );
     }
     
     //Row
     public String getTbllistindex01(){return tbllistindex01.get();}
     public void setTbllistindex01(String tbllistindex01){this.tbllistindex01.set(tbllistindex01);}
     //
     public String getTbllistindex02(){return tbllistindex02.get();}
     public void setTbllistindex02(String tbllistindex02){this.tbllistindex02.set(tbllistindex02);}
     //
     public String getTbllistindex03(){return tbllistindex03.get();}
     public void setTbllistindex03(String tbllistindex03){this.tbllistindex03.set(tbllistindex03);}
     //Inquiry Status
     public String getTbllistindex04(){return tbllistindex04.get();}
     public void setTbllistindex04(String tbllistindex04){this.tbllistindex04.set(tbllistindex04);}
//     //
//     public String getTbllistindex05(){return tbllistindex05.get();}
//     public void setTbllistindex05(String tbllistindex05){this.tbllistindex05.set(tbllistindex05);}
//     //
//     public String getTbllistindex06(){return tbllistindex06.get();}
//     public void setTbllistindex06(String tbllistindex06){this.tbllistindex06.set(tbllistindex06);}
//     
     
     
     //ID
     public String getTblcinqindex01(){return tblcinqindex01.get();}
     public void setTblcinqindex01(String tblcinqindex01){this.tblcinqindex01.set(tblcinqindex01);}
     //
     public String getTblcinqindex02(){return tblcinqindex02.get();}
     public void setTblcinqindex02(String tblcinqindex02){this.tblcinqindex02.set(tblcinqindex02);}
     //
     public String getTblcinqindex03(){return tblcinqindex03.get();}
     public void setTblcinqindex03(String tblcinqindex03){this.tblcinqindex03.set(tblcinqindex03);}
     //
     public String getTblcinqindex04(){return tblcinqindex04.get();}
     public void setTblcinqindex04(String tblcinqindex04){this.tblcinqindex04.set(tblcinqindex04);}
     
     //
     public String getTblcinqindex05(){return tblcinqindex05.get();}
     public void setTblcinqindex05(String tblcinqindex05){this.tblcinqindex05.set(tblcinqindex05);}
     //sVhclIDxx
     public String getTblcinqindex06(){return tblcinqindex06.get();}
     public void setTblcinqindex06(String tblcinqindex06){this.tblcinqindex06.set(tblcinqindex06);}
     //
     public String getTblcinqindex07(){return tblcinqindex07.get();}
     public void setTblcinqindex07(String tblcinqindex07){this.tblcinqindex07.set(tblcinqindex07);}
     //
     public String getTblcinqindex08(){return tblcinqindex08.get();}
     public void setTblcinqindex08(String tblcinqindex08){this.tblcinqindex08.set(tblcinqindex08);}
     // 
     public String getTblcinqindex09(){return tblcinqindex09.get();}
     public void setTblcinqindex09(String tblcinqindex09){this.tblcinqindex09.set(tblcinqindex09);}
     // 
     public String getTblcinqindex10(){return tblcinqindex10.get();}
     public void setTblcinqindex10(String tblcinqindex10){this.tblcinqindex10.set(tblcinqindex10);}
//     public Date getTblcinqindex10() {return tblcinqindex10.get();}
//     public void setTblcinqindex10(Date date) {this.tblcinqindex10.set(date);}
//     public SimpleObjectProperty<Date> tblcinqindex10Property() {return tblcinqindex10;}

     // 
     public String getTblcinqindex11(){return tblcinqindex11.get();}
     public void setTblcinqindex11(String tblcinqindex11){this.tblcinqindex11.set(tblcinqindex11);}
     
     
     public String getTblcinqindex12(){return tblcinqindex12.get();}
     public String getTblcinqindex13(){return tblcinqindex13.get();}
     public String getTblcinqindex14(){return tblcinqindex14.get();}
     public String getTblcinqindex15(){return tblcinqindex15.get();}
     //public String getTblcinqindex16(){return tblcinqindex16.get();}
     public String getTblcinqindex17(){return tblcinqindex17.get();}
     public String getTblcinqindex18(){return tblcinqindex18.get();}
     //public String getTblcinqindex19(){return tblcinqindex19.get();}
     //public String getTblcinqindex20(){return tblcinqindex20.get();}
     public String getTblcinqindex21(){return tblcinqindex21.get();}
     //public String getTblcinqindex22(){return tblcinqindex22.get();}
    //public String getTblcinqindex23(){return tblcinqindex23.get();}
     public String getTblcinqindex24(){return tblcinqindex24.get();}
     //public String getTblcinqindex25(){return tblcinqindex25.get();}
     //public String getTblcinqindex26(){return tblcinqindex26.get();}
     //public String getTblcinqindex27(){return tblcinqindex27.get();}
     //public String getTblcinqindex28(){return tblcinqindex28.get();}
     public String getTblcinqindex29(){return tblcinqindex29.get();}
     public String getTblcinqindex30(){return tblcinqindex30.get();}
     public String getTblcinqindex31(){return tblcinqindex31.get();}
     public String getTblcinqindex32(){return tblcinqindex32.get();}
     public String getTblcinqindex33(){return tblcinqindex33.get();}
     public String getTblcinqindex34(){return tblcinqindex34.get();}
     public String getTblcinqindex35(){return tblcinqindex35.get();}

     public void setTblcinqindex12(String tblcinqindex12){this.tblcinqindex12.set(tblcinqindex12);}
     public void setTblcinqindex13(String tblcinqindex13){this.tblcinqindex13.set(tblcinqindex13);}
     public void setTblcinqindex14(String tblcinqindex14){this.tblcinqindex14.set(tblcinqindex14);}
     public void setTblcinqindex15(String tblcinqindex15){this.tblcinqindex15.set(tblcinqindex15);}
     //public void setTblcinqindex16(String tblcinqindex16){this.tblcinqindex16.set(tblcinqindex16);}
     public void setTblcinqindex17(String tblcinqindex17){this.tblcinqindex17.set(tblcinqindex17);}
     public void setTblcinqindex18(String tblcinqindex18){this.tblcinqindex18.set(tblcinqindex18);}
     //public void setTblcinqindex19(String tblcinqindex19){this.tblcinqindex19.set(tblcinqindex19);}
     //public void setTblcinqindex20(String tblcinqindex20){this.tblcinqindex20.set(tblcinqindex20);}
     public void setTblcinqindex21(String tblcinqindex21){this.tblcinqindex21.set(tblcinqindex21);}
     //public void setTblcinqindex22(String tblcinqindex22){this.tblcinqindex22.set(tblcinqindex22);}
     //public void setTblcinqindex23(String tblcinqindex23){this.tblcinqindex23.set(tblcinqindex23);}
     public void setTblcinqindex24(String tblcinqindex24){this.tblcinqindex24.set(tblcinqindex24);}
     //public void setTblcinqindex25(String tblcinqindex25){this.tblcinqindex25.set(tblcinqindex25);}
     //public void setTblcinqindex26(String tblcinqindex26){this.tblcinqindex26.set(tblcinqindex26);}
     //public void setTblcinqindex27(String tblcinqindex27){this.tblcinqindex27.set(tblcinqindex27);}
     //public void setTblcinqindex28(String tblcinqindex28){this.tblcinqindex28.set(tblcinqindex28);}
     public void setTblcinqindex29(String tblcinqindex29){this.tblcinqindex29.set(tblcinqindex29);}
     public void setTblcinqindex30(String tblcinqindex30){this.tblcinqindex30.set(tblcinqindex30);}
     public void setTblcinqindex31(String tblcinqindex31){this.tblcinqindex31.set(tblcinqindex31);}
     public void setTblcinqindex32(String tblcinqindex32){this.tblcinqindex32.set(tblcinqindex32);}
     public void setTblcinqindex33(String tblcinqindex33){this.tblcinqindex33.set(tblcinqindex33);}
     public void setTblcinqindex34(String tblcinqindex34){this.tblcinqindex34.set(tblcinqindex34);}
     public void setTblcinqindex35(String tblcinqindex35){this.tblcinqindex35.set(tblcinqindex35);}
     

     
}
