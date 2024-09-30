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
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.guanzon.appdriver.agent.ShowMessageFX;

/**
 *
 * @author AutoGroup Programmers
 */
/**
 * Utility class providing various common methods for date formatting, text
 * manipulation, and other utilities.
 */
public class CustomCommonUtil {

    /**
     * Converts a string representation of a date to a {@link LocalDate} object.
     *
     * @param val The date in string format (yyyy-MM-dd).
     * @return The {@link LocalDate} representation of the input string.
     * @throws DateTimeParseException If the text cannot be parsed to a
     * {@link LocalDate}.
     */
    public static LocalDate strToDate(String val) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(val, dateFormatter);
    }

    /**
     * Formats a {@link Date} object to a string in the format yyyy-MM-dd.
     *
     * @param fdValue The {@link Date} object to be formatted.
     * @return The formatted date string.
     */
    public static String xsDateShort(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(fdValue);
        return date;
    }

    /**
     * Formats a {@link Date} object to a string with month name and day in the
     * format "MMMM dd, yyyy".
     *
     * @param fdValue The {@link Date} object to be formatted.
     * @return The formatted date string with month name.
     */
    public static String xsDateWMonthName(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String date = sdf.format(fdValue);
        return date;
    }

    /**
     * Converts a date string in the format "MMMM dd, yyyy" to a string in the
     * format yyyy-MM-dd.
     *
     * @param fsValue The date string to be converted.
     * @return The converted date string in the format yyyy-MM-dd.
     * @throws ParseException If the input string cannot be parsed.
     */
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

    /**
     * Sets the behavior of a {@link TextField} to automatically convert text to
     * uppercase as it is typed.
     *
     * @param textField The {@link TextField} to which the behavior will be
     * applied.
     */
    public static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    /**
     * Sets the behavior of a {@link TextArea} to automatically convert text to
     * uppercase as it is typed.
     *
     * @param textArea The {@link TextArea} to which the behavior will be
     * applied.
     */
    public static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    /**
     * Adds a text limiter to a {@link TextField} to restrict its length to a
     * specified maximum number of characters.
     *
     * @param textField The {@link TextField} to which the limiter will be
     * applied.
     * @param maxLength The maximum length of text allowed.
     */
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

    /**
     * Formats a full name by extracting the initial of the first name and
     * appending the last name.
     *
     * @param fsFullName The full name in the format "FirstName LastName".
     * @return The formatted name with the first name initial and last name.
     */
    public static String formatName(String fsFullName) {
        String[] lsNameParts = fsFullName.split(" ");
        String lsFNameInitial = lsNameParts[0].substring(0, 1);
        String lsLName = lsNameParts[lsNameParts.length - 1];
        return lsFNameInitial + ". " + lsLName;
    }

    /**
     * Sets the disabled state for the given nodes.
     *
     * <p>
     * For example, to disable multiple nodes:</p>
     * <pre>
     * {@code
     * Button button = new Button("Submit");
     * TextField textField = new TextField();
     * setDisable(true, button, textField);  // This will disable both the button and the text field
     * }
     * </pre>
     *
     * @param disable if {@code true}, disables the nodes; otherwise, enables
     * them.
     * @param nodes the nodes to be disabled or enabled.
     */
    public static void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    /**
     * Sets the visibility state for the given nodes.
     *
     * <p>
     * For example, to hide or show multiple nodes:</p>
     * <pre>
     * {@code
     * Label label = new Label("Hidden Label");
     * Button button = new Button("Click Me");
     * setVisible(false, label, button);  // This will hide both the label and the button
     * }
     * </pre>
     *
     * @param visible if {@code true}, makes the nodes visible; otherwise, hides
     * them.
     * @param nodes the nodes to be shown or hidden.
     */
    public static void setVisible(boolean visible, Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(visible);
        }
    }

    /**
     * Sets the managed state for the given nodes. A managed node is included in
     * the layout calculations, so setting it to {@code false} will exclude it
     * from layout, but the node will still exist in the scene graph.
     *
     * <p>
     * For example, to remove nodes from layout calculations:</p>
     * <pre>
     * {
     * @code
     * TextField textField = new TextField("Managed TextField");
     * Label label = new Label("Managed Label");
     * setManaged(false, textField, label);  // These nodes will no longer be part of the layout
     * }
     * </pre>
     *
     * @param managed if {@code true}, includes the nodes in layout; otherwise,
     * excludes them.
     * @param nodes the nodes to be included or excluded from the layout.
     */
    public static void setManaged(boolean managed, Node... nodes) {
        for (Node node : nodes) {
            node.setManaged(managed);
        }
    }
}
