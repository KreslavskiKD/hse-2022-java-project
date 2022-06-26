package ru.hse.fmcs.tickgame.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import ru.hse.fmcs.tickgame.R;

public class PlayerView extends ConstraintLayout {
    private TextView circleView;
    private TextView loginView;

    public PlayerView(@NonNull Context context, String login, int color) {
        super(context);

        View root = inflate(context, R.layout.room_player, this);
        circleView = root.findViewById(R.id.circleView);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_solid_900);
        circleView.setTypeface(typeface);
        circleView.setTextColor(color);

        loginView = root.findViewById(R.id.loginView);
        loginView.setText(login);
        loginView.setTextColor(color);
    }
}
