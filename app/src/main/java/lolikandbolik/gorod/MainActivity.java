package lolikandbolik.gorod;



import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

    GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameView(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Постоянная портретная ориентация приложения

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //Полноэкранность приложения

        setContentView(game);
    }

}
