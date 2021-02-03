/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */

import es.datastructur.synthesizer.GuitarString;

import java.util.Arrays;
import java.util.ServiceConfigurationError;

public class GuitarHero {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        char[] keyboardArray = keyboard.toCharArray();
        GuitarString[] strings = new GuitarString[keyboardArray.length];
        for (int i = 0; i < keyboardArray.length; i++) {
            strings[i] = new GuitarString(440 * Math.pow(2, ((double) i - 24) / 12));
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                for (int i = 0; i < keyboardArray.length; i++) {
                    if (key == keyboardArray[i]) {
                        strings[i].pluck();
                    }
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (GuitarString s : strings) {
                sample += s.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString s : strings) {
                s.tic();
            }
        }
    }
}

