package org.magnos.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.magnos.entity.Controller.Control;
import org.magnos.entity.helper.Vector;

public class TestController
{

	public static final EntityCore core = new EntityCore();

	public static Component<Vector> POSITION = EntityCore.newComponent( "position", new Vector() );
	public static Component<Vector> VELOCITY = EntityCore.newComponent( "velocity", new Vector() );
	public static Component<Vector> ACCELERATION = EntityCore.newComponent( "acceleration", new Vector() );

	public static Controller PHYSICS_SIMPLE = EntityCore.newController("physics-simple", new Control() {
		public void control( Entity e, Object updateState ) {
			float dt = (Float)updateState;
			e.get(POSITION).addsi( e.get(VELOCITY), dt );
		}
	});

	public static Controller PHYSICS = EntityCore.newController("physics", new Control() {
		public void control( Entity e, Object updateState ) {
			float dt = (Float)updateState;
			e.get(POSITION).addsi( e.get(VELOCITY), dt );
			e.get(VELOCITY).addsi( e.get(ACCELERATION), dt );
		}
	});

	public static Template SPRITE = EntityCore.newTemplate( "sprite", new Components(POSITION, VELOCITY), new Controllers(PHYSICS_SIMPLE), null );

	@Test
	public void testUpdate()
	{
		Entity e = new Entity(SPRITE);
		e.enable(PHYSICS_SIMPLE);

		Vector p = e.get(POSITION);
		Vector v = e.get(VELOCITY);
		p.x = 5.0f;
		p.y = 2.0f;
		v.x = 3.0f;
		v.y = -1.5f;

		final float dt = 0.5f;
		
		e.update( dt );

		assertEquals( 6.5f, p.x, 0.0001f );
		assertEquals( 1.25f, p.y, 0.0001f );

		e.disable(PHYSICS_SIMPLE);
		e.update( dt );

		assertEquals( 6.5f, p.x, 0.0001f );
		assertEquals( 1.25f, p.y, 0.0001f );

		e.enable(PHYSICS_SIMPLE);
		e.update( dt );

		assertEquals( 8.0f, p.x, 0.0001f );
		assertEquals( 0.5f, p.y, 0.0001f );
	}

}
