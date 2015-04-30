package com.flowpowered.noise.module.modifier.transformer;

import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.modifier.Modifier;

public class RotatePoint extends Modifier {

    // Rotation angles
    private final double xAngle;
    private final double yAngle;
    private final double zAngle;

    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double x3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double y3Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z1Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z2Matrix;
    // An entry within the 3x3 rotation matrix used for rotating the
    // input value.
    private final double z3Matrix;

    public RotatePoint(Module source, double xAngle, double yAngle, double zAngle) {
        super(source);
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;

        final double xCos, yCos, zCos, xSin, ySin, zSin;
        xCos = Math.cos(Math.toRadians(xAngle));
        yCos = Math.cos(Math.toRadians(yAngle));
        zCos = Math.cos(Math.toRadians(zAngle));
        xSin = Math.sin(Math.toRadians(xAngle));
        ySin = Math.sin(Math.toRadians(yAngle));
        zSin = Math.sin(Math.toRadians(zAngle));

        x1Matrix = ySin * xSin * zSin + yCos * zCos;
        y1Matrix = xCos * zSin;
        z1Matrix = ySin * zCos - yCos * xSin * zSin;
        x2Matrix = ySin * xSin * zCos - yCos * zSin;
        y2Matrix = xCos * zCos;
        z2Matrix = -yCos * xSin * zCos - ySin * zSin;
        x3Matrix = -ySin * xCos;
        y3Matrix = xSin;
        z3Matrix = yCos * xCos;
    }

    public double getXAngle() {
        return xAngle;
    }

    public double getYAngle() {
        return yAngle;
    }

    public double getZAngle() {
        return zAngle;
    }

    @Override
    public double get(double x, double y, double z) {
        double nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
        double ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
        double nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
        return source.get(nx, ny, nz);
    }

}
