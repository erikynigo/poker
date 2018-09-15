package com.poker.metadata;

/**
 * Reusable constants utilized throughout this application.
 *
 * @author Erik Ynigo
 */
public class GameConstants
{
    public static final int CARDS_IN_HAND = 5;

    // Used for printing out the evaluation only
    public static final String[] SIMPLIFIED_RANK = {null,null,"2","3","4","5","6","7","8","9","10","J","Q","K","A"};

    // Used for calculating the kicker score
    public static final Float[] KICKER_POSITION_MULTIPLIER = { 1.0f, 0.1f, 0.01f, 0.001f, 0.0001f};
}
