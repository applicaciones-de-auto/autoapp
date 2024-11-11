package org.guanzon.autoapp.models.cashiering;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class Cashier_Receivables {

    private SimpleStringProperty tblindex01;
    private CheckBox select;
    private SimpleStringProperty tblindex02; // car
    private SimpleStringProperty tblindex03; // date from
    private SimpleStringProperty tblindex04; // date to
    private SimpleStringProperty tblindex05; // type
    private SimpleStringProperty tblindex06; // customer name
    private SimpleStringProperty tblindex07; // reference no
    private SimpleStringProperty tblindex08; // particulars
    private SimpleStringProperty tblindex09; // location
    private SimpleStringProperty tblindex10; // gross amnt
    private SimpleStringProperty tblindex11; // dsc amnt
    private SimpleStringProperty tblindex12; // trans amnt
    private SimpleStringProperty tblindex13; // deduct amnt
    private SimpleStringProperty tblindex14; // paid amnt
    private SimpleStringProperty tblindex15; // si no
    private SimpleStringProperty tblindex16; // cc slip no
    private SimpleStringProperty tblindex17; // pr no
    private SimpleStringProperty tblindex18; // soa no
    private SimpleStringProperty tblindex19; // journal entry no
    private SimpleStringProperty tblindex20; // transform
    private SimpleStringProperty tblindex21; // trans by
    private SimpleStringProperty tblindex22; //  trans to
    private SimpleStringProperty tblindex23; // trans date
    private SimpleStringProperty tblindex24;

    public Cashier_Receivables(String tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex04,
            String tblindex05,
            String tblindex06,
            String tblindex07,
            String tblindex08,
            String tblindex09,
            String tblindex10,
            String tblindex11,
            String tblindex12,
            String tblindex13,
            String tblindex14,
            String tblindex15,
            String tblindex16,
            String tblindex17,
            String tblindex18,
            String tblindex19,
            String tblindex20,
            String tblindex21,
            String tblindex22,
            String tblindex23,
            String tblindex24) {
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.select = new CheckBox();
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex10 = new SimpleStringProperty(tblindex10);
        this.tblindex11 = new SimpleStringProperty(tblindex11);
        this.tblindex12 = new SimpleStringProperty(tblindex12);
        this.tblindex13 = new SimpleStringProperty(tblindex13);
        this.tblindex14 = new SimpleStringProperty(tblindex14);
        this.tblindex15 = new SimpleStringProperty(tblindex15);
        this.tblindex16 = new SimpleStringProperty(tblindex16);
        this.tblindex17 = new SimpleStringProperty(tblindex17);
        this.tblindex18 = new SimpleStringProperty(tblindex18);
        this.tblindex19 = new SimpleStringProperty(tblindex19);
        this.tblindex20 = new SimpleStringProperty(tblindex20);
        this.tblindex21 = new SimpleStringProperty(tblindex21);
        this.tblindex22 = new SimpleStringProperty(tblindex22);
        this.tblindex23 = new SimpleStringProperty(tblindex23);
        this.tblindex24 = new SimpleStringProperty(tblindex24);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    public String getTblindex06() {
        return tblindex06.get();
    }

    public void setTblindex06(String tblindex06) {
        this.tblindex06.set(tblindex06);
    }

    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

    public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }

    public String getTblindex10() {
        return tblindex10.get();
    }

    public void setTblindex10(String tblindex10) {
        this.tblindex10.set(tblindex10);
    }

    public String getTblindex11() {
        return tblindex11.get();
    }

    public void setTblindex11(String tblindex11) {
        this.tblindex11.set(tblindex11);
    }

    public String getTblindex12() {
        return tblindex12.get();
    }

    public void setTblindex12(String tblindex12) {
        this.tblindex12.set(tblindex12);
    }

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }

    public String getTblindex14() {
        return tblindex14.get();
    }

    public void setTblindex14(String tblindex14) {
        this.tblindex14.set(tblindex14);
    }

    public String getTblindex15() {
        return tblindex15.get();
    }

    public void setTblindex15(String tblindex15) {
        this.tblindex15.set(tblindex15);
    }

    public String getTblindex16() {
        return tblindex16.get();
    }

    public void setTblindex16(String tblindex16) {
        this.tblindex16.set(tblindex16);
    }

    public String getTblindex17() {
        return tblindex17.get();
    }

    public void setTblindex17(String tblindex17) {
        this.tblindex17.set(tblindex17);
    }

    public String getTblindex18() {
        return tblindex18.get();
    }

    public void setTblindex18(String tblindex18) {
        this.tblindex18.set(tblindex18);
    }

    public String getTblindex19() {
        return tblindex19.get();
    }

    public void setTblindex19(String tblindex19) {
        this.tblindex19.set(tblindex19);
    }

    public String getTblindex20() {
        return tblindex20.get();
    }

    public void setTblindex20(String tblindex20) {
        this.tblindex20.set(tblindex20);
    }

    public String getTblindex21() {
        return tblindex21.get();
    }

    public void setTblindex21(String tblindex21) {
        this.tblindex21.set(tblindex21);
    }

    public String getTblindex22() {
        return tblindex22.get();
    }

    public void setTblindex22(String tblindex22) {
        this.tblindex22.set(tblindex22);
    }

    public String getTblindex23() {
        return tblindex23.get();
    }

    public void setTblindex23(String tblindex23) {
        this.tblindex23.set(tblindex23);
    }

    public String getTblindex24() {
        return tblindex24.get();
    }

    public void setTblindex24(String tblindex24) {
        this.tblindex24.set(tblindex24);
    }

}
