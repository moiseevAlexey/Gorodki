package lolikandbolik.gorod;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.lang.Math;



public class Bat {  //Бита

    //Координаты концов биты
    float x1;
    float y1;
    float x2;
    float y2;

    //Координата центра вращения биты
    float xc;
    float yc;

    float bRadius;  //Радиус биты
    float speed;    //Скорость полета биты

    double angle;   //Текщий угол поворота биты
    double dAngle;  //Уголо поворота биты при смене кадра


    float startLine;    //Линия начального броска
    float halfStartLine;    //Линия полукона

    boolean flying; //Флаг полета

    Paint pBat; //Палитра биты
    Paint pStartLine;   //Палитра линии броска
    GameView gameView;  //GameView


    public Bat(GameView gameView)   //Конструктор
    {
        this.gameView = gameView;

        //Установка начальных параметров биты
        speed = gameView.displayHeight/60;
        bRadius = this.gameView.displayWidth/(float)8;
        angle = 0;
        dAngle = Math.PI / 16;

        //Настройка палитры биты
        pBat = new Paint();
        pBat.setStrokeWidth(this.gameView.displayWidth/(float)80);
        pBat.setColor(Color.BLACK);

        //Натсройка палитры линии броска
        pStartLine = new Paint();
        pStartLine.setStrokeWidth(this.gameView.displayWidth/(float)40);
        pStartLine.setColor(Color.BLUE);

        //Вычисление линии броска
        startLine = this.gameView.displayHeight/(float)1.2;
        halfStartLine = this.gameView.displayHeight/(float)1.8;
        goToStart();
    }


    public void move()  //Движение биты
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


    public void goToStart() //Установка биты на линию броска
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


    public void draw(Canvas canvas) //Отрисовка биты
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
