package com.provys.common.datatype;

import com.provys.common.exception.InternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class is similar to StringReader, but is optimized for parsing string character-by-character and provides methods
 * for peeking on the next character (so that one make decision on ending current part and moving to next one).
 * It is used internally by various parsers.
 * It almost implements {@code Iterator<Character>}, the only problem being that it would require nesting of
 * {@code char} return value in {@code next} function - and as it brings no benefit, it was not implemented at the
 * moment.
 *
 */
public class StringParser {

    private static final Logger LOG = LogManager.getLogger(StringParser.class);

    /**
     * Defines sign handling in number parsers.
     */
    public enum SignHandling{
        /**
         * Sign must be specified and is included in specified number of characters
         */
        MANDATORY(true),
        /**
         * Sign is optional and is included in specified number of characters
         */
        INCLUDED(true),
        /**
         * Sign is optional and specified number of characters is for digits, excluding the sign
         */
        EXTEND(false),
        /**
         * Sign cannot be specified (throws exception when sign is found)
         */
        NONE(false);

        private final boolean included;

        private SignHandling(boolean included) {
            this.included = included;
        }

        public int adjustCount(int count) {
            return included ? count - 1 : count;
            }
    }

    private final String string;
    private int pos;

    /**
     * Creates new {@code StringParser} for specified String
     * @param string is value to be parsed
     */
    public StringParser(String string) {
        this.string = Objects.requireNonNull(string);
        this.pos = 0;
    }

    /**
     * @return string parser is based on
     */
    public String getString() {
        return string;
    }

    /**
     * @return current position of parser; it points to next character to be read and it is 0 when parser is created
     */
    public int getPos() {
        return pos;
    }

    /**
     * Set position of parser. Position should be index of next character to be read.
     *
     * @param pos is position to be set
     */
    public void setPos(int pos) {
        if (pos < 0) {
            throw new InvalidParameterException("Cannot set position of string parser to negative value " + pos);
        }
        this.pos = pos;
    }

    /**
     * Returns {@code true} if the string has more elements. (In other words, returns {@code true} if {@link #next}
     * would return an element rather than throwing an exception.)
     *
     * @return {@code true} if the string has more elements
     */
    public boolean hasNext() {
        return pos<string.length();
    }

    /**
     * Returns next character in the string and moves current position.
     *
     * @return next character
     * @throws NoSuchElementException when being at the end of the String
     */
    public char next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return string.charAt(pos++);
    }

    /**
     * Peek at next character, without moving position of the parser.
     *
     * @return character that will be returned next
     * @throws StringIndexOutOfBoundsException in case position is after end of the string
     */
    public char peek() {
        return string.charAt(pos);
    }

    /**
     * Return current character, without moving position of the parser. It returns last character obtained if there was
     * no setPos, character on index before one specified if setPos has been used
     *
     * @return character that will be returned next
     * @throws StringIndexOutOfBoundsException in case position is after end of the string
     */
    public char current() {
        return string.charAt(pos - 1);
    }

    /**
     * Parse unsigned integer from the string, reading only digits. It reads no more than maxChars characters, stops
     * when first non-numeric character or end of string is reached. Throws error if less than minChars numeric
     * characters were read.
     *
     * @param minChars is minimal number of characters to be read
     * @param maxChars is maximal number of characters to be read
     * @return unsigned integer parsed from current position in the string
     */
    public int readUnsignedInt(int minChars, int maxChars) {
        if (minChars > maxChars) {
            throw new InvalidParameterException("Parse int: minChars (" + minChars+ ") > maxChars ( " + maxChars + ")");
        }
        if (minChars <= 0) {
            throw new InvalidParameterException("Parse int: minChars (" + minChars + ") <= 0");
        }
        // no need for maxChars < 0 condition as maxChars >= minChars
        int minPos = pos + minChars;
        int maxPos = pos + maxChars;
        long result = 0;
        while (hasNext() && (peek() >= '0') && (peek() <= '9')) {
            result = result * 10 + next() - '0';
            if (result > Integer.MAX_VALUE) {
                throw new InternalException(LOG, "Integer overflow parsing number");
            }
            if (pos == maxPos) {
                break;
            }
        }
        if (pos < minPos) {
            throw new InternalException(LOG, "Invalid character " + Character.toString(peek()) + " reading number," +
                    " minimal length " + minChars + ", current position " + (pos - minPos + minChars));
        }
        return (int) result;
    }

    /**
     * Parse unsigned integer from the string, reading only digits. It reads exactly chars characters, throws error if
     * non-numeric character or end of string is reached.
     *
     * @param chars is number of characters to be read
     * @return unsigned integer parsed from current position in the string
     */
    public int readUnsignedInt(int chars) {
        return readUnsignedInt(chars, chars);
    }

    /**
     * Parse signed integer from the string, reading only sign (optional) and digits. It reads at least minChars and
     * at most maxChars characters (plus potentially sign, depending on signHandling), throws error if non-numeric
     * character or end of string is reached
     *
     * @param minChars is minimal number of characters (see also signHandling)
     * @param maxChars is amximal number of characters (see also signHandling)
     * @param signHandling defines how should sign be handled. It might be forbidden, required and it might be included
     *                    in number of characters specified in minChars / maxChars or added on top of that
     * @return number read from String
     */
    public int readInt(int minChars, int maxChars, SignHandling signHandling) {
        boolean negative = false;
        if (peek() < '0') {
            switch (next()) {
                case '-':
                    negative = true;
                    minChars = signHandling.adjustCount(minChars);
                    maxChars = signHandling.adjustCount(maxChars);
                    break;
                case '+':
                    minChars = signHandling.adjustCount(minChars);
                    maxChars = signHandling.adjustCount(maxChars);
                    break;
                default:
                    throw new InternalException(LOG, "Invalid first character in read integer " + peek());
            }
            if (signHandling == SignHandling.NONE) {
                throw new InternalException(LOG, "Sign not expected when parsing with sign-handling NONE");
            }
        } else {
            if (signHandling == SignHandling.MANDATORY) {
                throw new InternalException(LOG, "Sign required and missing");
            }
        }
        int result = readUnsignedInt(minChars, maxChars);
        return negative ? -result : result;
    }

    /**
     * Parse signed integer from the string, reading only sign (optional) and digits. It reads chars characters (plus
     * potentially sign, depending on signHandling), throws error if non-numeric character or end of string is reached
     *
     * @param chars is number of characters to be read (see also signHandling)
     * @param signHandling defines how should sign be handled. It might be forbidden, required and it might be included
     *                    in number of characters specified in chars or added on top of that
     * @return number read from String
     */
    public int readInt(int chars, SignHandling signHandling) {
        return readInt(chars, chars, signHandling);
    }
}
