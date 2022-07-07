package io.github.greenmc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Despical
 * <p>
 * Created at 6.07.2022
 */
public class Git {

	public static void gitInit(Path directory) throws IOException, InterruptedException {
		runCommand(directory, "git", "init");
	}

	public static void gitStage(Path directory) throws IOException, InterruptedException {
		runCommand(directory, "git", "add", "-A");
	}

	public static void gitCommit(Path directory, String message) throws IOException, InterruptedException {
		runCommand(directory, "git", "commit", "-m", message);
	}

	public static void gitPush(Path directory) throws IOException, InterruptedException {
		runCommand(directory, "git", "push");
	}

	public static void gitClone(Path directory, String originUrl) throws IOException, InterruptedException {
		runCommand(directory.getParent(), "git", "clone", originUrl, directory.getFileName().toString());
	}

	public static void runCommand(Path directory, String... command) throws IOException, InterruptedException {
		if (!Files.exists(directory)) {
			throw new RuntimeException("Can't run the command in non-existing directory '" + directory + "'");
		}

		ProcessBuilder processBuilder = new ProcessBuilder().command(command).directory(directory.toFile());
		Process process = processBuilder.start();
		int exit = process.waitFor();

		if (exit == 0) {
			System.out.println("Process finished successfully.");
		}
	}
}