package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.algorithm.Angle;
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

import java.util.Collections;
import java.util.SortedMap;

public class MeasurePolygonWithAcuteAngle extends MeasureOSHDB<Number, OSMEntitySnapshot> {

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
                // example: isColsed == true -> result 87
                // without isColsed == true -> result 320
                .map(snapshot -> {
                    Geometry g = snapshot.getGeometryUnclipped();
                    Integer n = StaticGeometry.numPoints(g);
                    for (int i = 0; i < n - 1; i++) {
                        int j = i + 1;
                        if (j >= g.getNumPoints()) j = j - n + 1;
                        int k = j + 1;
                        if (k >= g.getNumPoints()) k = k - n + 1;
                        // Value of angle can be changed later
                        if ((Angle.angleBetween(StaticGeometry.pointN(g,i).getCoordinate(),StaticGeometry.pointN(g,j).getCoordinate(), StaticGeometry.pointN(g,k).getCoordinate())<0.1)
                                && !StaticGeometry.equalsExact(StaticGeometry.pointN(g,i), StaticGeometry.pointN(g,j))
                                && !StaticGeometry.equalsExact(StaticGeometry.pointN(g,i), StaticGeometry.pointN(g,k))
                                && !StaticGeometry.equalsExact(StaticGeometry.pointN(g,k), StaticGeometry.pointN(g,j)))
                                return 1.; }
                    return 0.;
                })
                .sum());
    }
}
