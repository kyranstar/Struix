

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

@RunWith(AllTests.AllTestsRunner.class)
final class AllTests {

	private static final File CLASSES_DIR = findClassesDir();

	private AllTests() {
		// static only
	}

	/**
	 * Finds and runs tests.
	 */
	public static class AllTestsRunner extends Suite {

		/**
		 * Constructor.
		 *
		 * @param clazz
		 *            the suite class - <code>AllTests</code>
		 * @throws InitializationError
		 *             if there's a problem
		 */
		public AllTestsRunner(final Class<?> clazz) throws InitializationError {
			super(clazz, findClasses());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.junit.runners.Suite#run(org.junit.runner.notification.RunNotifier)
		 */
		@Override
		public void run(final RunNotifier notifier) {
			initializeBeforeTests();

			super.run(notifier);
		}

		private static Class<?>[] findClasses() {
			final List<File> classFiles = new ArrayList<File>();
			findClasses(classFiles, CLASSES_DIR);
			final List<Class<?>> classes = convertToClasses(classFiles, CLASSES_DIR);
			return classes.toArray(new Class[classes.size()]);
		}

		private static void initializeBeforeTests() {
			// do one-time initialization here
		}

		private static List<Class<?>> convertToClasses(final List<File> classFiles, final File classesDir) {

			final List<Class<?>> classes = new ArrayList<Class<?>>();
			for (final File file : classFiles) {
				if (!file.getName().endsWith("Test.class")) {
					continue;
				}
				String name = file.getPath().substring(classesDir.getPath().length() + 1).replace('/', '.')
						.replace('\\', '.');
				name = name.substring(0, name.length() - 6);
				Class<?> c;
				try {
					c = Class.forName(name);
				} catch (final ClassNotFoundException e) {
					throw new AssertionError(e);
				}
				if (!Modifier.isAbstract(c.getModifiers())) {
					classes.add(c);
				}
			}

			// sort so we have the same order as Ant
			Collections.sort(classes, new Comparator<Class<?>>() {
				@Override
				public int compare(final Class<?> c1, final Class<?> c2) {
					return c1.getName().compareTo(c2.getName());
				}
			});

			return classes;
		}

		private static void findClasses(final List<File> classFiles, final File dir) {
			for (final File file : dir.listFiles()) {
				if (file.isDirectory()) {
					findClasses(classFiles, file);
				} else if (file.getName().toLowerCase().endsWith(".class")) {
					classFiles.add(file);
				}
			}
		}
	}

	private static File findClassesDir() {
		try {
			final String path = AllTests.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			return new File(URLDecoder.decode(path, "UTF-8"));
		} catch (final UnsupportedEncodingException impossible) {
			// using default encoding, has to exist
			throw new AssertionError(impossible);
		}
	}
}