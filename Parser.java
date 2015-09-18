import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is thread safe.
 */
public class Parser {
	private final File file; // keep it immutable
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public Parser(File f) {
		file = f;
	}

	// you wanted to be threadsafe
	public String getContent() {

	}

	// you wanted to be threadsafe
	public String getContentWithoutUnicode() {
		return getContent(false);
	}

	// dont repeat yourself
	private String getContent(boolean withUnicode) throws IOException {
		lock.readLock().lock(); // lock the whole file access
		String output = "";
		try {
			FileInputStream i = new FileInputStream(file); // bufferedstream  recommended
			int data;

			while ((data = i.read()) > 0) {
				if (withUnicode || data < 0x80) { // is this what you intent with non unicode? hmm
					output += (char) data;
				}
			}
		} finally {
			lock.readLock().unlock(); // Unlock read
		}

		return output;
	}

	// thread safe
	public void saveContent(String content) throws IOException {

		lock.writeLock().lock();
		try {
			FileOutputStream o = new FileOutputStream(file); // bufferedstream  recommended
			for (int i = 0; i < content.length(); i += 1) {
				o.write(content.charAt(i));
			}
		} finally {
			lock.readLock().unlock(); // Unlock read
		}
	}
}
