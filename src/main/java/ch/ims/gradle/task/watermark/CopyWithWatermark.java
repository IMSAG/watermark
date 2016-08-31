package ch.ims.gradle.task.watermark;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.internal.file.copy.CopyAction;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.ParallelizableTask;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by mfr on 21.01.2016.
 */
@ParallelizableTask
public class CopyWithWatermark extends Copy {

	private String text = "MARK";
	private int size = 50;
	private String color = "#000000";
	private int rotate = 0;
	private BigDecimal opacity = new BigDecimal(0.3);


	@Input
	public void setText(String text){
		this.text = text;
	}

	@Input
	public void setSize(int size) {
		this.size = size;
	}

	@Input
	public void setColor(String color) {
		this.color = color;
	}

	@Input
	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	@Input
	public void setOpacity(BigDecimal opacity) {
		this.opacity = opacity;
	}

	@Override
	protected CopyAction createCopyAction() {
		File destinationDir = getDestinationDir();
		if (destinationDir == null) {
			throw new InvalidUserDataException("No copy destination directory has been specified, use 'into' to specify a target directory.");
		}
		return new FileCopyWithWatermarkAction(text, size, color, rotate, opacity, getFileLookup().getFileResolver(destinationDir));
	}

}
