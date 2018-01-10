package cop5618;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FJBufferedImage extends BufferedImage {

	/** Constructors */

	public FJBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}

	public FJBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
	}

	public FJBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
	}

	/**
	 * Creates a new FJBufferedImage with the same fields as source.
	 * 
	 * @param source
	 * @return
	 */
	public static FJBufferedImage BufferedImageToFJBufferedImage(BufferedImage source) {
		Hashtable<String, Object> properties = null;
		String[] propertyNames = source.getPropertyNames();
		if (propertyNames != null) {
			properties = new Hashtable<String, Object>();
			for (String name : propertyNames) {
				properties.put(name, source.getProperty(name));
			}
		}
		return new FJBufferedImage(source.getColorModel(), source.getRaster(), source.isAlphaPremultiplied(),
				properties);
	}

	@Override
	public void setRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize) {
		/**** IMPLEMENT THIS METHOD USING PARALLEL DIVIDE AND CONQUER *****/
		ForkJoinPool fjpool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		List<SetRGB> eList = new 
				ArrayList<>();
		for (int row = xStart; row < this.getHeight(); row++) {
			SetRGB event = new SetRGB(this, row, yStart, getWidth(), 1, rgbArray, row, scansize);
			eList.add(event);
		}
		fjpool.invokeAll(eList);
	}
	
	@Override
	public int[] getRGB(int xStart, int yStart, int w, int h, int[] rgbArray, int offset, int scansize) {
		/**** IMPLEMENT THIS METHOD USING PARALLEL DIVIDE AND CONQUER *****/
		ForkJoinPool fjpool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		List<ExtractRGB> eList = new 
				ArrayList<>();
		for (int row = 0; row < this.getHeight(); row++) {
			ExtractRGB event = new ExtractRGB(this, row, yStart, getWidth(), 1, rgbArray, row, scansize);
			eList.add(event);
		}
		fjpool.invokeAll(eList);
		return rgbArray;
	}

	
	
	
	class SetRGB extends RecursiveAction implements Callable<Void> {
		private BufferedImage image;
		private int[] rgbArray;
		private int xStart;
		private int yStart;
		private int offset;
		private int scansize;
		private int w;
		private int h;

		public SetRGB(BufferedImage image, int xStart, int yStart, int w, int h, int[] rgbArray, int offset,
				int scansize) {
			this.image = image;
			this.rgbArray = rgbArray;
			this.xStart = xStart;
			this.yStart = yStart;
			this.w = w;
			this.h = h;
			this.scansize = scansize;
			this.offset = offset;
		}

		@Override
		protected void compute() {
			for (int j = yStart; j < w; j++) {
				int pix = rgbArray[xStart*w + j];
				setRGB(j, xStart, pix);
			}
		}

		@Override
		public Void call() throws Exception {
			compute();
			return null;
		}
	}

	
	class ExtractRGB extends RecursiveAction implements Callable<Void> {
		private BufferedImage image;
		private int[] rgbArray;
		private int xStart;
		private int yStart;
		private int offset;
		private int scansize;
		private int w;
		private int h;

		public ExtractRGB(BufferedImage image, int xStart, int yStart, int w, int h, int[] rgbArray, int offset,
				int scansize) {
			this.image = image;
			this.rgbArray = rgbArray;
			this.xStart = xStart;
			this.yStart = yStart;
			this.w = w;
			this.h = h;
			this.scansize = scansize;
			this.offset = offset;
		}

		@Override
		protected void compute() {
			int row = xStart;
			for (int col = 0; col < image.getWidth(); col++) {
				rgbArray[(image.getWidth()*row) + col] = image.getRGB(col, row);
			}
		}

		@Override
		public Void call() throws Exception {
			compute();
			return null;
		}
	}
}
