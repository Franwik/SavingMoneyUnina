package smu.DAO;

import java.sql.SQLException;
import java.util.List;

import smu.DTO.Familiar;

public interface FamiliarDAO extends DAO<Familiar>{

    public List<Familiar> getByEmail(String email) throws SQLException;

}
