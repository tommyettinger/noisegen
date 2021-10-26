package com.github.tommyettinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.NumberUtils;

public class NoiseGen extends ApplicationAdapter {

    public Noise noise;
    public int width = 512;
    public int height = 512;
    public float curvature = 1f;
    public float middle = 0.5f;
    public String output = "noise.png";

    public boolean debug = true;

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
    public NoiseGen(Noise n, int w, int h, float curvature, float middle, boolean debug, String out) {
        noise = n;
        width = w;
        height = h;
        this.curvature = curvature;
        this.middle = middle;
        this.debug = debug;
        output = out;
    }
    /**
     * A generalization on bias and gain functions that can represent both; this version is branch-less.
     * This is based on <a href="https://arxiv.org/abs/2010.09714">this micro-paper</a> by Jon Barron, which
     * generalizes the earlier bias and gain rational functions by Schlick. The second and final page of the
     * paper has useful graphs of what the s (shape) and t (turning point) parameters do; shape should be 0
     * or greater, while turning must be between 0 and 1, inclusive. This effectively combines two different
     * curving functions so they continue into each other when x equals turning. The shape parameter will
     * cause this to imitate "smoothstep-like" splines when greater than 1 (where the values ease into their
     * starting and ending levels), or to be the inverse when less than 1 (where values start like square
     * root does, taking off very quickly, but also end like square does, landing abruptly at the ending
     * level). You should only give x values between 0 and 1, inclusive.
     * @param x progress through the spline, from 0 to 1, inclusive
     * @param shape must be greater than or equal to 0; values greater than 1 are "normal interpolations"
     * @param turning a value between 0.0 and 1.0, inclusive, where the shape changes
     * @return a float between 0 and 1, inclusive
     */
    public static float barronSpline(final float x, final float shape, final float turning) {
        final float d = turning - x;
        final int f = NumberUtils.floatToRawIntBits(d) >> 31, n = f | 1;
        return ((turning * n - f) * (x + f)) / (Float.MIN_NORMAL - f + (x + shape * d) * n) - f;
    }

    @Override
    public void create() {
        Pixmap pm = new Pixmap(width, height, Pixmap.Format.RGB888);
        if(curvature == 1f){
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if(debug) {
                        int v = (int) (noise.getConfiguredNoise(x, y) * 127.999f + 127.999f);
                        if ((v & -256) == 256)
                            pm.drawPixel(x, y, 0xFF0000FF);
                        else if (v < 0)
                            pm.drawPixel(x, y, 0x0000FFFF);
                        else
                            pm.drawPixel(x, y, v * 0x010101 << 8 | 255);
                    }
                    else {
                        int v = Math.min(Math.max((int)(noise.getConfiguredNoise(x, y) * 127.999f + 127.999f), 0), 255);
                        pm.drawPixel(x, y, v * 0x010101 << 8 | 255);
                    }
                }
            }
        }
        else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if(debug) {
                        int v = (int) (255.999f * barronSpline(noise.getConfiguredNoise(x, y) * 0.5f + 0.5f, curvature, middle));
                        if ((v & -256) == 256)
                            pm.drawPixel(x, y, 0xFF0000FF);
                        else if (v < 0)
                            pm.drawPixel(x, y, 0x0000FFFF);
                        else
                            pm.drawPixel(x, y, v * 0x010101 << 8 | 255);
                    }
                    else {
                        int v = Math.min(Math.max((int)(255.999f*barronSpline(noise.getConfiguredNoise(x, y) * 0.5f + 0.5f, curvature, middle)), 0), 255);
                        pm.drawPixel(x, y, v * 0x010101 << 8 | 255);
                    }
                }
            }
        }
        PixmapIO.writePNG(Gdx.files.local(output), pm);
        System.exit(0);
    }
}