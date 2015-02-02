VehicleSearchWithNeo4j : Schnittstelle für eine durchsuchbare Fahrzeugdatenbank mit Neo4j
=========================================================================================
Dieses Projekt ist der Prototyp für eine Suche einer Fahrzeugdatenbank, die auf Neo4j basiert.
Fahrzeuge lassen sich damit anhand von Begriffen und Baujahrzeiträumen finden.

Das Projekt entstand im Rahmen einer Bachelorarbeit an der Hochschule für Technik und Wirtschaft Berlin mit dem Titel
"Entwicklung einer markenübergreifenden Suche für Fahrzeuge mit Hilfe einer Graphdatenbank".

Verfasser: Eike Schulte-Kersmecke

Eingereicht am 17. Februar 2014

Voraussetzungen:
----------------
- Maven 3
- Neo4j 2.1.6
- Java 8

Schnittstelle für die Suche
---------------------------
Für die Suche steht die Schnittstelle VehicleSearchEngine zur Verfügung. Sie besitzt zwei Methoden.


```
  public Collection<VehicleNode> search(String searchString);
```

```
  public Collection<String> autocomplete(String searchTerm);
```

Die Methode ```search()``` sucht anhand einer Zeichenkette, die ein oder mehrere Suchbegriffe enthält,
nach passenden Fahrzeugen in der Datenbank. Die einzelnen Suchbegriffe sollten dabei durch Leerzeichen voneinander getrennt sein.
Beispiel: „Audi A4 B6 Kombi“
Für die Bereichssuche nach Baujahren gilt folgende Syntax: ```<Suchbegriffe>;<Einheitenname><Operator><Wert>```

Beispiel: „Audi A4; Baujahr > 2006; Baujahr < 2012“

Bereichsangaben werden durch ein Semikolon eingeleitet. Der Suchbegriff kann um beliebig viele Bereichsangaben erweitert werden.
Sind eingegebene Suchbegriffe unvollständig, werden Ergebnisse geliefert, in denen der Suchbegriff als Zeichenkette vorkommt.

Die Methode ```autocomplete()``` sucht anhand eines unvollständigen Begriffes in Form einer Zeichenkette nach möglichen Begriffen, die mit den gegebenen Zeichen beginnen.

Schnittstelle zum Speichern
---------------------------
Zum Speichern von Fahrzeugdaten steht die Schnittstelle VehicleDataPersistenceService mit drei Methoden zur Verfügung


```
public void save(VehicleNode vehicleNode);
```

```
public void save(VehicleNode vehicleNode, VehicleMetaData additionalMetaData);
```

```
public void saveBatch(final Map<VehicleNode, VehicleMetaData> batchData);
```

Die Methode ```save(VehicleNode vehicleNode)``` speichert ein Fahrzeug in Form eines Objekts vom Typ VehicleNode in der Datenbank,
wobei dieses so angepasst wird, dass es über jeden Begriff (getrennt durch Leerzeichen) aus dem Namen zu finden ist.

```save(VehicleNode vehicleNode, VehicleMetaData additionalMetaData)``` ermöglicht das Speichern eines Fahrzeuges mit zusätzlichen Metadaten.

Für Datensätze bestehend aus mehreren Fahrzeug mit optionalen Metainformationen steht die Methode
``` saveBatch(final Map<VehicleNode, VehicleMetaData> batchData)``` zur Verfügung.