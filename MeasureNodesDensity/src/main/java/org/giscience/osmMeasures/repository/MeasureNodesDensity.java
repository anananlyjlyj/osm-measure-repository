package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.geom.Geometry;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.measures.tools.Index;
import org.giscience.measures.tools.Lineage;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.util.geometry.Geo;

import java.util.SortedMap;

public class MeasureNodesDensity extends MeasureOSHDB<Number, OSMEntitySnapshot> {


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
        return Index.reduce(
                mapReducer
                        .osmTag(p.getOSMTag())
                        .filter(snapshot -> snapshot.getGeometryUnclipped().getNumPoints() > 5)
                        // make sure is polygon
                        .filter(snapshot -> snapshot.getGeometry().getDimension()==1)
                        .aggregateBy(snapshot -> snapshot.getEntity().getId())
                        .sum(snapshot -> {
                            Geometry g = snapshot.getGeometryUnclipped();
                            return Geo.areaOf(g)/g.getNumPoints();
                        }),
                Lineage::sum);

    }
}
