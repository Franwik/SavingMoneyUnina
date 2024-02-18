package smu.DAO;

import java.sql.SQLException;
import java.util.List;

import smu.DTO.Card;

public interface CardDAO extends DAO<Card>{

    List<Card> getByEmail(String email) throws SQLException;

    List<Card> getByCF(String CF) throws SQLException;

}
