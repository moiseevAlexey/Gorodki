package ru.lolik.gorodki_practice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Vector;

/**
 * Created by John on 06.07.2016.
 */
public class Field {

    float x_lu;
    float y_lu;
    float x_ru;
    float y_ru;
    float x_ld;
    float y_ld;
    float x_rd;
    float y_rd;
    float edgeLength;
    float speed;

    Vector<Gorodok> gorodki;

    float cellSize;

    Paint p;
    GameView gameView;


    public Field(GameView gameView)
    {
        this.gameView = gameView;

        speed = gameView.displayWidth/60;
        edgeLength = this.gameView.displayWidth/(float)4;

        cellSize = edgeLength / 16;

        p = new Paint();
        p.setStrokeWidth(this.gameView.displayWidth/(float)320);
        p.setColor(Color.BLUE);

        x_lu = 0;
        y_lu = this.gameView.displayHeight * (float)0.08;
        x_ld = x_lu;
        y_ld = y_lu + edgeLength;
        x_ru = x_lu + edgeLength;
        y_ru = y_lu;
        x_rd = x_ru;
        y_rd = y_ld;

        gorodki = new Vector<Gorodok>();
        fillGorodki();
    }


    public void move()
    {
        if ((x_lu < 0 - speed) || (x_ru > gameView.displayWidth - speed))
        {
           speed = -speed;
        }
        x_lu += speed;
        x_ld += speed;
        x_ru += speed;
        x_rd += speed;

        for (int i = 0; i < gorodki.size(); i++)
        {
            gorodki.get(i).move();
        }

        for (int i = 0; i < gorodki.size(); i++)
        {
            if (gorodki.get(i).offScreen) {
                gorodki.remove(i);
            }
        }
    }


    public boolean isFieldEmpty()
    {
        if (gorodki.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean isAllKnocked()
    {
        for (int i = 0; i < gorodki.size(); i++)
        {
            if (!gorodki.get(i).knocked) {
                return false;
            }
        }
        return true;
    }


    public void knockDownGorodok(int i)
    {
        gorodki.get(i).knocked = true;
        gorodki.get(i).p.setAlpha(160);
    }


    public boolean isBatCollision()
    {
        if (gameView.isCollisionByCohenSutherland(x_lu, y_lu, x_rd, y_rd,
                gameView.bat.x1, gameView.bat.y1, gameView.bat.x2, gameView.bat.y2)) {
            return true;
        }
        else {
            return false;
        }
    }


    public void draw(Canvas canvas)
    {
        p.setColor(Color.WHITE);
        canvas.drawRect(x_lu, y_lu, x_rd, y_rd, p);
        p.setColor(Color.BLUE);
        canvas.drawLine(x_lu,y_lu,x_ld,y_ld,p);
        canvas.drawLine(x_lu,y_lu,x_ru,y_ru,p);
        canvas.drawLine(x_ld,y_ld,x_rd,y_rd,p);
        canvas.drawLine(x_ru,y_ru,x_rd,y_rd,p);


        for (int i = 0; i < gorodki.size(); i++)
        {
            gorodki.get(i).draw(canvas);
        }
    }


    public void fillGorodki() {

        switch (gameView.level)
        {
            case 0:
                gorodki.addElement(new Gorodok(this, 6, 15, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 10, 15, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 13, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 10, 2, cellSize));
                break;
            case 1:
                gorodki.addElement(new Gorodok(this, 8, 8, 0, cellSize));
                gorodki.addElement(new Gorodok(this, 14, 8, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 2, 8, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 2, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 14, 2, cellSize));
                break;
            case 2:
                gorodki.addElement(new Gorodok(this, 8, 8, 0, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 11, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 5, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 11, 8, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 5, 8, 2, cellSize));
                break;
            case 3:
                gorodki.addElement(new Gorodok(this, 8, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 3, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 13, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 1, 15, 0, cellSize));
                gorodki.addElement(new Gorodok(this, 15, 15, 0, cellSize));
                break;
            case 4:
                gorodki.addElement(new Gorodok(this, 8, 15, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 11, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 5, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 11, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 13, 0, cellSize));
                break;
            case 5:
                gorodki.addElement(new Gorodok(this, 8, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 3, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 13, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 2, 11, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 14, 11, 1, cellSize));
                break;
            case 6:
                gorodki.addElement(new Gorodok(this, 3, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 13, 14, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 15, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 11, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 9, 0, cellSize));
                break;
            case 7:
                gorodki.addElement(new Gorodok(this, 5, 8, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 11, 8, 2, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 15, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 11, 1, cellSize));
                gorodki.addElement(new Gorodok(this, 8, 13, 0, cellSize));
                break;
        }
    }
}
