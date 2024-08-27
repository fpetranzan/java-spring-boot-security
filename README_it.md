# Spring Boot Security with JWT Implementation

lang [IT]: [en](https://github.com/fpetranzan/java-spring-boot-security/blob/master/README.md) | [it](https://github.com/fpetranzan/java-spring-boot-security/blob/master/README_it.md)

Questo progetto è un'implementazione pratica sulla sicurezza utilizzando Spring Boot e JSON Web Tokens (JWT).
Per la gestione dei dati è stato utilizzato MySQL.

## Cosa è stato implementato
* Registrazione utente
* Verifica email
* Login utente
* Password dimenticata
* Verifica a due fattori
* Aggiornamento del token
* Cambio Password
* Sistema di logout

### Dettagli implementazione
* Autenticazione JWT
* Criptaggio della password utilizzando BCrypt
* Autorizzazioni basate su ruoli utilizzando Spring Security
* Personalizzazione nella gestione degli accessi non autorizzati

## Tecnologie
* Spring Boot 3.2.4
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven
* MySQL

## Come avviarlo
Per avviare questo progetto, dovrai aver installato sulla tua macchina locale:

* JDK 17+
* Maven 3+


Per costruire e avviare il progetto, segui i seguenti passaggi:

* Clona il progetto: `https://github.com/fpetranzan/java-spring-boot-security`
* Vai alla cartella del progetto: cd spring-boot-security-jwt
* Aggiungi il database "jwt_security" a MySQL
* Costruisci il progetto: mvn clean install
* Avvia il progetto: mvn spring-boot:run

-> L'applicazione sarà disponibile a http://localhost:8088.

## Testare il progetto

### Testare la verifica dell'email
Per poter testare l'invio delle email sul tuo ambiente locale, dovrai utilizzare il seguente progetto open-souce:
https://github.com/gessnerfl/fake-smtp-server

Dopo aver installato il file `.jar`, spostalo nella cartella che preferisci.
Apri il terminale nella cartella in cui è presente il file .jar ed esegui il comando:

```
java -jar fake-smtp-server-<version>.jar
```

Apri una nuova finestra del tuo browser e vai al percorso: `http://localhost:8080/`

A questo percorso ti sarà possibile vedere le email che invierai

### Testare gli endpoints del progetto
Una collection postman è stata creata con tutte le chiamate già presenti.
In questo modo potrai testare tutti gli endpoint che sono stati creati.
Puoi recuperare la collection e importarla sul tuo Postman da qui:

[java-spring-boot-security - Postman Collection](https://github.com/fpetranzan/spring-boot-jwt-security/blob/master/src/main/resources/spring-boot-jwt-security.postman_collection.json)

