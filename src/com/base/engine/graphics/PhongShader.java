package com.base.engine.graphics;

import com.base.engine.core.ResourceLoader;
import com.base.engine.maths.Matrix4f;
import com.base.engine.maths.Vector3f;
import com.base.engine.utils.RenderUtil;

public class PhongShader extends Shader {

	private static final PhongShader instance = new PhongShader();

	public static PhongShader getInstance() {
		return instance;
	}

	private static Vector3f ambientLight;

	private PhongShader() {
		super();

		addVertexShader(ResourceLoader.loadShader("phongVertex.vert"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.frag"));
		compileShader();

		addUniform("transform");
		addUniform("baseColor");
		addUniform("ambientLight");
	}

	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtil.unbindTextures();

		setUniform("transform", projectedMatrix);
		setUniform("baseColor", material.getColor());
		setUniform("ambientLight", ambientLight);
	}

	public static Vector3f getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}

}
