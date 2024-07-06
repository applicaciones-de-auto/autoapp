/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.autoapp.utils;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 * Date : 3/14/2023
 *
 * @author Arsiela
 */
public class InputTextFormatterUtil extends TextFormatter<String> {

    public InputTextFormatterUtil(Pattern pattern) {
        super(new TextUnaryOperator(pattern));
    }

    private static class TextUnaryOperator implements UnaryOperator<TextFormatter.Change> {

        private final Pattern pattern;

        public TextUnaryOperator(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public TextFormatter.Change apply(TextFormatter.Change change) {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {

                return null;
            }
        }
    }

}
