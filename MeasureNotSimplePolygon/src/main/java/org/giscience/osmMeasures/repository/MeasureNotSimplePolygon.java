package org.giscience.osmMeasures.repository;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.measures.tools.Index;
import org.giscience.measures.tools.Lineage;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBJdbc;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.geometry.OSHDBGeometryBuilder;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.DefaultTagInterpreter;


import java.util.Collections;
import java.util.SortedMap;

public class MeasureNotSimplePolygon extends MeasureOSHDB<Number, OSMEntitySnapshot> {

    @Override
    public Boolean refersToTimeSpan() {
        return false;
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
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMEntitySnapshot> mapReducer, OSHDBRequestParameter p) throws Exception {
        return Cast.result(mapReducer
                .osmType(OSMType.WAY)
                .osmTag(p.getOSMTag())
                .filter(snapshot -> snapshot.getGeometry().getDimension()==2)
                // dim == 2 same as defaultTagInterpreter.isArea(snapshot.getEntity());
                .map(snapshot -> {
                    try {
                        if (!snapshot.getGeometry().isSimple()) {return 1.;}
                        } catch (Exception e) {}
                        return 0.; })
                        .sum());
    }
}
