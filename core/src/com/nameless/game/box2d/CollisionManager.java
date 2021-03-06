package com.nameless.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.nameless.game.MathStatic;
import com.nameless.game.WeaponsInfo;
import com.nameless.game.actors.Character;
import com.nameless.game.actors.Loot;
import com.nameless.game.actors.items.BasicBullet;
import com.nameless.game.actors.items.RocketBullet;
import com.nameless.game.actors.player.Player;

/**
 * Created by Raul on 05/07/2017.
 */
public class CollisionManager implements ContactListener {

    public Array<Fixture> fixturesToDestroy;
    public Array<Body> bodiesToDestroy;


    public CollisionManager() {
        bodiesToDestroy = new Array<Body>();
        fixturesToDestroy = new Array<Fixture>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

        // Loot
        if(fixtureA.getBody().getUserData() instanceof Loot && fixtureB.getBody().getUserData() instanceof Player){
            ((Loot) fixtureA.getBody().getUserData()).setToDestroy = true;
            Loot.Type type = ((Loot) fixtureA.getBody().getUserData()).getType();
            float x = ((Loot) fixtureA.getBody().getUserData()).getX() + ((Loot) fixtureA.getBody().getUserData()).getWidth()/2;
            float y = ((Loot) fixtureA.getBody().getUserData()).getY() + ((Loot) fixtureA.getBody().getUserData()).getHeight()/2;
            ((Player) fixtureB.getBody().getUserData()).LootCollected(type, x, y);
        } else if(fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Loot){
            ((Loot) fixtureB.getBody().getUserData()).setToDestroy = true;
            Loot.Type type = ((Loot) fixtureB.getBody().getUserData()).getType();
            float x = ((Loot) fixtureB.getBody().getUserData()).getX() + ((Loot) fixtureB.getBody().getUserData()).getWidth()/2;
            float y = ((Loot) fixtureB.getBody().getUserData()).getY() + ((Loot) fixtureB.getBody().getUserData()).getHeight()/2;
            ((Player) fixtureA.getBody().getUserData()).LootCollected( type, x, y);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
        strain(fixtureA, impulse);
        strain(fixtureB, impulse);

        if(fixtureA.getBody().getUserData() instanceof RocketBullet){
            if(fixtureB.getBody().getUserData() instanceof Character) {
                Vector2 p1 = ((RocketBullet) contact.getFixtureA().getBody().getUserData()).p1;
                Vector2 p2 = ((RocketBullet) contact.getFixtureA().getBody().getUserData()).p2;
                ((Character) contact.getFixtureB().getBody().getUserData()).takeDamage(WeaponsInfo.ROCKET_DAMAGE, MathStatic.V2minusV2(p2, p1).nor());
            }
            ((RocketBullet) contact.getFixtureA().getBody().getUserData()).setToDestroy = true;
        }
        else if(contact.getFixtureB().getBody().getUserData() instanceof RocketBullet) {
            if(contact.getFixtureA().getBody().getUserData() instanceof Character) {
                Vector2 p1 = ((RocketBullet) contact.getFixtureB().getBody().getUserData()).p1;
                Vector2 p2 = ((RocketBullet) contact.getFixtureB().getBody().getUserData()).p2;
                ((Character) contact.getFixtureA().getBody().getUserData()).takeDamage(WeaponsInfo.ROCKET_DAMAGE, MathStatic.V2minusV2(p2, p1).nor());
            }
            ((RocketBullet) contact.getFixtureB().getBody().getUserData()).setToDestroy = true;
        }
    }

    // Check if it is bullet, if true destroy.
    private void strain(Fixture fixture, ContactImpulse impulse) {
        if(!(fixture.getBody().getUserData() instanceof BasicBullet)) return;
        BasicBullet actor = (BasicBullet) fixture.getBody().getUserData();
        if(actor.getNormalResistance() <= MathStatic.sum(impulse.getNormalImpulses()) ||
                actor.getTangentResistance() <= MathStatic.sum(impulse.getTangentImpulses())){
            actor.setToDestroy = true;
        }
    }
}
