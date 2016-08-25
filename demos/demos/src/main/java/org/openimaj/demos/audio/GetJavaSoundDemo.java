package org.openimaj.demos.audio;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GetJavaSound.java
 *
 * An example class showing how to acquire a list of data lines that support the desired format.
 *
 * Created by gberl on 8/24/2016.
 */
public class GetJavaSoundDemo {

	public static void main(String[] args) {
		AudioFormat desiredFormat = new AudioFormat(44100, 16, 1, true, false);

		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		List<Line.Info> availableLines = new ArrayList<>();

		for (Mixer.Info mixerInfo : mixers) {
			System.out.println("Mixer: " + mixerInfo);

			Mixer m = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lines = m.getTargetLineInfo();

			for (Line.Info lineInfo : lines) {
				try {
					m.open();
					if (m.getLine(lineInfo) instanceof TargetDataLine) {
						System.out.println("\tTarget data line: " + lineInfo);
						availableLines.add(lineInfo);
					}
				} catch (LineUnavailableException e) {
					System.err.println("\tLine unavailable");
				}
			}
		}

		// Print out the available lines we've found
		System.out.println("Available Lines: ");
		for (Line.Info info : availableLines) {
			System.out.println("\t" + info);
		}
	}

}
