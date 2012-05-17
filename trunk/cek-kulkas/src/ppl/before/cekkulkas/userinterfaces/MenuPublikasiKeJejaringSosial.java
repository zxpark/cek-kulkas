package ppl.before.cekkulkas.userinterfaces;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ppl.before.cekkulkas.R;
import ppl.before.cekkulkas.models.Bahan;
import ppl.before.cekkulkas.models.Resep;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MenuPublikasiKeJejaringSosial extends Activity{

	private Resep resep;
	
	private SharedPreferences sharedPref;
	
    /** Name to store the users access token */
	private String prefAccessTokenTwitter = "accessToken";
    /** Name to store the users access token secret */
    private static final String prefAccessTokenSecretTwitter = "accessTokenSecret";
	
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
    private static final String CONSUMER_KEY_TWITTER = "pxrH07GZxEyZ02Na269MZA";
    /** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
    private static final String CONSUMER_SECRET_TWITTER = "ITY5gC9DPDwOiPs9qkltHJPPAYZqJfz2KnvCMWHNY"; // XXX Encode in your app
    /** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
    private static final String CALLBACK_URL = "cekkulkas:///";
    
    private final String TWITPIC_API_KEY = "80407a1e15f66487c3bab442a70b25c1";
    
    private Configuration twitterConf;
    
    
    // FACEBOOK
    private static final String[] PERMISSIONS = new String[] {"publish_stream"};
    private static final String APP_ID = "127129437422417";
	private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private String messageToPost;
    

    /** Twitter4j object */
    private Twitter twitter;
    /** The request token signifies the unique ID of the request you are sending to twitter  */
    private RequestToken reqToken;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// title bar aplikasi
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.share);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		// mengambil objek resep yang akan ditampilkan detailnya dari extra
		resep = (Resep)getIntent().getSerializableExtra("resep");
		
		((TextView)findViewById(R.id.namaresepshare)).setText(resep.getNama());
        ((EditText)findViewById(R.id.komentarresep)).setText("Saya baru saja memasak "+resep.getNama());
		
		sharedPref = getSharedPreferences("twitterPref", MODE_PRIVATE);
		
		if(sharedPref.contains(prefAccessTokenTwitter)){
			((Button)findViewById(R.id.logintwitter)).setVisibility(View.GONE);
		} else {
			((Button)findViewById(R.id.checkboxtwitter)).setVisibility(View.GONE);
		}
		
		twitterConf = new ConfigurationBuilder()
	    .setMediaProviderAPIKey( TWITPIC_API_KEY )
	    .setOAuthConsumerKey( CONSUMER_KEY_TWITTER )
	    .setOAuthConsumerSecret( CONSUMER_SECRET_TWITTER )
	    .build();
		
		twitter = new TwitterFactory(twitterConf).getInstance();
		
		
		facebook = new Facebook(APP_ID);
		
		if(restoreCredentials(facebook)){
			((Button)findViewById(R.id.loginfacebook)).setVisibility(View.GONE);
		} else {
			((Button)findViewById(R.id.checkboxfacebook)).setVisibility(View.GONE);
		}
	}
	
	public void loginTwitter(View v){
		try{
			reqToken = twitter.getOAuthRequestToken(CALLBACK_URL);
			WebView twitterSite = new WebView(MenuPublikasiKeJejaringSosial.this);
			twitterSite.enablePlatformNotifications();
			twitterSite.loadUrl(reqToken.getAuthenticationURL());
			twitterSite.requestFocus(View.FOCUS_DOWN);
            twitterSite.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            if (!v.hasFocus()) {
                                v.requestFocus();
                            }
                            break;
                    }
                    return false;
                }
            });
            setContentView(twitterSite);
		} catch (TwitterException e){
			Toast.makeText(getApplicationContext(), "login twitter gagal", Toast.LENGTH_SHORT).show();
		}
	}

	public void loginFb(View v){
		facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		Uri uri = intent.getData();
		if(uri!=null && uri.toString().startsWith(CALLBACK_URL)){
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");
			
			try{
				AccessToken at = twitter.getOAuthAccessToken(reqToken, oauthVerifier);
				twitter.setOAuthAccessToken(at);
				
				String token = at.getToken();
                String secret = at.getTokenSecret();
                Editor editor = sharedPref.edit();
                editor.putString(prefAccessTokenTwitter, token);
                editor.putString(prefAccessTokenSecretTwitter, secret);
                editor.commit();
                
                setContentView(R.layout.share);
                
                ((TextView)findViewById(R.id.namaresepshare)).setText(resep.getNama());
                ((EditText)findViewById(R.id.komentarresep)).setText("Saya baru saja memasak "+resep.getNama());
                
                ((Button)findViewById(R.id.logintwitter)).setVisibility(View.GONE);
                ((CheckBox)findViewById(R.id.checkboxtwitter)).setVisibility(View.VISIBLE);
			} catch (TwitterException e){
				Toast.makeText(this, "autentikasi twitter error", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void share(View v){

		CheckBox checkBoxTwitter = (CheckBox)findViewById(R.id.checkboxtwitter);
		CheckBox checkBoxFb = (CheckBox)findViewById(R.id.checkboxfacebook);
		
		File f = new File("/data/data/ppl.before.cekkulkas/foto.jpg");
		try{
		    String foto = resep.getFoto();
			if(foto == null || foto.equals("")){
				foto = "r0";
			}
		    
		    InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(foto, "drawable", getPackageName()));
		    OutputStream out=new FileOutputStream(f);
		    byte buf[]=new byte[1024];
		    int len;
		    while((len=inputStream.read(buf))>0){
		    	out.write(buf,0,len);
		    }
		    out.flush();
		    out.close();
		    inputStream.close();
	    }
	    catch (IOException e){
	    	Log.i("asdf",e.getMessage());
	    }
		
		if(checkBoxTwitter.getVisibility() == View.VISIBLE && checkBoxTwitter.isChecked()){
			String token = sharedPref.getString(prefAccessTokenTwitter, null);
			String secret = sharedPref.getString(prefAccessTokenSecretTwitter,null);
			
			AccessToken at = new AccessToken(token, secret);
			
			twitter.setOAuthAccessToken(at);
			
			try{
				
				StatusUpdate status = new StatusUpdate(((EditText)findViewById(R.id.komentarresep)).getText()+" via @cekkulkas");
				status.setMedia(f);
				
				twitter.updateStatus(status);
				Toast.makeText(this, "resep berhasil dibagikan", Toast.LENGTH_SHORT).show();
			} catch (TwitterException e){
				Log.i("gagal tweet",e.getMessage());
				Toast.makeText(this, "gagal tweet ke twitter", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Tidak ada yang dishare ke twitter", Toast.LENGTH_SHORT).show();
		}
		
		if(checkBoxFb.getVisibility() == View.VISIBLE && checkBoxFb.isChecked() == true){
			Bundle parameters = new Bundle();
			
			String bahan = "";
			List<Bahan> listBahan = resep.getListBahan();
			for(int i=0; i<listBahan.size(); i++){
				Bahan temp = listBahan.get(i);
				bahan += (i+1)+". "+temp.getJumlah()+" "+temp.getSatuan()+" "+temp.getNama()+"\n"; 
			}
			
			String deskripsi=resep.getDeskripsi()+"\n\n\n"+bahan+"\n\n\n"+resep.getLangkah();
						
	        parameters.putString("message", ((EditText)findViewById(R.id.komentarresep)).getText()+"");
//	        parameters.putString("name", resep.getNama());
//	        parameters.putString("caption", resep.getKategori());
//	        parameters.putString("description", deskripsi);
//	        parameters.putString("method", "photos.upload");
//	        parameters.putString("picture", "https://si0.twimg.com/profile_images/2226016013/ic_launcher_cekkulkas_reasonably_small.png");
	        try {
		        facebook.request("me");
				String response = facebook.request("me/feed", parameters, "POST");
				Log.d("Tests", "got response: " + response);
				if (response == null || response.equals("") ||
				        response.equals("false")) {
					showToast("Tidak ada respon");
				}
				else {
					showToast("resep berhasil dibagikan");
				}
			} catch (Exception e) {
				showToast("Failed to post to wall!");
				e.printStackTrace();
				finish();
			}
		} else {
			Toast.makeText(this, "Tidak ada yang dishare ke facebook", Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean saveCredentials(Facebook facebook) {
    	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
    	editor.putString(TOKEN, facebook.getAccessToken());
    	editor.putLong(EXPIRES, facebook.getAccessExpires());
    	return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
    	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
    	facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
    	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
    	return facebook.isSessionValid();
	}
	
	class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	saveCredentials(facebook);
	    	
	    	((Button)findViewById(R.id.loginfacebook)).setVisibility(View.GONE);
            ((CheckBox)findViewById(R.id.checkboxfacebook)).setVisibility(View.VISIBLE);
	    }
	    public void onFacebookError(FacebookError error) {
	    	showToast("autentikasi facebook gagal!");
	    }
	    public void onError(DialogError error) {
	    	showToast("autentikasi facebook gagal!");
	        finish();
	    }
	    public void onCancel() {
	    	showToast("autentikasi facebook gagal!");
	    }
	}


	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
