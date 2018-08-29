package org.giscience.osmMeasures.repository;
import org.heigit.bigspatialdata.oshdb.util.tagInterpreter.TagInterpreter;
import org.heigit.bigspatialdata.oshdb.osm.OSMEntity;
import org.heigit.bigspatialdata.oshdb.osm.OSMRelation;
import org.heigit.bigspatialdata.oshdb.osm.OSMMember;

import java.util.Map;
import java.util.Set;

class FakeTagInterpreter implements TagInterpreter {
    int areaNoTagKeyId, areaNoTagValueId;
    Map<Integer, Set<Integer>> wayAreaTags;
    Map<Integer, Set<Integer>> relationAreaTags;
    Set<Integer> uninterestingTagKeys;
    int outerRoleId, innerRoleId, emptyRoleId;

    FakeTagInterpreter(
            int areaNoTagKeyId,
            int areaNoTagValueId,
            Map<Integer, Set<Integer>> wayAreaTags,
            Map<Integer, Set<Integer>> relationAreaTags,
            Set<Integer> uninterestingTagKeys,
            int outerRoleId,
            int innerRoleId,
            int emptyRoleId
    ) {
        this.areaNoTagKeyId = areaNoTagKeyId;
        this.areaNoTagValueId = areaNoTagValueId;
        this.wayAreaTags = wayAreaTags;
        this.relationAreaTags = relationAreaTags;
        this.uninterestingTagKeys = uninterestingTagKeys;
        this.outerRoleId = outerRoleId;
        this.innerRoleId = innerRoleId;
        this.emptyRoleId = emptyRoleId;
    }
    @Override
    public boolean isArea(OSMEntity entity) { return false; }
    @Override
    public boolean isLine(OSMEntity entity) { return true; }
    @Override
    public boolean hasInterestingTagKey(OSMEntity osm) { return false; }
    @Override
    public boolean isMultipolygonOuterMember(OSMMember osmMember) { return false; }
    @Override
    public boolean isMultipolygonInnerMember(OSMMember osmMember) { return false; }
    @Override
    public boolean isOldStyleMultipolygon(OSMRelation osmRelation) { return false; }

}
