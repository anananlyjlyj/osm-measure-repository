package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.geom.Geometry;
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

public class MeasurePolygonCompactness extends MeasureOSHDB<Number, OSMEntitySnapshot> {

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
        return Cast.result(mapReducer
                .osmType(OSMType.WAY)
                .filter(snapshot -> snapshot.getGeometryUnclipped().getNumPoints() > 4)
                // because we exclude triangle, which contains exactly 4 nodes
                .filter(snapshot -> {
                    // implemented according to Polsby-Popper Test
                    Geometry g = snapshot.getGeometryUnclipped();
                    Double perimeter = Geo.lengthOf(g.getBoundary());
                    Double PPD = 4*Math.PI*Geo.areaOf(g)/(perimeter*perimeter);
                    // PPD close to 1 means the polygon has great compactness
                    return PPD < 0.1;
                })
                .count());
    }
}
