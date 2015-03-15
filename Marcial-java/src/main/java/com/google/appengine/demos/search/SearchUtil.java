package com.google.appengine.demos.search;

/*
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.android.chrometophone.server.Backend;
import com.google.android.chrometophone.server.Content2;
import com.google.android.chrometophone.server.Util;
import com.google.android.chrometophone.server.utils.ParamsMap;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.gson.Gson;


public class SearchUtil 
{
	private final static Logger LOGGER = Logger.getLogger(SearchUtil.class.getName());

	public final static String CREATE_AND_INDEX_A_DOCUMENT_FOR_SEARCH = "createAndIndexADocumentForSearch";
	public final static String DELETE_DOCUMENTS_FOR_SEARCH = "deleteDocumentsForSearch";
	
	public final static SearchService searchService = SearchServiceFactory.getSearchService();
	public final static IndexSpec.Builder searchBuilder = IndexSpec.newBuilder();


	public static boolean postToBackendCreateDocument(Content2 content) 
	{
		if(content != null && Util.isAdmin(content.getEmail())) 
		{
			Gson gson = new Gson();
			String content_json = gson.toJson(content);

			// make a call to backend.
			try 
			{
				ParamsMap paramsMap = new ParamsMap();
				paramsMap.addParam("function", CREATE_AND_INDEX_A_DOCUMENT_FOR_SEARCH);
				paramsMap.addParam("content_json", content_json);

				// post to backends
				URL url = Backend.getUtilityUri();
				Backend.httpPost(url, 5, paramsMap);
			} 
			catch (Exception e) {
				LOGGER.severe("exception -> " +e.getMessage());
			}
		}

		return false;
	}
	
	public static boolean postToBackendDeleteDocuments(String email, List<String> docIds) 
	{
		if(Util.isAdmin(email)) {
		
			if(docIds != null) 
			{
				Gson gson = new Gson();
				String strDocIds = gson.toJson(docIds);
	
				// make a call to backend.
				try 
				{
					ParamsMap paramsMap = new ParamsMap();
					paramsMap.addParam("function", DELETE_DOCUMENTS_FOR_SEARCH);
					paramsMap.addParam("email", email);
					paramsMap.addParam("doc_ids", strDocIds);
	
					// post to backends
					URL url = Backend.getUtilityUri();
					Backend.httpPost(url, 5, paramsMap);
				} 
				catch (Exception e) {
					LOGGER.severe("exception -> " +e.getMessage());
				}
			}
		}

		return false;
	}

	public static void createAndIndexADocument(String email, Content2 content) 
	{
		List<Document> docList = new ArrayList<Document>();

		try
		{
			Document doc = Document.newBuilder()
					.setId(content.getId()) // Setting the document identifier
					.addField(Field.newBuilder().setName("body").setText(content.getBody()))
					.addField(Field.newBuilder().setName("phone_num").setText(content.getPhone_num()))
					.addField(Field.newBuilder().setName("phone_num_clean").setNumber(content.getPhoneNumClean()))
					.addField(Field.newBuilder().setName("inbox_outbox").setNumber(content.getInbox_outbox()))
					.addField(Field.newBuilder().setName("ts_server").setDate(content.getTS_server()))
					.build();

			docList.add(doc);

			// index the docs
			if(docList.size() > 0)
				indexDocuments(email, docList);
		}
		catch(Exception e)
		{
			LOGGER.severe("SearchUtil.createAndIndexADocument - " +e.getMessage());
		}
	}

	public static void indexDocuments(String email, List<Document> documents) 
	{
		Index index = getIndex(email);

		try {
			index.put(documents);
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				// retry putting the document

				LOGGER.severe("SearchUtil.indexDocuments - " +e.getMessage());
			}
		}
	}

	private static Index getIndex(String email)
	{
		IndexSpec indexSpec = searchBuilder.setName(email).build(); 
		Index index = searchService.getIndex(indexSpec);
		return index;
	}
	
	public static List<SearchResults> searchDocuments(String email, String queryString) 
	{
		//StringBuffer sbuf = new StringBuffer();
		List<SearchResults> searchResults = new ArrayList<SearchResults>();

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
	}


//	public static List<Content2> searchDocuments(String email, String queryString) 
//	{
//		List<Content2> contentList = new ArrayList<Content2>();
//
//		try 
//		{
//			Results<ScoredDocument> results = getIndex(email).search(queryString);
//
//			// Iterate over the documents in the results
//			for (ScoredDocument document : results) 
//			{
//				// handle results
//				Content2 content = new Content2(document);
//				contentList.add(content);
//			}
//		} 
//		catch (SearchException e) {
//			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
//				LOGGER.severe("SearchUtil.searchDocuments - " +e.getMessage());
//			}
//		}
//
//		return contentList;
//	}

	// AS: docIds are same as Content2 ids. 
	public static void deleteDocuments(String email, List<String> docIds) 
	{
		try
		{
			getIndex(email).delete(docIds);
		}
		catch(Exception e)
		{
			LOGGER.severe("SearchUtil.deleteDocuments - " +e.getMessage());
		}
	}
}
*/