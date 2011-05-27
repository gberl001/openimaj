/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.image.feature.local.detector.dog.extractor;


import org.openimaj.feature.OrientedFeatureVector;
import org.openimaj.image.FImage;
import org.openimaj.image.feature.local.descriptor.gradient.GradientFeatureProvider;
import org.openimaj.image.feature.local.descriptor.gradient.GradientFeatureProviderFactory;
import org.openimaj.image.feature.local.extraction.ScaleSpaceImageExtractorProperties;


/**
 * <p>
 * Class capable of extracting local descriptors from a circular region
 * in an image defined by its scale and centre. The actual feature 
 * extracted is determined by the {@link GradientFeatureProvider} that
 * is provided by the {@link GradientFeatureProviderFactory} set during
 * construction.
 * </p>
 * <p>
 * The GradientFeatureExtractor first calculates the dominant orientation
 * of the image patch described by the {@link ScaleSpaceImageExtractorProperties}
 * and then iterates over the pixels in an oriented square, centered on the
 * interest point, passing the gradient and magnitude values of the respective
 * pixel to the {@link GradientFeatureProvider}.
 * </p>
 * <p>
 * The size of the sampling square, relative to scale is set by a single parameter,
 * {@link #magnification}. For some types of feature provider, this number
 * might need to be set based on the internal settings of the provider. For example,
 * with a {@link SIFTFeatureProvider} this will probably be set to a constant multiplied
 * by the number of spatial bins of the feature. For SIFT, this constant is typically 
 * around 3, so with a standard 4-spatial binned SIFT provider, the {@link #magnification}
 * factor of the extractor should be about 12.
 * </p>
 * 
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>
 *
 */
public class GradientFeatureExtractor implements ScaleSpaceFeatureExtractor {
	DominantOrientationExtractor dominantOrientationExtractor;
	
	GradientFeatureProviderFactory factory;
	
	/**
	 * The magnification factor determining the size of the sampling
	 * region relative to the scale of the interest point.
	 */
	protected float magnification = 12;
	
	public GradientFeatureExtractor(GradientFeatureProviderFactory factory) {
		this(new DominantOrientationExtractor(), factory);
	}
	
	public GradientFeatureExtractor(DominantOrientationExtractor dominantOrientationExtractor, GradientFeatureProviderFactory factory) {
		this.dominantOrientationExtractor = dominantOrientationExtractor;
		this.factory = factory;
	}
	
	public GradientFeatureExtractor(DominantOrientationExtractor dominantOrientationExtractor, GradientFeatureProviderFactory factory, float magnification) {
		this(dominantOrientationExtractor, factory);
		this.magnification = magnification;
	}

	@Override
	public OrientedFeatureVector[] extractFeature(ScaleSpaceImageExtractorProperties<FImage> properties) {
		float [] dominantOrientations = dominantOrientationExtractor.extractFeatureRaw(properties);

		OrientedFeatureVector[] ret = new OrientedFeatureVector[dominantOrientations.length];

		for (int i=0; i<dominantOrientations.length; i++) {
			ret[i] = createFeature(properties.x, properties.y, properties.scale, dominantOrientations[i]);
		}

		return ret;
	}

	/*
	 * Iterate over the pixels in a sampling patch around the given feature coordinates
	 * and pass the information to a feature provider that will extract the relevant
	 * feature vector.
	 */
	protected OrientedFeatureVector createFeature(float fx, float fy, float scale, float orientation) {
		//create a new feature provider and initialise it with the dominant orientation
		GradientFeatureProvider sfe = factory.newProvider();
		sfe.setPatchOrientation(orientation);
		
		//the integer coordinates of the patch
		int ix = Math.round(fx);
		int iy = Math.round(fy);

		float sin = (float) Math.sin(orientation);
		float cos = (float) Math.cos(orientation);

		//get the amount of extra sampling outside the unit square requested by the feature
		float oversampling = sfe.getOversamplingAmount();
		
		//this is the size of the unit bounding box of the patch in the image in pixels
		float boundingBoxSize = magnification * scale;
		
		//the amount of extra sampling per side in pixels
		float extraSampling = oversampling * boundingBoxSize;
		
		//the actual sampling area is bigger than the boundingBoxSize by an extraSampling on each side
		float samplingBoxSize = extraSampling + boundingBoxSize + extraSampling;
		
		//In the image, the box (with sides parallel to the image frame) that contains the
		//sampling box is:
		float orientedSamplingBoxSize = Math.abs(sin * samplingBoxSize) + Math.abs(cos * samplingBoxSize);
		
		//now half the size and round to an int so we can iterate
		int orientedSamplingBoxHalfSize = Math.round(orientedSamplingBoxSize / 2.0f);

		//get the images and their size
		FImage mag = dominantOrientationExtractor.getOriHistExtractor().currentGradient;
		FImage ori = dominantOrientationExtractor.getOriHistExtractor().currentOrientation;
		int width = mag.width;
		int height = mag.height;
		
		//now pass over all the pixels in the image that *might* contribute to the sampling area
		for (int y = -orientedSamplingBoxHalfSize; y <= orientedSamplingBoxHalfSize; y++) {
			for (int x = -orientedSamplingBoxHalfSize; x <= orientedSamplingBoxHalfSize; x++) {
				int px = x + ix;
				int py = y + iy;
				
				//check if the pixel is in the image bounds; if not ignore it
				if (px >= 0 && px < width && py >= 0 && py < height) {
					//calculate the actual position of the sample in the patch coordinate system
					float sx = 0.5f + ((-sin * y + cos * x) - (fx - ix)) / boundingBoxSize;
					float sy = 0.5f + ((cos * y + sin * x) - (fy - iy)) / boundingBoxSize;
					
					//if the pixel is in the bounds of the sampling area then add it
					if (sx > -oversampling && sx < 1 + oversampling && sy > -oversampling && sy < 1 + oversampling) {
						sfe.addSample(sx, sy, mag.pixels[py][px], ori.pixels[py][px]);
					}
				}
			}
		}

		return sfe.getFeatureVector();
	}
}
