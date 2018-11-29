package com.nameless.game.pathfinding;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.nameless.game.maps.LevelManager;


public class GraphImp implements IndexedGraph<Node> {
    public Array<Node> nodes = new Array<Node>();
    protected int capacity;

    public GraphImp() {
        super();
    }

    public GraphImp(int capacity) {
        this.capacity = capacity;
    }


    public GraphImp(Array<Node> nodes) {
        this.nodes = nodes;

        // speedier than indexOf()
        for (int x = 0; x < nodes.size; ++x) {
            nodes.get(x).index = x;
        }
    }

    public Node getNodeByXY(int x, int y) {
        int modX = (int) (x / LevelManager.TILE_WIDTH);
        int modY = (int)(y / LevelManager.TILE_HEIGHT);

        return nodes.get((int) (LevelManager.WIDTH_IN_TILES * modY + modX));
    }

    public Node getNodeByXYFloat(float x, float y) {
        int modX = MathUtils.round(x);
        int modY = MathUtils.round(y);

        modX = (int)(modX / LevelManager.TILE_WIDTH);
        modY = (int)(modY / LevelManager.TILE_HEIGHT);

        return nodes.get((int) (LevelManager.WIDTH_IN_TILES * modY + modX));
    }

    public int getIndexByXY(float x, float y){
        int modX = MathUtils.round(x);
        int modY = MathUtils.round(y);

        modX = (int)(modX / LevelManager.TILE_WIDTH);
        modY = (int)(modY / LevelManager.TILE_HEIGHT);

        return ((int) (LevelManager.WIDTH_IN_TILES * modY + modX));
    }

    public Node getNodeByXYTiles(int x, int y) {
        return nodes.get((int) (LevelManager.WIDTH_IN_TILES * y + x));
    }

    public Node getNode(int index){return nodes.get(index);}

    @Override
    public int getIndex(Node node) {
        return nodes.indexOf(node, true);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }
}