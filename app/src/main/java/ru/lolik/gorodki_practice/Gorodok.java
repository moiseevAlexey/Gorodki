package ru.lolik.gorodki_practice;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by John on 06.07.2016.
 */
public class Gorodok {

    int x;
    int y;
    int type;

    float x_lu;
    float y_lu;
    float x_rd;
    float y_rd;

    float cellSize;

    boolean knocked;
    boolean offScreen;

    Field field;
    Paint p;


    public Gorodok(Field field, int x, int y, int type, float cellSize)
    {
        this.field = field;
        p = new Paint();

        this.cellSize = cellSize;

        this.x = x;
        this.y = y;
        this.type = type;

        knocked = false;
        offScreen = false;

        float x_origin = field.x_lu + x * cellSize;
        float y_origin = field.y_lu + y * cellSize;

        switch (type)
        {
            case 0: //Квадрат
                x_lu = x_origin - cellSize;
                y_lu = y_origin - cellSize;
                x_rd = x_origin + cellSize;
                y_rd = y_origin + cellSize;
                break;
            case 1: //Горизонтальный блок
                x_lu = x_origin - 2 * cellSize;
                y_lu = y_origin - cellSize;
                x_rd = x_origin + 2 * cellSize;
                y_rd = y_origin + cellSize;
                break;
            case 2: //Вертикальный блок
                x_lu = x_origin - cellSize;
                y_lu = y_origin - 2 * cellSize;
                x_rd = x_origin + cellSize;
                y_rd = y_origin + 2 * cellSize;
                break;
            default:
                x_lu = 0;
                y_lu = 0;
                x_rd = 0;
                y_rd = 0;
                break;
        }
    }


    public void move()
    {
        if(!knocked)
        {
            x_lu += field.speed;
            x_rd += field.speed;
        }
        else
        {
            y_lu -= field.gameView.displayHeight/45;
            y_rd -= field.gameView.displayHeight/45;
        }

        if (y_rd <= 0)
        {
            offScreen = true;
        }
    }


    public boolean isBatCollision()
    {
        if (field.gameView.isCollisionByCohenSutherland(x_lu, y_lu, x_rd, y_rd,
                field.gameView.bat.x1, field.gameView.bat.y1, field.gameView.bat.x2, field.gameView.bat.y2)){
            return  true;
        }
        else {
            return false;
        }
    }


    public void draw(Canvas canvas)
    {
        canvas.drawRect(x_lu, y_lu, x_rd, y_rd, p);
    }
}
