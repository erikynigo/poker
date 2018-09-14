package com.poker.exceptions;

/**
 * The IllegalHandException is thrown to indicate that a Hand object
 * does not meet the necessary requirements to be deemed a valid Hand.
 * Reasons for this include the Hand having less Card objects than what
 * GameConstants.CARDS_IN_HAND requires, having more Card objects, or
 * having duplicate Card objects.
 *
 * @author Erik Ynigo
 */
public class IllegalHandException extends Exception
{
    public IllegalHandException(String message)
    {
        super(message);
    }
}
