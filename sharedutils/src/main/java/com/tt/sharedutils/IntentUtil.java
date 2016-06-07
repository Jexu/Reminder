package com.tt.sharedutils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;

import java.util.List;

/**
 * Created by zhengguo on 5/13/16.
 */
public class IntentUtil {

    public static boolean goToAddressByOpenMap(Context context, String chooserTitle, long latitude, long longitude ) {
        String uriString = "geo:"+latitude+","+longitude;
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uriString ) );
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities( intent, 0 );
        if ( activities.size() > 0 ) {
            Intent chooser = Intent.createChooser( intent, chooserTitle );
            context.startActivity( chooser );
            return true;
        } else {
            return false;
        }
    }

    public static boolean sendEmail(Context context, String chooserTile, String[] url) {
        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.setType( "message/rfc822" );
        intent.putExtra( Intent.EXTRA_EMAIL, url );
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities( intent, 0 );
        if ( activities.size() > 0 ) {
            Intent chooser = Intent.createChooser( intent, chooserTile );
            context.startActivity( chooser );
            return true;
        }
        else {
            return false;
        }
    }

    public static void dialPhone(Context context, String phoneNum) {
        if ( phoneNum != null && !phoneNum.trim().equals( "" ) ) {
            Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:"+phoneNum ) );
            context.startActivity( intent );
        }
    }

    public static void sendSms(Context context, String address, String messageBody) {
        if ( address != null && !messageBody.trim().equals( "" ) ) {
            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.putExtra( "address", address );
            intent.putExtra( "sms_body", messageBody );
            intent.setType( "vnd.android-dir/mms-sms" );
            context.startActivity( intent );
        }
    }

    public static void openWebUrl( Context context, String url ) {
        if ( url != null && !url.trim().equals( "" ) ) {
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            context.startActivity( intent );
        }
    }

    public static void openWebUrl( Context context, Uri uri ) {
        Intent intent = new Intent( Intent.ACTION_VIEW, uri );
        context.startActivity( intent );
    }

    public static final int REQUEST_CODE_VOICE_INPUT = -1;
    public static boolean voiceInput(Activity context, String languageModel) {
        boolean isSupportVoiceInput = false;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);
        try {
            context.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
            isSupportVoiceInput = true;
        } catch (ActivityNotFoundException e) {
            isSupportVoiceInput = false;
        }
        return isSupportVoiceInput;
    }

}