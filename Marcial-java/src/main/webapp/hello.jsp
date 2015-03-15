<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.utils.SystemProperty" %>

<%--
  ~ Copyright (c) 2013 Google Inc. All Rights Reserved.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License"); you
  ~ may not use this file except in compliance with the License. You may
  ~ obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
  ~ implied. See the License for the specific language governing
  ~ permissions and limitations under the License.
  --%>

<%
  UserService userService = UserServiceFactory.getUserService();
  User user = userService.getCurrentUser();
%>

<!DOCTYPE html>

<html>

<head>
  <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
</head>

<body>

  <h2>Hello!</h2>

  <p>Hello, <% 
  			System.out.println(SystemProperty.version.get());
			System.out.println(SystemProperty.applicationVersion.get());
			System.out.println(System.getProperties());
			
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable ex) {
					System.out.println("You crashed thread " + t.getName());
					System.out.println("Exception was: " + ex.toString());
				}
			});
  
   %>.
     
  </p>

</body>
</html>