package com.blastbeatsandcode.clocksapp.controller;

/*
 * Interface for commands within the Clocks App
 * This will allow us to implement "Undo" and "Redo" actions.
 */
public interface ClockCommand {
    public void Execute();
    public void UnExecute();
}
