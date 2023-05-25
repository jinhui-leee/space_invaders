package org.newdawn.spaceinvaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Theme {

    enum ThemeState {BASIC, EARTH, MOON, MARS, MERCURY, JUPITER, VENUS, SATURN, SUN}

    ThemeState themeState;

    private BufferedImage background;

    private BufferedImage descriptionBackground;

    private BufferedImage mainBackground;

    private String themeColor;


    public Theme() {
        themeState = ThemeState.BASIC;
        setThemeBackground();
    }

    public void setThemeState(int themeChoice) {
        if (themeChoice == 0) themeState = ThemeState.BASIC;
        else if (themeChoice == 1) themeState = ThemeState.EARTH;
        else if (themeChoice == 2) themeState = ThemeState.MOON;
        else if (themeChoice == 3) themeState = ThemeState.MARS;
        else if (themeChoice == 4) themeState = ThemeState.MERCURY;
        else if (themeChoice == 5) themeState = ThemeState.JUPITER;
        else if (themeChoice == 6) themeState = ThemeState.VENUS;
        else if (themeChoice == 7) themeState = ThemeState.SATURN;
        else if (themeChoice == 8) themeState = ThemeState.SUN;

        setThemeBackground();
    }

    public void setThemeBackground() {

        URL backgroundUrl[] = new URL[3];
        BufferedImage background[] = new BufferedImage[3];


        if (themeState == ThemeState.BASIC) {
            backgroundUrl[0] = this.getClass().getResource("/images/background1.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d.png");
            themeColor = "#451d52";

        }
        else if (themeState ==ThemeState.EARTH) {
            backgroundUrl[0] = this.getClass().getResource("/images/background2.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description2.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d2.png");
            themeColor = "#4175dc";

        }
        else if (themeState ==ThemeState.MOON) {
            backgroundUrl[0] = this.getClass().getResource("/images/background3.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description3.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d3.png");
            themeColor = "#113c49";

        }

        else if (themeState ==ThemeState.MARS) {
            backgroundUrl[0] = this.getClass().getResource("/images/background4.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description4.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d4.png");
            themeColor = "#dd6d3e";
        }
        else if (themeState ==ThemeState.MERCURY) {
            backgroundUrl[0] = this.getClass().getResource("/images/background5.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description5.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d5.png");
            themeColor = "#303030";

        }
        else if (themeState ==ThemeState.JUPITER) {
            backgroundUrl[0] = this.getClass().getResource("/images/background6.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description6.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d6.png");
            themeColor = "#6c493d";

        }
        else if (themeState ==ThemeState.VENUS) {
            backgroundUrl[0] = this.getClass().getResource("/images/background7.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description7.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d7.png");
            themeColor = "#f7b006";

        }
        else if (themeState ==ThemeState.SATURN) {
            backgroundUrl[0] = this.getClass().getResource("/images/background8.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description8.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d8.png");
            themeColor = "#7d7353";
        }
        else if (themeState ==ThemeState.SUN) {
            backgroundUrl[0] = this.getClass().getResource("/images/background9.png");
            backgroundUrl[1] = this.getClass().getResource("/images/description9.png");
            backgroundUrl[2] = this.getClass().getResource("/images/background_d9.png");
            themeColor = "#ea3600";

        }

        try {
            background[0] = ImageIO.read(backgroundUrl[0]);
            background[1] = ImageIO.read(backgroundUrl[1]);
            background[2] = ImageIO.read(backgroundUrl[2]);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setMainBackground(background[0]);
        setDescriptionBackground(background[1]);
        setBackground(background[2]);

    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
    }

    public BufferedImage getDescriptionBackground() {
        return descriptionBackground;
    }

    public void setDescriptionBackground(BufferedImage descriptionBackground) {
        this.descriptionBackground = descriptionBackground;
    }

    public BufferedImage getMainBackground() {
        return mainBackground;
    }

    public void setMainBackground(BufferedImage mainBackground) {
        this.mainBackground = mainBackground;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public ThemeState getThemeState() {
        return themeState;
    }
}