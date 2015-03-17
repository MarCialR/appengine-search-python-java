
//Copyright 2011 Google Inc. All Rights Reserved.

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
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;



public class User extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SearchService searchService = SearchServiceFactory.getSearchService();
	private IndexSpec.Builder searchBuilder = IndexSpec.newBuilder();

	private static final Logger LOGGER = Logger.getLogger(User.class
			.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String outcome = null;	
		String searchOutcome = search(req);
		if (outcome == null) {
			outcome = searchOutcome;
		}
		req.setAttribute("outcome", outcome);
		req.getRequestDispatcher("user.jsp").forward(req, resp);
	}

	/**
	 * Searches the index for matching documents. If the query is not specified
	 * in the request, we search for any documents.
	 */

	private String search(HttpServletRequest req) {
		String email = "1001_Nights";
		
		String queryString = req.getParameter("query");
		if (queryString == null) {
			queryString = "";
		}

		List<Document> found = new ArrayList<Document>();		
		String outcome = null;
		
		
		
//-------------------------------------------------------------------------------------		
		//StringBuffer sbuf = new StringBuffer();
/*		List<SearchResults> searchResults = new ArrayList<SearchResults>();*/

		try 
		{
			LOGGER.severe("BEFORE IndexSpec.newBuilder().setName(email).build");
			
			//Results<ScoredDocument> results = getIndex(email).search(queryString);
			IndexSpec indexSpec = searchBuilder.setName(email).build();
			LOGGER.severe("AFTER IndexSpec.newBuilder().setName(email).build");
			
			Index index = searchService.getIndex(indexSpec);
			
			LOGGER.severe("AFTER SearchServiceFactory.getSearchService().getIndex(indexSpec)");

			Results<ScoredDocument> results = index.search(queryString);
			
			LOGGER.severe("AFTER index.search(queryString)");

/*	
			// Iterate over the documents in the results
			for (ScoredDocument document : results) 
			{
				// handle results
				//sbuf.append(document.getOnlyField("body").getText());
				SearchResults result = new SearchResults(document);
				searchResults.add(result);
			}
			
			LOGGER.severe("After fetching the results");
		} 
		catch (Exception e) {
			LOGGER.severe("SearchUtil.searchDocuments - " +e.getMessage());
			//if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				//LOGGER.severe("SearchUtil.searchDocuments - " +e.getMessage());
			//}
		}

		return searchResults;
*/		
//-------------------------------------------------------------------------------------		

			Utils.processFound(found, results);

		} catch (RuntimeException e) {
			LOGGER.log(Level.SEVERE,
					"Search with query '" + queryString + "' failed", e);
			outcome = "Search failed due to an error: " + e.getMessage();
		}
		req.setAttribute("found", found);
		return outcome;


	}

}
