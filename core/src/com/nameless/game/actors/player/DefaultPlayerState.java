package com.nameless.game.actors.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nameless.game.*;
import com.nameless.game.actors.Character;
import com.nameless.game.actors.items.*;
import com.nameless.game.actors.states.IState;
import com.nameless.game.flowfield.FlowFieldManager;
import com.nameless.game.maps.LevelManager;
import com.nameless.game.pathfinding.PathfindingDebugger;

public class DefaultPlayerState implements IState {

    private Player parent;

    Vector2 touchpad, touchpad2, aux;

    //private int actualNode = 0;
    private static int actualSector = -1;

    @Override
    public void Enter(Character parent) {
        this.parent = (Player) parent;

        if(actualSector < 0) actualSector = LevelManager.graph.getNodeByXYFloat(parent.getCenterX(), parent.getCenterY()).sector;

        touchpad = new Vector2(0,0);
        touchpad2 = new Vector2(0,0);
        aux = new Vector2(0,0);
    }

    @Override
    public void Update(float dt) {
        if(LevelManager.graph.getNodeByXYFloat(parent.getCenterX(), parent.getCenterY()).sector != actualSector) {
            actualSector = LevelManager.graph.getNodeByXYFloat(parent.getCenterX(), parent.getCenterY()).sector;
            FlowFieldManager.calcDistanceForEveryNode(parent.getCenterX(),
                    parent.getCenterY());
        } else {
            FlowFieldManager.calcDistanceForMySector(parent.getCenterX(), parent.getCenterY());
        }

        touchpad.set(parent.play.controller.MovePercentX, parent.play.controller.MovePercentY);
        touchpad2.set(parent.play.controller.TurnPercentX, parent.play.controller.TurnPercentY);

        if(!touchpad2.isZero()) parent.setRotation(touchpad2.angle());
        else if(!touchpad.isZero()) parent.setRotation(touchpad.angle());

        if(parent.play.controller.shoot) parent.SPEED = parent.INI_SPEED * .66f;
        else parent.SPEED = parent.INI_SPEED;

        // Shoot
        if(parent.play.controller.shoot && parent.weapons.getAmmo(VirtualController.ACTUAL_WEAPON) > 0) {
            Vector2 MuzzlePosAux = MathStatic.RotateVector2(parent.MuzzlePos, parent.getRotation(), aux.set(parent.getCenterX(), parent.getCenterY()));
            switch (VirtualController.ACTUAL_WEAPON){
                case WeaponsInfo.ROCKET:
                    if(System.currentTimeMillis() - parent.play.controller.lastTimeShot > WeaponsInfo.ROCKET_DELAY){
                        RocketBullet bullet = new RocketBullet(parent.rayHandler, parent.world, MuzzlePosAux.x, MuzzlePosAux.y, parent.getRotation());
                        parent.play.bg.addActor(bullet);
                        parent.weapons.removeAmmo(VirtualController.ACTUAL_WEAPON,1);
                        parent.play.controller.lastTimeShot = System.currentTimeMillis();
                        Gdx.input.vibrate(WeaponsInfo.ROCKET_DELAY/3);
                    }
                    break;
                case WeaponsInfo.GRENADE:
                    if(System.currentTimeMillis() - parent.play.controller.lastTimeShot > WeaponsInfo.GRENADE_DELAY){
                        GrenadeBullet bullet = new GrenadeBullet( parent.rayHandler,parent.world, MuzzlePosAux.x,MuzzlePosAux.y, parent.getRotation());
                        parent.play.bg.addActor(bullet);
                        parent.weapons.removeAmmo(VirtualController.ACTUAL_WEAPON,1);
                        parent.play.controller.lastTimeShot = System.currentTimeMillis();
                        if(parent.weapons.getAmmo(VirtualController.ACTUAL_WEAPON) == 0) parent.region = parent.atlas.findRegion(Constants.character + "_punch");
                        Gdx.input.vibrate(WeaponsInfo.GRENADE_DELAY/3);
                    }
                    break;
                case WeaponsInfo.SHOTGUN:
                    if(System.currentTimeMillis() - parent.play.controller.lastTimeShot > WeaponsInfo.SHOTGUN_DELAY){
                        ShotgunBullet bullet = new ShotgunBullet(parent.world, MuzzlePosAux.x,MuzzlePosAux.y, parent.getRotation());
                        parent.play.bg.addActor(bullet);
                        parent.weapons.removeAmmo(VirtualController.ACTUAL_WEAPON,1);
                        parent.play.controller.lastTimeShot = System.currentTimeMillis();
                        Gdx.input.vibrate(WeaponsInfo.SHOTGUN_DELAY/3);
                    }
                    break;
                case WeaponsInfo.PISTOL:
                    if(System.currentTimeMillis() - parent.play.controller.lastTimeShot > WeaponsInfo.PISTOL_DELAY){
                        PistolBullet bullet = new PistolBullet(parent.world, MuzzlePosAux.x,MuzzlePosAux.y, parent.getRotation());
                        parent.play.bg.addActor(bullet);
                        parent.weapons.removeAmmo(VirtualController.ACTUAL_WEAPON,1);
                        parent.play.controller.lastTimeShot = System.currentTimeMillis();
                        Gdx.input.vibrate(WeaponsInfo.PISTOL_DELAY/3);
                    }
                    break;
                case WeaponsInfo.UZI:
                    if(System.currentTimeMillis() - parent.play.controller.lastTimeShot > WeaponsInfo.UZI_DELAY) {
                        UziBullet bullet = new UziBullet(parent.world, MuzzlePosAux.x,MuzzlePosAux.y, parent.getRotation());
                        parent.play.bg.addActor(bullet);
                        parent.weapons.removeAmmo(VirtualController.ACTUAL_WEAPON,1);
                        parent.play.controller.lastTimeShot = System.currentTimeMillis();
                        Gdx.input.vibrate(WeaponsInfo.UZI_DELAY/3);
                    }
                    break;
            }

        }

        parent.body.setTransform(parent.body.getPosition().x, parent.body.getPosition().y, (float) Math.toRadians(parent.getRotation()));
        parent.body.setLinearVelocity(touchpad.x *parent.SPEED * dt , touchpad.y * parent.SPEED *dt);
        parent.setPosition(parent.body.getPosition().x - parent.getWidth()/2, parent.body.getPosition().y - parent.getHeight()/2);

        if(parent.setToDestroy) parent.remove();
    }

    @Override
    public void Exit() {

    }
}
