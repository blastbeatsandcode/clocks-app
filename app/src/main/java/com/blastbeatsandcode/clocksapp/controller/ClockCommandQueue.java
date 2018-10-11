package com.blastbeatsandcode.clocksapp.controller;

import java.util.ArrayList;
import java.util.List;

/*
 * Queue for storing commands.
 * This enables us to add Undo and Redo funcationality
 */
public class ClockCommandQueue
{
    // Holds a list of commands
    private List<ClockCommand> _stack = new ArrayList<ClockCommand>();

    private int _curIndex = 0;
    private int _maxIndex = 0;

    // Make the command queue a singleton
    private static ClockCommandQueue instance = null;
    public static ClockCommandQueue getInstance()
    {
        if (instance == null)
        {
            instance = new ClockCommandQueue();
        }
        return instance;
    }

    /*
     * Adds clock command to queue
     */
    public void add(ClockCommand c)
    {
        _stack.add(c);
        _curIndex++;
        _maxIndex = _curIndex;
    }

    /*
     * Performs an "Undo" operation.
     */
    public void undo()
    {
        // If 0 or below, we don't have any more commands to Undo
        if (_curIndex > 0) {
            _curIndex--; // Decrement current index
            _stack.get(_curIndex).UnExecute();
        }
    }


    /*
     * Performs a "Redo" operation
     */
    public void redo()
    {
        if(_curIndex < _maxIndex)
        {
            _stack.get(_curIndex).Execute();
            _curIndex++; // Increment current index
        }
    }
}
