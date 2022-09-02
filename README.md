# SHACL support analysis
<p align="center">
  <img src="https://github.com/CosimoGiani/SHACL-support-analysis/blob/main/files/shacl-logo.jpg" style="width:800px;">
</p>

## About The Project
The project studies the quality of the support for `SHACL` (**SHA**pes **C**onstraint **L**anguage), a specific for validating graph-based data over a set of conditions. \
A SHACL validation engine takes as input a data graph and a graph containing the shapes declaration. As a result, it produces a validation report concerning data violations. \
\
To analyze the efficiency of the SHACL validation three tools were considered:
* `TopBraid` by [TopQuadrant](https://www.topquadrant.com/)
* `RDF4J` by [Eclipse Foundation](https://www.eclipse.org/org/foundation/)
* `Neosemantics` ([Neo4J](https://neo4j.com/)) by Neo Technology

This empirical analysis also takes into account the relative memory consumption and the average time required by the aforementioned tools for their execution.

More details about the project in the [paper](paper.pdf).

### Built with
* [Java](https://www.java.com/it/)
* [Maven](https://maven.apache.org/)
* [TopBraid framework](https://github.com/TopQuadrant/shacl)
* [RDF4J framework](https://rdf4j.org/documentation/programming/shacl/)
* [Neosemantics](https://neo4j.com/labs/neosemantics/) plugin for Neo4J

## Usage
* The implementation for the `TopBraid` and `RDF4J` frameworks are in the homonymous folders of the repo (*Maven* is a requirement). To validate with these tools it is required a graph dataset (i.e. DBpedia) and a set of [shapes](https://github.com/CosimoGiani/SHACL-support-analysis/tree/main/shapes). Once having plugged in the dataset and the shapes, just run the single-class function to perform SHACL validation. The results are then exported as a report file.
* In order to validate with the `Neosemantics` plugin it is necessary to first install the database management system *Neo4J* and the configure the plugin to enable SHACL support. To perform validation the plugin leverages a graph query language called [Chyper](https://neo4j.com/developer/cypher/). Some useful Chyper commands can be found [here](https://github.com/CosimoGiani/SHACL-support-analysis/blob/main/Chyper%20commands.pdf).
