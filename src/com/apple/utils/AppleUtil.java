package com.apple.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.ClipboardManager;
import android.util.Log;

import com.apple.share.ShareUtils;

/**
 * ������ǹ������� ���ڹ���ʹ��
 * 
 * @author apple_hsp
 * 
 */
public class AppleUtil {

	public static AppleUtil instance;

	public AppleUtil() {
		// TODO Auto-generated constructor stub
	}

	public static AppleUtil getInstance() {
		if (instance == null)
			instance = new AppleUtil();
		return instance;
	}

	/**
	 * context ������
	 * 
	 * @param context
	 * @param content
	 */
	public static void onCopy(Context context, String content) {
		// �õ�����������
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * �õ�����ͼƬ����
	 * 
	 * @param imgUrl
	 *            �����ַ
	 */
	private String onGetImageType(String imgUrl) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bmp = null;
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			// ��ȡ���������ػ�������
			opts.inSampleSize = 10;
			// ����Ϊfalse��ȷ�����Եõ�bitmap != null
			opts.inJustDecodeBounds = false;
			InputStream is = conn.getInputStream();
			bmp = BitmapFactory.decodeStream(is, null, opts);
			return opts.outMimeType;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bmp != null && !bmp.isRecycled()) {
				// bmp.recycle() //����ͼƬ��ռ���ڴ�
				// system.gc() //����ϵͳ��ʱ����
				bmp = null;
			}
		}
		return "";
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public void onGetImagType(final String url) {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("HU", "====doInBackground"+onGetImageType(url));
			}
		}.start();
	}

	public class CheckImgTask extends AsyncTask {
		public String url;
		public String type;

		public CheckImgTask(String imgUrl) {
			url = imgUrl;
		}

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			type = onGetImageType(url);
			Log.i("HU", "====doInBackground"+type);
			return type;
		}

	}

	/**
	 * ʵ��ճ����
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// �õ�����������
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

}
