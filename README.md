# gestione-esami-universitari
La seguente è un applicazione di un sistema client/server per la gestione degli esami universitari. È stata realizzata come progetto per l'esame del corso di Reti di calcolatori e Lab dell'università degli studi di Napoli Parthenope.
## **Funzionalità**
### **Server universitario**
- Gestione concorrente delle richieste;
- Reperimento e inserimento di dati all'interno del database (esami e prenotazioni);
- Autenticazione studenti tramite matricola.
### **Segreteria**
- Gestione concorrente delle richieste;
- Inoltro delle richieste da parte del client dello studente al server universitario;
- Inserimento di nuovi esami tramite l'applicazione client della segreteria.
### **Studente**
- Autenticazione tramite matricola;
- Invio di richieste al server della segreteria (visione esami, inserimento prenotazione).
## **Linguaggio e librerie utilizzate**
L'applicazione è scritta interamente in Java, con l'utilizzo della libreria esterna Mysql **Connector/J** per la connessione al database Mysql.
## **Istruzione di esecuzione**
Posizionarsi all'interno della cartella /bin ed eseguire, su finestre di terminale diverse e nell'ordine mostrato, i seguenti comandi:  
**java -cp .:$HOME/gestionale_Uni/lib/mysql-connector-j-9.1.0.jar gestionale_Uni.UniServerApp**  
**java gestionale_Uni.StudentAdminOfficeSApp**  
**java gestionale_Uni.StudentClientApp**  
Per l'inserimento di nuovi esami, eseguire l'applicazione **StudentAdminOfficeCApp**.
