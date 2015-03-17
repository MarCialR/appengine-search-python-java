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


public class AsBadAsPossible extends HttpServlet {

	private static final long serialVersionUID = 1L;
	



	private static final Logger LOG = Logger.getLogger(AsBadAsPossible.class.getName());

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

	private String search(HttpServletRequest req) {
		String queryStr = req.getParameter("query");
		if (queryStr == null) {
			queryStr = "";
		}

		List<Document> found = new ArrayList<Document>();
		String outcome = null;
		try {
			Query query = Query.newBuilder()
					// .setOptions(QueryOptions.newBuilder().setLimit(limit).build()).build(queryStr);
					.setOptions(QueryOptions.newBuilder().build())
					.build(queryStr);

			LOG.info("Sending query " + query);

			
			SearchService searchService = SearchServiceFactory.getSearchService();
			IndexSpec.Builder searchBuilder = IndexSpec.newBuilder();			
			
			// call to .build() makes no difference
			// INDEX = SearchServiceFactory.getSearchService()
			// .getIndex(IndexSpec.newBuilder().setName("1001_Nights").build());
			Index index = searchService.getIndex(searchBuilder.setName("1001_Nights").build());

			LOG.severe("Fresh index instance BEFORE INDEX.search(query)");
			Results<ScoredDocument> results = index.search(query);
			LOG.severe("Fresh index instance AFTER INDEX.search(query)");

			Utils.processFound(found, results);

		} catch (RuntimeException e) {
			LOG.log(Level.SEVERE,
					"Search with query '" + queryStr + "' failed", e);
			outcome = "Search failed due to an error: " + e.getMessage();
		}
		req.setAttribute("found", found);
		return outcome;
	}

}
