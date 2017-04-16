package com.flowpowered.noise.module.source;

import static org.junit.Assert.*;

import org.junit.Test;

public class PerlinTest {

	/**
	 * Sentry test to make sure future modifications
	 * wont fudge this method
	 */
	@Test
	public void testGetMaxValue() {
		Perlin perlin = new Perlin();
		perlin.setPersistence(0.714);
		perlin.setOctaveCount(8);
		
		double max = 0;
		for (int i = 0; i < perlin.getOctaveCount(); i++) {
			max += Math.pow(perlin.getPersistence(), i);
		}
		double methodVal = perlin.getMaxValue();
		assertEquals(max, methodVal, 5e-16);
	}

}
