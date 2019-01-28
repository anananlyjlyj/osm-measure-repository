package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import org.geotools.filter.function.StaticGeometry;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
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

public class MeasurePolygonsWithRepeatedPoints extends MeasureOSHDB<Number, OSMEntitySnapshot> {

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
                .filter(snapshot -> snapshot.getGeometry().getDimension()==1)
                .filter(snapshot -> ((LineString) snapshot.getGeometryUnclipped()).isClosed())
                .map(snapshot -> {
                    Geometry g = snapshot.getGeometryUnclipped();
                    for (int i = 0; i < g.getNumPoints() - 1; i++) {
                        try {
                            if (StaticGeometry.equalsExact(StaticGeometry.pointN(g,i),StaticGeometry.pointN(g,i+1))) {
                                return 1.; }
                        } catch (Exception e) {}
                        }
                    return 0.;
                })
                .sum());
    }
}
