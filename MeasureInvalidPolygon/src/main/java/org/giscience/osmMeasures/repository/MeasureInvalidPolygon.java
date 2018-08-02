package org.giscience.osmMeasures.repository;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBJdbc;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.geometry.OSHDBGeometryBuilder;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.DefaultTagInterpreter;


import java.util.Collections;
import java.util.SortedMap;

public class MeasureInvalidPolygon extends MeasureOSHDB<Number, OSMEntitySnapshot> {

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
        OSHDBJdbc oshdb = (OSHDBJdbc) this.getOSHDB();
        DefaultTagInterpreter defaultTagInterpreter = new DefaultTagInterpreter(oshdb.getConnection());
        return Cast.result(mapReducer
                .tagInterpreter(new FakeTagInterpreter(
                        -1,
                        -1,
                        Collections.emptyMap(),
                        Collections.emptyMap(),
                        Collections.emptySet(),
                        -1,
                        -1,
                        -1
                ))
                .osmType(OSMType.WAY)
                .filter(snapshot -> defaultTagInterpreter.isArea(snapshot.getEntity()))
                .map(snapshot -> {
                        if (!OSHDBGeometryBuilder.getGeometry(snapshot.getEntity(),
                                snapshot.getTimestamp(), defaultTagInterpreter
                        ).isSimple()) {
                            return 1;
                        }
                        else
                        {return 0;}
                            })
                .sum());

    }
}
