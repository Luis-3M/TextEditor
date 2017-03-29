package TextEditor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Main class
 * 
 * @author Luis3M
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
		Path path = FileSystems.getDefault().getPath("INSERT YOUR ABSOLUTE FILE'S PATH");
		FileBuffer fb = new FileBuffer();
		fb.open(path);
		new BufferView(fb);
	}
}
