package angerona.fw.serialize;

import java.io.File;
import java.io.OutputStream;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.serialize.transform.FolAtomTransform;

/**
 * Helper class encapsulates the exception handling during xml serialization.
 * @author Tim Janus
 */
public class SerializeHelper {
	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(SerializeHelper.class);
	
	/** the used serializer */
	private static Serializer serializer = null;
	
	/**
	 * Initialize the SerializeHelper
	 */
	private static void init() {
		if(serializer == null) {
			RegistryMatcher matcher = new RegistryMatcher();
			matcher.bind(Atom.class, FolAtomTransform.class);
			serializer = new Persister(matcher);
		}
	}
	
	/**
	 * loads the xml in the given file assuming that the given class is anotated by simplexml library
	 * @param cls		class information about the object to deserialize.
	 * @param filename	the name of the file where the xml is located.
	 * @return			An instance of with the type T containing the xml data.
	 */
	public static <T> T loadXml(Class<T> cls, File source) {
		init();
		T obj = null;
		try {
			obj = cls.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			obj = serializer.read(cls, source);
		} catch (Exception e) {
			LOG.error("Something went wrong during loading of '{}': {}", source.getPath(), e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * writes the given data-class into the given destination file
	 * @param data			object serializable by simplexml framework
	 * @param destination	file referencing the destination of the xml writing.
	 */
	public static <T> void writeXml(T data, File destination) {
		init();
		try {
			serializer.write(data, destination);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error occored during writing of '{}': {}, ", destination.getPath(), e.getMessage());
		}
	}
	
	/**
	 * output the given data-class into the given output-stream.
	 * @param data		object serializable by simplexml framework.
	 * @param output	output-stream referencing the object which receives the xml output.
	 */
	public static<T> void outputXml(T data, OutputStream output) {
		init();
		try {
			serializer.write(data, output);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error occored during output of xml data: {}, ", e.getMessage());
		}
	}
}
