package com.blastbeatsandcode.clocksapp.controller;

import java.util.ArrayList;
import java.util.List;

public class ClockCommandQueue
{
    // Holds a list of commands
    private static List<ClockCommand> _stack = new ArrayList<ClockCommand>();

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
        //c.Execute();

    }

    /*
     * Performs an "Undo" operation.
     */
    public void undo()
    {
        _stack.get(_curIndex).UnExecute();
        _curIndex--; // Decrement current index
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
