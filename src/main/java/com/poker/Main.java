package com.poker;

import com.poker.exceptions.CannotEvaluateException;
import com.poker.exceptions.IllegalHandException;
import com.poker.metadata.Rank;
import com.poker.metadata.Suit;
import com.poker.model.Card;
import com.poker.model.Hand;
import com.poker.model.PokerHandEvaluation;
import com.poker.service.PokerHandEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class, used to showcase example usage of this application, such as:
 * hand creation, evaluator construction, evaluation api, result interpretation
 * and printing out the result.
 *
 * @author Erik Ynigo
 */
public class Main
{
    public static void main(String[] args) throws IllegalHandException, CannotEvaluateException
    {
        // Sample usage
        final Hand hand1 = new Hand(new ArrayList<Card>() {{
            add(new Card(Rank.TWO, Suit.HEART));
            add(new Card(Rank.THREE, Suit.DIAMOND));
            add(new Card(Rank.FOUR, Suit.SPADE));
            add(new Card(Rank.FIVE, Suit.CLUB));
            add(new Card(Rank.SIX, Suit.SPADE));
        }});

        final Hand hand2 = new Hand(new ArrayList<Card>() {{
            add(new Card(Rank.THREE, Suit.CLUB));
            add(new Card(Rank.FOUR, Suit.CLUB));
            add(new Card(Rank.FIVE, Suit.CLUB));
            add(new Card(Rank.SIX, Suit.CLUB));
            add(new Card(Rank.SEVEN, Suit.DIAMOND));
        }});

        List<Hand> hands = new ArrayList<Hand>() {{
            add(hand1);
            add(hand2);
        }};

        // Create evaluator
        PokerHandEvaluator pokerHandEvaluator = new PokerHandEvaluator();

        // Evaluate
        // Note that evaluation returns a list of winners, as it's possible
        // to have more than one winner. In this case, the pot is split.
        List<PokerHandEvaluation> winners = pokerHandEvaluator.evaluate(hands);

        // Print out winner
        if (winners.size() == 1)
        {
            PokerHandEvaluation winner = winners.get(0);
            System.out.println("Winning hand:\n");
            System.out.println(winner.toString());
        }
        else
        {
            System.out.println("Tie! Split the pot between:\n");
            for (PokerHandEvaluation winner : winners)
            {
                System.out.println(winner.toString()+"\n");
            }

        }
    }
}
