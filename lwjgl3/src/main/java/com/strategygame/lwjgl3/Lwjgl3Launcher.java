package com.strategygame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.strategygame.StrategyGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new StrategyGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // === ÉQUIVALENT DE TON Main.java ===
        config.setTitle("Strategic Conquest - ISIL 25/26");
        config.setWindowedMode(1280, 720);
        config.setForegroundFPS(60);

        // VSync (propre + stable)
        config.useVsync(true);

        // Icônes (optionnel mais propre)
        config.setWindowIcon(
                "libgdx128.png",
                "libgdx64.png",
                "libgdx32.png",
                "libgdx16.png"
        );

        // Compatibilité OpenGL (on garde, recommandé)
        config.setOpenGLEmulation(
                Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20,
                0,
                0
        );

        return config;
    }
}
