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
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.DefaultTagInterpreter;
import org.heigit.bigspatialdata.oshdb.util.tagtranslator.TagTranslator;

import java.util.Collections;
import java.util.SortedMap;

public class MeasureNodesWithAddressInsideABuilding extends MeasureOSHDB<Number, OSMEntitySnapshot> {


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
    OSHDBJdbc oshdb = (OSHDBJdbc) this.getOSHDB();
    DefaultTagInterpreter defaultTagInterpreter = new DefaultTagInterpreter(oshdb.getConnection());
    TagTranslator translator = new TagTranslator(oshdb.getConnection());
    final String ADDRESS_KEY = "addr:housenumber";
    return Cast.result(mapReducer
            //.osmType(OSMType.NODE)
            .osmTag(entity -> {
                return entity.hasTagKey("building") || entity.hasTagKey("amenity"))
            .filter(snapshot -> {
                return snapshot.getEntity().hasTagKey(translator.getOSHDBTagKeyOf(ADDRESS_KEY));
            })
            .containsWhich(mapReduce -> {mapReduce.osmTag("");})
            .count());
    }
}
