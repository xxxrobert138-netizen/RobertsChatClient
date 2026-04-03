package com.example.robertschat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private TextView textViewChat;
    private EditText editText;
    private WebSocket webSocket;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewChat = findViewById(R.id.textView);
        editText = findViewById(R.id.editTextText);

        client = new OkHttpClient();
        startWebSocket();
    }

    private void startWebSocket() {
        Request request = new Request.Builder()
                .url("wss://robertschatserver.onrender.com/")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                runOnUiThread(() -> textViewChat.setText(text));
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
                t.printStackTrace();
            }
        });
    }

    public void onPressButtonUpdate(View view) {
        if (webSocket != null) webSocket.close(1000, null);
        startWebSocket();
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
}
