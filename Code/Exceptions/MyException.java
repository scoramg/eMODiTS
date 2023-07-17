/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author amarquezgr
 */
public class MyException extends Exception {
    public MyException() { 
        super(); 
    }
    public MyException(String message) { 
        super(message); 
    }
    public MyException(String message, Throwable cause) { 
        super(message, cause); 
    }
    public MyException(Throwable cause) { 
        super(cause); 
    }
}
