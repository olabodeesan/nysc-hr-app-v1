/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.exceptions;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler parent;

    public CustomExceptionHandler(ExceptionHandler parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.parent;
    }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i =
                getUnhandledExceptionQueuedEvents().iterator();
                i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav =
                        fc.getApplication().getNavigationHandler();
                try {
                    nav.handleNavigation(fc, null, "/login.jsf?faces-redirect=true");
                    fc.renderResponse();                 
                } finally {
                    i.remove();
                }
            }
        }
        getWrapped().handle();
    }
}
