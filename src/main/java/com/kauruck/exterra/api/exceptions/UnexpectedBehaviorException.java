package com.kauruck.exterra.api.exceptions;

/**
 * A general error that is thrown, when something happened, that should really be impossible to happen.
 * This error "should" never appear in your log.
 * @since 1.0
 */
public class UnexpectedBehaviorException extends Exception {
    public UnexpectedBehaviorException(String message) {
        super("There was an unexpected Behavior in the mod. Please report the error, with the following message: " + message);
    }
}
