package angerona.fw.serialize;

import java.io.File;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.reflection.BooleanExpression;
import angerona.fw.reflection.Condition;
import angerona.fw.reflection.FolFormulaVariable;
import angerona.fw.reflection.Value;
import angerona.fw.serialize.transform.ConditionTransform;
import angerona.fw.serialize.transform.FolAtomTransform;
import angerona.fw.serialize.transform.FolFormulaTransform;
import angerona.fw.serialize.transform.SpeechActStrategy;
import angerona.fw.serialize.transform.ValueTransform;
import angerona.fw.serialize.transform.VariableTransform;

/**
 * Helper class encapsulates the exception handling during XML serialization.
 * @author Tim Janus
 */
public class SerializeHelper {
	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(SerializeHelper.class);
	
	/** the used serializer */
	private static Serializer serializer = null;
	
	/**
	 * Initialize the SerializeHelper: Register transforms.
	 */
	private static void init() {
		if(serializer == null) {
			RegistryMatcher matcher = new RegistryMatcher();
			matcher.bind(FolFormula.class, FolFormulaTransform.class);
			matcher.bind(FOLAtom.class, FolAtomTransform.class);			
			
			matcher.bind(FolFormulaVariable.class, 
					new VariableTransform<FolFormulaVariable>() {
						@Override
						protected Class<FolFormulaVariable> getCls() {
							return FolFormulaVariable.class;
						}
			});
			
			matcher.bind(Value.class, ValueTransform.class);
			matcher.bind(BooleanExpression.class, ConditionTransform.class);
			matcher.bind(Condition.class, ConditionTransform.class);
			
			//matcher.bind(SpeechAct.class, SpeechActTransform.class);
			
			serializer = new Persister(new SpeechActStrategy(), matcher);
		}
	}
	
	/**
	 * Loads XML in the given file assuming that the given class is annotated by simple-xml library,
	 * if the XML in the file cannot be parsed the method throws an exception. For an exception
	 * free version of this method see loadXmlTry().
	 * @param cls		Class information about the object to de-serialize
	 * @param source	The file object that contains the XML expressions that shall be loaded
	 * @return			An instance of type T containing the data stored in the XML file.
	 * @throws Exception	If an error occured during the parsing of the XML file.
	 */
	public static <T> T loadXml(Class<T> cls, File source) throws Exception {
		init();
		return serializer.read(cls, source);
	}
	
	/**
	 * Loads the XML in the given file assuming that the given class is annotated by simplexml library
	 * @param cls		class information about the object to de-serialize.
	 * @param source	the name of the file where the XML is located.
	 * @return			An instance of type T containing the XML data or null if an error occured during
	 * 					parsing the XML file.
	 */
	public static <T> T loadXmlTry(Class<T> cls, File source) {
		try {
			return loadXml(cls, source);
		} catch (Exception e) {
			LOG.error("Something went wrong during loading of '{}': {}", source.getPath(), e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads the XML expressions provided by the given Reader source, therefore it uses the given class that must
	 * be annotated by simple-xml library annotations. This method throws exceptions, for a exception-free version
	 * that return null in a failure case see loadXmlTry().
	 * @param cls		A class containing simple-xml annotations that allow de/serialization
	 * @param source	A Java Reader object thats acts as source for the XML expressions
	 * @return			A instance of type T that contains the data stored in the XML expressions
	 * @throws Exception	If an error occurred during parsing the XML expressions
	 */
	public static <T> T loadXml(Class<T> cls, Reader source) throws Exception {
		init();
		return serializer.read(cls, source);
	}
		
	
	/**
	 * Loads the XML expressions provided by the given Reader source, therefore it uses the given class that must
	 * be annoteated by simple-xml library annotations. This methods returns null in a failure case, for a version
	 * that throws exceptions containing more error information see loadXml().
	 * @param cls		A class containing simple-xml annotations that allow de/serialization
	 * @param source	A Java Reader object thats acts as source for the XML expressions
	 * @return			A instance of type T that contains the data stored in the XML expressions
	 */
	public static <T> T loadXmlTry(Class<T> cls, Reader source) {
		try {
			return loadXml(cls, source);
		} catch (Exception e) {
			LOG.error("Something went wrong during XML loading: '{}'", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads the given data-class from the given XML string, this method throws exceptions,
	 * for a exception-free version that returns null in a failure case see loadXmlTry().
	 * @param cls	A class containing simple-xml annotations that allow de/serialization
	 * @param xml	A string containing XML expressions that shall be loaded
	 * @return		An object of type cls that contains the data saved in the XML expressions
	 * @throws Exception	If an error occurs when parsing the XML.
	 */
	public static <T> T loadXml(Class<T> cls, String xml) throws Exception {
		return loadXml(cls, new StringReader(xml));
	}
	
	/**
	 * Loads the given data-class from the given XML strings, this method returns null if
	 * an error occurs for an exception version of this method see loadXml()
	 * @param cls	A class containing the simple-xml annotations that allow de/serializoations
	 * @param xml	A string containing XML expressions that shall be loaded
	 * @return		An object of type cls that contains the data saved in the XML expressions or
	 * 				null if an error occured during parsing the XML.
	 */
	public static <T> T loadXmlTry(Class<T> cls, String xml) {
		return loadXmlTry(cls, new StringReader(xml));
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
