package ru.hse.fmcs.tickgame.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.hse.fmcs.GameObject;
import ru.hse.fmcs.Room;
import ru.hse.fmcs.RoomServiceGrpc;
import ru.hse.fmcs.tickgame.GameContext;
import ru.hse.fmcs.tickgame.R;


public class RoomActivity extends Activity {
    private static final String TAG = "RoomActivity";

    private RoomServiceGrpc.RoomServiceStub stub;
    private ManagedChannel channel;
    private LinearLayout roomLogLayout;
    private LinearLayout roomButtonsLayout;
    private LinearLayout playersLayout;
    private TextView numberPlayersToStartView;
    int numberPlayersToStart = 0;
    private final ArrayList<GameObject.Player> players = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.room);

        String roomName = getIntent().getStringExtra("lobby_id");

        channel = ManagedChannelBuilder.forAddress(GameContext.getServerAddress(), GameContext.getRoomPort()).usePlaintext().build();

        Room.JoinToRoomRequest req = Room.JoinToRoomRequest.newBuilder().setLogin(GameContext.getLogin()).setRoomName(roomName).build();

        StreamObserver<Room.RoomEvent> clientObserver = new StreamObserver<Room.RoomEvent>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(Room.RoomEvent value) {
                processRoomEvent(value);
            }

            @Override
            public void onError(Throwable t) {
                if (t != null) Log.d(TAG, "clientObserver error: \n" + t.getMessage());
                else Log.d(TAG, "clientObserver error");
            }

            @Override
            public void onCompleted() {
                onDestroy();
            }

        };

        stub = RoomServiceGrpc.newStub(channel);
        stub.joinToRoom(req, clientObserver);

        TextView roomNameTextView = findViewById(R.id.roomText);
        roomNameTextView.setText(roomName);

        roomLogLayout = findViewById(R.id.roomLogLayout);
        roomButtonsLayout = findViewById(R.id.roomButtonsLayout);
        playersLayout = findViewById(R.id.roomPlayersLayout);
        numberPlayersToStartView = findViewById(R.id.numberPlayersToStartTextView);

        Button leaveBtn = new Button(roomButtonsLayout.getContext());
        leaveBtn.setText("Leave");
        leaveBtn.setOnClickListener(view -> {
                onBackPressed();
            }
        );
        roomButtonsLayout.addView(leaveBtn);
    }

    @Override
    public void onBackPressed() {
        channel.shutdown();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        channel.shutdown();
        super.onDestroy();
    }

    private void processRoomEvent(Room.RoomEvent value) {

        Log.d(TAG, value.getEventCase().toString());
        switch (value.getEventCase()) {
            case JOINTOROOMRESPONSE:
                processJoinToRoomResponse(value.getJoinToRoomResponse());
                break;
            case OTHERPLAYERJOINEDEVENT:
                processOtherPlayerJoinedEvent(value.getOtherPlayerJoinedEvent());
                break;
            case OTHERPLAYERDISCONNECTEDEVENT:
                processOtherPlayerDisconnectedEvent(value.getOtherPlayerDisconnectedEvent());
                break;
            case GAMESTARTEDEVENT:
                processGameStartedEvent(value.getGameStartedEvent());
                break;
        }
    }

    private void processJoinToRoomResponse(Room.JoinToRoomResponse res) {
        boolean success = res.getSuccess();
        Log.d(TAG, res.getSuccess() ? "true" : "false");
        ArrayList<GameObject.Player> playerList = new ArrayList<>(res.getPlayerList());
        numberPlayersToStart = res.getNumberPlayersToStart();
        if (success) {
            synchronized (players) {
                players.addAll(playerList);
            }
            runOnUiThread(() -> addToLog("Connected successfully...", Color.GREEN));
        } else {
            String comment = res.getComment();
            synchronized (players) {
                players.clear();
            }
            runOnUiThread(() -> roomLogLayout.addView(generateView(roomLogLayout.getContext(), comment, Color.RED)));
            runOnUiThread(() -> channel.shutdownNow());
            runOnUiThread(() -> addToLog("Connection failed", Color.RED));
        }
        runOnUiThread(this::draw);

    }

    private TextView generateView(android.content.Context context, String text, int color) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        tv.setTextColor(color);
        return tv;
    }


    private void processOtherPlayerJoinedEvent(Room.OtherPlayerJoinedEvent res) {
        GameObject.Player player = res.getPlayer();

        synchronized (players) {
            players.add(player);
        }

        String text = player.getLogin() + " joined";
        runOnUiThread(() -> addToLog(text, Color.WHITE));
        runOnUiThread(this::draw);
    }

    private void processOtherPlayerDisconnectedEvent(Room.OtherPlayerDisconnectedEvent res) {
        synchronized (players) {
            for (int i = players.size() - 1; i >= 0; i -= 1) {
                if (players.get(i).getLogin().equals(res.getPlayerLogin())) {
                    players.remove(i);
                }
            }
        }

        String text = res.getPlayerLogin() + " disconnected";
        runOnUiThread(() -> addToLog(text, Color.WHITE));
        runOnUiThread(this::draw);
    }

    private void processGameStartedEvent(Room.GameStartedEvent res) {
        String text = "GAME STARTED";
        runOnUiThread(() -> addToLog(text, Color.GREEN));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(() ->{
                    Intent intent = new Intent(this, GameActivity.class);
                    Bundle b = new Bundle();
                    b.putString("game_id", res.getGameId());
                    intent.putExtras(b);
                    startActivity(intent);
                }
        );
    }

    private void addToLog(String text, int color) {
        TextView tv = generateView(roomLogLayout.getContext(), text, color);
        tv.setTypeface(ResourcesCompat.getFont(roomLogLayout.getContext(), R.font.fa_thin_100));
        roomLogLayout.addView(tv);
    }

    @SuppressLint("SetTextI18n")
    private void draw() {
        playersLayout.removeAllViews();
        synchronized (players) {
            for (GameObject.Player player : players) {
                String login = player.getLogin();
                int color = Color.parseColor(player.getColor());
                TextView tv = generateView(playersLayout.getContext(), login, color);
                tv.setPadding(15, 15, 15, 15);
                playersLayout.addView(tv);
            }
            numberPlayersToStartView.setText(players.size() + "/" + numberPlayersToStart);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}

