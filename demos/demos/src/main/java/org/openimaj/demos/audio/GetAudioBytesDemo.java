package org.openimaj.demos.audio;

import org.openimaj.audio.AudioStream;
import org.openimaj.audio.SampleChunk;
import org.openimaj.audio.filters.HanningAudioProcessor;
import org.openimaj.video.xuggle.XuggleAudio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * GetAudioBytesDemo.java
 * <p>
 * A class to capture the raw audio bytes from an audio file.
 * <p>
 * Created by gberl on 8/25/2016.
 */
public class GetAudioBytesDemo {

	public GetAudioBytesDemo() {
//		openIMAJExample(false);
		coreJavaExample();
	}


	private void openIMAJExample(boolean useHannFunction) {
		AudioStream stream;
		int bytesPerFrame;
		int counter = 0;

		stream = new XuggleAudio(getClass().getResource("/org/openimaj/demos/audio/chord.wav"));
		bytesPerFrame = stream.getFormat().getJavaAudioFormat().getFrameSize();
		if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
			// If not specified, just get 1 byte at a time
			bytesPerFrame = 1;
		}

		// Set a buffer to 1024 frames (arbitrary value)
		int totalBytes = 1024 * bytesPerFrame;

		// Create an array to hold the byte data
		byte[] audioBytes = new byte[totalBytes];

		if (useHannFunction) {

			// Hanning processor on top of the main audio stream
			stream =
					new HanningAudioProcessor(stream, 1024) {
						private int counter = 0;

						@Override
						public SampleChunk processSamples(final SampleChunk sample) {
							for (Byte b : sample.getSamples()) {
								System.out.println(b);
								counter++;
							}
							System.out.println("Processed " + counter + " bytes so far");
							return sample;
						}
					};

		}


		SampleChunk s;
		while ((s = stream.nextSampleChunk()) != null) {
			for (Byte b : s.getSamples()) {
				System.out.println(b);
				counter++;
			}
		}
		System.out.println("Processed " + counter + " bytes so far");
	}


	private void coreJavaExample() {
		AudioInputStream stream;
		int totalFramesRead = 0;
		int bytesPerFrame = 0;
		try {
			stream = AudioSystem.getAudioInputStream(getClass().getResource("/org/openimaj/demos/audio/chord.wav"));
			bytesPerFrame = stream.getFormat().getFrameSize();
			if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
				// If not specified, just get 1 byte at a time
				bytesPerFrame = 1;
			}

			// Set a buffer to 1024 frames (arbitrary value)
			int totalBytes = 1024 * bytesPerFrame;

			// Create an array to hold the byte data
			byte[] audioBytes = new byte[totalBytes];

			int bytesRead;
			int framesRead;
			// Try reading some bytes from the file
			while ((bytesRead = stream.read(audioBytes)) != -1) {
				// Calculate the number of frames actually read
				framesRead = bytesRead / bytesPerFrame;
				for (Byte b : audioBytes) {
					System.out.println(b);
				}
//				System.out.println("Just read " + framesRead + " frames (" + bytesRead + " bytes)");
				totalFramesRead += framesRead;
			}

		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}

		System.out.println("Completed, read a total of " + totalFramesRead + " frames (" + (totalFramesRead * bytesPerFrame) + " bytes)");

	}

	public static void main(String[] args) {
		new GetAudioBytesDemo();
	}
}
