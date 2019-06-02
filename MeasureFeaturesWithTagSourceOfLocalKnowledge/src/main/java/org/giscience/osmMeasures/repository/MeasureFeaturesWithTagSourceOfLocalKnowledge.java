package org.giscience.osmMeasures.repository;

import jdk.nashorn.internal.parser.JSONParser;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBJdbc;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.OSHDBTag;
import org.heigit.bigspatialdata.oshdb.util.tagtranslator.TagTranslator;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.SortedMap;

import static org.springframework.cglib.core.Constants.SOURCE_FILE;

public class MeasureFeaturesWithTagSourceOfLocalKnowledge extends MeasureOSHDB<Number, OSMEntitySnapshot> {

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
        // Read different tags for source knowledge: source = local knowledge or source = local_knowledge;
        OSHDBJdbc oshdb = (OSHDBJdbc) this.getOSHDB();
        TagTranslator tagTranslator = new TagTranslator(oshdb.getConnection());
        ArrayList<String> elem = new ArrayList<String>();
        elem.add("local knowledge");
        elem.add("local_knowledge");
        return Cast.result(mapReducer
                .osmTag(p.getOSMTag())
                // -- ENTITY FILTER --
                .osmEntityFilter(entity -> {
                        OSHDBTag tag = tagTranslator
                                .getOSHDBTagOf(elem.get(0), elem.get(1));
                    return entity.hasTagValue(tag.getKey(), tag.getValue());
                })

                // -- MAPPING --
                .map(OSMEntitySnapshot::getEntity)
//                .map(snapshot -> {
//                    Stream<OSMNode> wayNodes;
//                    wayNodes = ((OSMWay)snapshot.getEntity()).getRefEntities(snapshot.getTimestamp());
//                    System.out.println(wayNodes.toString());
//                    return wayNodes;
//                })

                // -- AGGREGATION --
                //.aggregateByTimestamp()
                .count());
    }
}
