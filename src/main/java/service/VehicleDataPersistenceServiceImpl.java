package service;

import domain.Baujahr;
import domain.Term;
import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.BaujahrRepository;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import support.StringSplitterUtils;
import support.VehicleMetaData;

import java.util.*;

import static com.google.common.base.Ascii.toLowerCase;

@Component
public class VehicleDataPersistenceServiceImpl implements VehicleDataPersistenceService {

    private final VehicleNodeRepository _vehicleNodeRepository;
    private final TermRepository _termRepository;
    private final BaujahrRepository _baujahrRepository;

    public VehicleDataPersistenceServiceImpl(VehicleNodeRepository vehicleNodeRepository, TermRepository termRepository, BaujahrRepository baujahrRepository) {
        _vehicleNodeRepository = vehicleNodeRepository;
        _termRepository = termRepository;
        _baujahrRepository = baujahrRepository;
    }

    @Override
    public void save(final VehicleNode vehicleNode) {
        save(vehicleNode, null);
    }

    @Override
    public void save(final VehicleNode vehicleNode, final VehicleMetaData additionalMetaData) {
        final Collection<Term> termsFromTokens = getTermsFrom(vehicleNode);
        if (additionalMetaData != null) {
            termsFromTokens.addAll(getTermsFor(vehicleNode, additionalMetaData.getAdditionalMetaData()));
        }
        _vehicleNodeRepository.save(vehicleNode);
        _termRepository.save(termsFromTokens);
        if (additionalMetaData != null && additionalMetaData.getBaujahrFrom() > 0) {
            final List<Integer> baujahrRange = createBaujahrRangeFrom(additionalMetaData);
            for (Integer baujahr : baujahrRange) {
                _baujahrRepository.save(getBaujahrFrom(vehicleNode, baujahr));
            }
        }
    }

    @Override
    public void saveBatch(final Map<VehicleNode, VehicleMetaData> batchData) {
        final List<Term> allTerms = relateAllTermsToNodes(batchData);
        final List<Baujahr> allBaujahrs = relateAllBaujahrNodesToNodes(batchData);
        _vehicleNodeRepository.save(batchData.keySet());
        _termRepository.save(allTerms);
        _baujahrRepository.save(allBaujahrs);
    }

    /**
     * Erzeugt anhand eines Datensatzes von Fahrzeugknoten und optionalen Metainformationen eine Liste von Objekten
     * vom Typ Baujahr. Diese sind den zugehörigen Fahrzeugknoten zugeordnet. Jedes Baujahr kommt dabei nur als ein Objekt vor.
     *
     * @param batchData Der Datensatz von Fahrzeugknoten und Metainformationen, für den die Baujahre erzeugt werden sollen.
     * @return Eine Liste von Objekte des Typs Baujahr, die den zugehörigen Fahrzeugknoten zugeordnet sind.
     */
    private List<Baujahr> relateAllBaujahrNodesToNodes(Map<VehicleNode, VehicleMetaData> batchData) {
        final List<Baujahr> allBaujahrs = new ArrayList<>();
        for (final VehicleNode node : batchData.keySet()) {
            final VehicleMetaData vehicleMetaData = batchData.get(node);

            if (vehicleMetaData.getBaujahrFrom() > 0) {
                final List<Integer> baujahrRange = createBaujahrRangeFrom(vehicleMetaData);
                for (Integer baujahr : baujahrRange) {
                    final Baujahr baujahrNode = getBaujahrFrom(node, baujahr);
                    if (allBaujahrs.contains(baujahrNode)) {
                        int index = allBaujahrs.indexOf(baujahrNode);
                        final Baujahr foundBaujahr = allBaujahrs.get(index);
                        foundBaujahr.addRelationTo(node);
                        allBaujahrs.set(index, foundBaujahr);
                    } else {
                        allBaujahrs.add(baujahrNode);
                    }
                }
            }
        }
        return allBaujahrs;
    }

    /**
     * Liefert anhand der angegebenene Metainformationen eines Fahrzeuges eine Liste von Baujahrwerten, die
     * anhand des ersten und letzten Baujahres erzeugt wurden. Die Liste umfasst alle in dem Zeitraum liegenenden Jahre.
     *
     * @param vehicleMetaData die Metainformationen, anhand derer die Jahre eines Bauzeitraums erzeugt werden sollen.
     * @return Eine Liste von Integerwerten, die alle Jahre eines angegebenen Bauzeitraums repräsentieren.
     */
    private List<Integer> createBaujahrRangeFrom(VehicleMetaData vehicleMetaData) {
        final List<Integer> allBaujahre = new ArrayList<>();
        if (vehicleMetaData.getBaujahrFrom() > 0 && vehicleMetaData.getBaujahrTo() > 0) {
            for (int i = vehicleMetaData.getBaujahrFrom(); i <= vehicleMetaData.getBaujahrTo(); i++) {
                allBaujahre.add(i);
            }
        } else if (vehicleMetaData.getBaujahrFrom() > 0) {
            allBaujahre.add(vehicleMetaData.getBaujahrFrom());
        } else if (vehicleMetaData.getBaujahrFrom() > 0) {
            allBaujahre.add(vehicleMetaData.getBaujahrTo());
        }
        return allBaujahre;
    }

    /**
     * Liefert eine Liste von Suchbegriffen, die anhand des angegebenen Datensatzes von Fahrzeugknoten mit optionalen
     * Metainformationen erzeugt wurden. Die Suchbegriffe als Objekte vom Typ Term werden dabei den Fahrzeugen zugeordnet.
     *
     * @param batchData Der Datensatz von Fahrzeugen und Metainformationen, anhand dessen die Suchbegriffe erzeugt und
     *                  zugeordnet werden sollen
     * @return Eine Liste von zugeordneten Suchbegriffen, die anhand des angegebenen Datensatzes erzeugt wurden.
     */
    private List<Term> relateAllTermsToNodes(Map<VehicleNode, VehicleMetaData> batchData) {
        final List<Term> allTerms = new ArrayList<>();
        for (final VehicleNode node : batchData.keySet()) {
            Collection<Term> termsForNode = getTermsFrom(node);
            termsForNode.addAll(getTermsFor(node, batchData.get(node).getAdditionalMetaData()));
            for (final Term term : termsForNode) {
                if (allTerms.contains(term)) {
                    int index = allTerms.indexOf(term);
                    final Term foundTerm = allTerms.get(index);
                    foundTerm.addRelationTo(node);
                    allTerms.set(index, foundTerm);
                } else {
                    allTerms.add(term);
                }
            }
        }
        return allTerms;
    }

    /**
     * Liefert ein Objekt vom Typ Baujahr anhand eines Fahrzeugknotens und einer Jahreszahl.
     * Das Baujahr wird dabei dem Fahrzeugknoten zugeordnet.
     * Ist ein Baujahr bereits in der Datenbank vorhanden, wird diese ergännzt um die Verbindung zum angegebenen
     * Fahrzeugknoten zurückgeliefert, statt dass ein neues erzeugt wird.
     *
     * @param vehicleNode Das Fahrzeug zu dem das Baujahr erzeugt werden soll.
     * @param baujahr     Die Jahreszahl des zu erzeugenden Baujahres.
     * @return Das Baujahr, welches anhand des Fahrzeuges und der Jahreszahl erzeugt wurde.
     */
    private Baujahr getBaujahrFrom(final VehicleNode vehicleNode, int baujahr) {
        Baujahr baujahrNode = _baujahrRepository.findByValue(baujahr);
        if (baujahrNode == null) {
            baujahrNode = new Baujahr();
            baujahrNode.setValue(baujahr);
        }
        baujahrNode.addRelationTo(vehicleNode);
        return baujahrNode;
    }

    /**
     * Liefert anhand eines Fahrzeuges und zugehörigen Suchbegriffen in Form von einem Set von Strings Objekte vom Typ Term.
     * Diese werden mit dem angegebenene Fahrzeugknoten (VehicleNode) verbunden. Desweiteren bestehen die Namen der Suchbegriffe
     * aus Gründen der Vereinheitlichung nur aus Kleinbuchstaben.
     * Sind bereits Suchbegriffe mit den angegebenen Namen in der Datenbank vorhanden, werden diese verwendet und um die
     * Verbindung zum Fahrzeugknoten erweitert.
     *
     * @param vehicleNode Das Fahrzeug, zu dem die zu erzeugenden Suchbegriffe verbunden werden sollen
     * @param tokens      Die zum Fahrzeug zugehörigen Suchbegriffe in Form eines Sets von Strings
     * @return Eine Liste von Suchbegriffen als Objekte vom Typ Term, die zum angegebenen Fahrzeug zugeordnet wurden.
     */
    private Collection<Term> getTermsFor(final VehicleNode vehicleNode, final Set<String> tokens) {
        final Collection<Term> terms = new ArrayList<>();
        for (final String token : tokens) {
            Term term = _termRepository.findByName(token);
            if (term == null) {
                term = new Term();
            }
            term.setName(toLowerCase(token));
            term.addRelationTo(vehicleNode);
            terms.add(term);
        }
        return terms;
    }

    /**
     * Liefert anhand einer VehicleNode die Suchbegriffe aus dem Namen des Fahrzeugs als Objekte vom Typ Term.
     *
     * @param vehicleNode Das Fahrzeug, für das Suchbegriffe erzeugt werden sollen.
     * @return Eine Liste von Suchbegriffen, die anhand des Fahrzeugnamens (Begriffe getrennt durch Leerzeichen) erzeugt wurden.
     */
    private Collection<Term> getTermsFrom(final VehicleNode vehicleNode) {
        final Set<String> tokenizedModelName = StringSplitterUtils.tokenize(vehicleNode);
        return getTermsFor(vehicleNode, tokenizedModelName);
    }

}
