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
                .osmTag(p.getOSMTag())
                .filter(snapshot -> snapshot.getGeometry().getDimension()==1)
                //.filter(snapshot -> ((LineString) snapshot.getGeometryUnclipped()).isClosed())
                // example: isColsed == true -> result 87
                // without isColsed == true -> result 320
                .map(snapshot -> {
                    Geometry g = snapshot.getGeometryUnclipped();
                    for (int i = 0; i < g.getNumPoints() - 1; i++) {
                        for(int j = i+1; j < g.getNumPoints() - 1; j++)
                            if (StaticGeometry.equalsExact(StaticGeometry.pointN(g,i),StaticGeometry.pointN(g,j))) {
                            return 1.; }
                        }
                    return 0.;
                })
                .sum());
    }
}
