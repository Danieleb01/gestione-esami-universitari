package gestionale_Uni;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/** 
   Una classe per leggere stringhe e numeri da un flusso di input.
   Questa classe e' particolarmente adatta per chi comincia 
   a programmare in Java.
   Costruisce il necessario lettore bufferizzato, 
   gestisce le eccezioni I/O e converte le stringhe in numeri.
*/

public class ConsoleReader 
{  /**
      Costruisce un lettore da console da un flusso di input 
      come per esempio System.in
      @param inStream un flusso di input 
   */
   public ConsoleReader(InputStream inStream)
   {  reader = new BufferedReader
         (new InputStreamReader(inStream)); 
   }
   
   /**
      Legge una riga di input e la converte in un integer.
      La riga di input non puo' contenere altro che un integer.
      Non sono neppure ammessi spazi vuoti.
      @return l'integer digitato dall'utente
   */
   public int readInt() 
   {  String inputString = readLine();
      int n = Integer.parseInt(inputString);
      return n;
   }
   
   /**
      Legge una riga di input e la converte in un numero in virgola
      Mobile. La riga di input non puo' contenere altro che un  
      numero. Non sono neppure ammessi spazi vuoti.
      @return il numero digitato dall'utente
   */
   public double readDouble() 
   {  String inputString = readLine();
	   	    
      double x = Double.parseDouble(inputString);
      return x;
   }
   
   /**
      Legge una riga di input. Nel caso (improbabile) di
      una eccezione I/O, il programma si arresta. 
      @return la riga di input digitata dall'utente, null
      alla fine dell'input
   */
   public String readLine() 
   {  String inputLine = "";

      try
      {  inputLine = reader.readLine();
      }
      catch(IOException e)
      {  System.out.println(e);
         System.exit(1);
      }
    
      return inputLine;
   }

   private BufferedReader reader; 
}
