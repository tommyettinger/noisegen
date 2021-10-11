package com.github.tommyettinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public class NoiseGen extends ApplicationAdapter {

    public Noise noise;
    public int width = 512;
    public int height = 512;

    public NoiseGen() {
        noise = new Noise();
    }
    public NoiseGen(Noise n) {
        noise = n;
    }
    public NoiseGen(Noise n, int w, int h) {
        noise = n;
        width = w;
        height = h;
    }

    @Override
    public void create() {
        Pixmap pm = new Pixmap(width, height, Pixmap.Format.RGB888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int v = (int)(noise.getConfiguredNoise(x, y) * 127.999f + 127.999f);
                pm.drawPixel(x, y, v * 0x010101 << 8 | 255);
            }
        }
        PixmapIO.writePNG(Gdx.files.local("noise.png"), pm);
        System.out.println("Done!");
        System.exit(0);
    }
}