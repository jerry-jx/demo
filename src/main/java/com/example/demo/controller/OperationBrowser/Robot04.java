package com.example.demo.controller.OperationBrowser;

/**
 * Created by jerry-jx on 2018/4/3.
 */
import java.awt.*;
import java.awt.event.*;
/**this class will close an maxmimum IE window in the 1024*768's screen resolution's machine.*/
public class Robot04{
    public static void main(String[] args)
        throws AWTException{
        Robot robot = new Robot();
        robot.mouseMove(1005,10);
        robot.delay(2000);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(2000);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }//end main

}//end class Robot04

//这个程序的GUI版本。
 //   Robot04GUI.java



