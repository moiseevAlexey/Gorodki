package lolikandbolik.gorod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView{  //Тело игры

    private SurfaceHolder holder;

    //Игровые оббъекты
    Bat bat;
    Field field;
    GameManager gameManager;

    Paint p;    //Палитра

    //Разрешение экрана
    float displayHeight;
    float displayWidth;

    //Игровые данные
    int level;
    boolean halfRound;
    boolean won,lost;
    int batsThrown;
    int batsAmount;
    int maxLevel;

    public GameView(Context context)    //Конструктор
    {
        super(context);

        //Чтение разрешения экрана устройства
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        displayHeight = displaymetrics.heightPixels;
        displayWidth = displaymetrics.widthPixels;

        holder = getHolder();

        //Создание палитры
        p = new Paint();
        p.setTextSize(displayHeight/60);

        //Инициализация игровых данных
        won = false;
        lost = false;
        level = 0;
        halfRound = false;
        batsThrown = 0;
        batsAmount = 16;
        maxLevel = 8;

        //Создание игровых объектов
        gameManager = new GameManager(this);
        bat = new Bat(this);
        field = new Field(this);


        holder.addCallback(new SurfaceHolder.Callback()
        {

            /** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                gameManager.setRunning(false);
                while (retry)
                {
                    try
                    {
                        // ожидание завершение потока
                        gameManager.join();
                        retry = false;
                    }
                    catch (InterruptedException e) { }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder)
            {
                gameManager.setRunning(true);
                gameManager.start();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
            }
        });
    }


    public boolean onTouchEvent(MotionEvent e)  //Обработка касания
    {
        float shotX = (int) e.getX();
        float shotY = (int) e.getY();

        if(e.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(bat.flying == false && !lost && !won)    //Запуск биты
            {
                bat.flying = true;
                bat.x1 = shotX;
                bat.x2 = shotX;
                bat.xc = shotX;
            }
            if(lost || won) //Запуск новой игры
            {
                lost = false;
                won = false;
                level = 0;
                batsThrown = 0;
                halfRound = false;

                p.setTextSize(displayHeight/60);
                p.setTextAlign(Paint.Align.LEFT);

                field.gorodki.removeAllElements();
                bat.goToStart();
                field.fillGorodki();

            }
        }

        return true;
    }


    public void nextGameLoop(Canvas canvas) //Игровой цикл
    {
        if (batsThrown > batsAmount-1 && level!=maxLevel)   //Проигрыш
        {
            lose(canvas);
            return;
        }

        if (level == maxLevel)  //Выигрыш
        {
            win(canvas);
            return;
        }

        findCollisions();   //Найти пересечения

        if (field.isAllKnocked())   //Сброс полукона
        {
            halfRound = false;
        }

        if (field.gorodki.isEmpty() && bat.flying == false) //Следующий уровень
        {
            goOnNextLevel();
        }

        //Движение игровых объектов
        field.move();
        bat.move();

        draw(canvas);   //Отрисовка экрана
    }


    public void win(Canvas canvas)  //Экран при победе
    {
        won = true;
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(displayHeight/20);
        canvas.drawColor(Color.CYAN);
        canvas.drawText("ПОБЕДА!", displayWidth/2f, displayHeight/2f, p);
        p.setTextSize(displayHeight/50);
        canvas.drawText("Чтобы начать новую игру, коснитесь экрана",displayWidth/2f,displayHeight/2f+displayHeight/10,p);

    }


    public void lose(Canvas canvas) //Экран при проигрыше
    {
        lost = true;
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(displayHeight/20);
        canvas.drawColor(Color.CYAN);
        canvas.drawText("Вы проиграли", displayWidth/2f, displayHeight/2f, p);
        p.setTextSize(displayHeight/50);
        canvas.drawText("Чтобы начать новую игру, коснитесь экрана",displayWidth/2f,displayHeight/2f+displayHeight/10,p);
    }


    public void goOnNextLevel() //Переход на следующий уровень
    {
        level++;
        field.fillGorodki();
    }


    public void findCollisions()    //Поиск пересечений
    {
        if (field.isBatCollision())
        {
            int i = 0;
            while (i < field.gorodki.size())
            {
                if (field.gorodki.get(i).isBatCollision()) {
                    field.knockDownGorodok(i);
                    halfRound = true;
                }
                i++;
            }
        }
    }


    public void getBinaryCode(int vertex[],float xmin, float ymin, float xmax, float ymax, float x,float y) //Получеие бинарного кода для вершин биты
    {
        if (x >= xmin)
            vertex[0] = 0;
        else
            vertex[0] = 1;
        if (x <= xmax)
            vertex[1] = 0;
        else
            vertex[1] = 1;
        if (y >= ymin)
            vertex[2] = 0;
        else
            vertex[2] = 1;
        if (y <= ymax)
            vertex[3] = 0;
        else
            vertex[3] = 1;

    }


    public boolean isCollisionByCohenSutherland(float xmin,float ymin,float xmax,float ymax,float x1, float y1, float x2, float y2) //Поиск пересечения методом Коэна-Сазерленда
    {
        int i;
        float tangle = (y2 - y1)/(x2 - x1); //(float)Math.tan(angle);
        float newY,newX;

        int First_vertex[] = new int[4];
        int Second_vertex[] = new int[4];
        int summ[] = new int[2];

        getBinaryCode(First_vertex,xmin,ymin,xmax,ymax,x1,y1);
        getBinaryCode(Second_vertex,xmin,ymin,xmax,ymax,x2,y2);


        for(i=0;i<4;i++)
        {
            if (First_vertex[i] == 1 && Second_vertex[i] == 1)
                return false;
            summ[0]+=First_vertex[i];
            summ[1]+=Second_vertex[i];
        }
        if(summ[0] == 0 || summ[1] == 0)
            return true;


        newY = tangle*(xmin - x1)+y1;
        if(newY >= ymin && newY <= ymax)
            return true;
        else
        {
            newX = x1 + 1/tangle*(ymax-y1);
            if(newX >= xmin && newX <= xmax)
                return true;
            else
            {
                newX = x1+1/tangle*(ymin-y1);
                if(newX >= xmin && newX <= xmax)
                    return true;
                else
                {
                    newY = tangle*(xmax - x1)+y1;
                    if (newY >= ymin && newY <= ymax)
                        return true;
                    else
                        return false;
                }

            }
        }
    }


    @Override
    public void draw(Canvas canvas) //Отрисовка
    {
        canvas.drawColor(Color.CYAN);
        field.draw(canvas);
        bat.draw(canvas);
        canvas.drawText("Бит брошено: "+ batsThrown, (float)10, displayHeight/2f - 20, p);
        canvas.drawText("Фигур сбито: "+ level, (float)10, displayHeight/2f + 20, p);
    }
}
