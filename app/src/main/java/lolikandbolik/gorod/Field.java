package lolikandbolik.gorod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Vector;


public class Field {    //Движущиеся поле

    //Координаты поля
    float x_lu;
    float y_lu;
    float x_ru;
    float y_ru;
    float x_ld;
    float y_ld;
    float x_rd;
    float y_rd;

    float edgeLength;   //Длина ребра
    float speed;    //Скорость движения

    Vector<Gorodok> gorodki;    //Список городков

    float cellSize; //Размер ячейки

    Paint p;    //Палитра поля
    GameView gameView;  //GameView


    public Field(GameView gameView) //Конструктор
    {
        this.gameView = gameView;

        //Установка параметров поля
        speed = gameView.displayWidth/60;
        edgeLength = this.gameView.displayWidth/(float)4;

        cellSize = edgeLength / 16;

        //Настройка палитры поля
        p = new Paint();
        p.setStrokeWidth(this.gameView.displayWidth/(float)320);
        p.setColor(Color.BLUE);

        //Установка стартовой позиции
        x_lu = 0;
        y_lu = this.gameView.displayHeight * (float)0.08;
        x_ld = x_lu;
        y_ld = y_lu + edgeLength;
        x_ru = x_lu + edgeLength;
        y_ru = y_lu;
        x_rd = x_ru;
        y_rd = y_ld;

        //Создание городков
        gorodki = new Vector<Gorodok>();
        fillGorodki();
    }


    public void move()  //Движение поля
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


    /*public boolean isFieldEmpty()   //Проверка пустоты поля
    {
        if (gorodki.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }*/


    public boolean isAllKnocked()   //Выбиты все городки
    {
        for (int i = 0; i < gorodki.size(); i++)
        {
            if (!gorodki.get(i).knocked) {
                return false;
            }
        }
        return true;
    }


    public void knockDownGorodok(int i) //Выбить городок
    {
        gorodki.get(i).knocked = true;
        gorodki.get(i).p.setAlpha(160);
    }


    public boolean isBatCollision() //Проверка пересечения с битой
    {
        if (gameView.isCollisionByCohenSutherland(x_lu, y_lu, x_rd, y_rd,
                gameView.bat.x1, gameView.bat.y1, gameView.bat.x2, gameView.bat.y2)) {
            return true;
        }
        else {
            return false;
        }
    }


    public void draw(Canvas canvas) //Отрисовка поля
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


    public void fillGorodki() { //Уровни игры

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
