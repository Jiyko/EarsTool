package com.radicalninja.logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.menny.android.anysoftkeyboard.R;
import com.radicalninja.logger.ui.MapsActivity;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

//import com.example.aishwarya.thirdapplication.R;
//import com.example.aishwarya.thirdapplication.viewactivity.TakeImageActivity;

public class VideoActivity extends AppCompatActivity {

	Long tsLong = System.currentTimeMillis();
	String timeStamp = tsLong.toString();
	//String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
    String directoryName = "/videoDIARY/";
	private PopupWindow popupWindow;
	String awsAccessKey = "Insert Key here";
	String awsSecretKey = "Insert Key here";
	String MY_BUCKET = "adaptvideos";
	String fileLocation = "something_goes_here";
	String OBJECT_KEY = "nameOfTheFile, once storeed on S3";
	public final String TAG = "Encrypt";

	private TransferUtility transferUtility;

	public static String UserID;
	public String theCurrentDate;

	private File testRoot;
	private final static int REQUEST_RESULT_IMAGE = 1;
	private final static int REQUEST_RESULT_VIDEO = 7;
	public Encryption encryption;
	public static boolean dialogShown = false;



	//public String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

	public static String getSecureId(Context context) {
		String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		return android_id;
	}

	public static String convertDate(String dateInMilliseconds, String dateFormat) {
		return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
	}

	public void setTheDate()
	{
		Calendar cal = Calendar.getInstance();
		long when = cal.getTimeInMillis();
		String timey = Long.toString(when);
		String theTime = convertDate(timey, "dd-MM-yyyy hh:mm:ss");
		theCurrentDate = theTime;
		System.out.println("The time changed into nice format is: " + theTime);
	}

	void showDialog() {



		MyDialogFragment myDialogFragment = new MyDialogFragment();
		myDialogFragment.show(getFragmentManager(), "");
//		mStackLevel++;
//
//		// DialogFragment.show() will take care of adding the fragment
//		// in a transaction.  We also want to remove any currently showing
//		// dialog, so make our own transaction and take care of that here.
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//		if (prev != null) {
//			ft.remove(prev);
//		}
//		ft.addToBackStack(null);
//
//		// Create and show the dialog.
//		DialogFragment newFragment = MyDialogFragment.newInstance(mStackLevel);
//		newFragment.show(ft, "dialog");
	}

	void showDialog2() {
		MyDialogFragmentThree myDialogFragment = new MyDialogFragmentThree();
		myDialogFragment.show(getFragmentManager(), "WARNING");



	}

	public void showDialog3(View v){
		MyDialogFragment myDialogFragment = new MyDialogFragment();
		myDialogFragment.show(getFragmentManager(),"INTO");
	}





//	public void credentialsProvider(){
//		CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//				getApplicationContext(),
//				us-east-1:dbacd6aa-9393-475e-b687-xxxxxxxxx&quot;
//		)
//	}

//	CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//			getApplicationContext(),
//			"us-west-2:41d228a6-292a-4ebb-9f37-cf96c33063a2", // Identity Pool ID
//			Regions.US_WEST_2 // Region
//	);
//
//	AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
//	TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

//	TransferObserver observer = transferUtility.upload(
//			MY_BUCKET,     /* The bucket to upload to */
//			OBJECT_KEY,    /* The key for the uploaded object */
//			MY_FILE        /* The file where the data to upload exists */
//	);

	private void beginUpload(String filePath) {
		if (filePath == null) {
			//Toast.makeText(this, "Could not find the filepath of the selected file",Toast.LENGTH_LONG).show();
			return;
		}
		String newFilePath = UserID +"/" +  theCurrentDate;
		//Toast.makeText(this, "The file is uploading, using the name: " + newFilePath,Toast.LENGTH_LONG).show();
		Log.d("uploading, using: " + newFilePath, "");
		File file = new File(filePath);
		TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, newFilePath,
				file);
        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
		// observer.setTransferListener(new UploadListener());
	}



	public class encryptAsyncTask extends AsyncTask<String, Void, String> {

		String idAndDate = UserID +"_" +  theCurrentDate;


		@Override
		protected String doInBackground(String... params) {
			String path = null;
			try {
				com.anysoftkeyboard.utils.Log.d(TAG,"We are starting encrytopn 1 - in doInBackgound AsyncTask ENCRYTPTION!");
				path = encryption.encrypt(idAndDate, params[0]);
				//Toast.makeText(VideoActivity.this, "Encrypting.",  Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				//Toast.makeText(VideoActivity.this, "Encrypting.1",  Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				//Toast.makeText(VideoActivity.this, "Encrypting.2",  Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				//Toast.makeText(VideoActivity.this, "Encrypting.3",  Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				//Toast.makeText(VideoActivity.this, "Encrypting.4",  Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			//Toast.makeText(VideoActivity.this, "File has been sucessfully uploaded! ", Toast.LENGTH_LONG).show();

			return path;
		}

//

		@Override
		protected void onPostExecute(String path){

			Toast.makeText(VideoActivity.this, "File has been sucessfully encrypted! ", Toast.LENGTH_LONG).show();

			new uploadAsyncTask().execute(path);
//			uploadAsynTask upload = new uploadAsynTask();
//			upload.execute()

		}
	}



	public class uploadAsyncTask extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) {
			com.anysoftkeyboard.utils.Log.d(TAG,"We are starting encrytopn 5 - In upload task" +
					" ");
			beginUpload(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			Toast.makeText(VideoActivity.this, "File has been sucessfully uploaded! ", Toast.LENGTH_LONG).show();
			com.anysoftkeyboard.utils.Log.d(TAG,"We are starting encrytopn 5 - Upload finishedk" +
					" ");
			showDialog2();
		}

		//		@Override
//		protected void onPostExecute(Void... params){
//			Toast.makeText(VideoActivity.this, "File has been sucessfully uploaded! ", Toast.LENGTH_LONG).show();
//
//
//		}


	}








	public boolean createDirectory(String path){
		File mydir = new File(Environment.getExternalStorageDirectory().toString() + path);
		if(!mydir.exists()){
			mydir.mkdirs();
		}
		else{
			Log.d("error", "dir already exisits");
		}
		return true;

	}

	private void initiatePopupWindow() {
		try {
		// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater)this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.screen_popup,
					(ViewGroup) findViewById(R.id.popup_element));
			popupWindow = new PopupWindow(layout, 300, 370, true);
			popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getActionBar().setTitle("Video Diary");
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("UO Video Diary App");
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		//getSupportActionBar().setHomeButtonEnabled(false);


		UserID = getSecureId(this);
		setContentView(R.layout.activity_video);
        //initiatePopupWindow();

		setTheDate();
		//showDialog();



//		if(!dialogShown)
//		{
//			Toast.makeText(this, "User ID = " + UserID, Toast.LENGTH_LONG).show();
//			showDialog2();
//			dialogShown = true;
//
//		}

		//showDialog2();
		encryption = new Encryption();



	}

//

	private static final int VIDEO_CAPTURE = 101;
	Uri videoUri;
	public void onRecordVideo(View v) {

		//showDialog2();


//		Intent i;
//
//		//isVideo=((CompoundButton)findViewById(R.id.is_video)).isChecked();
//
//
//		Toast.makeText(this, "NUMBER 15", Toast.LENGTH_LONG).show();
//		i=new VideoRecorderActivity.IntentBuilder(this)
//				.facing(Facing.FRONT)
//				.facingExactMatch()
//				.to(new File(testRoot, "portrait-front.mp4"))
//				.updateMediaStore()
//				.durationLimit(10000)
//				.debug()
//				//.flashModes(FLASH_MODES)
//				.build();
//
//		startActivityForResult(i, VIDEO_CAPTURE);
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
			intent.putExtra("android.intent.extra.quickCapture", true);
			createDirectory(directoryName);
			File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directoryName +  timeStamp + ".mp4");
			String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + directoryName +  timeStamp + ".mp4";
			Log.d("VideoActivity","This the the Video Uri in the on RECROD VIEW method using the newPath Variable: " + newPath);
			Log.d("VideoActivity","This the the Video Uri in the on RECROD VIEW method using the mediafile Variable: " + mediaFile);

			videoUri = Uri.fromFile(mediaFile);
			Log.d("VideoActivity","This the the Video Uri in the on RECROD VIEW method: " + videoUri);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
			//intent.putExtra(MediaStore.EXTRA_OUTPUT, newPath);
			//intent.putExtra("STRING_I_NEED", newPath);

			//intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
			startActivityForResult(intent, VIDEO_CAPTURE);
			//intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		} else {
			Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
		}
	}

	public void onRecordVideo2(View v)
	{
		//startActivity(new Intent(this, CameraActivity.class));

		Intent takeVideoActivity = new Intent(this, TakeVideoActivity.class);
		startActivityForResult(takeVideoActivity, REQUEST_RESULT_VIDEO);


//		getFragmentManager().beginTransaction()
//				.replace(R.id.LinearLayout1, Camera2VideoFragment.newInstance())
//				.commit();
	}

	public void openMaps(View v){
		Intent i = new Intent(getApplicationContext(), MapsActivity.class);
		startActivity(i);
	}



	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIDEO_CAPTURE) {
		  if (resultCode == RESULT_OK) {

			  //THIS IS THE ONE THAT WORKS ON ALL PHONES BUT SD CARD PHONES
			  String path = data.getData().getPath();

			  // END
			  //String newPath = data.getExtras().get("videoUri");


			  // THIS IS WHAT PUTS IT IN VIDEO DIRECTORY 3/10/16
			  //String path = videoUri.toString();



//			  if(data.getExtras().getString("STRING_I_NEED") == null)
//			  {
//				  Log.d("VideoActivity", "String is null");
//			  }

				//String string = data.getExtras().get("STRING_I NEED");
			  Log.d("VideoActivity", "The next attempt is: " + videoUri.toString());
			  Log.d("VideoActivity", "The old method was: " + data.getData().getPath());
			  //Log.d("VideoActivity", "The old method was: " + string);

			  //Log.d("VideoActivity", "This the the getting extra info from intent method: " + data.getExtras().get("STRING_PATH"));
			  //Log.d("VideoActivity", "This the the getting extra info from intent method 2 : " + data.getExtras().get("data"));


			  Log.d("VideoActivity", "The path returned by the intent data obbject was: " + path);
			  Log.d("VideoActivity", "The path returned by absolute path external storage was : " + Environment.getExternalStorageDirectory().getAbsolutePath());
			  Log.d("VideoActivity", "The path returned by  external storage was : " + Environment.getExternalStorageDirectory());
			  Uri path2 = data.getData();
				  //VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
			  //mVideoView.setVideoURI(videoUri);
			  Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();


			  //Toast.makeText(this, "Path used to find encrypted file is :\n" + path, Toast.LENGTH_LONG).show();




			  //TRY CANCEL PENDING INTENT HERE:

			  //AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//			  Intent intentReminder = new Intent(this, AlarmReceiver2.class);
//			  PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intentReminder, 0);
//			  alarmIntent.cancel();
//			  alarmMgr.cancel(alarmIntent);


			  // NEW TRY
			  //NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			  //notificationManager.cancel(1);

			  //Toast.makeText(this, "We have cancelled the repeating every 15 alarm!", Toast.LENGTH_LONG).show();

			  // END TRY CANCEL PENDING INTENT HERE!






			  //MainActivity.endRepeatingAlarm = true;



			  //mVideoView.setMediaController(new MediaController(this));
			  //mVideoView.requestFocus();
			  //mVideoView is the replay after video
			  //mVideoView.start();

			  // TURN OFF WHILE TESTING ENCRYPTION!!

			  transferUtility = Util.getTransferUtility(this);
			  new encryptAsyncTask().execute(path);
//			  try {
//				  encryption.encrypt(UserID, path);
//				  Toast.makeText(this, "Encrypting.",  Toast.LENGTH_LONG).show();
//			  } catch (IOException e) {
//				  Toast.makeText(this, "Encrypting.1",  Toast.LENGTH_LONG).show();
//				  e.printStackTrace();
//			  } catch (NoSuchAlgorithmException e) {
//				  Toast.makeText(this, "Encrypting.2",  Toast.LENGTH_LONG).show();
//				  e.printStackTrace();
//			  } catch (NoSuchPaddingException e) {
//				  Toast.makeText(this, "Encrypting.3",  Toast.LENGTH_LONG).show();
//				  e.printStackTrace();
//			  } catch (InvalidKeyException e) {
//				  Toast.makeText(this, "Encrypting.4",  Toast.LENGTH_LONG).show();
//				  e.printStackTrace();
//			  }
			  //beginUpload(path);
		  } else if (resultCode == RESULT_CANCELED) {
		    	Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_LONG).show();
		  } else {
		     Toast.makeText(this, "Failed to record video",  Toast.LENGTH_LONG).show();
	        }
	   }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		startService(new Intent(this, MainActivity.class));
	}

}
