package net.royawesome.jlibnoise;

public class Noise {
	public static final int X_NOISE_GEN = 1619;
	public static final int Y_NOISE_GEN = 31337;
	public static final int Z_NOISE_GEN = 6971;
	public static final int SEED_NOISE_GEN = 1013;
	public static final int SHIFT_NOISE_GEN = 8;


	public static double GradientCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality){

		// Create a unit-length cube aligned along an integer boundary.  This cube
		// surrounds the input point.

		int x0 = (x> 0.0)?(int)x:(int) x-1;
		int x1 = x0 + 1;

		int y0 = (y>0.0)?(int)y:(int)y-1;
		int y1 = y0 + 1;

		int z0 = (z >0.0)?(int)z:(int)z-1;
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		if(quality == NoiseQuality.FAST){
			xs = (x - (double)x0);
			ys = (y - (double)y0);
			zs = (z - (double)z0);
		}else if( quality == NoiseQuality.STANDARD){
			xs = Utils.SCurve3 (x - (double)x0);
			ys = Utils.SCurve3 (y - (double)y0);
			zs = Utils.SCurve3 (z - (double)z0);
		}else{

			xs = Utils.SCurve5 (x - (double)x0);
			ys = Utils.SCurve5 (y - (double)y0);
			zs = Utils.SCurve5 (z - (double)z0);

		}

		// Now calculate the noise values at each vertex of the cube.  To generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0   = GradientNoise3D (x, y, z, x0, y0, z0, seed);
		n1   = GradientNoise3D (x, y, z, x1, y0, z0, seed);
		ix0  = Utils.LinearInterp (n0, n1, xs);
		n0   = GradientNoise3D (x, y, z, x0, y1, z0, seed);
		n1   = GradientNoise3D (x, y, z, x1, y1, z0, seed);
		ix1  = Utils.LinearInterp (n0, n1, xs);
		iy0  = Utils.LinearInterp (ix0, ix1, ys);
		n0   = GradientNoise3D (x, y, z, x0, y0, z1, seed);
		n1   = GradientNoise3D (x, y, z, x1, y0, z1, seed);
		ix0  = Utils.LinearInterp (n0, n1, xs);
		n0   = GradientNoise3D (x, y, z, x0, y1, z1, seed);
		n1   = GradientNoise3D (x, y, z, x1, y1, z1, seed);
		ix1  = Utils.LinearInterp (n0, n1, xs);
		iy1  = Utils.LinearInterp (ix0, ix1, ys);

		return Utils.LinearInterp (iy0, iy1, zs);

	}

	public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed){
		// Randomly generate a gradient vector given the integer coordinates of the
		// input value.  This implementation generates a random number and uses it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (
				X_NOISE_GEN    * ix
				+ Y_NOISE_GEN    * iy
				+ Z_NOISE_GEN    * iz
				+ SEED_NOISE_GEN * seed)
				& 0xffffffff;
		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = Utils.RandomVectors[(vectorIndex << 2)    ];
		double yvGradient = Utils.RandomVectors[(vectorIndex << 2) + 1];
		double zvGradient = Utils.RandomVectors[(vectorIndex << 2) + 2];

		// Set up us another vector equal to the distance between the two vectors
		// passed to this function.
		double xvPoint = (fx - (double)ix);
		double yvPoint = (fy - (double)iy);
		double zvPoint = (fz - (double)iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector.  The resulting value is gradient noise.  Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) * 2.12;
	}

	public static int IntValueNoise3D(int x, int y, int z, int seed){
		// All constants are primes and must remain prime in order for this noise
		// function to work correctly.
		int n = (X_NOISE_GEN * x + Y_NOISE_GEN * y + Z_NOISE_GEN * z + SEED_NOISE_GEN * seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;

	}
	
	public static double ValueCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality){
		 // Create a unit-length cube aligned along an integer boundary.  This cube
		  // surrounds the input point.
		  int x0 = (x > 0.0? (int)x: (int)x - 1);
		  int x1 = x0 + 1;
		  int y0 = (y > 0.0? (int)y: (int)y - 1);
		  int y1 = y0 + 1;
		  int z0 = (z > 0.0? (int)z: (int)z - 1);
		  int z1 = z0 + 1;

		  // Map the difference between the coordinates of the input value and the
		  // coordinates of the cube's outer-lower-left vertex onto an S-curve.
		  double xs = 0, ys = 0, zs = 0;
		  if(quality == NoiseQuality.FAST){
				xs = (x - (double)x0);
				ys = (y - (double)y0);
				zs = (z - (double)z0);
			}else if( quality == NoiseQuality.STANDARD){
				xs = Utils.SCurve3 (x - (double)x0);
				ys = Utils.SCurve3 (y - (double)y0);
				zs = Utils.SCurve3 (z - (double)z0);
			}else{

				xs = Utils.SCurve5 (x - (double)x0);
				ys = Utils.SCurve5 (y - (double)y0);
				zs = Utils.SCurve5 (z - (double)z0);

			}


		  // Now calculate the noise values at each vertex of the cube.  To generate
		  // the coherent-noise value at the input point, interpolate these eight
		  // noise values using the S-curve value as the interpolant (trilinear
		  // interpolation.)
		  double n0, n1, ix0, ix1, iy0, iy1;
		  n0   = ValueNoise3D (x0, y0, z0, seed);
		  n1   = ValueNoise3D (x1, y0, z0, seed);
		  ix0  = Utils.LinearInterp (n0, n1, xs);
		  n0   = ValueNoise3D (x0, y1, z0, seed);
		  n1   = ValueNoise3D (x1, y1, z0, seed);
		  ix1  = Utils.LinearInterp (n0, n1, xs);
		  iy0  = Utils.LinearInterp (ix0, ix1, ys);
		  n0   = ValueNoise3D (x0, y0, z1, seed);
		  n1   = ValueNoise3D (x1, y0, z1, seed);
		  ix0  = Utils.LinearInterp (n0, n1, xs);
		  n0   = ValueNoise3D (x0, y1, z1, seed);
		  n1   = ValueNoise3D (x1, y1, z1, seed);
		  ix1  = Utils.LinearInterp (n0, n1, xs);
		  iy1  = Utils.LinearInterp (ix0, ix1, ys);
		  return Utils.LinearInterp (iy0, iy1, zs);

	}
	
	public static double ValueNoise3D(int x, int y, int z, int seed){
		return 1.0 - ((double)IntValueNoise3D (x, y, z, seed) / 1073741824.0);

	}

}
