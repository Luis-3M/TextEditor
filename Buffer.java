package TextEditor;

import java.util.LinkedList;
import java.lang.StringBuilder;

class Buffer {

	private Cursor cursor;
	private LinkedList<StringBuilder> lines;

	Buffer() {
		this.cursor = new Cursor();
		this.lines = new LinkedList<StringBuilder>();

		lines.add(new StringBuilder(""));
	}

	/**
	 * Get Cursor Position.
	 * 
	 * @return cursor
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Set Cursor Position.
	 * 
	 * @param cursor
	 */
	public void setCursor(Cursor cursor) {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorX >= 0 && cursorX <= getSize()) {
			int linesLength = lines.get(cursorY).length();

			if (cursorY >= 0 && cursorY <= linesLength) {
				cursor.setX(cursorX);
				cursor.setY(cursorY);
			}
		}
	}

	/**
	 * Set cursor to previous position.
	 */
	public void moveToPreviousCursor() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorX > 0 && (cursorY - 1) == 0) {
			cursor.setX(cursorX - 1);
			cursor.setY(lines.get(cursorX - 1).length());
		} else if (cursorY - 1 > 0) {
			cursor.setY(cursorY - 1);
		}
	}

	/**
	 * Set cursor to next position.
	 */
	public void moveToNextCursor() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorY + 1 <= lines.get(cursorX).length()) {
			cursor.setY(cursorY + 1);
		} else if (cursorX + 1 < getSize()) {
			cursor.setX(cursorX + 1);
			cursor.setY(0);
		}
	}

	/**
	 * Set cursor to the previous line.
	 */
	public void movePreviousLine() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorX > 0) {
			cursor.setX(cursorX - 1);
			int previousLength = lines.get(cursorX - 1).length();

			if (cursorY < previousLength) {
				cursor.setY(cursorY);
			} else
				cursor.setY(previousLength);
		}
	}

	/**
	 * Set cursor to the next line.
	 */
	public void moveNextLine() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorX + 1 < getSize()) {
			cursor.setX(cursorX + 1);
			int nextLength = lines.get(cursorX + 1).length();

			if (cursorY < nextLength) {
				cursor.setY(cursorY);
			} else
				cursor.setY(nextLength);
		}
	}

	/**
	 * Set cursor to between lines.
	 */
	public void moveToBetweenLine() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorX + 1 < getSize()) {
			cursor.setX(cursorX);
			int nextLength = lines.get(cursorX + 1).length();

			if (cursorY < nextLength) {
				cursor.setY(cursorY);
			} else
				cursor.setY(nextLength);
		}
	}

	/**
	 * Set cursor to the beginning of the line.
	 */
	public void moveToStartLine() {

		getCursor().setY(0);
	}

	/**
	 * Set cursor to the end of the line.
	 */
	public void moveToEndLine() {

		String line = getNthLine(getCursor().getX()).toString();
		getCursor().setY(line.length());
	}

	/**
	 * Get buffer.
	 * 
	 * @return LinkedList
	 */
	public LinkedList<StringBuilder> getAllLines() {
		return lines;
	}

	/**
	 * Get buffer size.
	 * 
	 * @return int
	 */
	public int getSize() {
		return lines.size();
	}

	/**
	 * Get nth line.
	 * 
	 * @param pos
	 * @return StringBuilder
	 */
	public StringBuilder getNthLine(int pos) {
		if (pos >= 0 && pos < lines.size())
			return lines.get(pos);
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Insert string into the buffer.
	 * 
	 * @param str
	 */
	public void insertString(String str) {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (!str.contains("\n")) {
			cursor.setY(cursorY + str.length());
			StringBuilder currentLine = lines.get(cursorX);
			currentLine.insert(cursorY, str);
		} else
			throw new IllegalArgumentException();

	}

	/**
	 * Insert line (\n).
	 * 
	 */
	public void insertLine() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		StringBuilder currentLine = lines.get(cursorX);
		StringBuilder nextLine = new StringBuilder(new String(currentLine.substring(cursorY, currentLine.length())));

		currentLine.delete(cursorY, currentLine.length());
		lines.add(cursorX + 1, nextLine);
		cursor.setX(cursorX + 1);
		cursor.setY(0);
	}

	/**
	 * Delete line from the buffer.
	 * 
	 */
	private void deleteLine() {
		int cursorX = cursor.getX();

		StringBuilder previousLine = lines.get(cursorX - 1);
		int previousLineLength = previousLine.length();
		StringBuilder currentLine = lines.get(cursorX);
		previousLine.insert(previousLineLength, currentLine);
		lines.remove(cursorX);

		cursor.setX(cursorX - 1);
		cursor.setY(previousLineLength);
	}

	/**
	 * Folds line to the next position.
	 */
	public void foldLine() {
		int cursorX = cursor.getX();

		StringBuilder currentLine = lines.get(cursorX);
		StringBuilder nextLine = new StringBuilder(new String(currentLine.substring(currentLine.lastIndexOf(" ") + 1)));
		currentLine.delete(currentLine.lastIndexOf(" "), currentLine.length());
		lines.add(cursorX + 1, nextLine);
		cursor.setX(cursorX + 1);
		cursor.setY(nextLine.length());
	}

	/**
	 * Insert char into the String.
	 * 
	 * @param character
	 */
	public void insertChar(Character character) {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (!(character == '\n')) {
			cursor.setY(cursorY + 1);
			StringBuilder currentLine = lines.get(cursorX);
			currentLine.insert(cursorY, character);
		} else
			insertLine();
	}

	/**
	 * Delete char from the buffer.
	 * 
	 */
	public void deleteChar() {
		int cursorX = cursor.getX();
		int cursorY = cursor.getY();

		if (cursorY > 0) {
			StringBuilder currentLine = lines.get(cursorX);
			currentLine.deleteCharAt(cursorY - 1);
			cursor.setY(cursorY - 1);
		} else if (cursorX != 0) {
			deleteLine();
		}
	}
}