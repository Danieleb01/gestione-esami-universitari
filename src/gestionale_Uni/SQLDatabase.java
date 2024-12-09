package gestionale_Uni;
import java.sql.*;

/**
 * Interfaccia che estende Database e definisce metodi che effettuano operazioni su database di tipo sql
 * @author Daniele
 */
public interface SQLDatabase extends Database {
    public abstract PreparedStatement getStatement(String SQLStatement) throws Exception;
    public abstract ResultSet executeStatement(PreparedStatement stm) throws Exception;
    public abstract int insertOrUpdate(PreparedStatement stm) throws Exception;
    public abstract void closeEverything(PreparedStatement stm, ResultSet result);
    public abstract void closeEverything(PreparedStatement stm);
}
