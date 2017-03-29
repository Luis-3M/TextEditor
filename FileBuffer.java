package TextEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileBuffer extends Buffer {

	private Path savePath;
	private boolean modified = false;

	/**
	 * Save Buffer into a file.
	 * 
	 * @throws IOException
	 */

	public void save() throws IOException {
		saveAs(savePath);
	}

	/**
	 * Save file to Path.
	 * 
	 * @throws IOException
	 * 
	 */
	public void saveAs(Path path) throws IOException {
		savePath = path;
		File fileOut = new File(savePath.toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
		if (!fileOut.exists()) {
			fileOut.createNewFile();
		}
		try {
			for (int i = 0; i < getSize(); i++) {
				StringBuilder sb = getNthLine(i);
				bw.write(sb.toString());
				bw.write('\n');
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				bw.close();
			} catch (IOException ex) {
				throw ex;
			}
		}
	}

	/**
	 * Open buffer from a file.
	 * 
	 * @throws IOException
	 * 
	 */
	public void open(Path path) throws IOException {
		File fileIn = path.toFile();
		BufferedReader br = new BufferedReader(new FileReader(fileIn));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				insertString(line);
				insertChar('\n');
			}
			br.close();
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				throw ex;
			}
		}
		savePath = path;
	}

	public void insert(char c) {
		super.insertChar(c);
		modified = true;
	}
}
