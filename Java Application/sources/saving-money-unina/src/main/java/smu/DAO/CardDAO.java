package smu.DAO;

import smu.DTO.Card;
import smu.DTO.ReportCard;

import java.sql.SQLException;
import java.util.*;
import java.time.*;

public interface CardDAO {

    List<Card> getByEmail(String email) throws SQLException;

    List<Card> getByCF(String CF) throws SQLException;

    Card getByNumber(String cardNumber) throws SQLException;

    int insert(Card card) throws SQLException;

    int update(Card card) throws SQLException;

    int delete(String cardNumber) throws SQLException;

    List<ReportCard> getReports(LocalDate firstDay, LocalDate lastDay) throws SQLException;

}
