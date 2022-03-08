# Application mit Hello World Rest Endpunkt

## Prerequisite
* Java 11
* Maven

## Beschreibung
Dies ist eine Spring Boot Applikation, welche einen einzelnen Rest Endpunkt hat. Der GET-Endpunkt liegt auf dem Pfad `/hello` und antwortet mit `Hello World!`  

## Konfiguration
Die Applikation ist so konfiguriert, dass sie auf Umgebungsvariablen hört. Konkret ist die Umgebungsvariable `HELLO_VALUE` dafür verantwortlich was der Endpunkt antwortet. So kann der Response angepasst werden. Response Template: `Hello ${HELLO_VALUE}!`.

Also mit einer Umgebungsvariable `HELLO_VALUE=Karl` liefert der Endpunkt `/hello` eben `Hello Karl!` zurück.

