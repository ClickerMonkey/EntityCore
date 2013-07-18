package org.magnos.entity;

import org.magnos.entity.ComponentDynamic.Dynamic;
import org.magnos.entity.Controller.Control;
import org.magnos.entity.View.Renderer;

public class EntityCore
{

	protected static final int DEFAULT_INITIAL_CAPACITY = 64;


	protected static IdContainer<View> views = new IdContainer<View>( DEFAULT_INITIAL_CAPACITY );

	protected static IdContainer<Controller> controllers = new IdContainer<Controller>( DEFAULT_INITIAL_CAPACITY );

	protected static IdContainer<Template> templates = new IdContainer<Template>( DEFAULT_INITIAL_CAPACITY );

	protected static IdContainer<Component<?>> components = new IdContainer<Component<?>>( DEFAULT_INITIAL_CAPACITY );


	protected EntityCore()
	{
	}
	
	public static View newView( String name )
	{
		return newView( name, null );
	}

	public static View newView( String name, Renderer defaultRenderer )
	{
		return views.addDefinition( new View( views.nextId(), name, defaultRenderer ) );
	}

	public static void setViewDefault( View view, Renderer defaultRenderer )
	{
		views.getDefinition( view ).renderer = defaultRenderer;
	}

	public static View newViewAlternative( View view, Renderer renderer )
	{
		return views.addInstance( new View( view.id, view.name, renderer ) );
	}

	public static IdContainer<View> getViews()
	{
		return views;
	}

	public static Controller newController( String name )
	{
		return newController( name, null );
	}

	public static Controller newController( String name, Control defaultControl )
	{
		return controllers.addDefinition( new Controller( controllers.nextId(), name, defaultControl ) );
	}

	public static void setControllerDefault( Controller controller, Control defaultControl )
	{
		controllers.getDefinition( controller ).control = defaultControl;
	}

	public static Controller newControllerAlternative( Controller controller, Control control )
	{
		return controllers.addInstance( new Controller( controller.id, controller.name, control ) );
	}

	public static <T> Component<T> newComponent( String name )
	{
		return components.addDefinition( new ComponentUndefined<T>( components.nextId(), name ) );
	}

	public static <T> Component<T> newComponent( String name, ComponentFactory<T> factory )
	{
		return components.addDefinition( new ComponentDistinct<T>( components.nextId(), name, factory ) );
	}

	public static <T> Component<T> newComponentAlternative( Component<T> component, ComponentFactory<T> factory )
	{
		return components.addInstance( new ComponentDistinct<T>( component.id, component.name, factory ) );
	}

	public static <T> Component<T> newComponentShared( String name, ComponentFactory<T> factory )
	{
		return components.addDefinition( new ComponentShared<T>( components.nextId(), name, factory ) );
	}

	public static <T> Component<T> newComponentSharedAlternative( Component<T> component, ComponentFactory<T> factory )
	{
		return components.addInstance( new ComponentShared<T>( component.id, component.name, factory ) );
	}

	public static <T> Component<T> newComponentDynamic( String name, Dynamic<T> dynamic )
	{
		return components.addDefinition( new ComponentDynamic<T>( components.nextId(), name, dynamic ) );
	}

	public static <T> Component<T> newComponentDynamicAlternative( Component<T> component, Dynamic<T> dynamic )
	{
		return components.addInstance( new ComponentDynamic<T>( component.id, component.name, dynamic ) );
	}

	public static <T> Component<T> newComponentConstant( String name, T constant, boolean settable )
	{
		return components.addDefinition( new ComponentConstant<T>( components.nextId(), name, constant, settable ) );
	}

	public static <T> Component<T> newComponentConstantAlternative( Component<T> component, T constant, boolean settable )
	{
		return components.addInstance( new ComponentConstant<T>( component.id, component.name, constant, settable ) );
	}

	public static <T> Component<T> newComponentAlias( Component<T> component, Component<T> alias )
	{
		return components.addInstance( new ComponentAlias<T>( alias.id, alias.name, component.id ) );
	}

	public static IdContainer<Component<?>> getComponents()
	{
		return components;
	}

	public static Template newTemplate()
	{
		return new Template();
	}

	public static Template newTemplate( String name, Components componentSet, Controllers controllerSet, View view )
	{
		return templates.addDefinition( new Template( templates.nextId(), name, null, componentSet, controllerSet, view ) );
	}

	public static Template newTemplate( Template base, String name, Components componentSet, Controllers controllerSet, View view )
	{
		Template t = templates.addDefinition( base.extend( templates.nextId(), name ) );

		for (Component<?> component : componentSet.values)
		{
			t.add( component );
		}

		for (Controller controller : controllerSet.values)
		{
			t.add( controller );
		}

		if ( view != null )
		{
			t.setView( view );
		}

		return t;
	}

	public static IdContainer<Template> getTemplates()
	{
		return templates;
	}

	public static void clear()
	{
		views.clear();
		controllers.clear();
		components.clear();
		templates.clear();
	}

}
