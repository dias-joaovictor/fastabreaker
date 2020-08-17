package org.nal.fastabreaker.business.service;

import java.io.Serializable;

public class FastaFile implements Serializable {

	private static final long serialVersionUID = 2335442076418625655L;

	private String fileName;

	private StringBuilder content;

	public static FastaFile newFastaFile(final int maxSize) {
		final FastaFile fastaFile = new FastaFile();
		fastaFile.content = new StringBuilder(maxSize);
		return fastaFile;
	}

	private FastaFile() {
		super();
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getFullFileName() {
		return System.getProperty("file.separator")//
				.concat(this.fileName)//
				.concat(".fasta");
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return this.content.toString();
	}

	public void addContent(final String content) {
		this.content.append(content);
	}

	@Override
	public String toString() {
		return String.format("FastaFile [fileName=%s]", this.fileName);
	}

}
