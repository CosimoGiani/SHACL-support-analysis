package com.shacl.rdf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.exceptions.ValidationException;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.shacl.ShaclSail;

public class AppRDF4J {
	
	private static final String SHAPES = "/<<SHAPE_FILE>>.ttl";
	private static final String DATASET = "/<<DATASET_NAME>>.nt";
	private static final String REPORT = "/<<REPORT_NAME>>.ttl";
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Starting application: " + java.time.LocalTime.now());
		
		Path path = Paths.get(".").toAbsolutePath().normalize();
		
		// Create the sail repository for data storage
		ShaclSail shaclSail = new ShaclSail(new MemoryStore());
		SailRepository sailRepository = new SailRepository(shaclSail);
        sailRepository.init();
        
        try (SailRepositoryConnection connection = sailRepository.getConnection()) {
        	
        	// Read the shapes
        	connection.begin();
        	FileReader shaclRules = new FileReader(path + "/resources" + SHAPES);
        	connection.add(shaclRules, "", RDFFormat.TURTLE, RDF4J.SHACL_SHAPE_GRAPH);
        	connection.commit();
        	
        	// Read the data
            connection.begin();
            FileReader data = new FileReader(path + "/resources" + DATASET);
            connection.add(data, "", RDFFormat.NTRIPLES);
            
            try {
            	
            	// Perform validation for the data in the repository
            	System.out.println("Starting validation: " + java.time.LocalTime.now());
            	connection.commit();
            	
            } catch (RepositoryException e) {
            	
            	// If an exception is raised during the validation ...
            	System.out.println("Validation failed: " + java.time.LocalTime.now());
				Throwable cause = e.getCause();
				
				// ... a violation report is written
				if (cause instanceof ValidationException) {
					
					Model validationReportModel = ((ValidationException) cause).validationReportAsModel();
					String report = path + "/resources" + REPORT;
					File reportFile = new File(report);
					reportFile.createNewFile();
					OutputStream reportOutputStream = new FileOutputStream(reportFile);
					Rio.write(validationReportModel, reportOutputStream, RDFFormat.TURTLE);
			
				}
				
				throw e;
				
			}
        	
        }
		
	}

}
