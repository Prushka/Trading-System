package phase2.trade.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Dummy implementation of the Material Design Tooltip
 *
 * Issues:
 * <ul>
 *     <li>The colour palette might be off by a margin (as stated in the CSS file)</li>
 * </ul>
 */
public class JFXTooltip extends Tooltip {
    private static final String STYLE_SHEET =
            JFXTooltip.class.getResource("/css/jfx-tooltip.css").toExternalForm();
    private static final String DEFAULT_CSS_CLASS = "jfx-tooltip";
    private Node layoutNode;

    /**
     * The JFXTooltip allow the use of a Material Design Tooltip.
     *
     * Issues:
     * <ul>
     *     <li>The colour palette might be off by a margin (as stated in the CSS file)</li>
     * </ul>
     *
     * @param tooltip    The text that should be displayed
     * @param layoutNode The node that will be used to determine the position of this tooltip. The node should not be null.
     * @throws NullPointerException Throws a NullPointerException, if the layoutNode is null.
     * @see <a href="https://material.io/guidelines/components/tooltips.html#tooltips-tooltips-desktop">Material Design Desktop Tooltip</a>
     */
    public JFXTooltip(String tooltip, Node layoutNode) {
        super(tooltip);
        this.layoutNode = layoutNode;
        initialize();
    }

    /**
     * The JFXTooltip allow the use of a Material Design Tooltip.
     *
     * Issues:
     * <ul>
     *     <li>The colour palette might be off by a margin (as stated in the CSS file)</li>
     * </ul>
     *
     * @param layoutNode The node that will be used to determine the position of this tooltip. The node should not be null.
     * @throws NullPointerException Throws a NullPointerException, if the layoutNode is null.
     * @see <a href="https://material.io/guidelines/components/tooltips.html#tooltips-tooltips-desktop">Material Design Desktop Tooltip</a>
     */
    public JFXTooltip(Node layoutNode) {
        super();
        this.layoutNode = layoutNode;
        initialize();
    }

    private void initialize() {
        // Check for a null value (use @NotNull)
        if (layoutNode == null) {
            throw new NullPointerException("The tooltips layout node is null");
        }
        // Adding default style information
        this.getStyleClass().add(DEFAULT_CSS_CLASS);
        this.getScene().setUserAgentStylesheet(STYLE_SHEET);
        this.setFont(Font.font("Roboto Medium"));
        this.setPrefHeight(22);
        // The popup should adjust itself, iff it would overflow the screen
        this.setAutoFix(true);
        // Try to calculate the bounds before doing anything
        this.getScene().getRoot().autosize();
        // Do this, if the tooltip is displayed
        this.setOnShown(ev -> {
            // Get the horizontal center and the vertical top of the layoutNode
            // relative to the screen
            Point2D nodeLayout = layoutNode.localToScreen(
                    0.5 * (layoutNode.getLayoutBounds().getMinX() +
                            layoutNode.getLayoutBounds().getWidth()),
                    layoutNode.getLayoutBounds().getMinY());
            // Adjust those values (the tooltip has built in hard coded offset)
            // to be at the bottom end of node
            double ownerX = nodeLayout.getX();
            double ownerY = nodeLayout.getY() - 7 +
                    layoutNode.getLayoutBounds().getHeight();
            // Adjust the coordinates of the tooltip according to its width
            // (the height is constant, because of the Material Design specification)
            Parent parent = this.getScene().getRoot();
            this.setAnchorX(ownerX - 0.5 * (parent.getBoundsInParent().getWidth() +
                    parent.getBoundsInParent().getMinX()));
            this.setAnchorY(ownerY + 14); // Distance of the tooltip to the actual
            // node (14 Pixel as in the desktop specification)
            // Fade in transition (duration: 150ms)
            FadeTransition transition = new FadeTransition(Duration.millis(150),
                    this.getScene().getRoot());
            transition.setFromValue(0);
            transition.setToValue(0.9);
            transition.setCycleCount(1);
            // Set the deceleration curve to show this tooltip
            transition.setInterpolator(Interpolator.SPLINE(0.0, 0.0, 0.2, 1));
            transition.play();
        });
        // Do this, if the tooltip will be hiding
        this.setOnHiding(ev -> {
            // Fade out transition (duration: 150ms, is shown for 1.5s after leaving the associated node)
            FadeTransition transition = new FadeTransition(Duration.millis(150),
                    this.getScene().getRoot());
            transition.setFromValue(0.9);
            transition.setToValue(0);
            transition.setCycleCount(1);
            transition.setDelay(Duration.millis(1500));
            // Set the acceleration curve to hide this tooltip
            transition.setInterpolator(Interpolator.SPLINE(0.4, 0.0, 1, 1));
            transition.play();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(Node ownerNode, double layoutX, double layoutY) {
        this.layoutNode = ownerNode;
        super.show(ownerNode, layoutX, layoutY);
    }

    public Node getLayoutNode() {
        return layoutNode;
    }

    public void setLayoutNode(Node layoutNode) {
        this.layoutNode = layoutNode;
    }
}