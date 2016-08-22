package com.logicmonitor.msp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.dao.JedisDao;
import com.logicmonitor.msp.dao.impl.JedisDaoImpl;

/**
 * Servlet Filter implementation class htmlFilter
 */
public class htmlFilter implements Filter {

    /**
     * Default constructor. 
     */
    public htmlFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	      HttpServletRequest request = (HttpServletRequest) servletRequest;
	      HttpServletResponse response = (HttpServletResponse) servletResponse;
	      String service = request.getParameter("service");
//	      String ticket = request.getParameter("ticket");
	      Cookie[] cookies = request.getCookies();
	      if(service != null)
	          System.out.println(request.getRemoteHost() + " " + request.getRequestURL().toString()+ " " + service);
	      else
	          System.out.println(request.getRemoteHost() + " " + request.getRequestURL().toString() + " " + "service is null");
	      String username = "";
	      if(null != cookies) {
	          for(Cookie cookie : cookies) {
	              if("sso".equals(cookie.getName())) {
	                  username = cookie.getValue();
	                  break;
	              }
	          }
	      }
	      
//	      if(null == service && null != ticket) {
//	          filterChain.doFilter(request, response);
//	          return ;
//	      }
	      
	      if(null != username && !"".equals(username)) {
	          long time = System.currentTimeMillis();
	          String timeString = username + time;
	          JedisDao dao = new JedisDaoImpl();
	          dao.set(timeString, username);
	          StringBuilder url = new StringBuilder();
	          url.append("/redirect.htm?service=");
	          if(0 <= service.indexOf("?")) {
	              url.append(service.replace('?', '&'));
	          }
	          else {
	              url.append(service);
	          }
	          url.append("&ticket=").append(timeString);
	          System.out.println("from filter: " + url.toString());
	          response.sendRedirect(url.toString());
	      }
	      else {
	          filterChain.doFilter(request, response);
	      }
	    }
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
