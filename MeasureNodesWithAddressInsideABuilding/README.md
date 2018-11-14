# README

## The Measure

The measure counts nodes with tag "address" inside a building

## Overview

This zip file contains a measure that consumes OSM data.  The measure is evaluated for grid cells of a global grid (ISEA 3H DGGS) and returns a number for each grid cell.

The content of the zip file is structured as follows:

data/       data about the measure
src/        java code
pom.xml     maven pom

The map `data` contains json files with a semantic description of the measure and of referenced information.  It also contains the SOAP code of the measure.  The SOAP code has automatically been converted to JAVA code, which can be found in the map `src`.  If the code of the measure could not be parsed correctly, there exists a file `ERROR.md`.

## Running the Code

The code can be installed and run as a Maven project.  In case of doubt, please download one of the following editors and import as a Maven project:

[IntelliJ IDEA](https://www.jetbrains.com/idea/)
[Eclipse](https://www.eclipse.org/downloads/)

The dependencies should automatically be downloaded and the REST server should be able to run.  You will find your measure under the following URL (please adapt the bounding box in a suitable way):

http://localhost:8080/api/nodes-with-address-inside-a-building/grid?resolution=14&bbox=11.8,57.6,12,57.9

The result can easily be visualized using the [geogrid.js](http://github.com/giscience/geogrid.js) library.
