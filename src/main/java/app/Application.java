package app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Application {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("app.Powers");
        config.setWindowedMode(800, 480);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setMaximized(true);
        new Lwjgl3Application(new Powers(), config);
    }
}
