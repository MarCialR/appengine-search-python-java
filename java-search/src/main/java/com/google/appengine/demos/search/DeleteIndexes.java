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


public class DeleteIndexes extends HttpServlet {

	private static final long serialVersionUID = 1L;
	



	private static final Logger LOG = Logger.getLogger(DeleteIndexes.class.getName());

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
	private static String INDEXNAME="Deleteme";
	/**
	 * Searches the index for matching documents. If the query is not specified
	 * in the request, we search for any documents.
	 */

	private String search(HttpServletRequest req) {

		String outcome = null;
		try {
			String myDocId = "PA6-5000";
			
			SearchService searchService = SearchServiceFactory.getSearchService();
			
			
			IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEXNAME).build(); 
		Index index = searchService.getIndex(indexSpec);	
	    

		index.delete(myDocId);
		index.deleteSchema();


		} catch (RuntimeException e) {
			LOG.log(Level.SEVERE,
					"Failed", e);
			outcome = "Search failed due to an error: " + e.getMessage();
		}

		return outcome;
	}

}
