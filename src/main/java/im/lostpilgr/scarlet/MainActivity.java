package im.lostpilgr.scarlet;

import java.util.ArrayList;
import java.util.Locale;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import im.lostpilgr.scarlet.service.MainService;

/**
 * Created by Tom Black on 5/16/13.
 */

public class MainActivity extends Activity implements
        TextToSpeech.OnInitListener {
    /** Called when the activity is first created. */

    private Context context = getApplicationContext();

    private Button sendBtn;
    private EditText sendText;
    private ListView listMessages;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> messages;
    private TextToSpeech tts;

    private AccountManager accountManager = AccountManager.get(context);
    Account[] accounts = accountManager.getAccountsByType("com.google");

    private Intent service = new Intent(context, MainService.class);
    startService(service);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // user input
        listMessages = (ListView) findViewById(R.id.listMessages);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendText = (EditText) findViewById(R.id.sendText);
        sendText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return(actionId == EditorInfo.IME_ACTION_GO);
            }
        });

        // backend objects
        messages = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.listitem, messages);
        tts = new TextToSpeech(this, this);

        // set up message list for input
        listMessages.setAdapter(adapter);

        // button on click event
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if(result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                sendBtn.setEnabled(true);
            }
        } else {
            Log.e("TTS","Initialization Failed!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()) {
            case R.id.action_clear:
                clearMessages();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "No settings to change yet.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearMessages() {
        messages.clear();
        adapter.notifyDataSetChanged();
    }

    private void speakOut() {
        String text = sendText.getText().toString();
        if(!text.isEmpty()) {
            messages.add("You: " + text);
            adapter.notifyDataSetChanged();
            sendText.setText("");
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter some text.", Toast.LENGTH_SHORT).show();
        }
    }
}