package angerona.fw.gui.docking;

/**
 * A library of DockableDecorators, because the decorators are stateless the atomic
 * decorators do not need to be instantiated every time they are used. Instead 
 * instatiate them use the instances provides by this library.
 * @author Tim Janus
 */
public class DecoratorLibrary {
	public static final DockableDecorator closeDecorator = new CloseButtonDecorator();
}
