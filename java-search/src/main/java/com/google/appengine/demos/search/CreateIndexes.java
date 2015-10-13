// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.appengine.demos.search;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.search.Field;

public class CreateIndexes extends HttpServlet {

	private static final long serialVersionUID = 1L;
	



	private static final Logger LOG = Logger.getLogger(CreateIndexes.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String outcome = null;
		String searchOutcome = search(req);
		if (outcome == null) {
			outcome = searchOutcome;
		}
		req.setAttribute("outcome", outcome);
		req.getRequestDispatcher("asbadaspossible.jsp").forward(req, resp);
	}

	/**
	 * Searches the index for matching documents. If the query is not specified
	 * in the request, we search for any documents.
	 */
private static String INDEXNAME="Deleteme";
	private String search(HttpServletRequest req) {

		String outcome = null;
		try {

			
			SearchService searchService = SearchServiceFactory.getSearchService();
		
			
				IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEXNAME).build(); 
			Index index = searchService.getIndex(indexSpec);
			

			String myDocId = "PA6-5000";
			
			Document doc = Document.newBuilder()
					.setId(myDocId) // Setting the document identifer is optional. If omitted, the search service will create an identifier.

				    .addField(Field.newBuilder().setName("content").setText("the rain in spain"))
				    .build();


			    
			    try {
			        index.put(doc);
			    } catch (PutException e) {
			        if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
			            // retry putting the document
			        }
			    }

		} catch (RuntimeException e) {
			LOG.log(Level.SEVERE,
					"Failed", e);
			outcome = "Search failed due to an error: " + e.getMessage();
		}

		return outcome;
	}

}
