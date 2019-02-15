package com.sikoramarek.View.Implementations.View3D;

import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SharedResources;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.ViewInterface;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;

import static com.sikoramarek.Common.Config.*;
import static javafx.scene.input.MouseEvent.*;

/**
 * JavaFX View Class implements ViewInterface class
 * For viewing passed board model and passing input from user to Observable Class
 */
public class JavaFX3DView implements ViewInterface{

    /*
        3D SECTION ===========
     */
    private final Xform world = new Xform();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private static double CAMERA_INITIAL_DISTANCE = -2000;
    private static double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static double CAMERA_INITIAL_Y_ANGLE = 0.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private Xform viewBoard = new Xform();

    private Xform cornerObjects = new Xform();
    private Xform cornerLights = new Xform();

    Text text = new Text(-WIDTH/2+50, -HEIGHT, "Welcome");
    /*
        =========== END OF 3D SECTION ===========

        =========== MODEL VIEW SECTION ==========
     */

    private BoxB[][] viewBoardTable;

//    private Stage primaryStage;
    private Group root = new Group();
    private Scene scene;

//    private InputHandler inputHandler = new InputHandler();

    private boolean ongoingUpdateFromModel = false;
    private boolean ongoingUpdateFromView = false;

    private int droppedFrames = 0;
    private int renderedFrames = 0;
    private boolean showDeadDot = false;

    private long mousePressedTime;

    private Color DEAD_COLOR = new Color(0.5, 0.5, 0.5, 0.01);
    private PhongMaterial aliveMaterial = new PhongMaterial(Color.SNOW);
    private Image bumpMap;
    private Image bumpMap2;

    /*
            ========= END OF MODEL VIEW SECTION ======
     */

    int iterator = 0;
    Timeline timeline;
    Color toggleColor;

    /**
     * Constructor that takes primary stage from caller
     */
    public JavaFX3DView (){
//        this.primaryStage = primaryStage;
        try{
            bumpMap = new Image("bumpmap.jpg");
            bumpMap2 = new Image("bumpmap2.jpg");
        }catch (IllegalArgumentException exception){
            Logger.error(exception.getMessage(), this);
        }
    }

    /**
     * viewInit method
     * Sets stages title, creates and initialises reflecting rectangle table for holding view`s side rectangles
     * Defines rectangle appearance
     * Calls scene set and view methods for showing window.
     * Calls attachListeners function to attach proper listeners to stage and scene
     */
    @Override
    public void viewInit() throws SystemConfigTooWeekException {
        aliveMaterial.setBumpMap(bumpMap);
        aliveMaterial.setSelfIlluminationMap(bumpMap2);

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();

        long startTime = System.currentTimeMillis();
        Logger.log("Initiating Grid for 3D view", this);
        initGrid();
        Logger.log("Done. Taken "+(System.currentTimeMillis()-startTime)+" ms", this);
        world.getChildren().add(viewBoard);
        Logger.log("Preparing scene and window", this);
        startTime = System.currentTimeMillis();
        scene = new Scene(root, WIDTH, HEIGHT,true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setCursor(Cursor.CROSSHAIR);

        cornerObjects.setVisible(true);

        long timeTaken = System.currentTimeMillis()-startTime;
        Logger.log("  ... Done. Taken "+timeTaken+" ms", this);


//        primaryStage.setTitle("Game Of Life  v " + VERSION);

        camera.setFieldOfView(50);
        scene.setCamera(camera);
//        System.out.println("================== Game Start ==================");

    }

    public Scene getScene() {
//        handleKeyboard(scene, world);
//        handleMouse(scene, world);
        welcomeAnimation();
        return scene;
    }

    private void welcomeAnimation() {
        toggleColor = Color.WHITE;
        timeline = new Timeline(getKeyframes());
        timeline.setOnFinished((event) -> {
            timeline.stop();
            toggleColor = Color.BLACK;
            timeline = new Timeline(getKeyframes());
            iterator = 0;
            timeline.setOnFinished(event1 -> timeline.stop());
            timeline.play();
        });
        timeline.play();
    }

    private KeyFrame[] getKeyframes(){
        KeyFrame[] keyframes = new KeyFrame[X_SIZE];
        for (int i = 0; i < keyframes.length; i++) {
            keyframes[i] = new KeyFrame(Duration.millis(i*10), event -> {toggleRow(toggleColor);});
        }
        return keyframes;
    }

    private void toggleRow(Color color) {
        for (int i = 0; i < viewBoardTable.length; i++) {
            PhongMaterial material = (PhongMaterial) viewBoardTable[i][iterator].getMaterial();
            material.setDiffuseColor(color);
            BoxB boxB = (BoxB) viewBoardTable[i][iterator];
            boxB.setVisible(true);
            if(toggleColor == Color.BLACK){
                boxB.setVisible(false);
            }
        }
        if (iterator < X_SIZE-1){
            iterator++;
        }else{
            iterator = 0;
        }


    }


    private void initGrid() throws SystemConfigTooWeekException {
        viewBoardTable = new BoxB[Y_SIZE][X_SIZE];
        /*
          start positions for scene centering
         */
        long initStartTime = System.currentTimeMillis();
        long counter = System.currentTimeMillis();
        int startXposition = 0 - WIDTH/2;
        int startYposition = 0 - HEIGHT/2;
        for (int boardYposition = 0; boardYposition < Y_SIZE; boardYposition++) {
            long timeTaken = System.currentTimeMillis() - initStartTime;

                if(timeTaken < 500){
                    if (boardYposition % 25 == 0) {
                        System.out.print(".");
                    }
                }

            for (int boardXposition = 0; boardXposition < X_SIZE; boardXposition++) {

                Xform boxXform = new Xform();
                BoxB boxToAdd = new BoxB(RECTANGLE_WIDTH,RECTANGLE_HEIGHT,RECTANGLE_HEIGHT, boardXposition, boardYposition);
                boxToAdd.setTranslateX(startXposition + boardXposition * RECTANGLE_WIDTH);
                boxToAdd.setTranslateY(startYposition + boardYposition * RECTANGLE_HEIGHT);
                /*
                Optional Z position fun
                 */
//                boxToAdd.setTranslateZ(  Math.sin((double) boxToAdd.getBoardX()/90) * 1000  );
                boxToAdd.setMaterial(new PhongMaterial(Color.RED));
                boxToAdd.setVisible(false);
//                boxToAdd.setEffect(new Bloom());
                /*
                    optional secondary color
                 */
//                ((PhongMaterial) boxToAdd.getMaterial()).setSpecularColor(Color.YELLOWGREEN);

                /*
                Optional maps here
                 */
//                ((PhongMaterial) boxToAdd.getMaterial()).setDiffuseMap(new Image("illum.jpg"));

                boxXform.getChildren().add(boxToAdd);

                viewBoardTable[boardYposition][boardXposition] = boxToAdd;
                viewBoard.getChildren().add(boxXform);
                if(timeTaken > 500){
                    long currentTime = System.currentTimeMillis();
                    if(counter - currentTime < -500){
                        counter = System.currentTimeMillis();
                        Logger.log( "\n- "+
                                new DecimalFormat("#0.0")
                                        .format((double)
                                                (X_SIZE*boardYposition+boardXposition)/(Y_SIZE*X_SIZE)*100) +" % ", this);
                    }
                }

                if(timeTaken > 5000){
                    throw new SystemConfigTooWeekException("Creating 3d View takes too long, impossible to run in 3D View");
                }
            }
        }
        initAuxiliaryItems();
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(0.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    /**
     * refresh method takes board as argument, scans model board and updates representing view board accordingly.
     *
     * ongoingUpdateFromModel and ongoingUpdateFromView must be set to false for update to take place
     * if not, function add a drop frame and discard this view update
     *
     * @param board - 2D board from model
     */
    @Override
    public void refresh(Dot[][] board) {
        if (!ongoingUpdateFromModel && !ongoingUpdateFromView) {
            ongoingUpdateFromModel = true;

            Platform.runLater(() -> {
                for (int i = 0; i < Y_SIZE; i++) {
                    for (int j = 0; j < X_SIZE; j++) {
                        BoxB box = viewBoardTable[i][j];
                        if (board[i][j] != null) {
                            box.setVisible(true);
                            ((PhongMaterial) box.getMaterial()).
                                    setDiffuseColor(board[i][j].getColor());
                        } else {
                            if(showDeadDot){
                                box.setVisible(true);
                                ((PhongMaterial) box.getMaterial()).setDiffuseColor(DEAD_COLOR);
                            }else{
                                box.setVisible(false);
                            }

                        }
                    }
                }
                renderedFrames++;
                ongoingUpdateFromModel = false;
            });
        } else {
            droppedFrames++;
        }
    }

    /**
     * handleInput method for handling view side and routing for InputHandler class
     *             and/or updateViewOnPos function
     *
     * @param me - any input event acceptable, mouse or key event are supported here, else is discarded
     */


    @Override
    public void handleMouse(MouseEvent me) {

//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
        if (me.getEventType().equals(MOUSE_PRESSED)){
//            @Override public void handle(MouseEvent me) {
                mousePressedTime = System.currentTimeMillis();
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
        }
//        });

//        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent me) {
        if (me.getEventType().equals(MOUSE_DRAGGED)){
                me.consume();
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() + mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() - mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
                }
//                else if (me.isSecondaryButtonDown()) {
//                    double z = camera.getTranslateZ();
//                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
//                    camera.setTranslateZ(newZ);
//                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
                }

        }
//        });

//        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent me) {
        if (me.getEventType().equals(MOUSE_RELEASED)){
                if(System.currentTimeMillis() - mousePressedTime < 300){
                    //TODO
//                    inputHandler.handleInput(me);
                    if (me.getButton() == MouseButton.PRIMARY) {
                        if(me.getPickResult().getIntersectedNode() != null
                                && me.getPickResult().getIntersectedNode().getClass().equals(BoxB.class)){
                            updateViewOnPos(me);


                        }

                    }
                }
            }
//        });
    }

    @Override
    public Text getText() {
        return text;
    }


    @Override
    public void handleKeyboard(KeyEvent event) {
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
                int speedModifier = 5;
                if(event.isShiftDown()){
                    speedModifier = 20;
                }
//                Point3D cameraAngle = camera.getRotationAxis();
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case L:
                        showDeadDot = !showDeadDot;
                        shrinkBoxes();
                        SharedResources.keyboardInput.add(KeyCode.L);
                        break;

                    case W:
                        camera.setTranslateZ(camera.getTranslateZ()+speedModifier);
                        break;
                    case S:
                        camera.setTranslateZ(camera.getTranslateZ()-speedModifier);
                        break;
                    case A:
                        camera.setTranslateX(camera.getTranslateX()-speedModifier);
                        break;
                    case D:
                        camera.setTranslateX(camera.getTranslateX()+speedModifier);
                        break;
                    case UP:
                        camera.setTranslateY(camera.getTranslateY()-speedModifier);
                        break;
                    case DOWN:
                        camera.setTranslateY(camera.getTranslateY()+speedModifier);
                        break;
                    case LEFT:
                        camera.setRotate(camera.getRotate()+2);
                        break;
                    case RIGHT:
                        camera.setRotate(camera.getRotate()-2);
                        break;
                    case V:
                        cornerObjects.setVisible(!cornerObjects.isVisible());
                        break;
                    default:
                        break;
                }
//            }
//        });
    }


    private void shrinkBoxes(){
        Platform.runLater(() -> {
            double amountToShrink = (double) RECTANGLE_HEIGHT/10;
            for (int i = 0; i < Y_SIZE; i++) {
                for (int j = 0; j < X_SIZE; j++) {
                    Box box = viewBoardTable[i][j];
                    if(showDeadDot){
                        box.setHeight(box.getHeight()- amountToShrink);
                        box.setWidth(box.getWidth()- amountToShrink);
                        box.setDepth(box.getDepth()- amountToShrink);
                    }else{
                        box.setHeight(box.getHeight()+ amountToShrink);
                        box.setWidth(box.getWidth()+ amountToShrink);
                        box.setDepth(box.getDepth()+ amountToShrink);
                    }

                }
            }
        });
    }


    private void initAuxiliaryItems(){
        int startXposition = 0 - WIDTH/2;
        int startYposition = 0 - HEIGHT/2;
        int[] xPositions = new int[]{startXposition, Math.abs(startXposition)};
        int[] yPositions = new int[]{startYposition, Math.abs(startYposition)};
        int[] zPositions = new int[]{-200,200};
        for (int z : zPositions) {
            for (int y : yPositions) {
                for (int x : xPositions) {
                    Xform boxXform = new Xform();
                    Box boxToAdd = new Box(RECTANGLE_WIDTH*5,RECTANGLE_HEIGHT*5,RECTANGLE_HEIGHT*5);
                    boxToAdd.setTranslateX(x);
                    boxToAdd.setTranslateY(y);
                    boxToAdd.setTranslateZ(z);
                    boxToAdd.setVisible(true);
                    boxToAdd.setMaterial(aliveMaterial);
                    boxXform.getChildren().add(boxToAdd);

                    cornerObjects.getChildren().add(boxXform);
/*
  Optional lights
 */
//                    PointLight light = new PointLight(Color.SNOW);
//                    light.setTranslateZ(z);
//                    light.setTranslateY(y);
//                    light.setTranslateX(x);
//                    cornerLights.getChildren().add(light);
                }

            }

        }
        Xform xform = new Xform();
        Box box = new Box(Math.abs(startXposition*2), RECTANGLE_HEIGHT*5, RECTANGLE_HEIGHT*5);
        box.setTranslateX(0);
        box.setTranslateY(startYposition);
        box.setTranslateZ(-200);
        box.setMaterial(new PhongMaterial(Color.color(0.1,0.1,0.1,0.1)));


        text.setY(startYposition);
        text.setTranslateZ(-200);
        text.setFill(Color.WHITE);
        text.setFont(new Font(30));
        xform.getChildren().add(text);

        xform.getChildren().add(box);

        viewBoard.getChildren().add(cornerObjects);
        viewBoard.getChildren().add(cornerLights);
        viewBoard.getChildren().add(xform);
    }


    /**
     * updateViewOnPos function
     * updates view side of model on-the-fly (before sending to model)
     * for immediate reaction to clicks
     *
     * ongoingUpdateFromView - variable for synchronising update rates - prevents flooding updates to view
     *
     * @param event - any input event acceptable, mouse or key event are supported here, else is discarded
     */
    private void updateViewOnPos(MouseEvent event) {
        ongoingUpdateFromView = true;
        Platform.runLater(() -> {
            if (event.getPickResult().getIntersectedNode() != null) {
                PhongMaterial material = ( (PhongMaterial) (  (Box) event.getPickResult().getIntersectedNode())  .getMaterial() );

                if (  material.getDiffuseColor().equals(DEAD_COLOR)  ) {
                    material.setDiffuseColor(Color.WHITE);
                }
                else {
                    material.setDiffuseColor(DEAD_COLOR);
                }
                ongoingUpdateFromView = false;
            }
        });

    }


    /**
     * getDroppedFrames function returns dropped frames count from last call
     * when called set droppedFrames to 0, and return count from call moment
     *
     * @return int count of dropped frames in time from last call to now
     */
    public int getDroppedFrames() {
        int droppedFramesCurrent = droppedFrames;
        droppedFrames = 0;
        return droppedFramesCurrent;
    }

    @Override
    public int getRenderedFrames() {
        int currentRenderedFrames = renderedFrames;
        renderedFrames = 0;
        return currentRenderedFrames;
    }

    @Override
    public String toString(){
        return "JavaFX 3D";
    }

}
