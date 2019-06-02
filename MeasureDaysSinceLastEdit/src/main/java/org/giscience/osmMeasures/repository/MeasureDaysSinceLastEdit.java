package org.giscience.osmMeasures.repository;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.SortedMap;

public class MeasureDaysSinceLastEdit extends MeasureOSHDB<Number, OSMEntitySnapshot> {

/*
    @Override
    public Boolean refersToTimeSpan() {
        return false;
    }

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
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMEntitySnapshot> mapReducer, OSHDBRequestParameter p) throws Exception {
        // EXAMPLE ONLY - PLEASE INSERT CODE HERE
        return Cast.result(mapReducer
                .osmTag(p.getOSMTag())
                .map(snapshot -> {
                    Date date_last_edit = snapshot.getEntity().getTimestamp().toDate();
                    LocalDate d_l_e = new java.sql.Date(date_last_edit.getTime()).toLocalDate();
                    LocalDate now = LocalDate.now();
                    long diff = ChronoUnit.DAYS.between(d_l_e, now);
                    return diff;
                })
                .average());
        // EXAMPLE END
    }
}
