/*package org.giscience.osmMeasures.repository;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.ints.*;
import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.rest.server.RequestParameter;
import org.giscience.measures.tools.Cast;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBH2;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBJdbc;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.OSMContributionView;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.OSMEntitySnapshotView;
import org.heigit.bigspatialdata.oshdb.api.object.OSMContribution;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.OSHDBBoundingBox;
import org.heigit.bigspatialdata.oshdb.util.celliterator.ContributionType;

import java.util.*;
import java.util.function.IntConsumer;

public class MeasureNumberOfTagRollback extends MeasureOSHDB<Number, OSMEntitySnapshot> {

    private static Int2IntMap tags(int[] rawTags) {
        Int2IntMap tags = new Int2IntArrayMap(rawTags.length/2);
        for(int i=0; i< rawTags.length; i +=2){
            tags.put(rawTags[i], rawTags[i+1]);
        }
        return tags;
    }
    private static class Tag implements Comparable<Tag> {
        public final int key;
        public final int value;

        public Tag(int key, int value){
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Tag o) {
            int c = Integer.compare(key, o.key);
            if(c != 0)         // if(c == 0)
                return Integer.compare(value, o.value);
            return c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key,value);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("Tag").add("key", key).add("value", value).toString();
        }
    }

    @Override
    public Boolean refersToTimeSpan() {
        return true;
    }

    @Override
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMEntitySnapshot> mapReducer, OSHDBRequestParameter p) throws Exception {
        return Cast.result(mapReducer
                .osmTag("highway")
                .count());
                /*.groupByEntity()
                // can be replaced by .aggregateBy(contribution -> contribution.getEntityAfter())?
                .flatMap(contributions -> {
                    if(contributions.isEmpty())
                        return Collections.emptyList();
                    long id = contributions.get(0).getEntityAfter().getId();
                    // Map key to a sequence of values
                    // so that k1 -> v1,v2,v3,v1,v2, ....  where the first element in the sequence is the oldest
                    Int2ObjectMap<IntList> keyValues = new Int2ObjectOpenHashMap<>();
                    // contribution
                    contributions.stream()
                            .filter(contribution -> contribution.is(ContributionType.TAG_CHANGE))
                            .forEach(c -> {
                                Int2IntMap before = tags(c.getEntityBefore().getRawTags());
                                Int2IntMap after = tags(c.getEntityAfter().getRawTags());

                                before.keySet().forEach(new java.util.function.IntConsumer() {
                                    @Override
                                    public void accept(int key) {
                                        IntList values = keyValues.computeIfAbsent(key, k -> new IntArrayList() );
                                        int value = before.get(key);
                                        if(values.isEmpty() || values.getInt(values.size()-1) != value)
                                            values.add(value);

                                        //delete tags
                                        if(!after.containsKey(key))
                                            values.add(-1);
                                    }
                                });

                                after.keySet().forEach(new IntConsumer(){
                                    @Override
                                    public void accept(int key) {
                                        IntList values = keyValues.computeIfAbsent(key, k -> new IntArrayList());
                                        int value = after.get(key);
                                        if(values.isEmpty() || values.getInt(values.size()-1) != value)
                                            values.add(value);
                                    }
                                });
                            });

                    Set<Tag> changedBack = new TreeSet<>();
                    for(int key : keyValues.keySet()){
                        IntList values = keyValues.get(key);
                        for(int i=0; i < values.size(); i++){
                            int v1 = values.getInt(i);
                            for(int j=i+1; j < values.size(); j++){
                                int v2 = values.getInt(j);
                                if(v1 == v2){
                                    changedBack.add(new Tag(key,v1));
                                    break;
                                }
                            }
                        }
                    }

                    if(changedBack.isEmpty())
                        return Collections.emptyList();

                    // convert set to list
                    List<Tag> result = new ArrayList<>(changedBack.size());
                    for(Tag t : changedBack)
                        result.add(t);
                    //TODO remove comment this debug output
                    //System.out.println(""+id+" -> "+result);
                    return result;
                }).aggregateBy(x -> x).count();

        res.forEach((tag,histo) -> {
            System.out.println(tag+" : "+histo);
        });*/

    }
    }*/