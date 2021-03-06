package it.uniroma1.di.simulejos.opengl;

import javax.media.opengl.GL2GL3;

public abstract class GLObject {
	protected final GL2GL3 gl;
	protected final int id;

	public GLObject(GL2GL3 gl, int id) {
		this.gl = gl;
		this.id = id;
	}
}
