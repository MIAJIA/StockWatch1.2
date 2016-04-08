package com.logicmonitor.msp.YahooFetcher;

/**
 * RuntimeException implementation class to catch YahooFechException
 */

public class YahooFetchException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	  public YahooFetchException()  {
	      // TODO Auto-generated constructor stub
	  }

	  public YahooFetchException(String message) {
	      super(message);
	      // TODO Auto-generated constructor stub
	  }

	  public YahooFetchException(Throwable cause) {
	      super(cause);
	      // TODO Auto-generated constructor stub
	  }

	  public YahooFetchException(String message, Throwable cause) {
	      super(message, cause);
	      // TODO Auto-generated constructor stub
	  }
}
