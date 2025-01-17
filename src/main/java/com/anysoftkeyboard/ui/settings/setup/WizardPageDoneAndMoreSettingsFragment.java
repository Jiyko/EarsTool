package com.anysoftkeyboard.ui.settings.setup;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anysoftkeyboard.ui.settings.KeyboardAddOnBrowserFragment;
import com.anysoftkeyboard.ui.settings.KeyboardThemeSelectorFragment;
import com.anysoftkeyboard.ui.settings.MainSettingsActivity;
import com.radicalninja.logger.MainActivity;
import com.sevencupsoftea.ears.R;

import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

import static android.content.Context.MODE_PRIVATE;
import static com.anysoftkeyboard.ui.settings.setup.FaceDetect.TICK;

//import com.menny.android.anysoftkeyboard.R;

public class WizardPageDoneAndMoreSettingsFragment extends WizardPageBaseFragment implements View.OnClickListener {

    private static final String TAG = "WizardPageDoneAndMoreSe";
    ImageView imageView3;
    ImageView imageView1;
    ImageView imageView2;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.keyboard_setup_wizard_page_additional_settings_layout, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.notificationSettings).setOnClickListener(this);
        view.findViewById(R.id.AppUsageSettings).setOnClickListener(this);
        view.findViewById(R.id.go_to_home_fragment_action).setOnClickListener(this);
        view.findViewById(R.id.go_to_languages_action).setOnClickListener(this);
        view.findViewById(R.id.go_to_theme_action).setOnClickListener(this);
        view.findViewById(R.id.go_to_all_settings_action).setOnClickListener(this);
        view.findViewById(R.id.TakeReferencePic).setOnClickListener(this);



        imageView1 = (ImageView)view.findViewById(R.id.imageView1);
        if(isAccessGranted()){
            Log.d(TAG, "onViewCreated: access granted");
            imageView1.setImageResource(R.drawable.green_tick);
        }

        imageView2 = (ImageView)view.findViewById(R.id.imageView2);
        if(checkNotificationEnabled()){
            Log.d(TAG, "onViewCreated: notifications");
            imageView2.setImageResource(R.drawable.green_tick);
        }

        imageView3 = (ImageView)view.findViewById(R.id.imageView3);

    }

    @Override
    protected boolean isStepCompleted(@NonNull Context context) {
        return false;//this step is never done! You can always configure more :)

    }

    @Override
    protected boolean isStepPreConditionDone(@NonNull Context context) {
        return SetupSupport.isThisKeyboardSetAsDefaultIME(context);
    }

    @Override
    public void onClick(View v) {
        MainSettingsActivity activity = (MainSettingsActivity) getActivity();
        Log.d(TAG, "the activity is: " + getActivity());
        switch (v.getId()) {
//            case R.id.show_keyboard_view_action:
//                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (inputMethodManager != null) {
//                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                }
//                break;
//            case R.id.go_to_home_fragment_action:
//                activity.onNavigateToRootClicked(v);
//                break;

            case R.id.go_to_home_fragment_action:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.go_to_languages_action:
                activity.addFragmentToUi(new KeyboardAddOnBrowserFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
                break;
            case R.id.go_to_theme_action:
                activity.addFragmentToUi(new KeyboardThemeSelectorFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
                break;
            case R.id.go_to_all_settings_action:
                activity.onNavigateToRootClicked(v);
                activity.openDrawer();
                break;

            case R.id.notificationSettings:
                Log.d(TAG, "onClick: 2");


                showDialog3(v);
//                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                //intent.putExtra("finishActivityOnSaveCompleted", true);
//                // intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings$SecuritySettingsActivity"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent,0);
                break;

            case R.id.AppUsageSettings:
                Log.d(TAG, "onClick: 1");

                //18th Jan 18 attempt at fix back button from stats app

                final Handler handler = new Handler();

                Runnable checkOverlaySetting = new Runnable() {

                    @Override
                    //@TargetApi(23)
                    public void run() {
                        Log.d(TAG, "run: 1");
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            Log.d(TAG, "run: 2");
                            return;
                        }

                        // 18th Jan 2018, below works, trying to stop using the intent ( ie try back button below).
                        if (((MainSettingsActivity) getActivity()).isAccessGranted2()) {
                            Log.d(TAG, "run: 3");
                            //You have the permission, re-launch MainActivity
                            //Intent i = new Intent(getActivity(), MainSettingsActivity.class);


                            mBaseContext.startActivity(mReLaunchTaskIntent);
                            mReLaunchTaskIntent = null;

                            Log.d(TAG, "run: 4");
                            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(i);
                            Log.d(TAG, "the activity is: " + getActivity());
                            return;
                        }

                        handler.postDelayed(this, 200);
                    }
                };




                Intent intentTwo = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intentTwo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentTwo.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intentTwo.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                handler.postDelayed(checkOverlaySetting, 1000);
                //intentTwo.putExtra("finishActivityOnSaveCompleted", true);

                startActivity(intentTwo);
                break;

            case R.id.TakeReferencePic:
                Log.d(TAG, "onClick: I clicked it");
                Intent intentThree = new Intent(getActivity(), FaceDetect.class);
                startActivity(intentThree);
                //imageView3.setImageResource(R.drawable.green_tick);

                break;

        }
    }



//
    public void showDialog3(View v) {


        Log.d("History", "In show Dialog3");


        final Handler handler2 = new Handler();

        Runnable checkOverlaySetting2 = new Runnable() {

            @Override
            //@TargetApi(23)
            public void run() {
                Log.d(TAG, "run: 1");
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Log.d(TAG, "run: 2");
                    return;
                }

                // 18th Jan 2018, below works, trying to stop using the intent ( ie try back button below).
                if (checkNotificationEnabled()) {
                    Log.d(TAG, "run: 41");
                    Log.d(TAG, "run: Notificiation Enabled moterfucker");
                    //You have the permission, re-launch MainActivity
                    Intent i = new Intent(getActivity(), MainSettingsActivity.class);
                    Log.d(TAG, "run: 42");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Log.d(TAG, "the activity is: " + getActivity());
                    return;
                }

                handler2.postDelayed(this, 200);
            }
        };



        //new ViewWeekStepCountTask().execute();
//        DialogFragment dialog = (DialogFragment) DialogFragment.instantiate(getActivity(), "MyDialogFragment" );
//        dialog.show(getFragmentManager(), "dialog");
        //myDialogFragment.show(getFragmentManager(), "INTO");

//        MyDialogFragment myDialogFragment = new MyDialogFragment();
//        myDialogFragment.show(getFragmentManager(), "INTO");

        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle("IMPORTANT");
        ad.setMessage("After changing setting for EARS Tool to ON, please press back button until " +
                "you return to this menu!");
        ad.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                //Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                //intent.putExtra("finishActivityOnSaveCompleted", true);
                // intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings$SecuritySettingsActivity"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ad.show();
        handler2.postDelayed(checkOverlaySetting2, 1000);
    }

    public boolean isAccessGranted() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean checkNotificationEnabled() {
        try{
            Log.d(TAG, "checkNotificationEnabled: in try");
            if(Settings.Secure.getString(getActivity().getContentResolver(),
                    "enabled_notification_listeners").contains(getActivity().getApplication().getPackageName()))
            {
                Log.d(TAG, "checkNotificationEnabled: in true");

                Log.d(TAG, "checkNotificationEnabled: true");
                return true;
            } else {

                Log.d(TAG, "checkNotificationEnabled: ruturn false");
                return false;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "checkNotificationEnabled: Did not get into settings?");
        return false;
    }

    private Context mBaseContext = null;
    private Intent mReLaunchTaskIntent = null;
    private Context mAppContext;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        mBaseContext = activity.getBaseContext();
        mReLaunchTaskIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1){
//            if(requestCode == RESULT_OK){
//
//                //imageView3 = (ImageView)findViewById(imageView2);
//                Log.d(TAG, "onActivityResult: setting image view for picture");
//
//                imageView3.setImageResource(R.drawable.green_tick);
//                }
//
//            }
//        }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ONONONONONONONONONONONON");
        imageView3.invalidate();
        imageView1.invalidate();
        imageView2.invalidate();


        if(isAccessGranted()){
            Log.d(TAG, "onViewCreated: access granted");
            imageView1.setImageResource(R.drawable.green_tick);
        }


        if(checkNotificationEnabled()){
            Log.d(TAG, "onViewCreated: notifications");
            imageView2.setImageResource(R.drawable.green_tick);
        }

        mContext = getActivity();
        
        if(mContext == null){
            Log.d(TAG, "onResume: NULL");
        }

        SharedPreferences preferences = mContext.getSharedPreferences(TICK, MODE_PRIVATE);
        int myTick = preferences.getInt("done",0);
        if(myTick == 1){
            Log.d(TAG, "onResume: WE HAVE DOEN THE PHONE THINGY YOU FUCKERS");
            imageView3.setImageResource(R.drawable.green_tick);

        }else{
            Log.d(TAG, "onResume: WE DO NOT HAVE THE SHRED PREFR WORKING");
        }




    }
}
