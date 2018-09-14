package com.poker.model;

import com.poker.metadata.Category;
import com.poker.metadata.GameConstants;
import com.poker.metadata.Rank;
import com.poker.metadata.Suit;

import java.util.List;

/**
 * The PokerHandEvaluation class is meant to be a transfer object that contains
 * data related to the evaluation of a Hand object. It includes the Hand
 * object that was evaluated, the highest ranking Category achieved by the
 * hand, the highest value within the Category (e.g. if the category was
 * TWO_PAIR, highest would contain the value of the rank of the greater
 * pair), and a score that is assigned to the hand to make evaluation
 * between PokerHandEvaluation objects easier. This score is determined by
 * adding up the score of the Category plus the highest Rank achieved in
 * the category.
 *
 * @author Erik Ynigo
 */
public class PokerHandEvaluation
{
    // The hand this score is representing
    public Hand hand;

    // The highest ranking category the hand achieved
    public Category category;

    // The highest value in the category achieved
    public Rank highestRank;

    // The highest values that fall outside the Category achieved. For example,
    // consider the hands [A][K][10][5][3] vs [A][K][10][5][2]
    // Both this hands would meet the HIGH_CARD category, so highestRank would
    // be [A]. We would have to go down to the last kicker card, where we would
    // determine that the hand [A][K][10][5][3] is the winner.
    public List<Rank> kickers;

    // A score assigned to the hand, based on the category and
    // highest rank achieved
    public int getScore()
    {
        if (category == null || highestRank == null)
        {
            return 0;
        }

        return category.getValue() + highestRank.getValue();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Hand: ");

        for (Card card : hand.getCards())
        {
            sb.append(getSimplifiedRank(card.getRank()));
            sb.append(getSimplifiedSuit(card.getSuit()));
            sb.append(" ");
        }

        sb.append("\nCategory: ").append(category.toString());
        sb.append("\nKickers: ");

        if (kickers != null)
        {
            for (Rank rank : kickers)
            {
                sb.append(getSimplifiedRank(rank));
            }
        }

        return sb.toString();
    }

    private String getSimplifiedRank(Rank rank)
    {
        return GameConstants.SIMPLIFIED_RANK[rank.getValue()];
    }

    private String getSimplifiedSuit(Suit suit)
    {
        if (suit == Suit.CLUB)
        {
            return "♣";
        }
        else if (suit == Suit.HEART)
        {
            return "♥";
        }
        else if (suit == Suit.SPADE)
        {
            return "♠";
        }
        else if (suit == Suit.DIAMOND)
        {
            return "♦";
        }

        return "?";
    }
}
