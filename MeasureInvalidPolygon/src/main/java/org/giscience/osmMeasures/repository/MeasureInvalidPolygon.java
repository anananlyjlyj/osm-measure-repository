package org.giscience.osmMeasures.repository;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.giscience.yajie.FakeTagInterpreter;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.geometry.OSHDBGeometryBuilder;

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
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMEntitySnapshot> mapReducer, RequestParameter p) throws Exception {
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
                .filter(snapshot -> {
                    return defaultTagInterpreter.isArea(snapshot.getEntity());
                })
                .map(snapshot -> {
                        if (!OSHDBGeometryBuilder.getGeometry(snapshot.getEntity(),
                                snapshot.getTimestamp(), defaultTagInterpreter
                        ).isSimple()) {
                            return 1;
                }})
                .sum());
        // EXAMPLE END
    }
}
