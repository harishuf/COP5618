package cop5618;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColorHistEq {

    static String[] labels = { "getRGB", "convert to HSB", "create brightness map", "probability array",
			"parallel prefix", "equalize pixels", "setRGB" };
	static int binCount = 256;

	static Timer colorHistEq_serial(BufferedImage image, BufferedImage newImage) {
		Timer times = new Timer(labels);
		ColorModel colorModel = ColorModel.getRGBdefault();
		int w = image.getWidth();
		int h = image.getHeight();
		int pixels = w * h;
		int bins = Math.min(binCount, pixels);
		times.now();
		int[] sourceArray = image.getRGB(0, 0, w, h, new int[pixels], 0, w);
		times.now();
		Object[] HSBArray = Arrays.stream(sourceArray).mapToObj(pixel -> Color.RGBtoHSB(colorModel.getRed(pixel), colorModel.getGreen(pixel),colorModel.getBlue(pixel),new float[3])).toArray();
		times.now();
		Map<Integer, Long> bMap = Arrays.stream(HSBArray).mapToInt(pixel -> Math.min((int)(((float[])pixel)[2]*bins), bins-1)).boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		int[] histogram = IntStream.range(0, bins).map(index -> bMap.containsKey(index) ? bMap.get(index).intValue() : 0).toArray();
		times.now();
		double[] pbArray = Arrays.stream(histogram).mapToDouble(bin -> (double)bin / (double)pixels).toArray();
		times.now();
		Arrays.parallelPrefix(pbArray, (x,y)->x+y);
		times.now();
		int[] equalArray = Arrays.stream(HSBArray).mapToInt(pixel -> Color.HSBtoRGB(((float[])pixel)[0],((float[])pixel)[1],(float)pbArray[Math.min((int) (((float[])pixel)[2]*bins), bins-1)])).toArray();
		times.now();
		newImage.setRGB(0, 0, w, h, equalArray, 0, w);
		times.now();
    	return times;
	}



	static Timer colorHistEq_parallel(FJBufferedImage image, FJBufferedImage newImage) {
		Timer times = new Timer(labels);
		ColorModel colorModel = ColorModel.getRGBdefault();
		int w = image.getWidth();
		int h = image.getHeight();
		int pixels = w * h;
		int bins = Math.min(binCount, pixels);
		times.now();
		int[] sourceArray = image.getRGB(0, 0, w, h, new int[pixels], 0, w);
		times.now();
		Object[] HSBArray = Arrays.stream(sourceArray).parallel().mapToObj(pixel -> Color.RGBtoHSB(colorModel.getRed(pixel),colorModel.getGreen(pixel),colorModel.getBlue(pixel),new float[3])).toArray();
		times.now();
		Map<Integer, Long> bMap = Arrays.stream(HSBArray).parallel().mapToInt(pixel -> Math.min((int)(((float[])pixel)[2]*bins), bins-1)).boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		int[] histogram = IntStream.range(0, bins).parallel().map(index -> bMap.containsKey(index) ? bMap.get(index).intValue() : 0).toArray();
		times.now();
		double[] pbArray = Arrays.stream(histogram).parallel().mapToDouble(bin -> (double)bin / (double)pixels).toArray();
		times.now();
		Arrays.parallelPrefix(pbArray, (x,y)->x+y);
		times.now();
		int[] equalArray = Arrays.stream(HSBArray).parallel().mapToInt(pixel -> Color.HSBtoRGB(((float[])pixel)[0],((float[])pixel)[1],(float)pbArray[Math.min((int) (((float[])pixel)[2]*bins), bins-1)])).toArray();
		times.now();
		newImage.setRGB(0, 0, w, h, equalArray, 0, w);
		times.now();
		return times;
	}

}
