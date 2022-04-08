package com.github.tommyettinger;

import java.util.Arrays;

/**
 * A small utility class to apply Gaussian blur to an array of data, usually representing a 2D field of
 * different lightness values. This is from
 * <a href="https://github.com/jmecn/jME3Tutorials/blob/master/src/main/java/net/jmecn/outscene/GaussianBlur.java">the
 * Chinese translation of jMonkeyEngine's examples</a>.
 */
public class GaussianBlur {

    private float[] kernel;
    private double sigma = 1.8;
    private float min = 0;
    private float max = 1;

    public GaussianBlur() {
        kernel = new float[0];
    }

    public GaussianBlur(float sigma) {
        kernel = new float[0];
        this.sigma = sigma;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double a) {
        this.sigma = a;
    }

    public void setClamp(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float[] filter(final float[] levelData, final int width, final int height) {
        if(sigma == 0f) return levelData;
        boolean flip = sigma < 0f;
        double sigma = this.sigma * this.sigma;
        final int size = width * height;

        makeGaussianKernel(sigma, Math.min(width, height));


        float[] temp = new float[size], original;
        if(flip)
            original = Arrays.copyOf(levelData, levelData.length);
        else original = null;
        blur(levelData, temp, width, height); // H Gaussian
        blur(temp, levelData, height, width); // V Gaussian
        if(flip){
            float half = (max - min) * 0.5f;
            for (int i = 0; i < levelData.length; i++) {
                levelData[i] = Math.min(Math.max(original[i] - levelData[i] + half, min), max);
            }
        }
        return levelData;
    }

    /**
     * @param inLevels
     * @param outLevels
     * @param width
     * @param height
     */
    private void blur(float[] inLevels, float[] outLevels, int width, int height) {
        final int k = kernel.length - 1;
        for (int row = 0; row < height; row++) {
            int index = row;
            for (int col = 0; col < width; col++) {
                float sum = 0;
                for (int m = -k; m <= k; m++) {
                    sum += inLevels[row * width + (col + m + width) % width] * kernel[Math.abs(m)];
                }
                outLevels[index] = Math.min(Math.max(sum, min), max);
                index += height;
            }
        }
    }

    private void makeGaussianKernel(final double sigma, int maxRadius) {
        int kRadius = (int) Math.ceil(sigma * 3.525509352823274) + 1; // Math.sqrt(-2 * Math.log(0.002)) is
        if (maxRadius < 50)
            maxRadius = 50; // too small maxRadius would result in inaccurate sum.
        if (kRadius > maxRadius)
            kRadius = maxRadius;
        if(kernel == null || kernel.length != kRadius)
            kernel = new float[kRadius];
        final double invSigma = 1.0 / sigma;
        for (int i = 0; i < kRadius; i++) {// Gaussian function
            double isig = i * invSigma;
            kernel[i] = (float) (Math.exp(-0.5 * isig * isig));
        }
        double sum; // sum over all kernel elements for normalization
        if (kRadius < maxRadius) {
            sum = kernel[0];
            for (int i = 1; i < kRadius; i++)
                sum += 2 * kernel[i];
        } else
            sum = sigma * Math.sqrt(2 * Math.PI);

        for (int i = 0; i < kRadius; i++) {
            kernel[i] = (float) (kernel[i] / sum);
        }
    }
}