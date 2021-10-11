package com.github.tommyettinger;

import com.badlogic.gdx.ApplicationAdapter;

public class NoiseGen extends ApplicationAdapter {

    public Noise noise;

    public NoiseGen() {
        noise = new Noise();
    }
    public NoiseGen(Noise n) {
        noise = n;
    }

    @Override
    public void create() {

        System.exit(0);
    }
}