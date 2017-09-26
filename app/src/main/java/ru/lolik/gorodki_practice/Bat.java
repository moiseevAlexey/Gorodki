package ru.lolik.gorodki_practice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.lang.Math;


/**
 * Created by John on 06.07.2016.
 */
public class Bat {

    float x1;
    float y1;
    float x2;
    float y2;
    float xc;
    float yc;

    float bRadius;
    float speed;

    double angle;
    double dAngle;


    float startLine;
    float halfStartLine;

    boolean flying;

    Paint pBat;
    Paint pStartLine;
    GameView gameView;


    public Bat(GameView gameView)
    {
        this.gameView = gameView;

        speed = gameView.displayHeight/60;
        bRadius = this.gameView.displayWidth/(float)8;
        angle = 0;
        dAngle = Math.PI / 16;

        pBat = new Paint();
        pBat.setStrokeWidth(this.gameView.displayWidth/(float)80);
        pBat.setColor(Color.BLACK);

        pStartLine = new Paint();
        pStartLine.setStrokeWidth(this.gameView.displayWidth/(float)40);
        pStartLine.setColor(Color.BLUE);

        startLine = this.gameView.displayHeight/(float)1.2;
        halfStartLine = this.gameView.displayHeight/(float)1.8;
        goToStart();
    }


    public void move()
    {
        if ((y1 < 0) && (y2 < 0))
        {
            gameView.batsThrown++;
            goToStart();
        }

        if (flying == true)
        {
            yc -= speed;
        }

        angle += dAngle;
        if (angle >= 2 * Math.PI)
        {
            angle -= 2 * Math.PI;
        }
        y1 = yc + (float)java.lang.Math.sin(angle) * bRadius;
        x1 = xc + (float)java.lang.Math.cos(angle) * bRadius;
        y2 = yc + (float)java.lang.Math.sin(angle + Math.PI) * bRadius;
        x2 = xc + (float)java.lang.Math.cos(angle + Math.PI) * bRadius;
    }


    public void goToStart()
    {
        xc = this.gameView.displayWidth/2;
        if (gameView.halfRound) {
            yc = halfStartLine;
        }
        else {
            yc = startLine;
        }

        flying = false;
    }


    public void draw(Canvas canvas)
    {
        if (gameView.halfRound) {
            canvas.drawLine(0, halfStartLine, gameView.displayWidth, halfStartLine, pStartLine);
        }
        else {
            canvas.drawLine(0, startLine, gameView.displayWidth, startLine, pStartLine);
        }

        canvas.drawLine(x1, y1, x2, y2, pBat);
    }
}
