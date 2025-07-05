package com.vrgc.eguidance.Activity.User;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vrgc.eguidance.R;

import java.util.HashMap;
import java.util.Map;

public class EmotionalTestActivity extends AppCompatActivity {
    TextView questionText;
    RadioGroup optionsGroup;
    Button nextButton;
    RadioButton opt1, opt2, opt3, opt4;

    int currentQuestion = 0;
    Map<String, Integer> emotionScores = new HashMap<>();

    String[][] questions = {
            {"How do you usually react to stress?",
                    "Try to stay calm",      // Happy
                    "Feel overwhelmed",      // Sad
                    "Get angry",             // Angry
                    "Avoid everything"},     // Fear

            {"How do you feel waking up?",
                    "Excited for the day",   // Happy
                    "Tired or unmotivated",  // Sad
                    "Annoyed",               // Angry
                    "Nervous about the day"},// Fear

            {"How do you react to failure?",
                    "See it as a lesson",    // Happy
                    "Blame yourself",        // Sad
                    "Blame others",          // Angry
                    "Fear trying again"},    // Fear

            {"How do you behave in a group?",
                    "Friendly and social",   // Happy
                    "Quiet or withdrawn",    // Sad
                    "Easily irritated",      // Angry
                    "Anxious"},              // Fear

            {"When someone hurts you...",
                    "Try to forgive",        // Happy
                    "Feel down",             // Sad
                    "Hold a grudge",         // Angry
                    "Get scared to face them"}, // Fear

            {"At your worst, you feel...",
                    "Disconnected",          // Sad
                    "Worried all the time",  // Fear
                    "Mad at everyone",       // Angry
                    "I try to stay positive"}, // Happy

            {"How do you express your feelings?",
                    "Openly",                // Happy
                    "Keep it in",            // Sad
                    "Aggressively",          // Angry
                    "Hide it out of fear"},  // Fear

            {"How often do you feel joy?",
                    "Often",                 // Happy
                    "Rarely",                // Sad
                    "Only when angry stops", // Angry
                    "Hard to feel safe"},    // Fear

            {"How do you handle rejection?",
                    "Move on",               // Happy
                    "Take it personally",    // Sad
                    "Feel insulted",         // Angry
                    "Fear more rejection"},  // Fear

            {"What’s your go-to coping mechanism?",
                    "Talking to someone",    // Happy
                    "Crying",                // Sad
                    "Yelling or venting",    // Angry
                    "Hiding alone"}          // Fear
    };

    String[] emotions = {"Happy", "Sad", "Angry", "Fear"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotional_test);

        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);
        nextButton = findViewById(R.id.btnNext);

        for (String emotion : emotions) {
            emotionScores.put(emotion, 0);
        }

        loadQuestion();

        nextButton.setOnClickListener(v -> {
            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = optionsGroup.indexOfChild(findViewById(selectedId));
            String emotion = emotions[selectedIndex];
            emotionScores.put(emotion, emotionScores.get(emotion) + 1);

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                showResult();
            }
        });
    }

    private void loadQuestion() {
        optionsGroup.clearCheck();
        questionText.setText(questions[currentQuestion][0]);
        opt1.setText(questions[currentQuestion][1]);
        opt2.setText(questions[currentQuestion][2]);
        opt3.setText(questions[currentQuestion][3]);
        opt4.setText(questions[currentQuestion][4]);
    }

    private void showResult() {
        String topEmotion = "Happy";
        int max = 0;

        for (Map.Entry<String, Integer> entry : emotionScores.entrySet()) {
            if (entry.getValue() > max) {
                topEmotion = entry.getKey();
                max = entry.getValue();
            }
        }

        String message = "";

        switch (topEmotion) {
            case "Happy":
                message = "You're feeling mostly Happy 😊. Keep spreading joy!";
                break;
            case "Sad":
                message = "You're feeling mostly Sad 😢. It's okay to feel this way, talk to someone you trust.";
                break;
            case "Angry":
                message = "You're feeling mostly Angry 😡. Try to cool off and share your feelings calmly.";
                break;
            case "Fear":
                message = "You're feeling mostly Fearful 😨. You might need reassurance and support.";
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle("Your Emotional State")
                .setMessage(message)
                .setPositiveButton("OK", (d, w) -> finish())
                .show();
    }
}