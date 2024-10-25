package ch.ims.gradle.task.watermark;

import org.gradle.api.internal.file.CopyActionProcessingStreamAction;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.copy.CopyAction;
import org.gradle.api.internal.file.copy.CopyActionProcessingStream;
import org.gradle.api.internal.file.copy.FileCopyDetailsInternal;
import org.gradle.api.tasks.WorkResult;
import org.gradle.api.tasks.WorkResults;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mfr on 21.01.2016.
 */
public class FileCopyWithWatermarkAction implements CopyAction {
	private final FileResolver fileResolver;

	private final String text;
	private final String color;
	private final int size;
	private final int rotate;
	private final BigDecimal opacity;

	public FileCopyWithWatermarkAction(String text, int size, String color, int rotate, BigDecimal opacity, FileResolver fileResolver) {
		this.fileResolver = fileResolver;
		this.text = text;
		this.size = size;
		this.color = color;
		this.rotate = rotate;
		this.opacity = opacity;
	}

	public WorkResult execute(CopyActionProcessingStream stream) {
		FileCopyWithWatermarkAction.FileCopyDetailsInternalAction action = new FileCopyWithWatermarkAction.FileCopyDetailsInternalAction();
		stream.process(action);
		return WorkResults.didWork(true);
	}

	private class FileCopyDetailsInternalAction implements CopyActionProcessingStreamAction {
		private FileCopyDetailsInternalAction() {
		}

		public void processFile(FileCopyDetailsInternal details) {
			File target = FileCopyWithWatermarkAction.this.fileResolver.resolve(details.getRelativePath().getPathString());
			addTextWatermark(details.getFile(),target);
		}
	}

	void addTextWatermark(File sourceImageFile, File destImageFile) {
		try {
			String fileName = sourceImageFile.getName();
			String format = fileName.substring(fileName.lastIndexOf(".") + 1);
			System.out.println("Add watermark to file " + fileName);
			BufferedImage sourceImage = ImageIO.read(sourceImageFile);
			Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			// initializes necessary graphic properties
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity.floatValue());
			g2d.setComposite(alphaChannel);
			AffineTransform orig = g2d.getTransform();
			g2d.setColor(Color.decode(color));
			g2d.setFont(new Font("Arial", Font.BOLD, size));
			FontMetrics fontMetrics = g2d.getFontMetrics();
			Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);
			// calculates the coordinate where the String is painted
			int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
			int centerY = (sourceImage.getHeight() + (int) (rect.getHeight() / 2)) / 2;

			// rotate the watermark
			g2d.rotate(Math.toRadians(rotate), (sourceImage.getWidth() / 2), (sourceImage.getHeight() / 2));

			// paints the textual watermark
			g2d.drawString(text, centerX, centerY);
			g2d.setTransform(orig);
			ImageIO.write(sourceImage, format, destImageFile);
			g2d.dispose();

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
