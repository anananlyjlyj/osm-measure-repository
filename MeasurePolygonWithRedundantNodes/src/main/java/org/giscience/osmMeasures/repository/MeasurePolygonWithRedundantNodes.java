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
                .osmType(OSMType.WAY)
                .osmTag("building")
                //.where(entity -> defaultTagInterpreter.isArea(entity))
                //.filter(snapshot -> ((LineString) snapshot.getGeometryUnclipped()).isClosed())
                .filter(snapshot -> snapshot.getGeometryUnclipped().getNumPoints() > 5)
                // because we excludes rectangles, which contains exactly 5 nodes
                .map(snapshot -> {
                    Geometry g = snapshot.getGeometryUnclipped();
                    for (int i = 0; i < g.getNumPoints() - 1; i++) {
                        for (int j = i + 1; j < g.getNumPoints() - 2; j++) {
                            // number of distance can be changed later
                            try {
                                if (Geo.isWithinDistance(StaticGeometry.pointN(g, i), StaticGeometry.pointN(g, j), 0.01)) {
                                    return 1.;
                                }
                            }catch (Exception e) {}
                            return 0.;
                        }
                    }return 0.;
                })
                .sum());
    }
}
