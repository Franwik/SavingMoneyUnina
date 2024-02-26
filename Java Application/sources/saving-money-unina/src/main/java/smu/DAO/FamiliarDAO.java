package smu.DAO;

import java.sql.SQLException;
import java.util.List;

import smu.DTO.Familiar;

public interface FamiliarDAO {

    public Familiar getByCF(String CF) throws SQLException;

    public List<Familiar> getByEmail(String email) throws SQLException;

    public int insert(Familiar familiar) throws SQLException;

    public int update(Familiar familiar) throws SQLException;

    public int delete(String CF) throws SQLException;

}
