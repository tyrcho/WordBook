package info.daviot.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * Defines an excpetion which contains a nested exception. The stack trace of the nested exception is added to the usual stack trace.
 * 
 * @author BDE
 * @author MDA
 * @version NP
 */
public class SerializableExceptionWithNested extends Exception implements Serializable {
    private String nestedExceptionStackTrace  = "";
    private String nestedExceptionStackString = "";
    private Class  nestedExceptionClass       = Object.class;

    /**
     * Build a new SerializableExceptionWithNested.
     * 
     * @param descr
     *            a textual description
     */
    public SerializableExceptionWithNested(String descr) {
        this(descr, null);
    }

    /**
     * Build a new SerializableExceptionWithNested based on given exception
     * 
     * @param descr
     *            a textual description
     * @param nested
     *            The source exception that caused the problem
     */
    public SerializableExceptionWithNested(String descr, Throwable nested) {
        super(descr);
        if (nested != null) {
            setNestedExceptionStackTrace(throwableToString(nested));
            setNestedExceptionStackString(nested.toString());
            nestedExceptionClass = nested.getClass();
        }
    }

    /**
     * Converts the exception to a String with the full stacktrace
     * 
     * @param exception
     *            the source exception
     * @return the stack trace
     */
    public static String throwableToString(Throwable exception) {
        if (exception == null) return "";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }

    /**
     * Converts the exception to a String with the full stacktrace
     * 
     * @return the stack trace
     */
    public String throwableToString() {
        return throwableToString(this);
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (!"".equals(nestedExceptionStackTrace)) {
            pw.println("Nested Exception:");
            pw.println(nestedExceptionStackTrace);
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (!"".equals(nestedExceptionStackTrace)) {
            ps.println("Nested Exception:");
            ps.println(nestedExceptionStackTrace);
        }
    }

    public String getNestedExceptionStackTrace() {
        return nestedExceptionStackTrace;
    }

    private void setNestedExceptionStackTrace(String nestedExceptionStackTrace) {
        this.nestedExceptionStackTrace = nestedExceptionStackTrace;
    }

    public String getNestedExceptionStackString() {
        return nestedExceptionStackString;
    }

    private void setNestedExceptionStackString(String nestedExceptionStackString) {
        this.nestedExceptionStackString = nestedExceptionStackString;
    }

    /**
     * Gets the nestedExceptionClass
     * 
     * @return Returns a Class
     */
    public Class getNestedExceptionClass() {
        return nestedExceptionClass;
    }

    /**
     * Sets the nestedExceptionClass
     * 
     * @param nestedExceptionClass
     *            The nestedExceptionClass to set
     */
    public void setNestedExceptionClass(Class nestedExceptionClass) {
        this.nestedExceptionClass = nestedExceptionClass;
    }

}