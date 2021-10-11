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
		description = "Generate noise and write it to images",
		mixinStandardHelpOptions = true)
public class HeadlessLauncher implements Callable<Integer> {

	public Noise noise = new Noise();
	@CommandLine.Option(names = {"-f", "--frequency"}, description = "The frequency of the noise, with high frequency changing rapidly.", defaultValue = "0.03125")
	public float frequency = 0x1p-5f;
	@CommandLine.Option(names = {"-s", "--seed"}, description = "The seed that determines how the noise will form using the given parameters.")
	public int seed = (int) MathUtils.random(0xFFFFFFFFL);

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
		new HeadlessApplication(new NoiseGen(noise), configuration){
			{
				try {
					mainLoopThread.join(30000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		return 0;
	}
}