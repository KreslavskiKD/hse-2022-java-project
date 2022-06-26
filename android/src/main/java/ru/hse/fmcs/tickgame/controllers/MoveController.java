package ru.hse.fmcs.tickgame.controllers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import ru.hse.fmcs.tickgame.Icons;
import ru.hse.fmcs.tickgame.R;
import ru.hse.fmcs.tickgame.objects.Direction;
import ru.hse.fmcs.tickgame.objects.OnMoveListener;

public class MoveController extends ConstraintLayout {
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private OnMoveListener moveListener;

    public MoveController(@NonNull Context context, OnMoveListener moveListener) {
        super(context);

        this.moveListener = moveListener;

        View root = inflate(context, R.layout.move_controller, this);
        leftButton = root.findViewById(R.id.leftBtn);
        rightButton = root.findViewById(R.id.rightBtn);
        downButton = root.findViewById(R.id.downBtn);
        upButton = root.findViewById(R.id.upBtn);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fa_solid_900);

        leftButton.setText(Icons.LEFT_ARROW);
        leftButton.setTypeface(typeface);
        leftButton.setOnClickListener(v -> onMove(Direction.LEFT));

        rightButton.setText(Icons.RIGHT_ARROW);
        rightButton.setTypeface(typeface);
        rightButton.setOnClickListener(v -> onMove(Direction.RIGHT));

        upButton.setText(Icons.UP_ARROW);
        upButton.setTypeface(typeface);
        upButton.setOnClickListener(v -> onMove(Direction.UP));

        downButton.setText(Icons.DOWN_ARROW);
        downButton.setTypeface(typeface);
        downButton.setOnClickListener(v -> onMove(Direction.DOWN));
    }



    private void onMove(Direction direction) {
        if (moveListener != null) {
            moveListener.onMove(direction);
        }
    }
}
