package org.guanzon.autoapp.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author John Dave
 */
/**
 * Utility class providing various common methods for date formatting, text
 * manipulation, and other utilities.
 */
public class CustomCommonUtil {

    /**
     * Converts a string representing a date in "yyyy-MM-dd" format to a
     * {@link LocalDate} object.
     *
     * This method takes a date in string format (e.g., "2024-10-01") and
     * converts it to a {@link LocalDate}. It expects the input string to follow
     * the "yyyy-MM-dd" format. If the input cannot be parsed, a
     * {@link DateTimeParseException} will be thrown.
     *
     * @param val The date string in "yyyy-MM-dd" format.
     * @return A {@link LocalDate} object representing the date.
     * @throws DateTimeParseException If the input string cannot be parsed into
     * a valid {@link LocalDate}.
     *
     * <b>Example:</b>
     * <pre>{@code
     * String dateStr = "2024-10-01";
     * LocalDate date = strToDate(dateStr);
     * System.out.println(date); // Outputs: 2024-10-01
     * }</pre>
     */
    public static LocalDate strToDate(String val) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(val, dateFormatter);
    }

    /**
     * Converts a {@link Date} object to a string in "yyyy-MM-dd" format.
     *
     * This method formats a {@link Date} object (e.g., from a timestamp) to a
     * string in the "yyyy-MM-dd" format, commonly used for database entries or
     * display.
     *
     * @param fdValue The {@link Date} object to be formatted.
     * @return A string representing the date in "yyyy-MM-dd" format.
     *
     * <b>Example:</b>
     * <pre>{@code
     * Date now = new Date();
     * String formattedDate = xsDateShort(now);
     * System.out.println(formattedDate); // Outputs: Current date in yyyy-MM-dd format
     * }</pre>
     */
    public static String xsDateShort(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fdValue);
    }

    /**
     * Converts a {@link Date} object to a string in "yyyy-MM-dd" format.
     *
     * This method formats a {@link Date} object (e.g., from a timestamp) to a
     * string in the "yyyy-MM-dd" format, commonly used for database entries or
     * display.
     *
     * @param fdValue The {@link Date} object to be formatted.
     * @return A string representing the date in "yyyy-MM-dd" format.
     *
     * <b>Example:</b>
     * <pre>{@code
     * LocalDate now = new LocalDate();
     * String formattedDate = xsDateShort(now);
     * System.out.println(formattedDate); // Outputs: Current date in yyyy-MM-dd format
     * }</pre>
     */
    public static String xsDateShort(LocalDate fdValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fdValue.format(formatter);
    }

    /**
     * Converts a date string from the "MMMM dd, yyyy" format to "yyyy-MM-dd".
     *
     * This method takes a string formatted with the month name (e.g., "October
     * 02, 2024") and converts it into the standard "yyyy-MM-dd" format. If the
     * input string cannot be parsed, it throws a {@link ParseException}.
     *
     * @param fsValue The date string in "MMMM dd, yyyy" format.
     * @return A string representing the date in "yyyy-MM-dd" format.
     * @throws ParseException If the input string cannot be parsed into a valid
     * date.
     *
     * <b>Example:</b>
     * <pre>{@code
     * String dateStr = "October 02, 2024";
     * String formattedDate = xsDateShort(dateStr);
     * System.out.println(formattedDate); // Outputs: 2024-10-02
     * }</pre>
     */
    public static String xsDateShort(String fsValue) throws ParseException {
        SimpleDateFormat fromUser = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        return myFormat.format(fromUser.parse(fsValue));
    }

    /**
     * Formats a {@link Date} object into a string with the month name and day
     * in the "MMMM dd, yyyy" format.
     *
     * This method converts a {@link Date} object into a more human-readable
     * format, where the month name is displayed in full, such as "October 02,
     * 2024".
     *
     * @param fdValue The {@link Date} object to be formatted.
     * @return A string representing the date in "MMMM dd, yyyy" format.
     *
     * <b>Example:</b>
     * <pre>{@code
     * Date now = new Date();
     * String formattedDate = xsDateWMonthName(now);
     * System.out.println(formattedDate); // Outputs: October 02, 2024 (or current date)
     * }</pre>
     */
    public static String xsDateWMonthName(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(fdValue);
    }

    /**
     * Sets the behavior of a {@link TextField} to automatically convert all
     * text input to uppercase.
     *
     * This method listens to changes in the {@link TextField} and converts any
     * typed input to uppercase in real-time.
     *
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField = new TextField();
     * setCapsLockBehavior(textField);
     * textField.setText("hello");
     * System.out.println(textField.getText()); // Outputs: HELLO
     * }</pre>
     */
    public static void setCapsLockBehavior(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                textField.setText(newValue.toUpperCase());
            }
        });
    }

    /**
     * Sets the behavior of a {@link TextField} to automatically convert all
     * text input to uppercase.
     *
     * This method listens to changes in the {@link TextField} and converts any
     * typed input to uppercase in real-time.
     *
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField1 = new TextField();
     * TextField textField2 = new TextField();
     * setCapsLockBehavior(textField1,textField2);
     * textField.setText("hello");
     * System.out.println(textField.getText()); // Outputs: HELLO
     * }</pre>
     */
    public static void setCapsLockBehavior(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (textField.getText() != null) {
                    textField.setText(newValue.toUpperCase());
                }
            });
        }
    }

    /**
     * Sets the behavior of a {@link TextArea} to automatically convert all text
     * input to uppercase.
     *
     * This method listens to changes in the {@link TextArea} and converts any
     * typed input to uppercase in real-time.
     *
     * @param textArea The {@link TextArea} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextArea textArea = new TextArea();
     * setCapsLockBehavior(textArea);
     * textArea.setText("hello world");
     * System.out.println(textArea.getText()); // Outputs: HELLO WORLD
     * }</pre>
     */
    public static void setCapsLockBehavior(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textArea.getText() != null) {
                textArea.setText(newValue.toUpperCase());
            }
        });
    }

    /**
     * Sets the behavior of a {@link TextArea} to automatically convert all text
     * input to uppercase.
     *
     * This method listens to changes in the {@link TextArea} and converts any
     * typed input to uppercase in real-time.
     *
     * @param textArea The {@link TextArea} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextArea textArea1 = new TextArea();
     * TextArea textArea2 = new TextArea();
     * setCapsLockBehavior(textArea1, textArea2);
     * textArea.setText("hello world");
     * System.out.println(textArea.getText()); // Outputs: HELLO WORLD
     * }</pre>
     */
    public static void setCapsLockBehavior(TextArea... textAreas) {
        for (TextArea textArea : textAreas) {
            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (textArea.getText() != null) {
                    textArea.setText(newValue.toUpperCase());
                }
            });
        }
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
            if (node != null) {
                node.setDisable(disable);
            }
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
            if (node != null) {
                node.setVisible(visible);
            }
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
            if (node != null) {
                node.setManaged(managed);
            }
        }
    }

    /**
     * Sets the provided text value to multiple {@link TextField} elements.
     *
     * @param fsValue the text value to be set for each TextField
     * @param txtFields one or more TextField objects to set the text for
     *
     * Example usage:      <pre>
     * {@code
     * TextField txtField1 = new TextField();
     * TextField txtField2 = new TextField();
     * setText("Sample Text", txtField1, txtField2);
     * }
     * </pre>
     */
    public static void setText(String fsValue, TextField... txtFields) {
        for (TextField txtField : txtFields) {
            if (txtField != null) {
                txtField.setText(fsValue);
            }
        }
    }

    /**
     * Sets the provided text value to multiple {@link TextArea} elements.
     *
     * @param fsValue the text value to be set for each TextArea
     * @param txtAreas one or more TextArea objects to set the text for
     *
     * Example usage:      <pre>
     * {@code
     * TextArea txtAreas1 = new TextArea();
     * TextArea txtAreas2 = new TextArea();
     * setText("Sample Text", txtAreas1, txtAreas2);
     * }
     * </pre>
     */
    public static void setText(String fsValue, TextArea... txtAreas) {
        for (TextArea txtArea : txtAreas) {
            if (txtArea != null) {
                txtArea.setText(fsValue);
            }
        }
    }

    /**
     * Sets the provided value to multiple {@link ComboBox} elements.
     *
     * @param fsValue the value to be set for each ComboBox
     * @param comboBoxes one or more ComboBox objects to set the value for
     *
     * Example usage:      <pre>
     * {@code
     * ComboBox<String> comboBox1 = new ComboBox<>();
     * ComboBox<String> comboBox2 = new ComboBox<>();
     * setValue("Option1", comboBox1, comboBox2);
     * }
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static void setValue(String fsValue, ComboBox... comboBoxes) {
        for (ComboBox comboBox : comboBoxes) {
            if (comboBox != null) {
                comboBox.setValue(fsValue);
            }
        }
    }

    /**
     * Sets the provided text value to multiple {@link Label} elements.
     *
     * @param fsValue the text value to be set for each Label
     * @param labels one or more Label objects to set the text for
     *
     * Example usage:      <pre>
     * {@code
     * Label label1 = new Label();
     * Label label2 = new Label();
     * setText("Sample Text", label1, label2);
     * }
     * </pre>
     */
    public static void setText(String fsValue, Label... labels) {
        for (Label label : labels) {
            if (label != null) {
                label.setText(fsValue);
            }
        }
    }

    /**
     * Sets the selected state for a variable number of CheckBox components.
     *
     * @param fbValue The value to set for the selected state (true to select,
     * false to deselect).
     * @param checkBoxes The CheckBox components to modify.
     *
     * <p>
     * Example usage:</p>
     * <pre>
     * CheckBox checkBox1 = new CheckBox("Option 1");
     * CheckBox checkBox2 = new CheckBox("Option 2");
     *
     * // Select all checkboxes
     * UIUtils.setSelected(true, checkBox1, checkBox2);
     *
     * // Deselect all checkboxes
     * UIUtils.setSelected(false, checkBox1, checkBox2);
     * </pre>
     */
    public static void setSelected(boolean fbValue, CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox != null) {
                checkBox.setSelected(fbValue);
            }
        }
    }

    /**
     * Sets the selected state for a variable number of RadioButton components.
     *
     * @param fbValue The value to set for the selected state (true to select,
     * false to deselect).
     * @param radioButtons The RadioButton components to modify.
     *
     * <p>
     * Example usage:</p>
     * <pre>
     * RadioButton radioBtn1 = new RadioButton("Option A");
     * RadioButton radioBtn2 = new RadioButton("Option B");
     *
     * // Select the first radio button
     * UIUtils.setSelected(true, radioBtn1);
     *
     * // Deselect the first radio button
     * UIUtils.setSelected(false, radioBtn1);
     * </pre>
     */
    public static void setSelected(boolean fbValue, RadioButton... radioButtons) {
        for (RadioButton radioBtn : radioButtons) {
            if (radioBtn != null) {
                radioBtn.setSelected(fbValue);
            }
        }
    }

    /**
     *
     *
     * @param foTab the tab from parent tabpane
     * @param foTabPane this is the parent of tab<pre>
     * Example usage:{@code
     * switchToTab(tab1 , TabPane);
     * }
     * </pre>
     */
    public static void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    /**
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField1 = new TextField();
     * TextField textField2 = new TextField();
     * inputDecimalOnly(textField1,textField2);
     * }</pre>
     */
    public static void inputDecimalOnly(TextField... txtFields) {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        for (TextField txtField : txtFields) {
            if (txtField != null) {
                txtField.setTextFormatter(new TextFormatterUtil(pattern));
            }
        }
    }

    /**
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField1 = new TextField();
     * TextField textField2 = new TextField();
     * inputNumberOnly(textField1,textField2);
     * }</pre>
     */
    public static void inputIntegerOnly(TextField... txtFields) {
        Pattern pattern = Pattern.compile("[0-9]*");
        for (TextField txtField : txtFields) {
            if (txtField != null) {
                txtField.setTextFormatter(new TextFormatterUtil(pattern));
            }
        }
    }

    /**
     * @param txtFields
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField1 = new TextField();
     * TextField textField2 = new TextField();
     * inputAlphOnly(textField1,textField2);
     * }</pre>
     */
    public static void inputAlphOnly(TextField... txtFields) {
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        for (TextField txtField : txtFields) {
            if (txtField != null) {
                txtField.setTextFormatter(new TextFormatterUtil(pattern));
            }
        }
    }

    /**
     * @param txtFields
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * TextField textField1 = new TextField();
     * TextField textField2 = new TextField();
     * inputAlphNumOnly(textField1,textField2);
     * }</pre>
     */
    public static void inputAlphNumOnly(TextField... txtFields) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        for (TextField txtField : txtFields) {
            if (txtField != null) {
                txtField.setTextFormatter(new TextFormatterUtil(pattern));
            }
        }
    }

    /**
     * @param foObject
     * @param textField The {@link TextField} to apply the behavior to.
     *
     * <b>Example:</b>
     * <pre>{@code
     * setDecimalFormat(object);
     * }</pre>
     */
    public static String setDecimalFormat(Object foObject) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        if (foObject != null) {
            return format.format(Double.parseDouble(String.valueOf(foObject)));
        }
        return null;
    }

}
