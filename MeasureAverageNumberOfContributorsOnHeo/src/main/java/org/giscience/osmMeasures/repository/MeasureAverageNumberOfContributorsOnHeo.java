package org.giscience.osmMeasures.repository;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.measures.tools.Index;
import org.giscience.measures.tools.Lineage;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMContribution;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;

import java.util.SortedMap;

public class MeasureAverageNumberOfContributorsOnHeo extends MeasureOSHDB<Number, OSMContribution> {

    @Override
    public Boolean refersToTimeSpan() {
        return true;
    }
/*
    @Override
    public Integer defaultDaysBefore() {
        return 3 * 12 * 30;
    }

    @Override
    public Integer defaultIntervalInDays() {
        return 30;
    }
*/

    @Override
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMContribution> mapReducer, OSHDBRequestParameter p) throws Exception {
        return Index.reduce(
                mapReducer
                        // -- ENTITY FILTER --
                        .osmType(OSMType.WAY)
                        .osmTag(p.getOSMTag())
                        // Filter HEO
                        .osmEntityFilter(entity -> entity.getVersion() >= 15)
                        /*
                        // -- MAPPING --
                        .groupByEntity()
                        .flatMap(contribution -> {

                    // Map: KEY: HEO and VALUE: list of contributors
                    Map<Long, HashSet<Integer>> HEONumberOFContributors = new HashMap<>();
                    for (OSMContribution c : contribution) {
                        HEONumberOFContributors.computeIfAbsent(c.getEntityAfter().getId(), empList -> new HashSet<>())
                                .add(c.getContributorUserId());
                    }

                    // List contains zero values !!!
                    // List with number of contributors per HEO
                    List<Integer> listNumberOfContributors = new ArrayList<>();
                    for (HashSet<Integer> c : HEONumberOFContributors.values()) {
                        Integer countContributors = c.size();
                        listNumberOfContributors.add(countContributors);
                    }
                    return listNumberOfContributors;
                })

                // -- AGGREGATION --
                //.aggregateByTimestamp()
                .collect();
                         */
                        .aggregateBy(snapshot -> snapshot.getEntityAfter().getId())
                        .sum(),
                Lineage::sum
                /*x -> {
                    // Berechnung des Durchschnitts: Anzahl der Contributors pro HEO durch gesamte Anzahl von Contributors
                    //System.out.print(result.stream().mapToDouble(x -> x).average());

                    // Berechnung des Ratios: Verhältnis von der gesamten Anzahl an Contributors zu den Contributors pro HEO
                    int totalNumberOfContributors = 0;
                    for (int number : result) {
                        totalNumberOfContributors += number;
                    }

                    ArrayList<Double> RatioContributorsList = new ArrayList<>();
                    for (int number : result) {
                        double RatioContributors = (double) number / totalNumberOfContributors *100;
                        RatioContributorsList.add(RatioContributors);
                    }

                    RatioContributorsList.forEach(x -> System.out.print(x + "\n")); */

                    /* example:
                    Map<Integer, Integer> result1 = new HashMap<>();
                    for (Integer unique : new HashSet<>(x)) {
                        result1.put(unique, Collections.frequency(x, unique));
                    }


                    // Filter Senior Mappers
                    return result1.entrySet().stream()
                            .filter(t -> t.getValue() >= 1000)
                            .count();
                    */

                );

    }
}
