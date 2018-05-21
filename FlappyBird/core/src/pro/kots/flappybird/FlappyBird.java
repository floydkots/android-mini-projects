package pro.kots.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    /*private ShapeRenderer shapeRenderer;*/
    private Texture background;
    private Texture topTube;
    private Texture bottomTube;
    Texture gameOver;
    private Animation<Texture> flappingAnimation;
    private float stateTime;
    private Texture[] birds;
    private float screenX;
    private float screenY;
    private float birdX;
    private float birdY;
    private float topTubeY;
    private float bottomTubeY;
    private float gameOverX;
    private float gameOverY;
    private int birdVelocityY = 0;

    private float maxTubeOffset;
    private final float GRAVITY = 1f;
    private final int JUMP_Y = -20;

    private float gap = 400;
    private Random randomGenerator;
    private float[] tubeOffsetsY;
    private float tubeVelocityX;
    private float[] tubeX;

    private final int NUMBER_OF_TUBES = 4;
    private float distanceBetweenTubes;

    int gameState = 0;

    Circle birdCircle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    private int score;
    private int scoringTube;

    private BitmapFont bitmapFont;

    @Override
    public void create () {
        spriteBatch = new SpriteBatch();
        /*shapeRenderer = new ShapeRenderer();*/
        background = new Texture("bg.png");
        topTube = new Texture("top_tube.png");
        bottomTube = new Texture("bottom_tube.png");
        gameOver = new Texture("game_over.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird_0.png");
        birds[1] = new Texture("bird_1.png");

        flappingAnimation = new Animation<Texture>(0.0825f, birds);
        screenX = Gdx.graphics.getWidth();
        screenY = Gdx.graphics.getHeight();
        birdX = screenX/2 - birds[0].getWidth()/2;
        birdY = screenY/2 - birds[0].getHeight()/2;
        topTubeY = screenY/2 + gap/2;
        bottomTubeY = screenY/2 - gap/2 - bottomTube.getHeight();
        gameOverX = screenX/2 - gameOver.getWidth()/2;
        gameOverY = screenY/2 - gameOver.getHeight()/2;

        maxTubeOffset = screenY/2 - gap/2 - 100;
        randomGenerator = new Random();
        tubeVelocityX = 4f;
        tubeOffsetsY = new float[NUMBER_OF_TUBES];
        tubeX = new float[NUMBER_OF_TUBES];
        distanceBetweenTubes = screenX * 3 / 4;

        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[NUMBER_OF_TUBES];
        bottomTubeRectangles = new Rectangle[NUMBER_OF_TUBES];

        score = 0;
        scoringTube = 0;

        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(10);

        setUpTubes();
    }

    private void setUpBird() {
        birdX = screenX/2 - birds[0].getWidth()/2;
        birdY = screenY/2 - birds[0].getHeight()/2;
    }

    private void setUpTubes() {
        for (int i = 0; i < NUMBER_OF_TUBES; i++) {

            tubeOffsetsY[i] = (randomGenerator.nextFloat() - 0.5f) * (screenY - gap - 200);
            tubeX[i] = screenX/2 - topTube.getWidth()/2 + screenX + i * distanceBetweenTubes;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, screenX, screenY);

        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 1) {
            if (tubeX[scoringTube] < screenX/2 - topTube.getWidth()/2) {
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if (scoringTube < NUMBER_OF_TUBES - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                birdVelocityY = JUMP_Y;

            }

            if (birdY > 0) {
                birdVelocityY += GRAVITY;
                birdY -= birdVelocityY;
            } else {
                gameState = 2;
            }

            for (int i = 0; i < NUMBER_OF_TUBES; i++) {
                if (tubeX[i] < - topTube.getWidth()) {
                    tubeX[i] += NUMBER_OF_TUBES * distanceBetweenTubes;
                    tubeOffsetsY[i] = (randomGenerator.nextFloat() - 0.5f) * (screenY - gap - 200);
                } else {
                    tubeX[i] -= tubeVelocityX;
                }

                spriteBatch.draw(topTube, tubeX[i], topTubeY + tubeOffsetsY[i]);
                spriteBatch.draw(bottomTube, tubeX[i], bottomTubeY + tubeOffsetsY[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], topTubeY + tubeOffsetsY[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], bottomTubeY + tubeOffsetsY[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

        } else if (gameState == 2) {
            spriteBatch.draw(gameOver, gameOverX, gameOverY);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                setUpBird();
                setUpTubes();
                score = 0;
                scoringTube = 0;
                birdVelocityY = 0;
            }
        }
        stateTime += Gdx.graphics.getDeltaTime();
        Texture bird = flappingAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(bird, birdX, birdY);

        bitmapFont.draw(spriteBatch, String.valueOf(score), 100, 200);

        spriteBatch.end();

        birdCircle.set(screenX/2, birdY + bird.getHeight()/2, bird.getWidth() / 2);



        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);*/

        for (int i = 0; i < NUMBER_OF_TUBES; i++) {
            /*shapeRenderer.rect(tubeX[i], topTubeY + tubeOffsetsY[i], topTube.getWidth(), topTube.getHeight());
            shapeRenderer.rect(tubeX[i], bottomTubeY + tubeOffsetsY[i], bottomTube.getWidth(), bottomTube.getHeight());*/

            boolean topOverlaps = Intersector.overlaps(birdCircle, topTubeRectangles[i]);
            boolean bottomOverlaps = Intersector.overlaps(birdCircle, bottomTubeRectangles[i]);
            if (topOverlaps || bottomOverlaps) {
                gameState = 2;
            }
        }

        /*shapeRenderer.end();*/

    }

    @Override
    public void dispose () {
        spriteBatch.dispose();
        background.dispose();
        for (Texture bird : birds) {
            bird.dispose();
        }
    }
}
