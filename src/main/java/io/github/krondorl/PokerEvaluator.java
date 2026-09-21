/*!
 * Poker Evaluator
 *
 * Copyright (c) 2026- Adam Burucs
 *
 * MIT Licensed
 */

package io.github.krondorl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Evaluates standard five-card poker hands.
 * <p>
 * The evaluator supports parsing cards from compact two-character notation and
 * classifying a hand according to standard poker hand categories.
 * </p>
 */
public final class PokerEvaluator {
    /**
     * The possible card ranks, ordered from {@link #TWO} to {@link #ACE}.
     */
    public enum Rank { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    /**
     * The four standard playing-card suits.
     */
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    /**
     * Represents a playing card with a rank and suit.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    public record Card(Rank rank, Suit suit) implements Comparable<Card> {
        /**
         * Compares this card with another card, first by rank and then by suit.
         *
         * @param other the card to compare with
         * @return a negative value, zero, or a positive value if this card is
         *         less than, equal to, or greater than {@code other}
         */
        @Override
        public int compareTo(Card other) {
            return Comparator.comparing(Card::rank)
                    .thenComparing(Card::suit)
                    .compare(this, other);
        }
    }

    /**
     * The supported five-card poker hand classifications.
     */
    public enum HandType {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND,
        STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    private PokerEvaluator() {}

    /**
     * Evaluates a five-card poker hand expressed in compact string notation.
     * <p>
     * Each card must contain a rank followed by a suit, for example
     * {@code "2s 3s 4s Ts As"}. Supported suits are {@code s}, {@code h},
     * {@code d}, and {@code c}. Face ranks use {@code T}, {@code J},
     * {@code Q}, {@code K}, and {@code A}.
     * </p>
     *
     * @param fiveCards a space-separated string containing exactly five cards
     * @return the classified poker hand type
     * @throws NullPointerException if {@code fiveCards} is {@code null}
     * @throws IllegalArgumentException if the input does not contain exactly
     *                                  five valid card tokens
     */
    public static HandType evaluateFiveCards(String fiveCards) {
        List<Card> parsedHand = parseHand(fiveCards);
        return evaluateFiveCards(parsedHand);
    }

    /**
     * Evaluates a list containing exactly five cards.
     *
     * @param fiveCards the five cards to evaluate
     * @return the classified poker hand type
     * @throws IllegalArgumentException if {@code fiveCards} is {@code null} or
     *                                  does not contain exactly five cards
     */
    public static HandType evaluateFiveCards(List<Card> fiveCards) {
        if (fiveCards == null || fiveCards.size() != 5) {
            throw new IllegalArgumentException("Hand must contain exactly 5 cards.");
        }

        List<Card> sorted = fiveCards.stream().sorted().toList();

        boolean isFlush = checkFlush(sorted);
        boolean isStraight = checkStraight(sorted);

        if (isStraight && isFlush) {
            if (sorted.get(0).rank() == Rank.TEN && sorted.get(4).rank() == Rank.ACE) {
                return HandType.ROYAL_FLUSH;
            }
            return HandType.STRAIGHT_FLUSH;
        }

        if (isFlush) return HandType.FLUSH;
        if (isStraight) return HandType.STRAIGHT;

        Map<Rank, Long> counts = getRankFrequencies(sorted);

        return switch (counts.size()) {
            case 2 -> counts.containsValue(4L) ? HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
            case 3 -> counts.containsValue(3L) ? HandType.THREE_OF_A_KIND : HandType.TWO_PAIR;
            case 4 -> HandType.ONE_PAIR;
            default -> HandType.HIGH_CARD;
        };
    }

    private static boolean checkFlush(List<Card> hand) {
        Suit firstSuit = hand.get(0).suit();
        return hand.stream().allMatch(card -> card.suit() == firstSuit);
    }

    private static boolean checkStraight(List<Card> hand) {
        boolean isStandardStraight = true;
        for (int i = 0; i < hand.size() - 1; i++) {
            if (hand.get(i + 1).rank().ordinal() != hand.get(i).rank().ordinal() + 1) {
                isStandardStraight = false;
                break;
            }
        }

        if (isStandardStraight) return true;

        return hand.get(0).rank() == Rank.TWO &&
                hand.get(1).rank() == Rank.THREE &&
                hand.get(2).rank() == Rank.FOUR &&
                hand.get(3).rank() == Rank.FIVE &&
                hand.get(4).rank() == Rank.ACE;
    }

    private static Map<Rank, Long> getRankFrequencies(List<Card> hand) {
        return hand.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
    }

    private static List<Card> parseHand(String hand) {
        String[] handToSplit = hand.split(" ");

        if (handToSplit.length != 5) {
            throw new IllegalArgumentException("Hand must contain exactly 5 cards.");
        }

        List<Card> cards = new ArrayList<>();

        for (String card : handToSplit) {
            if (card.length() != 2) {
                throw new IllegalArgumentException("Invalid card token: expected exactly 2 characters.");
            }

            char rankChar = card.charAt(0);
            char suitChar = card.charAt(1);

            Rank rank = switch (rankChar) {
                case '2' -> Rank.TWO;
                case '3' -> Rank.THREE;
                case '4' -> Rank.FOUR;
                case '5' -> Rank.FIVE;
                case '6' -> Rank.SIX;
                case '7' -> Rank.SEVEN;
                case '8' -> Rank.EIGHT;
                case '9' -> Rank.NINE;
                case 'T' -> Rank.TEN;
                case 'J' -> Rank.JACK;
                case 'Q' -> Rank.QUEEN;
                case 'K' -> Rank.KING;
                case 'A' -> Rank.ACE;
                default -> throw new IllegalArgumentException(
                        "Invalid rank: " + rankChar
                );
            };

            Suit suit = switch (suitChar) {
                case 's' -> Suit.SPADES;
                case 'h' -> Suit.HEARTS;
                case 'd' -> Suit.DIAMONDS;
                case 'c' -> Suit.CLUBS;
                default -> throw new IllegalArgumentException(
                        "Invalid suit: " + suitChar
                );
            };

            Card parsedCard = new Card(rank, suit);

            cards.add(parsedCard);
        }

        return cards;
    }
}