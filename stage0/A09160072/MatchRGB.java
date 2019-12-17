import ij.*;
import ij.io.Opener;
import ij.IJ;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import java.awt.Color;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.ImagePlus;

public class MatchRGB implements PlugIn {

    public void run(String args) {

        ImagePlus imp1 = IJ.getImage();
        ImageProcessor ip1 = imp1.getChannelProcessor();

        Opener opener = new Opener();
        String imageFilePath = "C:\\Users\\Arpen\\Desktop\\75a.jpg"; //path of an iamge wwth st. brhaviour
        ImagePlus imp2 = opener.openImage(imageFilePath);
        ImageProcessor ip2 = imp2.getChannelProcessor();

        double[] ip1red = normCumulativeHist(ip1, "r");
        double[] ip1green = normCumulativeHist(ip1, "g");
        double[] ip1blue = normCumulativeHist(ip1, "b");

        double[] ip2RED = normCumulativeHist(ip2, "r");
        double[] ip2GREEN = normCumulativeHist(ip2, "g");
        double[] ip2BLUE = normCumulativeHist(ip2, "b");

        int[] red = matchHistograms(ip1red, ip2RED); //function taken from the textbook
        int[] green = matchHistograms(ip1green, ip2GREEN);
        int[] blue = matchHistograms(ip1blue, ip2BLUE);

        apply(ip1, red, green, blue);
    }

    public double[] normCumulativeHist(ImageProcessor ip, String channel) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        int K = 256;
        if(channel.equals("r")) {
            ColorProcessor.setWeightingFactors(1,0,0);
        }
        else if(channel.equals("g")) {
            ColorProcessor.setWeightingFactors(0,1,0);
        }
        else if(channel.equals("b")) {
            ColorProcessor.setWeightingFactors(0,0,1);
        }
        int[] H = ip.getHistogram();
        double[] L = new double[H.length];

        L[0] = (double)(H[0])/(M*N);
        for (int i = 1; i < H.length; i++) {
            L[i] = (double)(H[i])/(M*N);
            L[i] = L[i - 1] + L[i];
        }
        return L;
    }

    public void apply(ImageProcessor ip, int[] red, int[] green,int[] blue) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        Color val;

        for(int i=0; i<M; i++) {
            for(int j=0; j<N; j++) {
                val = new Color(ip.getPixel(i, j));
                int[] arr = new int[]{red[val.getRed()], green[val.getGreen()], blue[val.getBlue()]};
                ip.putPixel(i, j, arr);
            }
        }
    }

    public int[] matchHistograms (double[] hA, double[] hR) { //function taken from the textbook
        int K = hA.length;
        int[] F = new int[K];
        for (int a = 0; a < K; a++) {
            int j = K - 1;
            do {
                F[a] = j;
                j--;
            } while (j >= 0 && hA[a] <= hR[j]);
        }
        return F;
    }


}