package com.example.robertschat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private WebSocket webSocket;
    private OkHttpClient client;
    private List<String> messages = new ArrayList<>();
    private ListView listView;
    private static final String KEY_FOR_CAR_LIST = "carlist";
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        String r = result.getData().getStringExtra("name");
        if (r == null) return;
        messages.add(r);
        updateListView();
    });

    private ActivityResultLauncher<Intent> launcherToCarInformatio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Toast.makeText(this, "jtijrjtrjejit", Toast.LENGTH_SHORT).show();
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listCars);
        updateListView();
        editText = findViewById(R.id.editTextView);
        client = new OkHttpClient();
        startWebSocket();
    }

    private void startWebSocket() {
        Request request = new Request.Builder()
                .url("https://robertschatserver.onrender.com")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                runOnUiThread(() -> {
                    messages.add(text);
                    updateListView();
                });
            }
        });
    }

    public void onPressButtonSend(View view) {
        String message = editText.getText().toString();
        if (webSocket != null && !message.isEmpty()) {
            webSocket.send(message);
            editText.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

    private void updateListView() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, messages);
        listView.setAdapter(arrayAdapter);
    }
}
