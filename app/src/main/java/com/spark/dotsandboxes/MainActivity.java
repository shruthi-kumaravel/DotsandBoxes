package com.spark.dotsandboxes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.spark.dotsandboxes.helper.Constants;
import com.spark.dotsandboxes.model.HumanPlayer;
import com.spark.dotsandboxes.model.Player;
import com.spark.dotsandboxes.view.GameView;
import com.spark.dotsandboxes.view.PlayersStateView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements PlayersStateView {
    protected GameView gameView;
    protected TextView player1name, player2name, player1state, player2state, player1occupying,
            player2occupying,eigthxeight,sixxsix;
    ImageView currentPlayerPointer;
    Player[] players;
    Integer[] playersPoints = new Integer[]{0, 0};
    Player currentPlayer;
    LinearLayout llp1,llp2;
    //SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sharedPref = getApplicationContext().getSharedPreferences("mode", 0);

        //Initializing view components
        gameView = findViewById(R.id.gameView);
        gameView.setPlayersState(this);
        player1name = findViewById(R.id.player1name);
        player2name = findViewById(R.id.player2name);
        player1state = findViewById(R.id.player1state);
        player2state = findViewById(R.id.player2state);
        player1occupying = findViewById(R.id.player1occupying);
        player2occupying = findViewById(R.id.player2occupying);
        currentPlayerPointer = findViewById(R.id.currentPlayerPointer);
        llp1 = findViewById(R.id.llplayer1);
        llp2 = findViewById(R.id.llplayer2);
        eigthxeight = findViewById(R.id.eightxeight);
        sixxsix = findViewById(R.id.sixxsix);

        //Hiding 6*6 or 8*8 according to selection
        if(Constants.SMALL){
            sixxsix.setVisibility(View.GONE);
            eigthxeight.setVisibility(View.VISIBLE);
        }else {
            sixxsix.setVisibility(View.VISIBLE);
            eigthxeight.setVisibility(View.GONE);
        }
        //Creating Players
        players = new Player[]{new HumanPlayer("Player 1"), new HumanPlayer("Player 2")};
        startGame(players);
        //Setting onclick listeners
        eigthxeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start8x8();
            }
        });

        sixxsix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start6x6();
            }
        });
    }

    // setting essential variables to draw 6*6 grids and dots.
    private void start6x6() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Dots And Boxes")
                .setMessage("Do you really want to play 6x6 ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameView.widthHeight=5;
                        float f = 824f;
                        GameView.radius = (float) 14 / f;
                        GameView.start = (float) 6 / f;
                        GameView.add1 = (float) 18 / f;
                        GameView.add2 = (float) 2 / f;
                        GameView.add3 = (float) 14 / f;
                        GameView.add4 = (float) 141 / f;
                        GameView.add5 = (float) 159 / f;
                        GameView.add6 = (float) 9 / f;
                        gameView.startGame(players);
                        Constants.SMALL = true;
                        updateState();
                        recreate();
                        eigthxeight.setVisibility(View.VISIBLE);
                        sixxsix.setVisibility(View.GONE);
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    // setting essential variables to draw 8*8 grids and dots.
    private void start8x8() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Dots And Boxes")
                .setMessage("Do you really want to play 8x8 ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameView.widthHeight=7;
                        float f = 1150f;
                        GameView.radius = (float) 14 / f;
                        GameView.start = (float) 6 / f;
                        GameView.add1 = (float) 18 / f;
                        GameView.add2 = (float) 2 / f;
                        GameView.add3 = (float) 14 / f;
                        GameView.add4 = (float) 141 / f;
                        GameView.add5 = (float) 159 / f;
                        GameView.add6 = (float) 9 / f;
                        gameView.startGame(players);
                        updateState();
                        eigthxeight.setVisibility(View.GONE);
                        sixxsix.setVisibility(View.VISIBLE);
                        Constants.SMALL = false;
                        recreate();
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }
    // Starting the game
    private void startGame(Player[] players) {
        gameView.startGame(players);
        updateState();
    }
    //Setting state according to the player
    public void updateState() {
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                if (currentPlayer == players[0]) {
                    player1state.setText("Thinking");
                    player2state.setText("Waiting");
//                    llp1.setBackgroundColor(R.color.playingcolor);
//                    llp2.setBackgroundColor(Color.TRANSPARENT);
                    currentPlayerPointer.setImageResource(R.drawable.ic_arrowleft);

                } else if (currentPlayer == players[1]) {
                    player2state.setText("Thinking");
                    player1state.setText("Waiting");
//                    llp2.setBackgroundColor(R.color.playingcolor);
//                    llp1.setBackgroundColor(Color.TRANSPARENT);
                    currentPlayerPointer.setImageResource(R.drawable.ic_arrowright);
                }
                //setting points to the view
                player1occupying.setText(playersPoints[0]+" Points");
                player2occupying.setText(playersPoints[1]+" Points");
            }
        });
    }
    //Setting current player
    @Override
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
        updateState();
    }
    //keeping track of player points
    @Override
    public void setPlayerBoxesCount(Map<Player, Integer> player_BoxesCount_map) {
        playersPoints[0] = (player_BoxesCount_map.get(players[0]));
        playersPoints[1] = (player_BoxesCount_map.get(players[1]));
        updateState();
    }
    //Announcing the winner.
    @Override
    public void setWinner(final Player winner) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Dots And Boxes")
                        .setMessage(winner.getName() + " Wins!")
                        .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                            }
                        })
                        .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });
    }

}
