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

/*
 * 

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PointCount {

	@PrimaryKey
	private String pKey = "";


	@Persistent
	private String userId = "";


	@Persistent
	private int totalPoint = 0;


	@Persistent
	private String currentDate = "";


	@Persistent
	private List<String> thisPointIdList = new ArrayList<String>();


	@Persistent
	private List<String> thisPointValueList = new ArrayList<String>();


	@Persistent
	private List<String> gTotalPointIdList = new ArrayList<String>();


	@Persistent
	private List<String> gTotalPointValueList = new ArrayList<String>();


	@Persistent
	private int lastTotalPoint = 0;


	@Persistent
	private String lastSummaryDt = "";


	@Persistent
	private int revisePoint = 0;


	@Persistent
	private Date pcUpdateDt = null;


	@Persistent
	private String filler01 = "";


	@Persistent
	private String filler02 = "";


	@Persistent
	private String filler03 = "";


	@Persistent
	private String filler04 = "";

	@Persistent
	private String filler05 = "";

	@Persistent
	private String filler06 = "";

	@Persistent
	private String filler07 = "";

	@Persistent
	private String filler08 = "";

	@Persistent
	private String filler09 = "";

	@Persistent
	private String filler10 = "";

	@Persistent
	private String compCd = "";

	@Persistent
	private Date createDt = new Date();

	@Persistent
	private String createUser = "";

	@Persistent
	private Date updateDt = new Date();

	@Persistent
	private String updateUser = "";

	
	public String getpKey() {
		return pKey;
	}

	public void setpKey(String pKey) {
		this.pKey = pKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getThisPointIdList() {
		return thisPointIdList;
	}

	public void setThisPointIdList(List<String> thisPointIdList) {
		this.thisPointIdList = thisPointIdList;
	}

	public List<String> getThisPointValueList() {
		return thisPointValueList;
	}

	public void setThisPointValueList(List<String> thisPointValueList) {
		this.thisPointValueList = thisPointValueList;
	}

	public List<String> getgTotalPointIdList() {
		return gTotalPointIdList;
	}

	public void setgTotalPointIdList(List<String> gTotalPointIdList) {
		this.gTotalPointIdList = gTotalPointIdList;
	}

	public List<String> getgTotalPointValueList() {
		return gTotalPointValueList;
	}

	public void setgTotalPointValueList(List<String> gTotalPointValueList) {
		this.gTotalPointValueList = gTotalPointValueList;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public int getLastTotalPoint() {
		return lastTotalPoint;
	}

	public void setLastTotalPoint(int lastTotalPoint) {
		this.lastTotalPoint = lastTotalPoint;
	}

	public String getLastSummaryDt() {
		return lastSummaryDt;
	}

	public void setLastSummaryDt(String lastSummaryDt) {
		this.lastSummaryDt = lastSummaryDt;
	}

	public int getRevisePoint() {
		return revisePoint;
	}

	public void setRevisePoint(int revisePoint) {
		this.revisePoint = revisePoint;
	}

	public Date getPcUpdateDt() {
		return pcUpdateDt;
	}

	public void setPcUpdateDt(Date pcUpdateDt) {
		this.pcUpdateDt = pcUpdateDt;
	}

	public String getFiller01() {
		return filler01;
	}

	public void setFiller01(String filler01) {
		this.filler01 = filler01;
	}

	public String getFiller02() {
		return filler02;
	}

	public void setFiller02(String filler02) {
		this.filler02 = filler02;
	}

	public String getFiller03() {
		return filler03;
	}

	public void setFiller03(String filler03) {
		this.filler03 = filler03;
	}

	public String getFiller04() {
		return filler04;
	}

	public void setFiller04(String filler04) {
		this.filler04 = filler04;
	}

	public String getFiller05() {
		return filler05;
	}

	public void setFiller05(String filler05) {
		this.filler05 = filler05;
	}

	public String getFiller06() {
		return filler06;
	}

	public void setFiller06(String filler06) {
		this.filler06 = filler06;
	}

	public String getFiller07() {
		return filler07;
	}

	public void setFiller07(String filler07) {
		this.filler07 = filler07;
	}

	public String getFiller08() {
		return filler08;
	}

	public void setFiller08(String filler08) {
		this.filler08 = filler08;
	}

	public String getFiller09() {
		return filler09;
	}

	public void setFiller09(String filler09) {
		this.filler09 = filler09;
	}

	public String getFiller10() {
		return filler10;
	}

	public void setFiller10(String filler10) {
		this.filler10 = filler10;
	}

	public String getCompCd() {
		return compCd;
	}

	public void setCompCd(String compCd) {
		this.compCd = compCd;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
Create:
	PointCount pcObj = new PointCount();
	PersistenceManager pm = PMF.get().getPersistenceManager();
	
	try{
		// property value setter
		pcObj.setRevisePoint(revisePoint);	// The type of 'revisePoint' is int.
	
		pm.makePersistent(pcObj);

	}catch(Exception e){
		// error handling
	}finally{
		pm.close();
	}

Retrieve:
	PersistenceManager pm = PMF.get().getPersistenceManager();

	try{
		PointCount d = pm.getObjectById(PointCount.class, pKey); // 'pKey' is primary Key of PointCount Entity
		pm.detachCopyAll(d);
   	
		// property value setter
		int revisePoint = d.getRevisePoint();

	} catch(Exception e) {
		// error handling
	} finally {
		pm.close();
	}

Update:

	PersistenceManager pm = PMF.get().getPersistenceManager();

	try{

		PointCount d = pm.getObjectById(PointCount.class, pKey); // 'pKey' is primary Key of PointCount Entity
		
		// property value setter
		pcObj.setRevisePoint(revisePoint);	// The type of 'revisePoint' is int.
	
		pm.makePersistent(pcObj);
	}catch(Exception e){
		// error handling
		
	}finally{
		pm.close();
	}

}*/
