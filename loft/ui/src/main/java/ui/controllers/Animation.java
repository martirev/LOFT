package ui.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Class for running animation on Login, Register and userInfo screen.
 * The class has methods for animating the weightbar, weights and arms on the person
 * The class also contains methods for stopping the animations when leaving the screen
 */
public abstract class Animation extends SceneSwitcher {
    private List<TranslateTransition> transitions = new ArrayList<>();
    private List<Timeline> timelines = new ArrayList<>();

    @FXML
    private Line bar;

    @FXML
    private Rectangle weightRightRectangle1;

    @FXML
    private Rectangle weightRightRectangle2;

    @FXML
    private Rectangle weightLeftRectangle1;

    @FXML
    private Rectangle weightLeftRectangle2;

    @FXML
    private Line upperRightLine1;

    @FXML
    private Line upperRightLine2;

    @FXML
    private Line upperLeftLine1;

    @FXML
    private Line upperLeftLine2;

    @FXML
    private Line lowerRightLine1;

    @FXML
    private Line lowerRightLine2;

    @FXML
    private Line lowerLeftLine1;

    @FXML
    private Line lowerLeftLine2;

    /**
    * Method initializing an aninmation, which moves the bar up and down indefinite.
    *
    * @param bar the weight bar
    */
    private void playAnimationBar(Line bar) {
        TranslateTransition translateTransitionBar = new TranslateTransition();
        transitions.add(translateTransitionBar);
        translateTransitionBar.setAutoReverse(true);
        translateTransitionBar.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransitionBar.setByY(45);
        translateTransitionBar.setDuration(Duration.seconds(4));
        translateTransitionBar.setNode(bar);
        translateTransitionBar.play();
    }

    /**
    * Method for animating the movements of the weights on the bar.  
    *
    * @param weight the weights on the bar
    */
    private void playAnimationWeight(Rectangle weight) {
        TranslateTransition translateTransitionWeights = new TranslateTransition();
        transitions.add(translateTransitionWeights);
        translateTransitionWeights.setAutoReverse(true);
        translateTransitionWeights.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransitionWeights.setByY(45);
        translateTransitionWeights.setDuration(Duration.seconds(4));
        translateTransitionWeights.setNode(weight);
        translateTransitionWeights.play();
    }

    /**
     * Method for animating the movement of the arms during lifting.
     *
     * @param lowerLeft1  lower left arm top
     * @param lowerLeft2  lower left arm bottom
     * @param lowerRight1 lower right arm top
     * @param lowerRight2 lower right arm bottom
     * @param upperRight1 upper right arm top
     * @param upperRight2 upper right arm bottom
     * @param upperLeft1  upper left arm top
     * @param upperleft2  upper left arm bottom
     */
    private void playAnimationArm(Line lowerLeft1, Line lowerLeft2,
                Line lowerRight1, Line lowerRight2, Line upperRight1, Line upperRight2,
                Line upperLeft1, Line upperleft2) {
        Rotate rotation1 = new Rotate();
        rotation1.pivotXProperty().bind(lowerLeft1.endXProperty());
        rotation1.pivotYProperty().bind(lowerLeft1.endYProperty());
        lowerLeft1.getTransforms().add(rotation1);
        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation1.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation1.angleProperty(), -75)));
        timelines.add(timeline1);
        timeline1.setAutoReverse(true);
        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline1.play();

        Rotate rotation2 = new Rotate();
        rotation2.pivotXProperty().bind(lowerLeft2.endXProperty());
        rotation2.pivotYProperty().bind(lowerLeft2.endYProperty());
        lowerLeft2.getTransforms().add(rotation2);
        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation2.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation2.angleProperty(), -65)));
        timelines.add(timeline2);
        timeline2.setAutoReverse(true);
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();

        Rotate rotation3 = new Rotate();
        rotation3.pivotXProperty().bind(lowerRight1.endXProperty());
        rotation3.pivotYProperty().bind(lowerRight1.endYProperty());
        lowerRight1.getTransforms().add(rotation3);
        Timeline timeline3 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation3.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation3.angleProperty(), 75)));
        timelines.add(timeline3);
        timeline3.setAutoReverse(true);
        timeline3.setCycleCount(Timeline.INDEFINITE);
        timeline3.play();

        Rotate rotation4 = new Rotate();
        rotation4.pivotXProperty().bind(lowerRight2.endXProperty());
        rotation4.pivotYProperty().bind(lowerRight2.endYProperty());
        lowerRight2.getTransforms().add(rotation4);
        Timeline timeline4 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation4.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation4.angleProperty(), 65)));
        timelines.add(timeline4);
        timeline4.setAutoReverse(true);
        timeline4.setCycleCount(Timeline.INDEFINITE);
        timeline4.play();

        Rotate rotation5 = new Rotate();
        upperLeft1.getTransforms().add(rotation5);
        Timeline timeline5 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation5.angleProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperLeft1.translateYProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperLeft1.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation5.angleProperty(), 40)),
                new KeyFrame(Duration.seconds(4), new KeyValue(upperLeft1.translateYProperty(), 9)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperLeft1.translateXProperty(), -9)));
        timelines.add(timeline5);
        timeline5.setAutoReverse(true);
        timeline5.setCycleCount(Timeline.INDEFINITE);
        timeline5.play();

        Rotate rotation6 = new Rotate();
        upperRight1.getTransforms().add(rotation6);
        Timeline timeline6 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation6.angleProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperRight1.translateYProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperRight1.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation6.angleProperty(), -47)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperRight1.translateYProperty(), 43)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperRight1.translateXProperty(), 18)));
        timelines.add(timeline6);
        timeline6.setAutoReverse(true);
        timeline6.setCycleCount(Timeline.INDEFINITE);
        timeline6.play();

        Rotate rotation7 = new Rotate();
        upperleft2.getTransforms().add(rotation7);
        Timeline timeline7 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation7.angleProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperleft2.translateYProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperleft2.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation7.angleProperty(), 47)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperleft2.translateYProperty(), 25)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperleft2.translateXProperty(), -26)));
        timelines.add(timeline7);
        timeline7.setAutoReverse(true);
        timeline7.setCycleCount(Timeline.INDEFINITE);
        timeline7.play();

        Rotate rotation8 = new Rotate();
        upperRight2.getTransforms().add(rotation8);
        Timeline timeline8 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation8.angleProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperRight2.translateYProperty(), 0)),
                new KeyFrame(Duration.ZERO, new KeyValue(upperRight2.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(rotation8.angleProperty(), -47)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperRight2.translateYProperty(), 58)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(upperRight2.translateXProperty(), 37)));
        timelines.add(timeline8);
        timeline8.setAutoReverse(true);
        timeline8.setCycleCount(Timeline.INDEFINITE);
        timeline8.play();
    }

    /**
     * Method for starting all animations.
     */
    protected void startAnimations() {
        playAnimationBar(bar);
        playAnimationWeight(weightRightRectangle1);
        playAnimationWeight(weightRightRectangle2);
        playAnimationWeight(weightLeftRectangle1);
        playAnimationWeight(weightLeftRectangle2);
        playAnimationArm(lowerLeftLine1, lowerLeftLine2,
                lowerRightLine1, lowerRightLine2, upperRightLine1, upperRightLine2,
                upperLeftLine1, upperLeftLine2);
    }

    /**
     * Method for pausing transistion when leaving scene.
     */
    protected void stopAllAnimations() {
        for (TranslateTransition transition : transitions) {
            stopTransitions(transition);
        }
        for (Timeline timeline : timelines) {
            stopTimelines(timeline);
        }
    }

    private void stopTransitions(TranslateTransition transition) {
        transition.stop();
    }

    private void stopTimelines(Timeline timeline) {
        timeline.stop();
    }

}
