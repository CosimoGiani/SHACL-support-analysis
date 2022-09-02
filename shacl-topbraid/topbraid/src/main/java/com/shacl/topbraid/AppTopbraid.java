package com.shacl.topbraid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.util.JenaUtil;

public class AppTopbraid {

	private static Logger logger = LoggerFactory.getLogger(AppTopbraid.class);
	private static final Marker MARKER = MarkerFactory.getMarker("marker");
	
	private static final String DIRECTORY = "/<<NAME_OF_DIRECTORY>>";
	private static final String SHAPES = "/<<SHAPE_FILE>>.ttl";
	private static final String DATASET = "/<<DATASET_NAME>>.nt";
	private static final String REPORT = "/<<REPORT_NAME>>.ttl";

	public static void main(String[] args) {

		try {

			logger.info("Starting application...");
			
			// Read the data and the shapes
			Path path = Paths.get(".").toAbsolutePath().normalize();
			String directory = path + "/resources" + DIRECTORY;
			Dataset dataset = TDBFactory.createDataset(directory);
			String shape = path + "/resources" + SHAPES;
			Model tdb = dataset.getDefaultModel();
			String source = path + "/resources" + DATASET;
			FileManager.get().readModel(tdb, source);
			Model shapeModel = JenaUtil.createDefaultModel();
			shapeModel.read(shape);

			logger.info("Starting validation...");
			
			// Perform validation of the shapes against the data stored inside the tdb
			Resource reportResource = ValidationUtil.validateModel(tdb, shapeModel, true);
			boolean conforms  = reportResource.getProperty(SH.conforms).getBoolean();
			
			logger.trace("Conforms = " + conforms);

			// If the standard is not respected, a report is written
			if (!conforms) {
				
				String report = path.toFile().getAbsolutePath() + "/resources" + REPORT;
				File reportFile = new File(report);
				reportFile.createNewFile();     
				OutputStream reportOutputStream = new FileOutputStream(reportFile);
				RDFDataMgr.write(reportOutputStream, reportResource.getModel(), RDFFormat.TURTLE);
				
			}
			
			logger.info("Closing application...");

		} catch (Throwable t) {
			
			logger.error(MARKER, t.getMessage(), t);
			
		}
		
	}
	
}
