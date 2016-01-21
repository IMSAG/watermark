package ch.ims.gradle.task.watermark;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.internal.file.copy.CopyAction;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.ParallelizableTask;

import java.io.File;

/**
 * Created by mfr on 21.01.2016.
 */
@ParallelizableTask
public class CopyWithWatermark extends Copy {

	private String text = "MARK";
	private int size = 50;
	private String color = "#000000";
	private int rotate = 0;


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

	@Override
	protected CopyAction createCopyAction() {
		File destinationDir = getDestinationDir();
		if (destinationDir == null) {
			throw new InvalidUserDataException("No copy destination directory has been specified, use 'into' to specify a target directory.");
		}
		return new FileCopyWithWatermarkAction(text, size, color, rotate, getFileLookup().getFileResolver(destinationDir));
	}

}
