package com.google.appengine.demos.search;

/*
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Date;
	import java.util.List;
	import java.util.logging.Logger;

	import javax.servlet.ServletException;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import org.json.JSONObject;

	import com.google.android.chrometophone.server.Content2;
	import com.google.android.chrometophone.server.RegisterServlet;
	import com.google.android.chrometophone.server.Util;
	import com.google.android.chrometophone.server.Whitelist;
	import com.google.android.chrometophone.server.WhitelistUtil;
	import com.google.android.chrometophone.server.analytics.CustomTracker;
	import com.google.android.chrometophone.server.memcache.StripeBillingInfoCache;
	import com.google.appengine.api.users.User;
	import com.google.gson.Gson;
	import com.google.gson.JsonElement;

	@SuppressWarnings("serial")
	public class UserSearchServlet  extends HttpServlet {

		private final static Logger LOGGER = Logger.getLogger(SearchServlet.class.getName());

		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
		{
			executeHTTPRequests(request, response);
		}

		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			executeHTTPRequests(request, response);
		}

		public void executeHTTPRequests(HttpServletRequest request, HttpServletResponse response) throws IOException 
		{
			Util.setCrossDomainHeader(request, response);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			String function = request.getParameter("function");

			User user = RegisterServlet.checkUser(request, response, false);
			if (user != null)
			{
				//String email = Util.getUserEmail(user);
				String email = user.getEmail();

				if (function != null && function.equalsIgnoreCase("searchMessages")) 
				{
					String queryString = request.getParameter("q");
					
					// get the documents
					//List<Content2> msgs = SearchUtil.searchDocuments(email, queryString);
					//String jsonMessages = "{\"messages\":" + new Gson().toJson(msgs) + "}";
					//response.getWriter().write(jsonMessages);
					
					LOGGER.severe("searchMessages - 1, servlet called");

					List<SearchResults> searchResults = SearchUtil.searchDocuments(email, queryString);
					response.getWriter().write(new Gson().toJson(searchResults));
					
					LOGGER.severe("searchMessages - 5, after rendering to JSON");
				}
				else 
				{
					Util.functionInvalid(request, response);
				}
			}
			else 
			{
				Util.userNotLoggedIn(request, response);
			}
		}
	}*/