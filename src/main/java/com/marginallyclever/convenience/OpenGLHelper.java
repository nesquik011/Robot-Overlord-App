package com.marginallyclever.convenience;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

public class OpenGLHelper {
	static public int drawAtopEverythingStart(GL2 gl2) {
		IntBuffer depthFunc = IntBuffer.allocate(1);
		gl2.glGetIntegerv(GL2.GL_DEPTH_FUNC, depthFunc);
		gl2.glDepthFunc(GL2.GL_ALWAYS);
		return depthFunc.get();
	}
	
	static public void drawAtopEverythingEnd(GL2 gl2, int previousState) {
		gl2.glDepthFunc(previousState);
	}
	
	static public boolean disableLightingStart(GL2 gl2) {
		boolean lightWasOn = gl2.glIsEnabled(GL2.GL_LIGHTING);
		gl2.glDisable(GL2.GL_LIGHTING);
		return lightWasOn;
	}
	static public void disableLightingEnd(GL2 gl2,boolean lightWasOn) {

		if(lightWasOn) gl2.glEnable(GL2.GL_LIGHTING);
	}
	
}
