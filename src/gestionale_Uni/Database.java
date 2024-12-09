package gestionale_Uni;

/**
 * Interfaccia che definisce un generico database con i due metodi principali: connect e close 
 * @author Daniele
 */
public interface Database {
    public abstract void connect() throws Exception;
    public abstract void close() throws Exception;
}
