package org.android10.fastfoodfinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.android10.fastfoodfinder.R;
import org.android10.voice.VoiceRecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	//id of the dialog used to choose between maps and augmented reality
	static final int CHOOSE_METHOD_DIALOG = 1;
	static final int ABOUT_DIALOG = 2;
	
	static int CURRENT_FASTFOOD_RESTAURANT = 0;
	static int CURRENT_FASTFOOD_RESTAURANT_IMAGE = 0;
	
	private IconContextMenu chooseActionContextMenu = null;
	
	private final int MENU_ITEM_1_ACTION_MAPS = 1;
	private final int MENU_ITEM_2_ACTION_AUGMENTED_REALITY = 2;
	
	//Google Local Search API Query variables
	private final String GOOGLE_SEARCH_API_URL = "http://ajax.googleapis.com/ajax/services/search/local?v=1.0&";
	private final String GOOGLE_SEARCH_API_QUERY = "&key=ABQIAAAAOyrsM9YxbvxqlZk8z_Uy9hRb2oMHTHqx5XHwASLxxSzsnB2O0BSpMzRshxiPTrZMuR-OAE2Cr5mmeA&rsz=8&filter=1";
	private final String GOOGLE_SEARCH_API_MCDONALDS = "q=Mc%20Donalds";
	private final String GOOGLE_SEARCH_API_BURGERKING = "q=Burger%20King";
	private final String GOOGLE_SEARCH_API_PIZZAHUT = "q=Pizza%20Hut";
	private final String GOOGLE_SEARCH_API_PANSANDCOMPANY = "q=Pans%20And%20Company";
	private final String GOOGLE_SEARCH_API_STARBUCKS = "q=Starbucks%20Coffee";
	private final String GOOGLE_SEARCH_API_DUNKINDONUTS = "q=Dunkin'%20Donuts";
	
	private String GOOGLE_SEARCH_API_CUSTOM = "q=";
	private String VOICE_RECOGNIZED_WORD = "";
	
	private final String URL_SPACE = "%20";
	
	String arUri;
	String mapUri;
	Intent mapCall;
	Uri geoUri;
	
	private VoiceRecognition voiceRecognition;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);              
        
        final Button btnMcDonalds = (Button) findViewById(R.id.btn_MainMenu01);
        final Button btnBurgerKing = (Button) findViewById(R.id.btn_MainMenu02);
        final Button btnPizzaHut = (Button) findViewById(R.id.btn_MainMenu03);
        final Button btnPanAndCompany = (Button) findViewById(R.id.btn_MainMenu04);
        final Button btnStarbucks = (Button) findViewById(R.id.btn_MainMenu05);
        final Button btnDunkinDonuts = (Button) findViewById(R.id.btn_MainMenu06);       
        
        this.voiceRecognition = new VoiceRecognition(this); 	//to recognize voice
        //------------------------------------------------------------------
        //I N I T    T H E    M E N U    W I T H    I T S    E V E N T S
        //------------------------------------------------------------------
        Resources res = getResources();
		chooseActionContextMenu = new IconContextMenu(this, CHOOSE_METHOD_DIALOG);
        chooseActionContextMenu.addItem(res, R.string.choose_action_maps, R.drawable.action_maps, MENU_ITEM_1_ACTION_MAPS);
        chooseActionContextMenu.addItem(res, R.string.choose_action_augmentedreality, R.drawable.action_ar, MENU_ITEM_2_ACTION_AUGMENTED_REALITY);
        
        //set onclick listener for context menu
        chooseActionContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
			@Override
			public void onClick(int menuId) {
				if (menuId == MENU_ITEM_1_ACTION_MAPS){
					switch (CURRENT_FASTFOOD_RESTAURANT)
    	    		{
    	    			case 1: //MC DONALDS
    	    				check_McDonalds();
    	    				break;
    	    			case 2: //BURGER KING
    	    				check_BurgerKing();
    	    				break;
    	    			case 3: //PIZZA HUT
    	    				check_PizzaHut();
    	    				break;
    	    			case 4: //PANS AND COMPANY
    	    				check_PanAndCompany();
    	    				break;
    	    			case 5: //STARBUCKS
    	    				check_Starbucks();
    	    				break;
    	    			case 6: //DUNKIN DONUTS
    	    				check_DunkinDonuts();
    	    				break;
    	    			case 7: //CUSTOM
    	    				check_Custom();
    	    				break;
    	    		}					
				}else if (menuId == MENU_ITEM_2_ACTION_AUGMENTED_REALITY){
					Intent arview = new Intent();
					arview.setAction(Intent.ACTION_VIEW);
					arUri = GOOGLE_SEARCH_API_URL;
					switch (CURRENT_FASTFOOD_RESTAURANT)
    	    		{
    	    			case 1: //MC DONALDS
    	    				arUri += GOOGLE_SEARCH_API_MCDONALDS;
    	    				break;
    	    			case 2: //BURGER KING
    	    				arUri += GOOGLE_SEARCH_API_BURGERKING;
    	    				break;
    	    			case 3: //PIZZA HUT
    	    				arUri += GOOGLE_SEARCH_API_PIZZAHUT;
    	    				break;
    	    			case 4: //PANS AND COMPANY
    	    				arUri += GOOGLE_SEARCH_API_PANSANDCOMPANY;
    	    				break;
    	    			case 5: //STARBUCKS
    	    				arUri += GOOGLE_SEARCH_API_STARBUCKS;
    	    				break;
    	    			case 6: //DUNKIN DONUTS
    	    				arUri += GOOGLE_SEARCH_API_DUNKINDONUTS;
    	    				break;
    	    			case 7:
    	    				arUri += GOOGLE_SEARCH_API_CUSTOM;
    	    				arUri += VOICE_RECOGNIZED_WORD.replace(" ", URL_SPACE);
    	    				break;
    	    		}						
					arUri += GOOGLE_SEARCH_API_QUERY;
					//arview.setDataAndType(Uri.parse(arUri), "application/mixare-json");
					arview.setDataAndType(Uri.parse(arUri), "application/android10-json");
					arview.putExtra("imageId", CURRENT_FASTFOOD_RESTAURANT_IMAGE);					
					startActivity(arview);										
				}
			}
        });
        
        //------------------------------------------------------------------
        //E V E N T S
        //------------------------------------------------------------------
        btnMcDonalds.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 1;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.mcdonalds_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});      
        
        btnBurgerKing.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 2;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.burgerking_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});       
        
        btnPizzaHut.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 3;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.pizzahut_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});             
        
        btnPanAndCompany.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 4;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.pansandcompany_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});             
        
        btnStarbucks.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 5;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.starbucks_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});              
        
        btnDunkinDonuts.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 6;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.dunkindonuts_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});                            
    }
    
    //------------------------------------------------------------------
    //A C T I V I T Y    R E S U L T
    //------------------------------------------------------------------
    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            
            if (matches.size()>=0){
            	this.VOICE_RECOGNIZED_WORD = matches.get(0).toString();
            	CURRENT_FASTFOOD_RESTAURANT = 7;
            	CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.custom_icon_ar;
            	showDialog(CHOOSE_METHOD_DIALOG);
			}
            else{
            	this.VOICE_RECOGNIZED_WORD = "";
            	Toast.makeText(this, getString(R.string.voice_not_results_msg), Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    C H O O S E    M E T H O D    D I A L O G
    //------------------------------------------------------------------
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	switch (id) {
    		case CHOOSE_METHOD_DIALOG:
    			return chooseActionContextMenu.createMenu(this.getString(R.string.choose_action_title));    			
    		case ABOUT_DIALOG:
    			dialog = createAboutDialog();
    			break;			
    	    default:
    	    	dialog = null;
    	    	break;
    	}
    	return dialog;
    };
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    D I A L O G S
    //------------------------------------------------------------------    
    private AlertDialog createAboutDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.license_title)
								.setMessage(R.string.license).setIcon(R.drawable.about)
								.setNeutralButton(R.string.about_button, null);
    	AlertDialog alert = builder.create();
    	return alert;
    }
    
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    M E N U
    //------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);		

		// Return true so that the menu gets displayed.
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Close the menu after a period of time.
		// Note that this STARTS the timer when the options menu is being
		// prepared, NOT when the menu is made visible.
		Timer timing = new Timer();
		timing.schedule(new TimerTask() {

			@Override
			public void run() {
				closeOptionsMenu();
			}
		}, 5000);
		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.voice:
				this.startVoice();
				break;
			case R.id.about:
				showDialog(ABOUT_DIALOG);
				break;
		}		
		return true;
	}
	
    //------------------------------------------------------------------
    //P R I V A T E     M E T H O D S
    //------------------------------------------------------------------
	private void check_McDonalds() {		
		mapUri = getResources().getString(R.string.map_location_uri_mcdonalds);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_BurgerKing() {
		mapUri = getResources().getString(R.string.map_location_uri_burgerking);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_PizzaHut() {
		mapUri = getResources().getString(R.string.map_location_uri_pizzahut);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_PanAndCompany() {
		mapUri = getResources().getString(R.string.map_location_uri_pansandcompany);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_Starbucks() {
		mapUri = getResources().getString(R.string.map_location_uri_starbucks);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_DunkinDonuts() {
		mapUri = getResources().getString(R.string.map_location_uri_dunkindonuts);
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void check_Custom(){
		mapUri = getResources().getString(R.string.map_location_uri_custom) + this.VOICE_RECOGNIZED_WORD;
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void startVoice(){
		if (this.voiceRecognition != null){
			if (this.voiceRecognition.recognitionAvailable()){
				startActivityForResult(this.voiceRecognition.getVoiceRecognitionIntent(getString(R.string.voice_find_msg)), 
									   this.voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE);
			}else{
				Toast.makeText(this, getString(R.string.voice_notavailable_msg), Toast.LENGTH_LONG).show();
			}
		}			
	}
    //------------------------------------------------------------------
    //P U B L I C     M E T H O D S
    //------------------------------------------------------------------
	
}