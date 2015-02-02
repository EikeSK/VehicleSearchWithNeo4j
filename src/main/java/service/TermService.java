package service;


import java.util.Collection;

/**
 * Repräsentiert eine Serviceklasse für den Zugriff auf die Datenbank mit Objekten vom Typ Term
 */
public interface TermService {

    /**
     * Liefert anhand eines Begriffs alle Suchbegriffe aus der Datenbank, die mit dem angegebenen Begriff beginnen.
     * Jeder Zugriff auf die Datenbank über diese Methode wird in einer Transaktion ausgeführt.
     *
     * @param name Der unvollständige Suchbegriff
     * @return eine Liste von vorhandenen Suchbegriffen, die mit der angegebenen Zeichenkette beginnen.
     */
    public Collection<String> findTermNamesByIncompleteName(final String name);
}
