package org.nal.fastabreaker.business.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.nal.fastabreaker.exception.ProcessException;

public final class FastaStripper {

	public static void strip(final File file, final File destinationFolder) {
		if (file == null || !file.exists()) {
			throw new ProcessException("The file doesn't exists");
		}
		if (destinationFolder == null || !destinationFolder.exists()) {
			throw new ProcessException("The destination folder doesn't exists");
		}

		strip(file).parallelStream().forEach(fastaFile -> {
			try {
				final FileWriter newFile = new FileWriter(destinationFolder.getAbsolutePath()//
						.concat(fastaFile.getFullFileName()));
				newFile.write(fastaFile.getContent());
				newFile.close();

//				if (!newFile.createNewFile()) {
//					throw new ProcessException(MessageFormat.format( //
//							"The fasta file {0} was not created, there is another file with same name. Reestar the process", //
//							fastaFile.getFileName()));
//				}
			} catch (final IOException e) {
				throw new ProcessException(e);
			}
		});

	}

	public static List<FastaFile> strip(final File file) {

		try {
			final List<String> data = Files.readAllLines(file.toPath());
			final List<FastaFile> fastaFiles = new ArrayList<>();
			FastaFile fastaFile = null;
			final AtomicInteger lineCounter = new AtomicInteger(1);
			for (final String line : data) {
				if (line.startsWith(">")) {
					if (fastaFile != null) {
						fastaFiles.add(fastaFile);
					}
					fastaFile = FastaFile.newFastaFile(0);
					fastaFile.setFileName(getfileNameFromFirstLine(line, lineCounter));
				} else if (fastaFile != null) {
					fastaFile.addContent(line);
					fastaFile.addContent(System.lineSeparator());
				}

				lineCounter.incrementAndGet();
			}
			fastaFiles.add(fastaFile);
			return fastaFiles;
		} catch (final IOException e) {
			throw new ProcessException("Error reading file", e);
		}

	}

	private static String getfileNameFromFirstLine(final String line, final AtomicInteger lineCounter) {
		final String[] values = line.split("\\|");
		if (values.length == 3) {
			return values[1];
		}
		throw new ProcessException(MessageFormat.format(//
				"The file header at line {0} is invalid, with value [{1}]. Process aborted.", //
				lineCounter.get(), //
				line));
	}

}
