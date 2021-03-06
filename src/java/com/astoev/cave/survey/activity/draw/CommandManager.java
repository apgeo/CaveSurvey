package com.astoev.cave.survey.activity.draw;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: almondmendoza
 * Date: 15/11/2010
 * Time: 12:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommandManager {
    private List<DrawingPath> currentStack;
    private List<DrawingPath> redoStack;

    public CommandManager() {
        currentStack = Collections.synchronizedList(new ArrayList<DrawingPath>());
        redoStack = Collections.synchronizedList(new ArrayList<DrawingPath>());
    }

    public void addCommand(DrawingPath command) {
        redoStack.clear();
        currentStack.add(command);
    }

    public void undo() {
        final int length = currentStackLength();

        if (length > 0) {
            final DrawingPath undoCommand = currentStack.get(length - 1);
            currentStack.remove(length - 1);
            redoStack.add(undoCommand);
        }
    }

    public int currentStackLength() {
        return currentStack.toArray().length;
    }


    public void executeAll(Canvas canvas) {
        if (currentStack != null) {
            synchronized (currentStack) {
                final Iterator<DrawingPath> i = currentStack.iterator();

                while (i.hasNext()) {
                    final DrawingPath drawingPath = i.next();
                    drawingPath.draw(canvas);
                }
            }
        }
    }

    public boolean hasMoreRedo() {
        return redoStack.toArray().length > 0;
    }

    public boolean hasMoreUndo() {
        return currentStack.toArray().length > 0;
    }

    public void redo() {
        final int length = redoStack.toArray().length;
        if (length > 0) {
            final DrawingPath redoCommand = redoStack.get(length - 1);
            redoStack.remove(length - 1);
            currentStack.add(redoCommand);
        }
    }
}
