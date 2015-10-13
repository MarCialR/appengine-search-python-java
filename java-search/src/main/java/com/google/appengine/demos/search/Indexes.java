// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.appengine.demos.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.search.Field;

public class Indexes extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String INDEXNAME = "Deleteme";

	private static String BIGTEXT = new String(new char[100000]);

	@SuppressWarnings("deprecation")
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PrintWriter pw = resp.getWriter();
		Index index;
		String what_param = req.getParameter("what");
		if (null==what_param)
			what_param = "INFO";

		switch (what_param) {
			case "CREATE":
				index = getIndex();
				pw.println("INSERTED 3DOCUMENTS IN " + INDEXNAME );
				break;
			
			case "INFO":
				index = getIndex();
				pw.println("StorageLimit " + index.getStorageLimit());
				pw.println("StorageUsage " + index.getStorageUsage());
				pw.println("Schema " + index.getSchema());
				
				break;				
			
			case "INSERT":
				index = getIndex();
				String[] myDocId = { "PA6-5000", "PA6-5003", "PA6-5002" };
				pw.println("INSERTING 3 DOCUMENTS IN " + INDEXNAME );

				for (String id : myDocId) {
					Document doc = Document.newBuilder()
							.setId(id)
							.addField(
									Field.newBuilder().setName("content")
											.setText("the rain in spain")).build();

					try {
						index.put(doc);
					} catch (PutException e) {
						if (StatusCode.TRANSIENT_ERROR.equals(e
								.getOperationResult().getCode())) {
						}
						pw.print("error, ");
					}
				}
				
				break;

			case "INSERT_MANY":
				index = getIndex();
				pw.println("INSERTING 100 DOCUMENTS IN " + INDEXNAME );
		        for(int i = 0; i < 1000; i++){
	
					Document doc = Document.newBuilder()
							.addField(
									Field.newBuilder().setName("content")
											.setText(BIGTEXT)).build();

					try {
						index.putAsync(doc);
					} catch (PutException e) {
						if (StatusCode.TRANSIENT_ERROR.equals(e
								.getOperationResult().getCode())) {
						}
						pw.print("error, ");
					}
		        }					
				break;
				
			case "DELETE_SCHEMA":
				index = getIndex();
				index.deleteSchema();
				pw.println("DELETED SCHEMA!");
				break;				

			case "DELETE_DOCS":
				index = getIndex();
				try {
				    // looping because getRange by default returns up to 100 documents at a time
				    while (true) {
				        List<String> docIds = new ArrayList<String>();
				        // Return a set of doc_ids.
				        GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
				        GetResponse<Document> response = index.getRange(request);
				        if (response.getResults().isEmpty()) {
				            break;
				        }
				        for (Document doc : response) {
				            docIds.add(doc.getId());
				        }
				        index.deleteAsync(docIds);
				    }
				} catch (RuntimeException e) {
					pw.println("Failed to delete documents");
				}					
				pw.println("DELETED DOCUMENTS!");

				break;				

			default:
				pw.println("NOTHING DONE!");
		}

	}

	private Index getIndex() {
		SearchService searchService = SearchServiceFactory.getSearchService();
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEXNAME).build();
		Index index = searchService.getIndex(indexSpec);
		return index;
	}


}
