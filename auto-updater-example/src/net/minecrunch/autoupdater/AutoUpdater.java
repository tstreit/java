package net.minecrunch.autoupdater;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import org.apache.commons.io.FileUtils;

public class AutoUpdater {

	public static void main(String[] args) throws IOException {
		// Define files to be compared
		Path path = FileSystems.getDefault().getPath("test.rtf");
		Path path2 = FileSystems.getDefault().getPath("file", "test.rtf");

		File file = new File("test.rtf");
		File file2 = new File("file/test.rtf");

		// Get file attributes
		BasicFileAttributes attributes = Files.readAttributes(path,
				BasicFileAttributes.class);
		BasicFileAttributes attributes2 = Files.readAttributes(path2,
				BasicFileAttributes.class);

		// Get creation time from file attributes
		FileTime creationTime = attributes.creationTime();
		FileTime creationTime2 = attributes2.creationTime();

		// Display file creation time
		System.out.println(creationTime);
		System.out.println(creationTime2);

		// Compare to files dates created
		if (creationTime.equals(creationTime2)) {
			// If file creation time match, do this
			System.out.println("File are the same, not doing anything.");
		} else {
			// If file creation time do not match, do this
			System.out.println("Files are not the same. Copying file.");
			FileUtils.copyFile(file, file2);
		}
	}
}