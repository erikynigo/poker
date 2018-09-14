package com.poker;

import com.poker.metadata.Rank;
import com.poker.metadata.Suit;
import com.poker.model.Card;
import com.poker.model.Hand;

import java.util.ArrayList;

/**
 * Reusable constants utilized throughout the testing process of this application.
 *
 * @author Erik Ynigo
 */
@SuppressWarnings("Duplicates")
public class TestConstants
{
    // Cards
    private static Card ACE_OF_HEARTS = new Card(Rank.ACE, Suit.HEART);
    private static Card ACE_OF_DIAMONDS = new Card(Rank.ACE, Suit.DIAMOND);
    private static Card ACE_OF_SPADES = new Card(Rank.ACE, Suit.SPADE);
    private static Card ACE_OF_CLUBS = new Card(Rank.ACE, Suit.CLUB);

    private static Card KING_OF_HEARTS = new Card(Rank.KING, Suit.HEART);
    private static Card KING_OF_DIAMONDS = new Card(Rank.KING, Suit.DIAMOND);
    private static Card KING_OF_SPADES = new Card(Rank.KING, Suit.SPADE);
    private static Card KING_OF_CLUBS = new Card(Rank.KING, Suit.CLUB);

    private static Card QUEEN_OF_HEARTS = new Card(Rank.QUEEN, Suit.HEART);
    private static Card QUEEN_OF_DIAMONDS = new Card(Rank.QUEEN, Suit.DIAMOND);
    private static Card QUEEN_OF_SPADES = new Card(Rank.QUEEN, Suit.SPADE);
    private static Card QUEEN_OF_CLUBS = new Card(Rank.QUEEN, Suit.CLUB);

    private static Card JACK_OF_HEARTS = new Card(Rank.JACK, Suit.HEART);
    private static Card JACK_OF_DIAMONDS = new Card(Rank.JACK, Suit.DIAMOND);
    private static Card JACK_OF_SPADES = new Card(Rank.JACK, Suit.SPADE);
    private static Card JACK_OF_CLUBS = new Card(Rank.JACK, Suit.CLUB);

    private static Card TEN_OF_HEARTS = new Card(Rank.TEN, Suit.HEART);
    private static Card TEN_OF_DIAMONDS = new Card(Rank.TEN, Suit.DIAMOND);
    private static Card TEN_OF_SPADES = new Card(Rank.TEN, Suit.SPADE);
    private static Card TEN_OF_CLUBS = new Card(Rank.TEN, Suit.CLUB);

    private static Card NINE_OF_HEARTS = new Card(Rank.NINE, Suit.HEART);
    private static Card NINE_OF_DIAMONDS = new Card(Rank.NINE, Suit.DIAMOND);
    private static Card NINE_OF_SPADES = new Card(Rank.NINE, Suit.SPADE);
    private static Card NINE_OF_CLUBS = new Card(Rank.NINE, Suit.CLUB);

    private static Card EIGHT_OF_HEARTS = new Card(Rank.EIGHT, Suit.HEART);
    private static Card EIGHT_OF_DIAMONDS = new Card(Rank.EIGHT, Suit.DIAMOND);
    private static Card EIGHT_OF_SPADES = new Card(Rank.EIGHT, Suit.SPADE);
    private static Card EIGHT_OF_CLUBS = new Card(Rank.EIGHT, Suit.CLUB);

    private static Card SEVEN_OF_HEARTS = new Card(Rank.SEVEN, Suit.HEART);
    private static Card SEVEN_OF_DIAMONDS = new Card(Rank.SEVEN, Suit.DIAMOND);
    private static Card SEVEN_OF_SPADES = new Card(Rank.SEVEN, Suit.SPADE);
    private static Card SEVEN_OF_CLUBS = new Card(Rank.SEVEN, Suit.CLUB);

    private static Card SIX_OF_HEARTS = new Card(Rank.SIX, Suit.HEART);
    private static Card SIX_OF_DIAMONDS = new Card(Rank.SIX, Suit.DIAMOND);
    private static Card SIX_OF_SPADES = new Card(Rank.SIX, Suit.SPADE);
    private static Card SIX_OF_CLUBS = new Card(Rank.SIX, Suit.CLUB);

    private static Card FIVE_OF_HEARTS = new Card(Rank.FIVE, Suit.HEART);
    private static Card FIVE_OF_DIAMONDS = new Card(Rank.FIVE, Suit.DIAMOND);
    private static Card FIVE_OF_SPADES = new Card(Rank.FIVE, Suit.SPADE);
    private static Card FIVE_OF_CLUBS = new Card(Rank.FIVE, Suit.CLUB);

    private static Card FOUR_OF_HEARTS = new Card(Rank.FOUR, Suit.HEART);
    private static Card FOUR_OF_DIAMONDS = new Card(Rank.FOUR, Suit.DIAMOND);
    private static Card FOUR_OF_SPADES = new Card(Rank.FOUR, Suit.SPADE);
    private static Card FOUR_OF_CLUBS = new Card(Rank.FOUR, Suit.CLUB);

    private static Card THREE_OF_HEARTS = new Card(Rank.THREE, Suit.HEART);
    private static Card THREE_OF_DIAMONDS = new Card(Rank.THREE, Suit.DIAMOND);
    private static Card THREE_OF_SPADES = new Card(Rank.THREE, Suit.SPADE);
    private static Card THREE_OF_CLUBS = new Card(Rank.THREE, Suit.CLUB);

    private static Card TWO_OF_HEARTS = new Card(Rank.TWO, Suit.HEART);
    private static Card TWO_OF_DIAMONDS = new Card(Rank.TWO, Suit.DIAMOND);
    private static Card TWO_OF_SPADES = new Card(Rank.TWO, Suit.SPADE);
    private static Card TWO_OF_CLUBS = new Card(Rank.TWO, Suit.CLUB);

    // Example hands for quick scenario testing
    public static Hand ACE_HIGH_HAND = new Hand(new ArrayList<Card>() {{
        add(ACE_OF_SPADES);
        add(TWO_OF_CLUBS);
        add(THREE_OF_DIAMONDS);
        add(SEVEN_OF_HEARTS);
        add(TEN_OF_CLUBS);
    }});

    public static Hand KING_HIGH_HAND = new Hand(new ArrayList<Card>() {{
        add(KING_OF_SPADES);
        add(QUEEN_OF_DIAMONDS);
        add(JACK_OF_CLUBS);
        add(TEN_OF_HEARTS);
        add(EIGHT_OF_HEARTS);
    }});

    public static Hand PAIR_OF_SEVENS_HAND = new Hand(new ArrayList<Card>() {{
        add(SEVEN_OF_CLUBS);
        add(SEVEN_OF_DIAMONDS);
        add(THREE_OF_HEARTS);
        add(TWO_OF_SPADES);
        add(EIGHT_OF_CLUBS);
    }});

    public static Hand PAIR_OF_FIVES_HAND = new Hand(new ArrayList<Card>() {{
        add(FIVE_OF_DIAMONDS);
        add(FIVE_OF_SPADES);
        add(SIX_OF_HEARTS);
        add(QUEEN_OF_CLUBS);
        add(ACE_OF_HEARTS);
    }});

    public static Hand PAIRS_OF_KINGS_AND_FOURS_HAND = new Hand(new ArrayList<Card>() {{
        add(KING_OF_HEARTS);
        add(KING_OF_DIAMONDS);
        add(FOUR_OF_SPADES);
        add(FOUR_OF_CLUBS);
        add(NINE_OF_DIAMONDS);
    }});

    public static Hand PAIRS_OF_JACKS_AND_EIGHTS_HAND = new Hand(new ArrayList<Card>() {{
        add(JACK_OF_DIAMONDS);
        add(JACK_OF_SPADES);
        add(EIGHT_OF_DIAMONDS);
        add(EIGHT_OF_SPADES);
        add(KING_OF_CLUBS);
    }});

    public static Hand THREE_NINES_HAND = new Hand(new ArrayList<Card>() {{
        add(NINE_OF_CLUBS);
        add(NINE_OF_HEARTS);
        add(NINE_OF_SPADES);
        add(FOUR_OF_HEARTS);
        add(TWO_OF_HEARTS);
    }});

    public static Hand THREE_SIXES_HAND = new Hand(new ArrayList<Card>() {{
        add(SIX_OF_CLUBS);
        add(SIX_OF_DIAMONDS);
        add(SIX_OF_SPADES);
        add(THREE_OF_SPADES);
        add(TWO_OF_DIAMONDS);
    }});

    public static Hand ACE_HIGH_STRAIGHT_HAND = new Hand(new ArrayList<Card>() {{
        add(TEN_OF_SPADES);
        add(JACK_OF_HEARTS);
        add(QUEEN_OF_HEARTS);
        add(KING_OF_SPADES);
        add(ACE_OF_DIAMONDS);
    }});

    public static Hand SIX_HIGH_STRAIGHT_HAND = new Hand(new ArrayList<Card>() {{
        add(TWO_OF_SPADES);
        add(THREE_OF_CLUBS);
        add(FOUR_OF_DIAMONDS);
        add(FIVE_OF_HEARTS);
        add(SIX_OF_DIAMONDS);
    }});

    public static Hand JACK_HIGH_FLUSH_HAND = new Hand(new ArrayList<Card>() {{
        add(THREE_OF_DIAMONDS);
        add(FOUR_OF_DIAMONDS);
        add(EIGHT_OF_DIAMONDS);
        add(TEN_OF_DIAMONDS);
        add(JACK_OF_DIAMONDS);
    }});

    public static Hand QUEEN_HIGH_FLUSH_HAND = new Hand(new ArrayList<Card>() {{
        add(TWO_OF_SPADES);
        add(FIVE_OF_SPADES);
        add(SEVEN_OF_SPADES);
        add(NINE_OF_SPADES);
        add(QUEEN_OF_SPADES);
    }});

    public static Hand ACES_HIGH_FULL_HOUSE_HAND = new Hand(new ArrayList<Card>() {{
        add(ACE_OF_CLUBS);
        add(ACE_OF_DIAMONDS);
        add(ACE_OF_HEARTS);
        add(FIVE_OF_CLUBS);
        add(FIVE_OF_SPADES);
    }});

    public static Hand KING_HIGH_FULL_HOUSE_HAND = new Hand(new ArrayList<Card>() {{
        add(KING_OF_DIAMONDS);
        add(KING_OF_SPADES);
        add(KING_OF_HEARTS);
        add(ACE_OF_CLUBS);
        add(ACE_OF_DIAMONDS);
    }});

    public static Hand FOUR_JACKS_HAND = new Hand(new ArrayList<Card>() {{
        add(JACK_OF_SPADES);
        add(JACK_OF_DIAMONDS);
        add(JACK_OF_HEARTS);
        add(JACK_OF_CLUBS);
        add(TWO_OF_DIAMONDS);
    }});

    public static Hand FOUR_FOURS_HAND = new Hand(new ArrayList<Card>() {{
        add(FOUR_OF_DIAMONDS);
        add(FOUR_OF_CLUBS);
        add(FOUR_OF_HEARTS);
        add(FOUR_OF_SPADES);
        add(ACE_OF_DIAMONDS);
    }});

    public static Hand KING_HIGH_STRAIGHT_FLUSH_HAND = new Hand(new ArrayList<Card>() {{
        add(NINE_OF_HEARTS);
        add(TEN_OF_HEARTS);
        add(JACK_OF_HEARTS);
        add(QUEEN_OF_HEARTS);
        add(KING_OF_HEARTS);
    }});

    public static Hand NINE_HIGH_STRAIGHT_FLUSH_HAND = new Hand(new ArrayList<Card>() {{
        add(FIVE_OF_SPADES);
        add(SIX_OF_SPADES);
        add(SEVEN_OF_SPADES);
        add(EIGHT_OF_SPADES);
        add(NINE_OF_SPADES);
    }});

    // Invalid hands to test proper validation
    public static Hand INVALID_HAND_NULL_CARDS = new Hand(null);

    public static Hand INVALID_HAND_NO_CARDS = new Hand(new ArrayList<Card>());

    public static Hand INVALID_HAND_LESS_THAN_5_CARDS = new Hand(new ArrayList<Card>() {{
        add(TWO_OF_SPADES);
        add(THREE_OF_CLUBS);
        add(FOUR_OF_HEARTS);
        add(FIVE_OF_DIAMONDS);
    }});

    public static Hand INVALID_HAND_MORE_THAN_5_CARDS = new Hand(new ArrayList<Card>() {{
        add(TWO_OF_SPADES);
        add(THREE_OF_CLUBS);
        add(FOUR_OF_HEARTS);
        add(FIVE_OF_DIAMONDS);
        add(SIX_OF_SPADES);
        add(SEVEN_OF_CLUBS);
        add(EIGHT_OF_HEARTS);
        add(NINE_OF_DIAMONDS);
    }});

    public static Hand INVALID_HAND_DUPLICATE_CARDS = new Hand(new ArrayList<Card>() {{
        add(TWO_OF_SPADES);
        add(TWO_OF_SPADES);
        add(ACE_OF_CLUBS);
        add(ACE_OF_CLUBS);
        add(ACE_OF_CLUBS);
    }});
}
