/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.autoapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.guanzon.appdriver.agent.ShowMessageFX;

/**
 *
 * @author User
 */
public class InputTextUtil {

    /*Convert Date to String*/
    public static LocalDate strToDate(String val) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(val, dateFormatter);
    }

    public static String xsDateShort(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(fdValue);
        return date;
    }

    public static String xsDateShort(String fsValue) throws ParseException {
        SimpleDateFormat fromUser = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lsResult = "";
        try {
            lsResult = myFormat.format(fromUser.parse(fsValue));
        } catch (ParseException e) {
            ShowMessageFX.Error(e.getMessage(), "xsDateShort", "Please inform MIS Department.");
            System.exit(1);
        }
        return lsResult;
    }

    public static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    public static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void addTextLimiter(TextField textField, int maxLength) {
        // Remove any existing listeners to avoid stacking them
        if (textField.getProperties().get("textLimiter") != null) {
            textField.textProperty().removeListener((ChangeListener<String>) textField.getProperties().get("textLimiter"));
        }

        // Flag to prevent recursive calls
        final boolean[] isUpdating = {false};

        // Create a new listener to limit the text length
        ChangeListener<String> textLimiter = (observable, oldValue, newValue) -> {
            if (isUpdating[0]) {
                return; // Skip if updating to avoid recursion
            }

            if (newValue.length() > maxLength) {
                isUpdating[0] = true; // Set flag before updating
                textField.setText(oldValue); // Revert to old value
                isUpdating[0] = false; // Reset flag after updating
            }
        };

        // Add the new listener to the text field
        textField.textProperty().addListener(textLimiter);
        // Store the listener reference for future removal if necessary
        textField.getProperties().put("textLimiter", textLimiter);
    }

}
