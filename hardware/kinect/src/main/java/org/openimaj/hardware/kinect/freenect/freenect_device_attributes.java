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
package org.openimaj.hardware.kinect.freenect;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : /usr/include/stdint.h:285</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@SuppressWarnings("all")
@Library("freenect-combined") 
public class freenect_device_attributes extends StructObject {
	public freenect_device_attributes() {
		super();
	}
	/**
	 * < Next device in the linked list<br>
	 * C type : freenect_device_attributes*
	 */
	@Field(0) 
	public Pointer<freenect_device_attributes > next() {
		return this.io.getPointerField(this, 0);
	}
	/**
	 * < Next device in the linked list<br>
	 * C type : freenect_device_attributes*
	 */
	@Field(0) 
	public freenect_device_attributes next(Pointer<freenect_device_attributes > next) {
		this.io.setPointerField(this, 0, next);
		return this;
	}
	/**
	 * < Serial number of this device's camera subdevice<br>
	 * C type : const char*
	 */
	@Field(1) 
	public Pointer<Byte > camera_serial() {
		return this.io.getPointerField(this, 1);
	}
	/**
	 * < Serial number of this device's camera subdevice<br>
	 * C type : const char*
	 */
	@Field(1) 
	public freenect_device_attributes camera_serial(Pointer<Byte > camera_serial) {
		this.io.setPointerField(this, 1, camera_serial);
		return this;
	}
}