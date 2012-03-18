package org.android10.voice;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;


/**
 * @author android10
 * Class to let voice recognition in an android application
 */
public class VoiceRecognition {
	private PackageManager pm;
	
	public final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	public VoiceRecognition(Context ctx){
		this.pm = ctx.getPackageManager();
	}
		
	//Check to see if a recognition activity is present 
	public boolean recognitionAvailable(){
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        
        if (activities.size() != 0) {
            return true;
        } else {
            return false;
        }
	}
	
	public Intent getVoiceRecognitionIntent(String message){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, message);
        return intent;
	}
}
