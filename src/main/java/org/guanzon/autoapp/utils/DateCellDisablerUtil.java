package org.guanzon.autoapp.utils;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

public class DateCellDisablerUtil {

    //    public static GRider oApp;

    /*Convert Date to String*/
//    public static LocalDate strToDate(String val) {
//        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(val, date_formatter);
//        return localDate;
//    }
    /**
     * Creates a callback to disable dates that are before or after a specified
     * range from the current date.
     *
     * @param daysToDisable Number of days to disable in the past and future.
     * @return Callback that disables dates outside the specified range.
     */
    public Callback<DatePicker, DateCell> createDisableDateCallback(int daysToDisable) {
        return param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setDisable(true);
                    return;
                }

                // Get today's date
                LocalDate today = LocalDate.now();

                // Calculate the minimum and maximum dates for disabling
                LocalDate minDate = today.minusDays(daysToDisable);
                LocalDate maxDate = today.plusDays(daysToDisable);

                setDisable(item.isBefore(minDate) || item.isAfter(maxDate));
            }
        };
    }

    /**
     * Creates a callback to disable dates that are before a specified range
     * from the current date.
     *
     * @param daysToDisable Number of days to disable in the past.
     * @return Callback that disables dates before the specified range.
     */
    public Callback<DatePicker, DateCell> createDisablePastDateCallback(int daysToDisable) {
        return param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setDisable(true);
                    return;
                }

                // Get today's date
                LocalDate today = LocalDate.now();

                // Calculate the minimum date for disabling
                LocalDate minDate = today.minusDays(daysToDisable);

                setDisable(item.isBefore(minDate));
            }
        };
    }

    /**
     * Creates a callback to disable dates that are after a specified range from
     * the current date.
     *
     * @param daysToDisable Number of days to disable in the future.
     * @return Callback that disables dates after the specified range.
     */
    public Callback<DatePicker, DateCell> createDisableFutureDateCallback(int daysToDisable) {
        return param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setDisable(true);
                    return;
                }

                // Get today's date
                LocalDate today = LocalDate.now();

                // Calculate the maximum date for disabling
                LocalDate maxDate = today.plusDays(daysToDisable);

                setDisable(item.isAfter(maxDate));
            }
        };
    }

}
