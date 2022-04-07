package com.github.tommyettinger.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.Noise;
import com.github.tommyettinger.NoiseGen;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "noisegen", version = "NoiseGen 0.0.1",
		description = "Generate noise and write it to an image file.",
		mixinStandardHelpOptions = true)
public class HeadlessLauncher implements Callable<Integer> {

	public Noise noise = new Noise();

	@CommandLine.Option(names = {"-f", "--frequency"}, description = "The frequency of the noise, with high frequency changing rapidly.", defaultValue = "0.03125")
	public float frequency = 0x1p-5f;

	@CommandLine.Option(names = {"-s", "--seed"}, description = "The seed that determines how the noise will form using the given parameters.")
	public int seed = (int) MathUtils.random(0xFFFFFFFFL);

	@CommandLine.Option(names = {"-O", "--octaves"}, description = "The amount of octaves to use; more increases detail.", defaultValue = "3")
	public int octaves = 3;

	@CommandLine.Option(names = {"-W", "--width"}, description = "The width of the resulting image.", defaultValue = "512")
	public int width = 512;

	@CommandLine.Option(names = {"-H", "--height"}, description = "The height of the resulting image.", defaultValue = "512")
	public int height = 512;

	@CommandLine.Option(names = {"-t", "--type"}, description = "The type of noise to generate; one of: simplex, perlin, cubic, foam, honey, mutant, value, white, cellular.", defaultValue = "white")
	public String type = "simplex";

	@CommandLine.Option(names = {"-F", "--fractal"}, description = "The fractal mode to use for most noise types; one of: fbm, billow, ridged.", defaultValue = "fbm")
	public String fractal = "fbm";

	@CommandLine.Option(names = {"-c", "--cellular"}, description = "The cellular return type to use for the cellular type; one of: value, lookup, distance, distance2, distance2add, distance2mul, distance2div.", defaultValue = "value")
	public String cellular = "value";

	@CommandLine.Option(names = {"-S", "--sharpness"}, description = "The sharpness multiplier for foam and mutant noise; higher than one means more extreme.", defaultValue = "1")
	public float sharpness = 1f;

	@CommandLine.Option(names = {"-C", "--curvature"}, description = "How steep the transition should be from black to white; must be positive.", defaultValue = "1")
	public float curvature = 1f;

	@CommandLine.Option(names = {"-M", "--middle"}, description = "When curvature is not 1.0, this determines where the noise starts to turn its curve; must be between 0 and 1, inclusive.", defaultValue = "0.5")
	public float middle = 0.5f;

	@CommandLine.Option(names = {"-m", "--mutation"}, description = "The extra 'spatial' value used by mutant noise; can be any float.", defaultValue = "0")
	public float mutation = 0f;

	@CommandLine.Option(names = {"-o", "--output"}, description = "The name and/or path for the output file.", defaultValue = "noise.png")
	public String output = "noise.png";

	@CommandLine.Option(names = {"-d", "--debug"}, description = "If true, draws higher-than-1 noise as red, and lower-than-negative-1 as blue.", defaultValue = "false")
	public boolean debug = false;

	@CommandLine.Option(names = {"-e", "--equalize"}, description = "If true, makes each grayscale value approximately as frequent as all other values.", defaultValue = "true")
	public boolean equalize = false;

	@CommandLine.Option(names = {"-b", "--blur"}, description = "If > 0, blurs all pixels (wrapping at the edges) outward by this distance in all directions.", defaultValue = "4.8")
	public float blurSigma = 0f;

	public int parseType(String t) {
		t = t.toLowerCase();
		switch (t) {
			case "perlin": return Noise.PERLIN_FRACTAL;
			case "cubic": return Noise.CUBIC_FRACTAL;
			case "foam": return Noise.FOAM_FRACTAL;
			case "honey": return Noise.HONEY_FRACTAL;
			case "mutant": return Noise.MUTANT_FRACTAL;
			case "value": return Noise.VALUE_FRACTAL;
			case "white": return Noise.WHITE_NOISE;
			case "cellular": return Noise.CELLULAR;
			case "blue": return Noise.BLUE_NOISE;
			default: return Noise.SIMPLEX_FRACTAL;
		}
	}

	public int parseFractal(String t) {
		t = t.toLowerCase();
		switch (t) {
			case "billow": return Noise.BILLOW;
			case "ridged": return Noise.RIDGED_MULTI;
			default: return Noise.FBM;
		}
	}

	public int parseCellular(String t) {
		t = t.toLowerCase();
		switch (t) {
			case "lookup": return Noise.NOISE_LOOKUP;
			case "distance": return Noise.DISTANCE;
			case "distance2": return Noise.DISTANCE_2;
			case "distance2add": return Noise.DISTANCE_2_ADD;
			case "distance2sub": return Noise.DISTANCE_2_SUB;
			case "distance2mul": return Noise.DISTANCE_2_MUL;
			case "distance2div": return Noise.DISTANCE_2_DIV;
			default: return Noise.CELL_VALUE;
		}
	}

	public static void main(String[] args) {
		int exitCode = new picocli.CommandLine(new HeadlessLauncher()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
		configuration.updatesPerSecond = -1;
		noise.setFrequency(frequency);
		noise.setSeed(seed);
		noise.setNoiseType(parseType(type));
		if(noise.getNoiseType() == Noise.BLUE_NOISE)
			noise.setFrequency(1f);
		noise.setFractalType(parseFractal(fractal));
		noise.setCellularReturnType(parseCellular(cellular));
		noise.setFractalOctaves(octaves);
		noise.setFoamSharpness(sharpness);
		noise.setMutation(mutation);
		new HeadlessApplication(new NoiseGen(noise, width, height, curvature, middle, debug, equalize, blurSigma, output), configuration){
			{
				try {
					mainLoopThread.join(300000L); // 5 minutes
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		return 0;
	}
}