# Breve panoramica
Questa demo è stata realizzata usando Spring boot, le websocket e il protocollo STOMP.
La demo attualmente funziona solo con 2 utenti (cris e james) ma è facilmente estendibile a più utenti.

## Demo in azione
https://youtu.be/lKW516ItHlU

## Situazione iniziale
Ogni client si collega al server dando vita a una connessione persistente e resta in attesa di messaggi indirizzati a lui.


## Acknowledge client -> server
Ogni volta che cris invia un messaggio a james si genera il seguente flusso:
1) cris genera un uuid lato client che verrà usato temporaneamente per marcare il messaggio appena creato/inviato (attributo da incollare all'elemento html)
2) il server riceve il messaggio, genera un UUID, salva il messaggio nel database e invia un pacchetto RECEIPT a cris contenente l'UUID creato dal client precedentemente e l'UUID generato dal server
3) cris riceve il pacchetto, estrae l'uuid generato da lui precedentemente e lo usa per trovare il messaggio inviato prima, poi a quel messaggio viene aggiunto l'UUID generato dal server (singola spunta).



L'UUID generato dal server è necessario perché in una futura implementazione, quando james  riceverà il messaggio da parte di cris, il server dovrà contattare cris  per confermare l'avvenuta ricezione (doppie spunte)

## Da server a james
Una volta che il server ha salvato il messaggio ricevuto da cris lo inoltra  a james. 
Una volta che james riceve il messaggio, estrae l'id del messaggio (UUID) e lo invia in un pacchetto ACK al server per informarlo dell'avvenuta ricezione.
Quando il server riceve l'ACK, estrae l'id e salva nel database l'evento di avvenuta ricezione (booleano).

## Acknowledge client <- server
Allo stato attuale, cris viene informato dell'avvenuta ricezione del messaggio inviato a james solamente quando ricarica la pagina.
L'idea è di creare e inviare un nuovo pacchetto RECEIPT a cris quando il server riceve il pacchetto ACK da james.
L'ultimo tentativo aveva generato un ciclo con i bean (da risolvere!).

