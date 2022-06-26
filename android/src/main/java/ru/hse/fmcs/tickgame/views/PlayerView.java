package ru.hse.fmcs.tickgame.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import ru.hse.fmcs.tickgame.R;

public class PlayerView extends ConstraintLayout {
    private TextView circleView;
    private TextView loginView;

    public PlayerView(@NonNull Context context, String login, int color) {
        super(context);

        View root = inflate(context, R.layout.room_player, this);
        circleView = root.findViewById(R.id.circleView);
        circleView.setTextColor(color);

        loginView = root.findViewById(R.id.loginView);
        loginView.setText(login);
        loginView.setTextColor(color);
    }
}
