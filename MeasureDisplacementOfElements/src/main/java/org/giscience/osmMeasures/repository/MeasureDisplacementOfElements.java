package org.giscience.osmMeasures.repository;

import com.vividsolutions.jts.geom.Coordinate;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.measures.tools.Index;
import org.giscience.measures.tools.Lineage;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.OSMContributionView;
import org.heigit.bigspatialdata.oshdb.api.object.OSMContribution;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.celliterator.ContributionType;
import org.heigit.bigspatialdata.oshdb.util.geometry.Geo;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.DefaultTagInterpreter;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.TagInterpreter;

import java.util.SortedMap;

public class MeasureDisplacementOfElements extends MeasureOSHDB<Number, OSMContribution> {

    @Override
    public Boolean refersToTimeSpan() {
        return false;
    }

    @Override
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMContribution> mapReducer, OSHDBRequestParameter p) throws Exception {
        return Index.reduce(
                mapReducer
                        .osmTag(p.getOSMTag())
                        .aggregateBy(contribution -> contribution.getEntityAfter().getId())
                        .filter(snapshot -> {
                            return  snapshot.getGeometryBefore().getDimension() == 2 &&
                                    snapshot.getContributionTypes().contains(ContributionType.GEOMETRY_CHANGE);
                                    // TODO: exclude exaggerated change
                                    //&&(Geo.areaOf(snapshot.getGeometryAfter()) / Geo.areaOf(snapshot.getGeometryBefore()) <= 10);
                        })
                        .sum(contribution -> {
                            Coordinate before = contribution.getGeometryBefore().getCentroid().getCoordinate();
                            Coordinate after = contribution.getGeometryAfter().getCentroid().getCoordinate();
                            com.vividsolutions.jts.geom.GeometryFactory gf = new com.vividsolutions.jts.geom.GeometryFactory();
                            return Geo.lengthOf(gf.createLineString(new Coordinate[] {before, after}));

                        }),
                Lineage::sum);
    }
}
