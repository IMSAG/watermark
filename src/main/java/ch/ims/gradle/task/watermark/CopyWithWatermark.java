package ch.ims.gradle.task.watermark;

import org.gradle.api.internal.file.copy.CopyAction;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Input;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by mfr on 21.01.2016.
 */
public class CopyWithWatermark extends Copy {

	@Input
	public String text = "MARK";
	@Input
	public int size = 50;
	@Input
	public String color = "#000000";
	@Input
	public int rotate = 0;
	@Input
	public BigDecimal opacity = BigDecimal.valueOf(0.3);

	public String getText() {
		return text;
	}

	public int getSize() {
		return size;
	}

	public String getColor() {
		return color;
	}

	public int getRotate() {
		return rotate;
	}

	public BigDecimal getOpacity() {
		return opacity;
	}

	@Override
	protected @NotNull CopyAction createCopyAction() {
		File destinationDir = getDestinationDir();
		return new FileCopyWithWatermarkAction(text, size, color, rotate, opacity, getFileLookup().getFileResolver(destinationDir));
	}

}
