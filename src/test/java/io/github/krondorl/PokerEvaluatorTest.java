/*!
 * Poker Evaluator
 *
 * Copyright (c) 2026- Adam Burucs
 *
 * MIT Licensed
 */

package io.github.krondorl;

import io.github.krondorl.PokerEvaluator.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.krondorl.PokerEvaluator.HandType.*;
import static io.github.krondorl.PokerEvaluator.Rank.*;
import static io.github.krondorl.PokerEvaluator.Suit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PokerEvaluatorTest {

    @Test
    @DisplayName("Should identify Royal Flush")
    void testRoyalFlush() {
        List<Card> hand = List.of(
                new Card(TEN, SPADES),
                new Card(JACK, SPADES),
                new Card(QUEEN, SPADES),
                new Card(KING, SPADES),
                new Card(ACE, SPADES)
        );
        assertEquals(ROYAL_FLUSH, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Straight Flush")
    void testStraightFlush() {
        List<Card> hand = List.of(
                new Card(FIVE, HEARTS),
                new Card(SIX, HEARTS),
                new Card(SEVEN, HEARTS),
                new Card(EIGHT, HEARTS),
                new Card(NINE, HEARTS)
        );
        assertEquals(STRAIGHT_FLUSH, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Four of a Kind")
    void testFourOfAKind() {
        List<Card> hand = List.of(
                new Card(NINE, CLUBS),
                new Card(NINE, DIAMONDS),
                new Card(NINE, HEARTS),
                new Card(NINE, SPADES),
                new Card(KING, HEARTS)
        );
        assertEquals(FOUR_OF_A_KIND, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Full House")
    void testFullHouse() {
        List<Card> hand = List.of(
                new Card(EIGHT, CLUBS),
                new Card(EIGHT, DIAMONDS),
                new Card(EIGHT, SPADES),
                new Card(JACK, HEARTS),
                new Card(JACK, DIAMONDS)
        );
        assertEquals(FULL_HOUSE, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Flush")
    void testFlush() {
        List<Card> hand = List.of(
                new Card(TWO, DIAMONDS),
                new Card(FIVE, DIAMONDS),
                new Card(SEVEN, DIAMONDS),
                new Card(JACK, DIAMONDS),
                new Card(ACE, DIAMONDS)
        );
        assertEquals(FLUSH, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify High Straight")
    void testStraight() {
        List<Card> hand = List.of(
                new Card(NINE, CLUBS),
                new Card(TEN, DIAMONDS),
                new Card(JACK, HEARTS),
                new Card(QUEEN, SPADES),
                new Card(KING, CLUBS)
        );
        assertEquals(STRAIGHT, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Ace-low Straight (Wheel: A-2-3-4-5)")
    void testAceLowStraight() {
        List<Card> hand = List.of(
                new Card(ACE, CLUBS),
                new Card(TWO, DIAMONDS),
                new Card(THREE, HEARTS),
                new Card(FOUR, SPADES),
                new Card(FIVE, CLUBS)
        );
        assertEquals(STRAIGHT, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Three of a Kind")
    void testThreeOfAKind() {
        List<Card> hand = List.of(
                new Card(SEVEN, CLUBS),
                new Card(SEVEN, DIAMONDS),
                new Card(SEVEN, HEARTS),
                new Card(TWO, SPADES),
                new Card(KING, CLUBS)
        );
        assertEquals(THREE_OF_A_KIND, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Two Pair")
    void testTwoPair() {
        List<Card> hand = List.of(
                new Card(KING, CLUBS),
                new Card(KING, DIAMONDS),
                new Card(FOUR, HEARTS),
                new Card(FOUR, SPADES),
                new Card(NINE, CLUBS)
        );
        assertEquals(TWO_PAIR, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify One Pair")
    void testOnePair() {
        List<Card> hand = List.of(
                new Card(JACK, CLUBS),
                new Card(JACK, DIAMONDS),
                new Card(THREE, HEARTS),
                new Card(FIVE, SPADES),
                new Card(EIGHT, CLUBS)
        );
        assertEquals(ONE_PAIR, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify High Card")
    void testHighCard() {
        List<Card> hand = List.of(
                new Card(TWO, CLUBS),
                new Card(FIVE, DIAMONDS),
                new Card(EIGHT, HEARTS),
                new Card(JACK, SPADES),
                new Card(ACE, CLUBS)
        );
        assertEquals(HIGH_CARD, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should evaluate hands regardless of card ordering in the list")
    void testUnsortedHand() {
        List<Card> hand = List.of(
                new Card(FOUR, SPADES),
                new Card(ACE, CLUBS),
                new Card(THREE, HEARTS),
                new Card(FIVE, CLUBS),
                new Card(TWO, DIAMONDS)
        );
        assertEquals(STRAIGHT, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Three of a Kind from String")
    void testThreeOfAKindString() {
        String hand = "7c 7d 7h 2s Kc";
        assertEquals(THREE_OF_A_KIND, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify Two Pair from String")
    void testTwoPairString() {
        String hand = "Kc Kd 4h 4s 9c";
        assertEquals(TWO_PAIR, PokerEvaluator.evaluateFiveCards(hand));
    }

    @Test
    @DisplayName("Should identify One Pair from String")
    void testOnePairString() {
        String hand = "Jc Jd 3h 5s 8c";
        assertEquals(ONE_PAIR, PokerEvaluator.evaluateFiveCards(hand));
    }
}