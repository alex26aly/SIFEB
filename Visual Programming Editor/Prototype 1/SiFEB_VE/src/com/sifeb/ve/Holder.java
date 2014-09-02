/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Pubudu
 */
public class Holder extends Pane {

    private Image blockImg, crossImg;
    private VBox actions;
    MainEditorController mainCtrl;
    private Button button;

    public Holder(MainEditorController mainCtrl) {

        this.setBlockImg(new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Holder.png")));
        this.crossImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/cross.png"));
        this.button = new Button();

        setButtonProperties(this.button);
        changeBackgroundOnHoverUsingEvents(this.button);
        super.getChildren().add(this.button);

        System.out.println("here");
        this.mainCtrl = mainCtrl;
        this.actions = new VBox();
        this.actions.relocate(18, 15);

        super.getChildren().add(this.actions);

        setEventHandlers();
    }

    public void setEventHandlers() {
        this.setOnDragDropped((DragEvent event) -> {
            System.out.println("dropped");
            Dragboard db = event.getDragboard();
            Parent p = ((Node) event.getGestureSource()).getParent();
            boolean success = false;
            if (db.hasString()) {
                String nodeId = db.getString();
                ActuatorBlock draggedBlock = (ActuatorBlock) p.lookup("#" + nodeId);

                if (draggedBlock != null) {                    
                    this.mainCtrl.addHolderAfterMe(this);
                    if (p.getClass().getName().contains("ActionBlock")) {
                        draggedBlock = new ActuatorBlock(draggedBlock.getType(), draggedBlock.getBlockImg(), draggedBlock.getBtnImg(), draggedBlock.isDragable());
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    if (draggedBlock.getType().equals("action")) {
                        this.addElementToVbox(draggedBlock);
                        success = true;
                    } else if (draggedBlock.getType().equals("actionC")) {
                        this.mainCtrl.changeHolderType(this, draggedBlock);
                        success = true;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        this.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                String dbStr = event.getDragboard().getString();
                if (dbStr.contains("action")) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    System.out.println("not allowed");
                }
            }
            event.consume();
        });

    }

    public void deleteElement() {

        ((Pane) this.getParent()).getChildren().remove(this);

    }

    public void addElementToVbox(Node node) {
        actions.getChildren().add(node);

    }

    public VBox getActions() {
        return actions;
    }

    public void setActions(VBox actions) {
        this.actions = actions;
    }

    public void setBlockImg(Image blockImg) {
        this.blockImg = blockImg;
        super.setPrefSize(this.blockImg.getWidth(), this.blockImg.getHeight());
        super.setBackground(new Background(new BackgroundImage(this.blockImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    private void setButtonProperties(Button button) {

        button.relocate(0, 7);
        button.setGraphic(new ImageView(this.crossImg));
        button.setMaxHeight(this.crossImg.getHeight());
        button.setMaxWidth(this.crossImg.getWidth());
        button.setMinHeight(this.crossImg.getHeight());
        button.setMinWidth(this.crossImg.getWidth());
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: transparent");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                deleteElement();

            }
        });
    }

    public void changeBackgroundOnHoverUsingEvents(final Node node) {

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(new DropShadow());
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(null);
            }
        });
    }

}
