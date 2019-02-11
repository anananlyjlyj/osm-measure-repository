package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.filter.function.StaticGeometry;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.geometry.Geo;

import java.util.Collections;
import java.util.SortedMap;

public class MeasurePolygonWithRedundantNodes extends MeasureOSHDB<Number, OSMEntitySnapshot> {


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
                .osmTag(p.getOSMTag())
                .filter(snapshot -> snapshot.getGeometry().getDimension()==1)
                //.filter(snapshot -> ((LineString) snapshot.getGeometryUnclipped()).isClosed())
                .map(snapshot -> {
                    Geometry g = snapshot.getGeometryUnclipped();
                    for (int i = 0; i < g.getNumPoints(); i++) {
                        for(int j = i+1; j < g.getNumPoints()-1;j++){
                            // number of distance can be changed later
                            if (Geo.isWithinDistance(StaticGeometry.pointN(g, i), StaticGeometry.pointN(g, j), 0.01)
                                && !StaticGeometry.equalsExact(StaticGeometry.pointN(g, i),StaticGeometry.pointN(g, j)) )
                            { return 1.;}
                    }}return 0.;
                })
                .sum());
    }
}
