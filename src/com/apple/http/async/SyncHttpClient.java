package com.apple.http.async;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Message;

public abstract class SyncHttpClient extends AsyncHttpClient {
	private int responseCode;
	/*
	 * as this is a synchronous request this is just a helping mechanism to pass
	 * the result back to this method. Therefore the result object has to be a
	 * field to be accessible
	 */
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

        @Override
        void sendResponseMessage(org.apache.http.HttpResponse response,int reqType) {
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response,reqType);
		};

		@Override
		protected void sendMessage(Message msg) {
			/*
			 * Dont use the handler and send it directly to the analysis
			 * (because its all the same thread)
			 */
			handleMessage(msg);
		}

		@Override
		public void onSuccess(HttpEntity content,int reqType) {
			try {
				result= EntityUtils.toString(content, "UTF-8");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content,int reqType) {
			result = onRequestFailed(error, content);
		}
	};

	/**
	 * @return the response code for the last request, might be usefull
	 *         sometimes
	 */
	public int getResponseCode() {
		return responseCode;
	}

	// Private stuff
    @Override
    protected void sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, AsyncHttpResponseHandler responseHandler,
			Context context,int reqType) {
		if (contentType != null) {
			uriRequest.addHeader("Content-Type", contentType);
		}
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler)
				.run();
	}

	public abstract String onRequestFailed(Throwable error, String content);

	public void delete(String url, RequestParams queryParams,
			AsyncHttpResponseHandler responseHandler) {
		// TODO what about query params??
		delete(url, responseHandler);
	}

	public String get(String url, RequestParams params) {
		this.get(url, params, responseHandler,0);
		/*
		 * the response handler will have set the result when this line is
		 * reached
		 */
		return result;
	}

	public String get(String url) {
		this.get(url, null, responseHandler, 0);
		return result;
	}

	public String put(String url, RequestParams params) {
		this.put(url, params, responseHandler);
		return result;
	}

	public String put(String url) {
		this.put(url, null, responseHandler);
		return result;
	}

	public String post(String url, RequestParams params) {
		this.post(url, params, responseHandler, 0);
		return result;
	}

	public String post(String url) {
		this.post(url, null, responseHandler,0);
		return result;
	}

	public String delete(String url, RequestParams params) {
		this.delete(url, params, responseHandler);
		return result;
	}

	public String delete(String url) {
		this.delete(url, null, responseHandler);
		return result;
	}

}
