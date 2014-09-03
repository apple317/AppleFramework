package com.apple.http.async;




import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}. The {@link #onSuccess(String)} method is designed to
 * be anonymously overridden with your own response handling code.
 * <p>
 * Additionally, you can override the {@link #onFailure(Throwable, String)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onStart() {
 * 		// Initiated the request
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(String response) {
 * 		// Successfully got a response
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(Throwable e, String response) {
 * 		// Response failed :(
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFinish() {
 * 		// Completed the request (either success or failure)
 * 	}
 * });
 * </pre>
 */
public class AsyncHttpResponseHandler {
	protected static final int SUCCESS_MESSAGE = 0;
	protected static final int FAILURE_MESSAGE = 1;
	protected static final int START_MESSAGE = 2;
	protected static final int FINISH_MESSAGE = 3;

	private Handler handler;

	/**
	 * Creates a new AsyncHttpResponseHandler
	 */
	public AsyncHttpResponseHandler() {
		// Set up a handler to post events back to the correct thread if
		// possible
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					AsyncHttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}

	//
	// Callbacks to be overridden, typically anonymously
	//

	/**
	 * Fired when the request is started, override to handle in your own code
	 */
	public void onStart() {
	}

	/**
	 * Fired in all cases when the request is finished, after both success and
	 * failure, override to handle in your own code
	 */
	public void onFinish() {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(HttpEntity content, int reqType) {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param headers
	 *            the headers of the HTTP response
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int statusCode, Header[] headers, HttpEntity content, int reqType) {
		onSuccess(statusCode, content,reqType);
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int statusCode, HttpEntity content, int reqType) {
		onSuccess(content,reqType);
	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @deprecated use {@link #onFailure(Throwable, String)}
	 */
	@Deprecated
	public void onFailure(Throwable error) {
	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @param content
	 *            the response body, if any
	 */
	public void onFailure(Throwable error, String content,int reqType) {
		// By default, call the deprecated onFailure(Throwable) for
		// compatibility
		onFailure(error);
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

	protected void sendSuccessMessage(int statusCode, Header[] headers,
			HttpEntity responseBody,int reqType) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] {
				new Integer(statusCode), headers, responseBody , reqType}));
	}

	protected void sendFailureMessage(Throwable e, String responseBody,int reqType) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody , reqType}));
	}

	protected void sendFailureMessage(Throwable e, byte[] responseBody,int reqType) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody , reqType}));
	}

	protected void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	protected void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}

	//
	// Pre-processing of messages (in original calling thread, typically the UI
	// thread)
	//

	protected void handleSuccessMessage(int statusCode, Header[] headers,
			HttpEntity responseBody, int reqType) {
		onSuccess(statusCode, headers, responseBody,reqType);
	}

	protected void handleFailureMessage(Throwable e, String responseBody ,int reqType) {
		onFailure(e, responseBody,reqType);
	}

	// Methods which emulate android's Handler and Message methods
	protected void handleMessage(Message msg) {
		Object[] response;

		switch (msg.what) {
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(Header[]) response[1], (HttpEntity) response[2],(Integer)response[3]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0], (String) response[1],(Integer)response[2]);
			break;
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;
		}
	}

	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}

		return msg;
	}

	// Interface to AsyncHttpRequest
	void sendResponseMessage(HttpResponse response,int reqType) {
		StatusLine status = response.getStatusLine();		
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
			}
			if (status.getStatusCode() != 200) {
				String responseBody = EntityUtils.toString(entity, "UTF-8");
				sendFailureMessage(
						new HttpResponseException(status.getStatusCode(),
								status.getReasonPhrase()), responseBody,reqType);
			} else {
//				byte[] result = EntityUtils.toByteArray(response.getEntity());
				sendSuccessMessage(status.getStatusCode(),
						response.getAllHeaders(), entity, reqType);
			}
		} catch (IOException e) {
			sendFailureMessage(e, (String) null, reqType);
		}
	}
}
