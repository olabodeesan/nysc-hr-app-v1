/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.exceptions;

import javax.faces.context.ExceptionHandler;

/**
 *
 * @author Ironhide
 */
public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {

    private javax.faces.context.ExceptionHandlerFactory parent;

    public ExceptionHandlerFactory(
            javax.faces.context.ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new CustomExceptionHandler(result);
        return result;
    }
}
