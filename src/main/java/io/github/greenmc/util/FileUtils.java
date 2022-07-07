package io.github.greenmc.util;

import com.google.common.io.Files;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

/**
 * @author Despical
 * <p>
 * Created at 6.07.2022
 */
public class FileUtils {

	public static void copyURLTo(String url, File file) throws IOException {
		ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
		FileOutputStream stream = new FileOutputStream(file);

		stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

		channel.close();
		stream.close();
	}

	public static void writeIfNotExists(Path path, String string, StandardOpenOption... options) throws IOException {
		if (!Files.readLines(path.toFile(), Charsets.UTF_8).contains(string)) {
			java.nio.file.Files.write(path, (string + System.lineSeparator()).getBytes(), options);
		}
	}

	public static void deleteDirWithFiles(Path path) throws IOException {
		java.nio.file.Files.walk(path)
				.sorted(Comparator.reverseOrder())
				.map(Path::toFile)
				.forEach(File::delete);
	}
}