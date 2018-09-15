package com.poker.exceptions;

/**
 * The CannotEvaluateException is thrown to indicate that PokerHandEvaluator
 * was unable to properly evaluate the Hands submitted. Reasons include
 * the Hands list being null, or having less than two Hand objects (as at
 * least two Hand objects are needed to evaluate one against the other to
 * determine a winning Hand).
 *
 * @author Erik Ynigo
 */
public class CannotEvaluateException extends Exception
{
    public CannotEvaluateException(String message)
    {
        super(message);
    }
}
