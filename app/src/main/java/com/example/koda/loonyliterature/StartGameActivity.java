package com.example.koda.loonyliterature;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Imports the Google Cloud client library
import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Locale;

public class StartGameActivity extends AppCompatActivity
        implements View.OnClickListener{
    private MadLib madlib;
    private Button SpeakButton;
    private Button SaveButton;
    private Button NextButton;

    private ByteString audioContents;

    String completedMadLib;

    private TextToSpeech speech;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        madlib = MyApplication.getMadLibObject();
        if(madlib != null){
            setContentView(R.layout.activity_story_screen);
            setMadLibStory();
            setButtons();
        }

    }

    private void setMadLibStory() {
        TextView text = findViewById( R.id.textView4);
        text.setText(madlib.title);
        completedMadLib = madlib.value.get(0);
        for (int i = 0; i < madlib.blanks.size(); i++) {
            completedMadLib += madlib.blanks.get(i) + madlib.value.get(i + 1);
        }
        TextView libDisplay = findViewById(R.id.MadLibDisplay);
        MyApplication.setMadLibString(completedMadLib);
        libDisplay.setText(completedMadLib);
        setUpSpeech();
    }

    private void setUpSpeech(){
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });
    }

    private void setUpGoogleSpeech(){
        // Instantiates a client
        System.out.print(getApplicationContext().getFilesDir().getAbsolutePath() + "" +
                "" +
                "" +
                "");
        Uri otherPath = Uri.parse("/data/user/0/com.example.koda.loonyliterature/app/src/main/res/My First Project-86a72278c309.json");
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS",otherPath.toString());

        new Thread(new Runnable() {
            public void run() {
                try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
                    // Set the text input to be synthesized
                    SynthesisInput input = SynthesisInput.newBuilder()
                            .setText(completedMadLib)
                            .build();

                    // Build the voice request, select the language code ("en-US") and the ssml voice gender
                    // ("neutral")
                    VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                            .setLanguageCode("en-US")
                            .setSsmlGender(SsmlVoiceGender.FEMALE)
                            .build();

                    // Select the type of audio file you want returned
                    AudioConfig audioConfig = AudioConfig.newBuilder()
                            .setAudioEncoding(AudioEncoding.MP3)
                            .build();

                    // Perform the text-to-speech request on the text input with the selected voice parameters and
                    // audio file type
                    SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
                            audioConfig);

                    // Get the audio contents from the response
                    audioContents = response.getAudioContent();
                    // Write the response to the output file.
//                    try (OutputStream out = new FileOutputStream("output.mp3")) {
//                        out.write(audioContents.toByteArray());
//                        System.out.println("Audio content written to file \"output.mp3\"");
//                    }
//                    catch(IOException e){
//                        e.printStackTrace();
//                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    private void setButtons() {
        SpeakButton = getGameButton(R.id.SpeakToMeButton);
        SpeakButton.setOnClickListener(this);
        SaveButton = getGameButton(R.id.SaveButton);
        SaveButton.setOnClickListener(this);
        NextButton = getGameButton(R.id.NextButton);
        NextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b = getGameButton(v.getId());
        switch (b.getId()){
            case R.id.SpeakToMeButton:
                speech.speak(completedMadLib, TextToSpeech.QUEUE_FLUSH, null);
//                if(audioContents != null){
//                    playMp3(audioContents.toByteArray());
//                }
                break;
            case R.id.SaveButton:
                startActivity(new Intent(getApplicationContext(), SaveStoryScreenActivity.class));
                break;
            case R.id.NextButton:
                Intent intent = new Intent(this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }

    }

    public void onPause(){
        if(speech !=null){
            speech.stop();
            speech.shutdown();
        }
        super.onPause();
    }


    private Button getGameButton(int newGameButton) {
        return findViewById(newGameButton);
    }
}
