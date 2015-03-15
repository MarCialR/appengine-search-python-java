package com.mru.cloud;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.PrintWriter;


//import javax.servlet.RequestDispatcher;

import com.google.appengine.api.utils.SystemProperty;


@SuppressWarnings("serial")
public class InternalsServlet extends HttpServlet {

		private final static String WHAT_Parameter = "WHAT";
		private final static String WHAT_BREAK = "break";
		private final static String WHAT_INFO = "info";
		
		private final static Logger log = Logger.getLogger("logger");
		

		public void doGet(HttpServletRequest request, HttpServletResponse resp)
				throws ServletException, IOException {

	        PrintWriter out= resp.getWriter();
	        String whattodo = request.getParameter(WHAT_Parameter);
	        
	        debug( WHAT_Parameter + "="+ whattodo, out);
	        
	        if(null== whattodo){
	            debug("you are missing WHAT parameter", out);
	            //RequestDispatcher rd = getServletContext().getRequestDispatcher("/");
	            //rd.include(request, resp);
	        }	        	
	        	
        	switch (whattodo){
	        	
        		case WHAT_BREAK:
	        		setDefaultHandler(request, resp);
	        		break;
	        	
        		case WHAT_INFO:
		        	info(out);
	        		break;
	        	
        		default:
	        		debug("your WHAT parameter does not match any", out);
	        		break;
        	}

		}
		
		
		public void setDefaultHandler(HttpServletRequest request, HttpServletResponse resp)
				throws ServletException, IOException {
	        PrintWriter out= resp.getWriter();			

	        debug(SystemProperty.version.get(), out);
	        
			System.out.println(SystemProperty.applicationVersion.get());
			System.out.println(System.getProperties());
			
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable ex) {
					System.out.println("You crashed thread " + t.getName());
					System.out.println("Exception was: " + ex.toString());
				}
			});
			debug("THIS IS YOUR VM .. AND YOU CAN BREAK IT HOWEVER YOU PLEASE!!", out);
		}

		private static void info(PrintWriter out){
			debug("SystemProperty.version.get() -> " + SystemProperty.version.get(), out);
			debug("SystemProperty.applicationVersion.get() -> " + SystemProperty.applicationVersion.get(), out);
	        for(Entry<Object, Object> e : System.getProperties().entrySet()) {
	        	debug(e, out);
	        }			
		}

		private static void debug(Object message, PrintWriter out){
			System.out.println(message);
			out.println(message);
		}

	}


