package com.apple.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.company.demo.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class ShareUtils implements IWeiboHandler.Response {
	// public static final String APP_KEY = "1970249002";
	// //
	// public static final String REDIRECT_URL = "http://www.hupu.com/";
    public static final String APP_ID = "微信授权 key";
	public static final String APP_KEY = "微博授权 key";
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static IWeiboShareAPI mWeiboShareAPI = null;
	private Oauth2AccessToken mAccessToken;

	private static ShareUtils instance;
	private static Context context;
	private WeiboAuth mWeiboAuth;
	private ShareInterface mShareCalls;
	public static Activity act;

	public static final int Share_Text_Type = 1;
	public static final int Share_Webpage_Type = 2;

	public static ShareUtils getInstance(Activity activity) {
		if (instance == null)
			instance = new ShareUtils();
		act = activity;
		context = act.getApplicationContext();
		return instance;
	}

	public void shareMessage(String shareUrl, String title, String description,
			Drawable mDrawable, int type) {
		try {
			mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(act, APP_KEY);
			if (!mWeiboShareAPI.isWeiboAppInstalled()) {
				mWeiboShareAPI
						.registerWeiboDownloadListener(new IWeiboDownloadListener() {
							@Override
							public void onCancel() {
								Toast.makeText(
										act,
										act.getResources()
												.getString(
														R.string.weibosdk_demo_cancel_download_weibo),
										Toast.LENGTH_SHORT).show();
							}
						});
			}
			if (mWeiboShareAPI.checkEnvironment(true)) {
				mWeiboShareAPI.registerApp();
				if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
					int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
					if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
						if (type == this.Share_Text_Type) {
							shareMultiMessage(title, mDrawable);
						} else if (type == this.Share_Webpage_Type) {
							shareWebMessage(shareUrl, title, description,
									mDrawable);
						}
					} else {
						if (type == this.Share_Text_Type) {
							sendSingleMessage(title, mDrawable);
						} else if (type == this.Share_Webpage_Type) {
							shareWebMessage(shareUrl, title, description,
									mDrawable);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setShareCalls(ShareInterface mShareCalls) {
		this.mShareCalls = mShareCalls;
	}

	public void onAuthSina() {
		mAccessToken = AccessTokenKeeper.readAccessToken(context);
		mWeiboAuth = new WeiboAuth(act, APP_KEY, REDIRECT_URL, SCOPE);
		mWeiboAuth.anthorize(new WeiboAuthListener() {
			@Override
			public void onComplete(Bundle values) {
				mAccessToken = Oauth2AccessToken.parseAccessToken(values);
				if (mAccessToken.isSessionValid()) {
					mShareCalls.onAuthListener(mAccessToken);
					AccessTokenKeeper.writeAccessToken(context, mAccessToken);
				} else {
					mShareCalls.onAuthListener(null);
					String code = values.getString("code");
					String message = "";
					if (!TextUtils.isEmpty(code)) {
						message = message + "\nObtained the code: " + code;
					}
				}
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onWeiboException(WeiboException e) {
			}

		});
	}

	public void shareMultiMessage(String shareTxt, Drawable mDrawable) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (!shareTxt.equals("")) {
			weiboMessage.textObject = getTextObj(shareTxt);
		}
		if (mDrawable != null) {
			weiboMessage.imageObject = getImageObj(mDrawable);
		}
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(request);
	}

	
	public void shareWebMessage(String shareUrl, String title,
			String description, Drawable mDrawable) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (mDrawable == null)
			mDrawable = context.getResources().getDrawable(
					R.drawable.ic_launcher);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
		Bitmap shareBitmap = null;
		try {
			shareBitmap = bitmapDrawable.getBitmap();
			if (shareBitmap == null)
				shareBitmap = ((BitmapDrawable) context.getResources()
						.getDrawable(R.drawable.ic_launcher)).getBitmap();
			else
				shareBitmap = zoomImg(shareBitmap, 100);
			weiboMessage.mediaObject = getWebpageObj(shareUrl, title,
					description, shareBitmap);
			weiboMessage.textObject = getTextObj(title);
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			request.transaction = String.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;
			mWeiboShareAPI.sendRequest(request);
		} catch (Exception e) {

		} finally {
			if (shareBitmap != null) {
				// shareBitmap.recycle();
				shareBitmap = null;
			}
		}
	}

	
	private WebpageObject getWebpageObj(String shareUrl, String title,
			String description, Bitmap mDrawable) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = description;
		mediaObject.setThumbImage(mDrawable);
		mediaObject.actionUrl = shareUrl;
		mediaObject.schema = title;
		mediaObject.defaultText = title;
		return mediaObject;
	}

	private ImageObject getImageObj(Drawable mDrawable) {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}

	private TextObject getTextObj(String shareTxt) {
		TextObject textObject = new TextObject();
		textObject.text = shareTxt;
		return textObject;
	}

	private void sendSingleMessage(String shareTxt, Drawable mDrawable) {
		WeiboMessage weiboMessage = new WeiboMessage();
		if (!shareTxt.equals("")) {
			weiboMessage.mediaObject = getTextObj(shareTxt);
		}
		if (mDrawable != null) {
			weiboMessage.mediaObject = getImageObj(mDrawable);
		}
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;
		mWeiboShareAPI.sendRequest(request);
	}

	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				mShareCalls.onAuthListener(mAccessToken);
				AccessTokenKeeper.writeAccessToken(context, mAccessToken);
			} else {
				mShareCalls.onAuthListener(null);
				String code = values.getString("code");
				String message = "";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onWeiboException(WeiboException e) {

		}
	}

	public void onWeixinFriendWebPage(Context mcontext, String shareUrl,
			String title, String des, Drawable mDrawable) {
		shareToWeixin(mcontext, shareUrl, title, des, mDrawable,
				Share_Webpage_Type);
	}

	/**
	 * 
	 */
	public void onWeixinFriendText(Context mcontext, String title) {
		shareToWeixin(mcontext, "", title, "", null, Share_Text_Type);
	}

	
	private void shareToWeixin(Context mcontext, String shareUrl, String title,
			String des, Drawable mDrawable, int type) {
		Bitmap b = null;
		try {
			IWXAPI api = WXAPIFactory.createWXAPI(act, APP_ID);
			api.registerApp(APP_ID);
			if (!api.isWXAppInstalled()) {
				Toast.makeText(
						act,
						act.getResources()
								.getString(
										R.string.weixinsdk_demo_has_not_installed_weibo),
						Toast.LENGTH_SHORT).show();
				return;
			}
			WXMediaMessage msg = null;
			switch (type) {
			case Share_Text_Type:
				WXTextObject textObj = new WXTextObject();
				textObj.text = title;
				msg = new WXMediaMessage();
				msg.mediaObject = textObj;
				msg.description = title;
				break;
			case Share_Webpage_Type:
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = shareUrl;
				msg = new WXMediaMessage(webpage);
				msg.title = title;
				msg.description = des;
				break;
			}

			if (type == Share_Webpage_Type && mDrawable != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
				b = bitmapDrawable.getBitmap();
				b = zoomImg(b, 100);
				if (b == null) {
					bitmapDrawable = ((BitmapDrawable) context.getResources()
							.getDrawable(R.drawable.ic_launcher));
					b = bitmapDrawable.getBitmap();
				}
				msg.thumbData = bmpToByteArray(b, true);
			}
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			if (type == Share_Text_Type)
				req.transaction = buildTransaction("text"); 
			else if (type == Share_Webpage_Type) {
				req.transaction = buildTransaction("webpage");
				msg.title = title;
				msg.description = des;
			}
			req.message = msg;
			req.scene = SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mcontext, mcontext.getString(R.string.share_weixin),
					300).show();
		} finally {
			if (b != null) {
				b = null;
			}
		}
	}

	public void onWeixinFriendQuanWebPage(Context mcontext, String shareUrl,
			String title, String des, Drawable mDrawable) {
		shareToFriendQuan(mcontext, shareUrl, title, des, mDrawable,
				Share_Webpage_Type);
	}

	/**
	 * 
	 */
	public void onWeixinFriendQuanText(Context mcontext, String title) {
		shareToFriendQuan(mcontext, "", title, "", null, Share_Text_Type);
	}

	/**
	 * 分享朋友圈
	 * 
	 */
	private void shareToFriendQuan(Context mcontext, String shareUrl,
			String title, String des, Drawable mDrawable, int type) {
		Bitmap b = null;
		ByteArrayOutputStream output = null;
		try {
			IWXAPI api = WXAPIFactory.createWXAPI(act, APP_ID);
			api.registerApp(APP_ID);
			WXMediaMessage msg = null;
			if (!api.isWXAppInstalled()) {
				Toast.makeText(
						act,
						act.getResources()
								.getString(
										R.string.weixinsdk_demo_has_not_installed_weibo),
						Toast.LENGTH_SHORT).show();
				return;
			}
			switch (type) {
			case Share_Text_Type:
				WXTextObject textObj = new WXTextObject();
				textObj.text = title;
				msg = new WXMediaMessage();
				msg.mediaObject = textObj;
				msg.description = title;
				break;
			case Share_Webpage_Type:
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = shareUrl;
				msg = new WXMediaMessage(webpage);
				msg.title = title;
				msg.description = des;
				break;
			}
			if (type == Share_Webpage_Type && mDrawable != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
				b = bitmapDrawable.getBitmap();
				if (b == null) {
					bitmapDrawable = ((BitmapDrawable) context.getResources()
							.getDrawable(R.drawable.ic_launcher));
					b = bitmapDrawable.getBitmap();
				}
				output = new ByteArrayOutputStream();
				b = zoomImg(b, 100);
				b.compress(CompressFormat.PNG, 70, output);
				byte[] result = output.toByteArray();
				msg.thumbData = result;
			}
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			if (type == Share_Text_Type)
				req.transaction = buildTransaction("text"); 
			else if (type == Share_Webpage_Type)
				req.transaction = buildTransaction("webpage"); 
			req.message = msg;
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
			api.sendReq(req);
		} catch (Exception e) {
			Toast.makeText(mcontext, mcontext.getString(R.string.share_weixin),
					300).show();
		} finally {
			if (b != null) {
				b = null;
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(mAccessToken.getExpiresTime()));
		String format = context
				.getString(R.string.weibosdk_demo_token_to_string_format_1);
		String message = String.format(format, mAccessToken.getToken(), date);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(act,
					act.getString(R.string.weibosdk_demo_toast_share_success),
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(act,
					act.getString(R.string.weibosdk_demo_toast_share_canceled),
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(
					act,
					act.getString(R.string.weibosdk_demo_toast_share_failed)
							+ "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap zoomImg(Bitmap bm, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

}
