package com.example.homework3mobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView music;
    Button play;

    MusicService musicService;
    MusicCompletionReceiver musicCompletionReceiver;
    Intent startMusicServiceIntent;
    boolean isBound = false;
    boolean isInitialized = false;
    int songLoc = 0;
    SeekBar seekbar1;
    SeekBar seekbar2;
    SeekBar seekbar3;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    int seek1int = 0;
    int seek2int = 0;
    int seek3int = 0;
    String songString;
    int songInt;
    int timeLeft = 30000;
    private CountDownTimer countDown;
    String sound1Str = "";
    String sound2Str = "";
    String sound3Str = "";

    public static final String INITIALIZE_STATUS = "intialization status";
    public static final String MUSIC_PLAYING = "music playing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner songSpinner = (Spinner) findViewById(R.id.songSpinner);
        Button songStarter = (Button) findViewById(R.id.startButton);
        seekbar1 = (SeekBar) findViewById(R.id.seek1);
        seekbar1.setMax(100);
        seekbar2 = (SeekBar) findViewById(R.id.seek2);
        seekbar2.setMax(100);
        seekbar3 = (SeekBar) findViewById(R.id.seek3);
        seekbar3.setMax(100);
        spinner1 = (Spinner) findViewById(R.id.spin1);
        spinner2 = (Spinner) findViewById(R.id.spin2);
        spinner3 = (Spinner) findViewById(R.id.spin3);

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek1int = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek2int = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek3int = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ArrayAdapter<CharSequence> songAdapter = ArrayAdapter.createFromResource(this, R.array.Song, android.R.layout.simple_spinner_item);
        songAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        songSpinner.setAdapter(songAdapter);
        songSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (position != 0){
                    songLoc = position;
                    songString = item;
                    if (songString.equals("Super Mario")){
                        songInt = 27;
                    }
                    else if (songString.equals("Tetris")){
                        songInt = 25;
                    }
                    else if (songString.equals("Hokies")){
                        songInt = 49;
                    }
                    musicService.musicPlayer.changeLocation(songLoc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> soundAdapter1 = ArrayAdapter.createFromResource(this, R.array.Sound, android.R.layout.simple_spinner_item);
        soundAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(soundAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (position != 0){
                    sound1Str = item;
                    musicService.musicPlayer.changeLocation(songLoc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> soundAdapter2 = ArrayAdapter.createFromResource(this, R.array.Sound, android.R.layout.simple_spinner_item);
        soundAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(soundAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (position != 0){
                    sound2Str = item;
                    musicService.musicPlayer.changeLocation(songLoc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> soundAdapter3 = ArrayAdapter.createFromResource(this, R.array.Sound, android.R.layout.simple_spinner_item);
        soundAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(soundAdapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (position != 0){
                    sound2Str = item;
                    musicService.musicPlayer.changeLocation(songLoc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        songStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    switch (musicService.getPlayingStatus()){
                        case 0:
                            musicService.startMusic();
                            play.setText("Pause");
                            break;
                        case 1:
                            musicService.pauseMusic();
                            play.setText("Resume");
                            break;
                        case 2:
                            musicService.resumeMusic();
                            play.setText("Pause");
                            break;
                    }
                }

                if (musicService.getPlayingStatus() != 0)
                {
                    musicService.musicPlayer.onCompletion(musicService.musicPlayer.getMedia());
                    music.setText(musicService.musicPlayer.getMusicName());
                    play.setText("Pause");
                }
            }
        });

        music= (TextView) findViewById(R.id.music);
        play= (Button) findViewById(R.id.startButton);
        play.setOnClickListener(this);

        if(savedInstanceState != null){
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            music.setText(savedInstanceState.getString(MUSIC_PLAYING));
        }

        startMusicServiceIntent= new Intent(this, MusicService.class);

        if(!isInitialized){
            startService(startMusicServiceIntent);
            isInitialized= true;
        }

        musicCompletionReceiver = new MusicCompletionReceiver(this);

    }

    @Override
    public void onClick(View view) {
        if (isBound) {
            switch (musicService.getPlayingStatus()){
                case 0:
                    musicService.startMusic();
                    Double seek1Double = 0.0;
                    Double seek2Double = 0.0;
                    Double seek3Double = 0.0;
                    int songTotal1 = 0;
                    int songTotal2 = 0;
                    int songTotal3 = 0;
                    seek1Double = seek1int / 100.0;
                    seek2Double = seek2int / 100.0;
                    seek3Double = seek3int / 100.0;
                    songTotal1 = (int) (songInt * seek1Double);
                    songTotal2 = (int) (songInt * seek2Double);
                    songTotal3 = (int) (songInt * seek3Double);
                    play.setText("Pause");
                    int finalSongTotal1 = songTotal1;
                    int finalSongTotal2 = songTotal2;
                    int finalSongTotal3 = songTotal3;
                    Intent intent = new Intent(this, MusicActivity.class);
                    startActivity(intent);
                    countDown = new CountDownTimer(timeLeft, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timeLeft = (int) millisUntilFinished;
                            if (30 - finalSongTotal1 == timeLeft/1000 && - finalSongTotal1 != 0){
                                if (sound1Str.equals("Clapping")){
                                    musicService.musicPlayer.changeLocation(4);
                                    musicService.startMusic();
                                    finish();
                                    startActivity(intent);
                                    intent.putExtra("picId", R.drawable.image2);
                                }
                                else if (sound1Str.equals("Cheering")){
                                    musicService.musicPlayer.changeLocation(5);
                                    musicService.startMusic();
                                }
                                else if (sound1Str.equals("Lets Go Hokies")){
                                    musicService.musicPlayer.changeLocation(6);
                                    musicService.startMusic();
                                }
                            }
                            if (30 - finalSongTotal2 == timeLeft/1000 && 30 - finalSongTotal2 != 0){
                                if (sound2Str.equals("Clapping")){
                                    musicService.musicPlayer.changeLocation(4);
                                    musicService.startMusic();
                                }
                                else if (sound2Str.equals("Cheering")){
                                    musicService.musicPlayer.changeLocation(5);
                                    musicService.startMusic();
                                }
                                else if (sound2Str.equals("Lets Go Hokies")){
                                    musicService.musicPlayer.changeLocation(6);
                                    musicService.startMusic();
                                }
                            }

                            if (30 - finalSongTotal3 == timeLeft/1000 && - finalSongTotal3 != 0){
                                if (sound3Str.equals("Clapping")){
                                    musicService.musicPlayer.changeLocation(4);
                                    musicService.startMusic();
                                }
                                else if (sound3Str.equals("Cheering")){
                                    musicService.musicPlayer.changeLocation(5);
                                    musicService.startMusic();
                                }
                                else if (sound3Str.equals("Lets Go Hokies")){
                                    musicService.musicPlayer.changeLocation(6);
                                    musicService.startMusic();
                                }
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                    break;
                case 1:
                    musicService.musicPlayer.changeLocation(songLoc);
                    musicService.pauseMusic();
                    play.setText("Resume");
                    break;
                case 2:
                    musicService.resumeMusic();
                    play.setText("Pause");
                    break;
            }
        }
    }


    public void updateName(String musicName) {

        music.setText(musicName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInitialized && !isBound){
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }

        registerReceiver(musicCompletionReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isBound){
            unbindService(musicServiceConnection);
            isBound= false;
        }

        unregisterReceiver(musicCompletionReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, music.getText().toString());
        super.onSaveInstanceState(outState);
    }


    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;

            switch (musicService.getPlayingStatus()) {
                case 0:
                    play.setText("Start");
                    break;
                case 1:
                    play.setText("Pause");
                    break;
                case 2:
                    play.setText("Resume");
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
            isBound = false;
        }
    };

}
