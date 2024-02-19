package smu.DAO;

import smu.DTO.Card;
import java.sql.SQLException;
import java.util.*;

public interface CardDAO {

    List<Card> getByEmail(String email) throws SQLException;

    List<Card> getByCF(String CF) throws SQLException;

    int insert(Card card) throws SQLException;

}
