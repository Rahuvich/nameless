package com.nameless.game.actors.items;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.nameless.game.Constants;
import com.nameless.game.MathStatic;
import com.nameless.game.actors.Character;
import com.nameless.game.actors.Loot;
import com.nameless.game.actors.enemies.Zombie;
import com.nameless.game.actors.player.Player;
import com.nameless.game.managers.ParticleEffectManager;

public class Explosion {

    public Explosion(World world, final RayHandler rayHandler, float range, final float DAMAGE, final float x, final float y) {
        ParticleEffectManager.getInstance().addParticle(ParticleEffectManager.Type.EXPLOSION, new Vector2(x,y), null);

        final PointLight light = new PointLight(rayHandler, 20, new Color(1f,.6f,.6f,.65f), 10, x,y);
        light.setSoftnessLength(0f);
        light.setActive(true);
        light.setContactFilter(Constants.LOW_FURNITURES_BIT, (short) 0x0000, (short) (Constants.OBSTACLES_BIT));


        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                light.remove();
            }
        }, .6f);

        // Raycast
        final Vector2 p1;
        Vector2[] p2;
        p1 = new Vector2(x,y);
        p2 = new Vector2[180];
        float angle = 0;
        for(int i = 0; i < p2.length; ++i){
            p2[i] =  MathStatic.RotateVector2(new Vector2(x+range, y),angle , p1);
            angle += 2;
        }

        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof Zombie || fixture.getBody().getUserData() instanceof Player){
                    ((Character) fixture.getBody().getUserData()).takeDamage(DAMAGE * 1/fraction, MathStatic.V2xf(MathStatic.V2minusV2(point,p1).nor(), 1/fraction));
                    if(!((Character) fixture.getBody().getUserData()).setToDestroy && !((Character) fixture.getBody().getUserData()).isOnFire()){
                        ((Character) fixture.getBody().getUserData()).setOnFire();
                    }
                }
                if(fixture.getBody().getUserData() instanceof GrenadeBullet){
                    ((GrenadeBullet) fixture.getBody().getUserData()).setToDestroy = true;
                }
                if(fixture.getBody().getUserData() instanceof Loot){
                    ((Loot) fixture.getBody().getUserData()).applyImpulse(MathStatic.V2xf(MathStatic.V2minusV2(point,p1).nor(), 1/fraction));
                }
                return 1;
            }
        };

        for(int i = 0; i < p2.length; ++i){
            world.rayCast(callback, p1, p2[i]);
        }
    }
}
