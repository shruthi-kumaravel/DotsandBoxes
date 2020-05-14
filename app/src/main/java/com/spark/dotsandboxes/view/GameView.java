package com.spark.dotsandboxes.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.RequiresApi;

import com.spark.dotsandboxes.R;
import com.spark.dotsandboxes.model.Direction;
import com.spark.dotsandboxes.model.Graph;
import com.spark.dotsandboxes.model.HumanPlayer;
import com.spark.dotsandboxes.model.Line;
import com.spark.dotsandboxes.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameView extends View implements Observer {
    // Setting variables for making grid and filling grids with color
    //for 6*6 f=824  8*8 f=1150
    public static float f=824f;
    public static  float radius = (float) 14 / f;
    public static float start = (float) 6 / f;
    public static float add1 = (float) 18 / f;
    public static float add2 = (float) 2 / f;
    public static float add3 = (float) 14 / f;
    public static float add4 = (float) 141 / f;
    public static float add5 = (float) 159 / f;
    public static float add6 = (float) 9 / f;
    public static int widthHeight = 5;

    //variable for keeping player colors.
    protected final int[] playerColors;
    protected Graph game;
    protected Line move;
    protected Paint paint;
    protected PlayersStateView playersState;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        paint = new Paint();

        //Registering touch inputs from the user.
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                receiveInput(event);
                return false;
            }
        });
        //saving user colors in array.
        playerColors = new int[]{getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent)};
    }

    public void setPlayersState(PlayersStateView playersState) {
        this.playersState = playersState;
    }
    //This method starts the game
    public void startGame(Player[] players) {
        game = new Graph(widthHeight, widthHeight, players);  //initializing the graph
        game.addObserver(this);
        new Thread() {
            @Override
            public void run() {
                game.start();
            }
        }.start();
        postInvalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0x00FFFFFF);
        int min = Math.min(getWidth(), getHeight());
        float radius = GameView.radius * min;
        float start = GameView.start * min;
        float add1 = GameView.add1 * min;
        float add2 = GameView.add2 * min;
        float add4 = GameView.add4 * min;
        float add5 = GameView.add5 * min;
        float add6 = GameView.add6 * min;

        //drawing lines of the grid.
        paint.setColor(0xFF000000);
        for (int i = 0; i < game.getHeight() + 1; i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                Line horizontal = new Line(Direction.HORIZONTAL, i, j);
                //This is for setting diffrent color for latest move

//                if (horizontal.equals(game.getLatestLine())) {
//
//                    paint.setColor(0xFF999999);
//                } else

                if (game.isLineOccupied(horizontal)) {
                    if (game.getLineOccupier(horizontal) == 1)
                        paint.setColor(playerColors[0]);
                    else
                        paint.setColor(playerColors[1]);
                } else {
                    paint.setColor(0xFFCCCB);
                }
                canvas.drawRect(start + add5 * j + add1, start + add5 * i
                        + add2, start + add5 * (j + 1), start + add5 * i + add1
                        - add2, paint);

                Line vertical = new Line(Direction.VERTICAL, j, i);

                //This is for setting diffrent color for latest move

//                if (vertical.equals(game.getLatestLine())) {
//
//                    paint.setColor(0xFF999999);
//                } else

                if (game.isLineOccupied(vertical)) {
                    if (game.getLineOccupier(vertical) == 1)
                        paint.setColor(playerColors[0]);
                    else
                        paint.setColor(playerColors[1]);
                } else {
                    paint.setColor(0xFFCCCB);

                }
                // drawing rectangle
                canvas.drawRect(start + add5 * i + add2, start + add5 * j
                        + add1, start + add5 * i + add1 - add2, start + add5
                        * (j + 1), paint);
            }
        }

        //drawing boxes

        for (int i = 0; i < game.getWidth(); i++) {
            for (int j = 0; j < game.getHeight(); j++) {
                paint.setColor(game.getBoxOccupier(j, i) == null ? Color.TRANSPARENT : playerColors[Player.indexIn(game.getBoxOccupier(j, i), game.getPlayers())]);
                canvas.drawRect(start + add5 * i + add1 + add2, start
                        + add5 * j + add1 + add2, start + add5 * i + add1
                        + add4 - add2, start + add5 * j + add1 + add4
                        - add2, paint);
                int left = (int) (start + add5 * i + add1 + add2);
                int right = (int)(start + add5 * i + add1 + add4 - add2);
                int top = (int)(start + add5 * j + add1 + add2);
                int bottom = (int)(start + add5 * j + add1 + add4 - add2);

            }
        }

        //paint points
        paint.setColor(getResources().getColor(R.color.colorAccent));
        for (int i = 0; i < game.getHeight() + 1; i++) {
            for (int j = 0; j < game.getWidth() + 1; j++) {
                canvas.drawCircle(start + add6 + j * add5 + 1, start + add6 + i * add5 + 1,
                        radius, paint);
            }
        }

        invalidate();
    }
    //taking user input
    private void receiveInput(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return;

        float touchX = event.getX();
        float touchY = event.getY();
        int min = Math.min(getWidth(), getHeight());
        float start = GameView.start * min;
        float add1 = GameView.add1 * min;
        float add2 = GameView.add2 * min;
        float add3 = GameView.add3 * min;
        float add5 = GameView.add5 * min;
        int d = -1, a = -1, b = -1;
        for (int i = 0; i < widthHeight+1; i++) {
            for (int j = 0; j < widthHeight; j++) {
                if ((start + add5 * j + add1 - add3) <= touchX
                        && touchX <= (start + add5 * (j + 1) + add3)
                        && touchY >= start + add5 * i + add2 - add3
                        && touchY <= start + add5 * i + add1 - add2 + add3) {
                    d = 0;
                    a = i;
                    b = j;
                }
                if (start + add5 * i + add2 - add3 <= touchX
                        && touchX <= start + add5 * i + add1 - add2 + add3
                        && touchY >= start + add5 * j + add1 - add3
                        && touchY <= start + add5 * (j + 1) + add3) {
                    d = 1;
                    a = j;
                    b = i;
                }
            }
        }

        //drawing line
        if ((a != -1) && (b != -1)) {
            Direction direction;
            if (d == 0)
                direction = Direction.HORIZONTAL;
            else
                direction = Direction.VERTICAL;
            move = new Line(direction, a, b);
            try {
                ((HumanPlayer) game.currentPlayer()).add(move);
            } catch (Exception e) {
                Log.e("GameView", e.toString());
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        playersState.setCurrentPlayer(game.currentPlayer());
        Map<Player, Integer> player_BoxCount_map = new HashMap<>();
        //calculating points
        for (Player player : game.getPlayers()) {
            player_BoxCount_map.put(player, game.getPlayerOccupyingBoxCount(player));
        }
        playersState.setPlayerBoxesCount(player_BoxCount_map);
        //getting or setting the winner
        Player winner = game.getWinner();
        if (winner != null) {
            playersState.setWinner(winner);
        }
    }
}
