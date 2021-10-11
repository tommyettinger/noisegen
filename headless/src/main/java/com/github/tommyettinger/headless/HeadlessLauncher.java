package com.github.tommyettinger.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.github.tommyettinger.NoiseGen;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "noisegen", version = "NoiseGen 0.0.1",
		description = "Generate noise and write it to images",
		mixinStandardHelpOptions = true)
public class HeadlessLauncher implements Callable<Integer> {

	public static void main(String[] args) {
		int exitCode = new picocli.CommandLine(new HeadlessLauncher()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
		configuration.updatesPerSecond = -1;
		new HeadlessApplication(new NoiseGen(), configuration);
		return 0;
	}
}