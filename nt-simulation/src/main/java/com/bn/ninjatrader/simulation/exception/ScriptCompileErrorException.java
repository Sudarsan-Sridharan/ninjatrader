package com.bn.ninjatrader.simulation.exception;

/**
 * Exception thrown when there is compile error in groovy script.
 * This class strips unnecessary things from error message like
 * exception class, and script file name.
 *
 * Original Error Message:
 * org.codehaus.groovy.control.MultipleCompilationErrorsException: startup failed:
 * script1494748347907351156843.groovy: 53: The current scope already contains a variable of the name oneBarAgo
 * @ line 53, column 9.
 * def oneBarAgo = 'x';
 * ^
 * 1 error
 *
 * New Error Message:
 * Error at line 53: The current scope already contains a variable of the name oneBarAgo @ line 53, column 9.
 * def oneBarAgo = 'x';
 * ^
 * 1 error
 *
 * @author bradwee2000@gmail.com
 */
public class ScriptCompileErrorException extends RuntimeException {

  private static final String EXCEPTION_CLASS = "org.codehaus.groovy.control.MultipleCompilationErrorsException: ";
  private static final String SCRIPT_CLASS = "script(\\w)+[.]groovy: ";
  private static final String STARTUP_FAILED = "startup failed:";
  private static final String ERROR_AT_LINE = "Error at line ";
  private static final String BLANK = "";

  public ScriptCompileErrorException(Exception e) {
    super(e.getMessage()
        .replace(EXCEPTION_CLASS, BLANK)
        .replace(STARTUP_FAILED, ERROR_AT_LINE)
        .replaceAll(SCRIPT_CLASS, BLANK));
  }
}
