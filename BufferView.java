package TextEditor;

import java.io.IOException;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class BufferView {

	private final int WIDTH = 80, HEIGHT = 30;

	public BufferView(FileBuffer filebuf) throws IOException {
		FileBuffer fb = filebuf;
		fb.getCursor();
		Screen screen = TerminalFacade.createScreen(new SwingTerminal(WIDTH, HEIGHT));
		ScreenWriter writer = new ScreenWriter(screen);

		screen.startScreen();
		showMenu(writer, screen);
		screen.completeRefresh();

		while (true) {
			Key k = screen.readInput();
			screen.refresh();
			keyEvent(k, screen, writer, fb);

		}
	}

	private void screenCursorUpdate(Screen screen, FileBuffer fb) {
		screen.setCursorPosition(fb.getCursor().getY(), fb.getCursor().getX());
	}

	private void reDraw(ScreenWriter writer, FileBuffer fb) {
		for (int i = 0; i < fb.getSize(); i++) {
			String line = fb.getNthLine(i).toString();
			writer.drawString(0, i, line);
		}
	}

	private void updateText(FileBuffer fb, Screen screen, ScreenWriter writer) {
		for (int i = 0; i < fb.getSize(); i++) {
			if (fb.getCursor().getY() == WIDTH) {
				fb.foldLine();
				fb.moveToBetweenLine();
				screen.clear();
			}
			String line = fb.getNthLine(i).toString();
			writer.drawString(0, i, line);
		}
	}

	public void showMenu(ScreenWriter writer, Screen screen) {
		writer.drawString((WIDTH / 3) - 1, HEIGHT / 3, "PRESS <ENTER> TO START");
		screen.completeRefresh();
		writer.drawString((WIDTH / 3) - 1, (HEIGHT / 3) + 1, "PRESS <ESCAPE> TO EXIT");
		screen.completeRefresh();
		writer.drawString((WIDTH / 3) - 1, (HEIGHT / 3) + 2, "PRESS <PAGE DOWN> TO SAVE THE DOCUMENT");
		screen.completeRefresh();
	}

	public void keyEvent(Key k, Screen screen, ScreenWriter writer, FileBuffer fb) throws IOException {

		if (k != null) {
			switch (k.getKind()) {
			case ArrowDown: {
				fb.moveNextLine();
				screenCursorUpdate(screen, fb);
				screen.completeRefresh();
				break;
			}
			case ArrowUp: {
				fb.movePreviousLine();
				screenCursorUpdate(screen, fb);
				screen.completeRefresh();
				break;
			}
			case ArrowLeft: {
				fb.moveToPreviousCursor();
				screenCursorUpdate(screen, fb);
				screen.completeRefresh();
				break;
			}
			case ArrowRight: {
				fb.moveToNextCursor();
				screenCursorUpdate(screen, fb);
				screen.completeRefresh();
				break;
			}
			case Enter: {
				fb.insertLine();
				fb.moveToBetweenLine();
				screenCursorUpdate(screen, fb);
				screen.clear();
				reDraw(writer, fb);
				screen.completeRefresh();
				break;
			}
			case Backspace: {
				fb.deleteChar();
				screen.clear();
				reDraw(writer, fb);
				screenCursorUpdate(screen, fb);
				screen.completeRefresh();
				break;
			}

			case PageDown: {
				fb.save();
				break;
			}

			case Home: {
				fb.moveToStartLine();
				screenCursorUpdate(screen, fb);
				break;
			}
			case End: {
				fb.moveToEndLine();
				screenCursorUpdate(screen, fb);
				break;
			}
			case Escape:
				screen.stopScreen();
				System.exit(1);
				break;
			default:
				fb.insert(k.getCharacter());
				updateText(fb, screen, writer);
				screenCursorUpdate(screen, fb);
			}
		}
	}
}
