# Vom Basis Wildfly Image zum Container inklusive App-Deployment

## Prerequisite 
* Docker Installation
* Java 11
* Maven

## Ausführen eines Wildfly Images aus einer public registry
Wir starten mit dem Ausführen eines vorgefertigten Images aus einem public repository. Dazu nehmen wir eine neuere Version des JBoss Servers - Wildfly. Das Image beinhaltet den WildFly Server und gibt per default port `9990` (Admin Konsole) und `8080` (Applikation Port) frei. Um das Image auszuführen, gibt man folgenden Befehl ein:

`docker run --name my-wildfly -d -p 8080:8080 quay.io/wildfly/wildfly:26.0.1.Final`

### Kurze Erklärung zu dem "docker run" Befehl
* `docker run` führt ein Image als Container aus
* `--name my-wildfly` der Container wird über diesen Namen referenziert
* `-d` Container wird detached ausgeführt, also im Hintergrund
* `-p 8080:8080` port forwarding von lokalem Port 8080 auf 8080 im Docker Container
* `quay.io/wildfly/wildfly:26.0.1.Final` Image Identifier

## Prüfen welche Container, Images vorhanden sind und ggf. aktuell laufen
* `docker ps` Welche vorhandenen Container laufen gerade
* `docker ps -a` Welche Container gibt es im Allgemeinen
* `docker stop <id>` Stoppt den Docker Container mit der <id>
* `docker rm <id>` Löscht den Docker Container mit der <id>
* `docker image ls` Welche Images sind lokal vorhanden
* `docker image rm <id>` Löscht Images mit der <id>

## Erweiterung des WildFly Images
Nun werden wir auf Basis des WildFly Images Anpassungen vornehmen. Um auf dem Image aufbauen zu können legen wir uns ein [Dockerfile](https://github.com/coc-university/docker-basics/blob/main/wildfly/Dockerfile) an und erweitern das Base Image. Wir refenzieren im `from` Teil das WildFly Image und fügen aus Gründen der Übersichtlichkeit den `CMD` Befehl aus dem Basis Image hinzu. Siehe `Dockerfile`. 

## Docker Image lokal bauen
Mit dem folgenden Befehl können wir das Image lokal bauen:
`docker build --tag=wildfly-example .`

### Kurze Erklärung zu dem "docker build" Befehl
* `docker build` baut ein Docker Image anhand eines Dockerfile
* `--tag=wildfly-exmaple` gibt dem Image einen Tag zur Identifizierung
* `.` ist der Pfad zum Dockerfile anhand dessen das Image gebaut wird

Nachdem das Image gebaut wurde, können wir es mit dem oben beschriebenen Befehl starten. Da das Image jetzt aber lokal vorhanden ist, müssen wir nicht mehr das Remote Repository referenzieren, sondern können den gerade genutzten Tag nehmen.

`docker run -d -p 6660:9990 -p 8080:8080 wildfly-example`

## Admin Account
Der WildFly Container läuft nun und wir kommen auch auf den Server. Allerdings können wir uns nicht einloggen, da wir keinen Admin Account haben. Dafür gibt es aber eine einfache Lösung: wir das "add-user.sh" script, das mit dem WildFly Server ausgeliefert wird.

Das script ist im Docker Container an der Stelle zu finden `/opt/jboss/wildfly/bin/add-user.sh`.

Wir geben dem script einen Nutzernamen (`admin`) und ein Password (`Admin#P4ssw0rd`) als Parameter mit und lassen das script im nicht aktiven Modus, also ohne CLI Interaktion laufen (`silent`).

`RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#P4ssw0rd --silent`

Wenn wir nun ein neues Image bauen und ausführen, können wir uns mit diesen Admin Credentials einloggen und zB. eine Applikation deployen.

## Application Deployment
In dem Ordner `spring-rest` ist eine Java Applikation, die uns am Pfad `/hello` ein Hello World liefert. Wir bauen die Applikation und legen sie in ein `.war` file mit dem Namen `spring-rest-0.0.1-SNAPSHOT.war`, welches wir dann in diesen Ordner hier kopieren. Das maven pom.xml ist bereits vorbereitet, so das man nicht mehr manuell kopieren muss - `mvn clean install package` im Spring Ordner reicht aus.

Für das automatisierte Deployment nutzen wir den Deployment Scan Mechanismus des WildFly Servers. Der WildFly Server wird in dem Pfad `/opt/jboss/wildfly/standalone/deployments/` nach geeigneten Paketen suchen und diese dann direkt auf den Server deployen.

Damit die Datei aber in dem Docker Container vorhanden ist, müssen wir sie in Dockerfile dem Image hinzufügen. Das machen wir über den folgenden Befehl:

`ADD spring-rest-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/`

Jetzt können wir das Image wieder bauen und den Docker Container inklusive Applikation starten. Tatsächlich hätten wir jetzt auch keine Bedarf mehr am Admin User und könnten uns diese jetzt unnötige Konfiguration sparen.

## Application Config
Die Spring Boot Applikation liefert per default am Hello World Endpunkt eben `Hello World!` zurück. Allerdings ist die Rückgabe über Environment Variablen konfigurierbar - das wissen wir aus der README.md in dem spring-rest Ordner. Damit können wir unser Docker Image und damit auch die Applikation auf unsere Bedürfnisse anpassen. Vielleicht soll das Image auf der Entwicklungsumgebung etwas anderes antworten, als auf der Produktivumgebung oder jeder Entwickler möchte persönlich begrüßt werden.

Dazu können wir die Konfiguration, also unsere Environment Variable, einfach in den Docker Container mitgeben. Dazu schreiben wir die folgende ENV Variable in das Dockerfile:

`ENV HELLO_VALUE="Karl"`

