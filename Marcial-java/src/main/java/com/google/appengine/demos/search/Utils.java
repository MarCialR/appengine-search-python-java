package com.google.appengine.demos.search;

import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.users.User;

public class Utils {
	private static final Logger LOG = Logger.getLogger(Utils.class
			.getName());	

	public static void processFound(List<Document> found, Results<ScoredDocument> results) {
		for (ScoredDocument scoredDoc : results) {
			User author = new User(
					getOnlyField(scoredDoc, "email", "user"), getOnlyField(
							scoredDoc, "domain", "example.com"));
			// Rather than presenting the original document to the
			// user, we build a derived one that holds author's nickname.
			List<Field> expressions = scoredDoc.getExpressions();
			String content = null;
			if (expressions != null) {
				for (Field field : expressions) {
					if ("content".equals(field.getName())) {
						content = field.getHTML();
						break;
					}
				}
			}
			if (content == null) {
				content = getOnlyField(scoredDoc, "content", "");
			}
			Document derived = Document
					.newBuilder()
					.setId(scoredDoc.getId())
					.addField(
							Field.newBuilder().setName("content")
									.setText(content))
					.addField(
							Field.newBuilder().setName("nickname")
									.setText(author.getNickname()))
					.addField(
							Field.newBuilder()
									.setName("published")
									.setDate(
											scoredDoc.getOnlyField(
													"published").getDate()))
					.build();
			found.add(derived);
		}
	}
	
	private static String getOnlyField(Document doc, String fieldName,
			String defaultValue) {
		if (doc.getFieldCount(fieldName) == 1) {
			return doc.getOnlyField(fieldName).getText();
		}
		LOG.severe("Field " + fieldName + " present "
				+ doc.getFieldCount(fieldName));
		return defaultValue;
	}
}
