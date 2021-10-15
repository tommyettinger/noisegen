# noisegen
A command-line tool to generate different types of noise as images.

# Usage
Run one of the releases, either the JAR using `java -jar noisegen-0.1.0.jar ...options...`
or the Win64-specific native-image release with `noisegen-win64-0.1.0.exe ...options...`.
The native-image release starts up very quickly on Windows; the JAR doesn't exactly do that.
The options can be... uh... let's just see the nice output of -h for help.

```
Usage: noisegen [-hV] [-c=<cellular>] [-C=<curvature>] [-f=<frequency>]
                [-F=<fractal>] [-H=<height>] [-m=<mutation>] [-M=<middle>]
                [-o=<output>] [-O=<octaves>] [-s=<seed>] [-S=<sharpness>]
                [-t=<type>] [-W=<width>]
Generate noise and write it to an image file.
  -c, --cellular=<cellular> The cellular return type to use for the cellular
                              type; one of: value, lookup, distance, distance2,
                              distance2add, distance2mul, distance2div.
  -C, --curvature=<curvature>
                            How steep the transition should be from black to
                              white; must be positive.
  -f, --frequency=<frequency>
                            The frequency of the noise, with high frequency
                              changing rapidly.
  -F, --fractal=<fractal>   The fractal mode to use for most noise types; one
                              of: fbm, billow, ridged.
  -h, --help                Show this help message and exit.
  -H, --height=<height>     The height of the resulting image.
  -m, --mutation=<mutation> The extra 'spatial' value used by mutant noise; can
                              be any float.
  -M, --middle=<middle>     When curvature is not 1.0, this determines where
                              the noise starts to turn its curve; must be
                              between 0 and 1, inclusive.
  -o, --output=<output>     The name and/or path for the output file.
  -O, --octaves=<octaves>   The amount of octaves to use; more increases detail.
  -s, --seed=<seed>         The seed that determines how the noise will form
                              using the given parameters.
  -S, --sharpness=<sharpness>
                            The sharpness multiplier for foam and mutant noise;
                              higher than one means more extreme.
  -t, --type=<type>         The type of noise to generate; one of: simplex,
                              perlin, cubic, foam, honey, mutant, value, white,
                              cellular.
  -V, --version             Print version information and exit.
  -W, --width=<width>       The width of the resulting image.
```

An example of a simple command line might be:
```
java -jar noisegen-0.1.0.jar -t perlin -W 1920 -H 1080 -o PerlinNoise.png
```

This sets the type to perlin (this is "Classic Perlin," before Ken Perlin created Simplex Noise), the
width and height to 1920x1080, and the name of the output file to PerlinNoise.png .

A more involved command line:
```
java -jar noisegen-0.1.0.jar -t foam -F ridged -f 0.02 -W 1920 -H 1080 -s 1 -O 4 -o FoamNoise.png
```

This sets the type to foam (a high quality and organic-seeming noise), the fractal type to ridged
(which creates lines of bright color on a darker background), the frequency to 0.02 (lower than the
default of 0.1.025, or 1.0/32.0), the width and height to 1920x1080, the seed to 1 (so all calls with
the same parameters and same seed will produce the same output), the octaves to 4 (increasing detail
over the default of 3 octaves), and the output file to FoamNoise.png .


# Thanks
This project uses the great [PicoCLI](https://picocli.info/) library for clean command-line handling.
The Win64 native-image version would not be possible if not for ByerN's work
getting Graal to play nice with libGDX; I have copied some configuration
and built libraries here from [his example repo](https://github.com/ByerN/libgdx-graalvm-example).
Of course, this uses [libGDX](https://libgdx.com/); I can't get by without it.

# Notes
To build the native EXE, I drop noisegen-0.1.0.jar into `graalvm-env/`, run the appropriate Visual
Studio variable setter (`vcvars64.bat`), then `build_native.bat`. This is all ByerN's work, big thanks!