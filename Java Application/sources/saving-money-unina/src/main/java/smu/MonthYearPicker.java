package smu;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class MonthYearPicker extends DatePicker {

    public MonthYearPicker() {
        // Nascondi il selettore del giorno
        setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(true);
                setVisible(false);
            }
        });

        // Personalizza la formattazione della data visualizzata
        setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return YearMonth.parse(string, formatter).atDay(1);
                } else {
                    return null;
                }
            }
        });
    }
}
