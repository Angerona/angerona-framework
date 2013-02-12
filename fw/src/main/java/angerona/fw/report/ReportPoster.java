package angerona.fw.report;


/**
 * Every class which implement this interface can be used as information 
 * provider for the report mechanism of Angerona. Typically this are agents
 * belief bases and all kind of Operators.
 * 
 * @author Tim Janus
 */
public interface ReportPoster {
	/** @return a more detailed name of the poster like the operator name */
	String getPosterName();
}
