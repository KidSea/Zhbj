package com.example.zhbj52;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * ��������ҳ��
 * 
 * @author yuxuehai
 * 
 */
public class NewsDetailActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.btn_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.wv_news_detail)
	private WebView mWebView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;

	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ����û�б���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		ViewUtils.inject(this);

		llControl.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);

		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		mUrl = getIntent().getStringExtra("url");

		mWebView.loadUrl(mUrl);

		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);// ��ʾ���Ű�ť
		settings.setUseWideViewPort(true);// ֧��˫������
		settings.setJavaScriptEnabled(true);// ֧��JS

		mWebView.setWebViewClient(new WebViewClient() {
			// ��ʼ������ҳ
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("��ʼ������ҳ��");
				pbLoading.setVisibility(View.VISIBLE);
			}

			// ��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("��ҳ���ؽ���");
				pbLoading.setVisibility(View.INVISIBLE);
			}

			// ����������ת���ߴ˷���
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("��ת����:" + url);
				view.loadUrl(url);// ����ת����ʱǿ���ڵ�ǰwebview�м���
				return true;
			}
		});

		// mWebView.goBack();�ϸ�ҳ��
		// mWebView.goForward();�¸�ҳ��
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				// ���ȷ����仯
				System.out.println("����:" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				// ��ҳ����
				System.out.println("��ҳ����:" + title);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_share:
			showShare();
			break;
		case R.id.btn_textsize:
			//�޸����ִ�Сѡ��ķ���
			showChooseDialog();
			break;
		default:
			break;

		}
	}
	private int mTempWhich;//��¼�����������С
	
	private int mCurrenWhich = 2;//��¼��ǰ�����С
    /**
     * չʾ�����С�ĵ���
     */
	private void showChooseDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��������");
		
		String[] items = new String[]{
				"���������","�������",
				"��������","С������","��С������"
		};
		
		builder.setSingleChoiceItems(items, mCurrenWhich, new DialogInterface.OnClickListener() {
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				WebSettings settings = mWebView.getSettings();
				
				//����ѡ��������޸�����
				switch (mTempWhich){
				case 0:
					//��������
					settings.setTextSize(TextSize.LARGEST);
					//settings.setTextZoom(28);
					break;
				case 1:
					//�������
					settings.setTextSize(TextSize.LARGER);
					//settings.setTextZoom(20);
					break;
				case 2:
					//��������
					settings.setTextSize(TextSize.NORMAL);
					//settings.setTextZoom(14);
					break;
				case 3:
					//С����
					settings.setTextSize(TextSize.SMALLER);
					//settings.setTextZoom(10);
					break;
				case 4:
					//��С����
					settings.setTextSize(TextSize.SMALLEST);
					//settings.setTextZoom(5);
					break;
				default:
					break;
				}
				mCurrenWhich = mTempWhich;
			}
			
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
	// ȷ��SDcard������ڴ���ͼƬtest.jpg
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();

		oks.setTheme(OnekeyShareTheme.SKYBLUE);// �޸�������ʽ

		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		//oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}
}
