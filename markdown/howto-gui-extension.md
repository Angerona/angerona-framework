How-to: Implement a GUI Extension for the Angerona Framework {#howtoguiextension}
============================================================

This article explains how Angerona can be extended with GUI components. Angerona uses the MVP
(Model-View-Presenter) pattern to integrate GUI components. Martin Folwer summarized the MVP pattern
[on his website](http://www.martinfowler.com/eaaDev/uiArchs.html#Model-view-presentermvp). 

This Article describes the base classes used to implement MVP components in Angerona. It starts by 
explaining the model, then the view and then the presenter. Then it exemplary shows how the MVP is
used to let the user see and control the state of the simulation. Then it explains how a MVP component
can be linked to the Angerona Framework in a way that it can be accessed either through the *Create Window*
menu item or by double-clicking on a entity/component of the simulation.

**BEWARE** - There are some deprecated classes, do not use those classes as basic for new classes
in the Angerona framework because they do not use the MVP pattern and will therefore be replaced by newer
variants which implement MVP in the near future.


The Model
---------

The *Model* contains the data that shall be represented for, and changed by, a user through the *View*. In
Angerona such models have to implement the @ref com.github.angerona.fw.util.Model interface. It is recommended
to extend the base class @ref com.github.angerona.fw.util.ModelAdapter that provides the implementation for the
Property and for map events. Those events can be used to inform observers about changing properties or changes in
maps of the *Model*. The *View* is an example of such a observer but the *Presenter* might also be interested in
receiving the property change events of the *Model*.



The View
--------

The *View* contains widgets that are used to represent and change the *Model*. In Angerona such a *View* has to 
implement the @ref com.github.angerona.fw.gui.base.View interface. This interface provides a root component of the view
and also extends observer interfaces. An Adapter that implements every event method is given by
@ref com.github.angerona.fw.gui.base.ViewAdapter but if you want to extend a JPanel or a similar SWING class you might
use @ref com.github.angerona.fw.gui.base.ObservingPanel as base class, which also adapts the *View* interface.



The Presenter
-------------

The *Presenter* is responsible to couple the *Model* with the *View*, such that both are completely independent from each 
other. That allows one *Model* to be shown by several *Views* for example and also allows easier unit testing. The *Presenter*
is implemented as abstract generic base class in Angerona: @ref com.github.angerona.fw.gui.base.Presenter. It is hard to separate the
logic that has to be handled by the *View* from the logic that has to be handled by the *Presenter* but the *Presenter* can be seen as
Controller and the *View* shall try to translate it's events in a way that they can be differentiated by the *Presenter*.

Imagine there is a *Model* containing a string and there are two *Views*, one representing the string in a text-field,
and one representing the string in a text-area. The *Presenter* might implement DocumentListener that is used in 
JAVA to react to text change events of JTextField and JTextArea instances. As long as the string shall be changed in 
the model as fast as possible this modeling is appropriate although it does not encapsulates the SWING events. 

Now imagine the change of the string in the *Model* leads to complex computations that might need some time, 
such that we do not want to react to text change events but wait for an user input that indicates that the changes 
to the string are ready. In such a case we also want to show if the value shown in the view is up to date. Also
imagine the String has to be in a special syntax, such that font-coloring can be used to indicate if the preview
is in the correct format.

With the above example we have the following design decisions: 

- How do we decouple the change of the string in the GUI from the *real* change.
- How do we implement syntax check and font-coloring.

Both points have to be implemented by the *Presenter*. In this example a *Presenter* might have a method 
*onTextChange()* that acts as delegate for the DocumentListener and a method *onValidate* that is called if
the user decides that the string is ready to be changed. To couple the *View* with the *Presenter* the *Presenter*
has to implement a Listener that is used by the *View* to delegate it's SWING related events like ActionListener,
KeyListener, DocumentListener etc. The View interface also has to provide information like the stylesheet used
to render the text, such that the *Presenter* can change those values.

We conclude that for simple cases the *Presenter* might implement an interface like ActionListener or KeyListener and
implement it's operations based on them. For this the *Presenter* has to register itself as listener to the correct
SWING GUI components. But if there are views that use different events to invoke the same change on the
model then a delegator Listener has to be created. The *View* interface has to provide methods that provide every information 
the *Presenter* needs to change like stylesheet information that define the font of a text for example.



Example: Showing and Controlling the Simulation State
-----------------------------------------------------

This Section introduces some classes that implement a simple MVP Component that is used for the representation and the
control of the simulation state. The classes can be found in package *com.github.angerona.fw.gui.simctrl*

But first we list the requirements for this MVP-Component:

- We want to load a simulation configuration.
- We want be able to initialize a loaded simulation.
- We want to perform one tick in an initialized simulation.
- We want to complete a loaded simulation without further user input.

The above list shows what the user can do, such that is provides the control requirements for the MVP-Component. Beside
those control requirements which we define as must have requirements a *View* of this *MVP* can also show the current 
simulation state.

The *View* interface is extended such that is provides the buttons that are responsible to control the simulation. It
also contains event methods that are invoked if properties change. (See @ref com.github.angerona.fw.gui.simctrl.SimulationControlView)
The classes @ref com.github.angerona.fw.gui.simctrl.SimulationControlBar and 
@ref com.github.angerona.fw.gui.simctrl.SimulationControlMenu are both implementations of this interface.

The *Model* is given as interface by @ref com.github.angerona.fw.gui.simctrl.SimulationControlModel, it allows an user to load a
simulation by using the setSimulationConfiguration() method and to initialize and run a simulation. It also provides the 
current state of the simulation.

The *Presenter* given in @ref com.github.angerona.fw.gui.simctrl.SimulationControlPresenter registers itself as ActionListener for
the buttons of the *View* such that it can delegate the clicks to the *Model*. It implements the entire control behavior, such that
the *View* only delegates it's user-inputs. As soon as we want to use a another event as the button-click event to control
the simulation we have to implement a custom listener that acts as delegator of the SWING events. Nevertheless both views are responsible 
for their button texts and therefore react to the property change events of the *Model*. The *SimulationControlBar* *View* also
outputs the state of the simulation in a text box.

The MVP-Component given in @ref com.github.angerona.fw.gui.simctrl.SimulationControlBarMVPComponent is used to register a 
GUI-Component as extension of the Angerona framework. This registration is explained in more detail in the next Section but the
*decorate()* method of the @ref com.github.angerona.fw.gui.base.ViewComponent interface that is implemented by this class allows
to change the title bar of the tab that holds the View in Angerona.



Registration in Angerona
------------------------

To register a View-Component as GUI extension in Angerona it has to implement the ViewComponent interface in 
@ref com.github.angerona.fw.gui.base.ViewComponent and it has to be added to the *getUIComponents()* method of 
the corresponding plug-in class. If it is part of the **gui** project the plug-in class
@ref com.github.angerona.fw.gui.DefaultUIPlugin shall be used.

If this has been done the new View-Component can be created by using the main menu: *Window --> Create...* 
or the widget can be added programmatically by using the class 
@ref com.github.angerona.fw.internal.ViewComponentFactory.

There also exist a more specialized type of GUI extensions for Angerona called **EntityViewComponent**, see
@ref com.github.angerona.fw.gui.base.EntityViewComponent. Such a component represents a view that shows an entity or a 
component of the Angerona simulation. An example is the @ref com.github.angerona.fw.gui.view.BeliefbaseView class. 
These view-components are special and cannot be created by the *Window --> Create...* menu item because they need an 
entity that acts as a data model. 

If you have implemented an **EntityViewComponent* for a specific component *C* and it is registered as plug-in as described earlier in
this Section than this View-Component gets opened if the user double clicks on the tree node of *C* in the Entity Explorer of
the current simulation.

The default perspective for the Angerona GUI is generated in the *createDefaultPerspective()* method of the AngeronaWindow class, 
see @ref com.github.angerona.fw.gui.AngeronaWindow. 