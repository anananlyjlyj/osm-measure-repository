package org.giscience.measures.repository;

import org.giscience.measures.rest.server.RestServer;
import org.heigit.bigspatialdata.oshdb.api.db.OSHDBH2;

import javax.ws.rs.core.UriBuilder;

import org.giscience.osmMeasures.repository.MeasureNodesWithAddressInsideABuilding;

public class Run {

    public static final String databaseFile = "{{insert-name-of-database-here}}";

    public static void main(String[] args) throws Exception {
        OSHDBH2 oshdb = new OSHDBH2(databaseFile).multithreading(true);
        RestServer restServer = new RestServer();
          restServer.register(new MeasureNodesWithAddressInsideABuilding().setOSHDB(oshdb));
        restServer.run();
    }
}
