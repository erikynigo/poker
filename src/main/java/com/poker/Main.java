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
 * Created by ynige001 on 9/11/18.
 */
public class Main
{
    public static void main(String[] args) throws IllegalHandException, CannotEvaluateException
    {
        // Sample usage
        final Hand aceHighHand = new Hand(new ArrayList<Card>() {{
            add(new Card(Rank.ACE, Suit.SPADE));
            add(new Card(Rank.TWO, Suit.CLUB));
            add(new Card(Rank.THREE, Suit.DIAMOND));
            add(new Card(Rank.SEVEN, Suit.HEART));
            add(new Card(Rank.TEN, Suit.CLUB));
        }});

        final Hand pairOfSevensHand = new Hand(new ArrayList<Card>() {{
            add(new Card(Rank.SEVEN, Suit.CLUB));
            add(new Card(Rank.SEVEN, Suit.DIAMOND));
            add(new Card(Rank.THREE, Suit.HEART));
            add(new Card(Rank.TWO, Suit.SPADE));
            add(new Card(Rank.EIGHT, Suit.CLUB));
        }});

        List<Hand> hands = new ArrayList<Hand>() {{
            add(aceHighHand);
            add(pairOfSevensHand);
        }};

        // Create evaluator
        PokerHandEvaluator pokerHandEvaluator = new PokerHandEvaluator();

        // Evaluate
        // Note that evaluation returns a list of winners, as it's possible
        // to have more than one winner. In this case, the pot is split.
        List<PokerHandEvaluation> winners = pokerHandEvaluator.evaluate(hands);

        // Print out winner
        PokerHandEvaluation winner = winners.get(0);
        System.out.print("Winning hand: \n" + winner.toString());
    }
}
