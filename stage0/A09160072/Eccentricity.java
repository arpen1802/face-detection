import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Eccentricity implements PlugIn {

    public void run(String arg) {
        ImagePlus imp = IJ.getImage();
        ImageProcessor ip = imp.getProcessor();

        double m02 = centralMoment(ip, 0,2);
        double m20 = centralMoment(ip, 2, 0);
        double m11 = centralMoment(ip, 1, 1 );

        double val = Math.sqrt(Math.pow(m02-m20,2) + 4*m11*m11);
        double ecc = (m02+m20 + val)/ (m02+m20 - val);

        IJ.log("" + ecc);
    }

    double moment(ImageProcessor I, int p, int q) { // taken from the textbook
        double Mpq = 0.0;
        for (int v = 0; v < I.getHeight(); v++) {
            for (int u = 0; u < I.getWidth(); u++) {
                if (I.getPixel(u, v) > 0) {
                    Mpq+= Math.pow(u, p) * Math.pow(v, q);
                    }
                }
            }
        return Mpq;
    }

    double centralMoment(ImageProcessor I, int p, int q) { // taken from the textbook
        double m00 = moment(I, 0, 0); // region area
        double xCtr = moment(I, 1, 0) / m00;
        double yCtr = moment(I, 0, 1) / m00;
        double cMpq = 0.0;
        for (int v = 0; v < I.getHeight(); v++) {
            for (int u = 0; u < I.getWidth(); u++) {
                if (I.getPixel(u, v) > 0) {
                    cMpq+= Math.pow(u-xCtr, p) * Math.pow(v-yCtr, q);
                    }
                }
            }
        return cMpq;
    }

}
