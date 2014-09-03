package com.apple.share;



import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public interface ShareInterface {
	public void onAuthListener(Oauth2AccessToken token);
	
}
