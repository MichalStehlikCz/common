package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static com.provys.common.datatype.StringParser.SignHandling.*;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
class StringParserTest {

    @Nonnull
    static Stream<Object[]> setPosTest() {
        return Stream.of(
                new Object[]{"abcdefg", 5, null}
                , new Object[]{"abcdefg", -1, "negative"}
                , new Object[]{"abcdefg", 0, null}
                , new Object[]{"abcdefg", 100, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void setPosTest(String string, int pos, @Nullable String message) {
        var parser = new StringParser(string);
        if (message == null) {
            parser.setPos(pos);
            assertThat(parser.getPos()).isEqualTo(pos);
        } else {
            assertThatThrownBy(() -> parser.setPos(pos)).hasMessageContaining(message);
        }
    }

    @Nonnull
    static Stream<Object[]> hasNextTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, true}
                , new Object[]{"abcdefg", 0, true}
                , new Object[]{"abcdefg", 100, false}
                , new Object[]{"abcdefg", 7, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void hasNextTest(String string, int pos, boolean hasNext) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        assertThat(parser.hasNext()).isEqualTo(hasNext);
    }

    @Nonnull
    static Stream<Object[]> nextTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, false, 'g'}
                , new Object[]{"abcdefg", 0, false, 'a'}
                , new Object[]{"abcdefg", 100, true, 'x'}
                , new Object[]{"abcdefg", 7, true, 'x'}
        );
    }

    @ParameterizedTest
    @MethodSource
    void nextTest(String string, int pos, boolean exception, char result) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(parser::next).isInstanceOf(NoSuchElementException.class);
        } else {
            assertThat(parser.next()).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(pos + 1);
        }
    }

    @Nonnull
    static Stream<Object[]> peekTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, false, 'g'}
                , new Object[]{"abcdefg", 0, false, 'a'}
                , new Object[]{"abcdefg", 100, true, 'x'}
                , new Object[]{"abcdefg", 7, true, 'x'}
        );
    }

    @ParameterizedTest
    @MethodSource
    void peekTest(String string, int pos, boolean exception, char result) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(parser::peek).isInstanceOf(StringIndexOutOfBoundsException.class);
        } else {
            assertThat(parser.peek()).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(pos);
        }
    }

    @Nonnull
    static Stream<Object[]> currentTest() {
        return Stream.of(
                new Object[]{"abcdefg", 7, false, 'g'}
                , new Object[]{"abcdefg", 1, false, 'a'}
                , new Object[]{"abcdefg", 100, true, 'x'}
                , new Object[]{"abcdefg", 8, true, 'x'}
                , new Object[]{"abcdefg", 0, true, 'x'}
        );
    }

    @ParameterizedTest
    @MethodSource
    void currentTest(String string, int pos, boolean exception, char result) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(parser::current).isInstanceOf(StringIndexOutOfBoundsException.class);
        } else {
            assertThat(parser.current()).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(pos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> readUnsignedIntTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, 2, 4, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, 4, false, 12, 4}
                , new Object[]{"ab123cdefg", 2, 2, 4, false, 123, 5}
                , new Object[]{"abc012345defg", 3, 2, 4, false, 123, 7}
                , new Object[]{"abc1defg", 3, 2, 4, true, 0, 0}
                , new Object[]{"abc+12defg", 3, 2, 4, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 3, 3, true, 0, 0}
                , new Object[]{"ab123cdefg", 2, 3, 3, false, 123, 5}
                , new Object[]{"abc12345defg", 3, 3, 3, false, 123, 6}
                , new Object[]{"ab12", 2, 3, 3, true, 0, 0}
                , new Object[]{"ab123", 2, 3, 3, false, 123, 5}
                , new Object[]{"ab1234567890123456", 2, 3, 15, true, 0, 0}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readUnsignedIntTest(String string, int pos, int minChars, int maxChars, boolean exception, int result,
                             int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(() -> parser.readUnsignedInt(minChars, maxChars));
        } else {
            assertThat(parser.readUnsignedInt(minChars, maxChars)).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    static Stream<Object[]> readUnsignedIntTest2() {
        return Stream.of(
                new Object[]{"abcdefg", 6, 2, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, false, 12, 4}
                , new Object[]{"ab123cdefg", 2, 2, false, 12, 4}
                , new Object[]{"abc012defg", 3, 2, false, 1, 5}
                , new Object[]{"abc1defg", 3, 2, true, 0, 0}
                , new Object[]{"abc+12defg", 3, 2, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 3, true, 0, 0}
                , new Object[]{"ab123cdefg", 2, 3, false, 123, 5}
                , new Object[]{"abc12345defg", 3, 3, false, 123, 6}
                , new Object[]{"ab12", 2, 3, true, 0, 0}
                , new Object[]{"ab123", 2, 3, false, 123, 5}
                , new Object[]{"ab1234567890123456", 2, 15, true, 0, 0}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readUnsignedIntTest2(String string, int pos, int chars, boolean exception, int result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(() -> parser.readUnsignedInt(chars));
        } else {
            assertThat(parser.readUnsignedInt(chars)).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> readIntTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, 2, 4, MANDATORY, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, 4, INCLUDED, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, 4, EXTEND, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, 4, NONE, true, 0, 0}
                , new Object[]{"ab+1cdefg", 2, 2, 4, MANDATORY, false, 1, 4}
                , new Object[]{"ab+1cdefg", 2, 2, 4, INCLUDED, false, 1, 4}
                , new Object[]{"ab+1cdefg", 2, 2, 4, EXTEND, true, 0, 0}
                , new Object[]{"ab+1cdefg", 2, 2, 4, NONE, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, 4, MANDATORY, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, 4, INCLUDED, false, 12, 4}
                , new Object[]{"ab12cdefg", 2, 2, 4, EXTEND, false, 12, 4}
                , new Object[]{"ab12cdefg", 2, 2, 4, NONE, false, 12, 4}
                , new Object[]{"ab-12345cdefg", 2, 2, 4, MANDATORY, false, -123, 6}
                , new Object[]{"ab-12345cdefg", 2, 2, 4, INCLUDED, false, -123, 6}
                , new Object[]{"ab-12345cdefg", 2, 2, 4, EXTEND, false, -1234, 7}
                , new Object[]{"ab-12345cdefg", 2, 2, 4, NONE, true, 0, 0}
                , new Object[]{"ab1234567890123456", 2, 3, 15, INCLUDED, true, 0, 0}
                , new Object[]{"ab-1234567890123456", 2, 3, 15, INCLUDED, true, 0, 0}
                , new Object[]{"ab+1234567890123456", 2, 3, 15, INCLUDED, true, 0, 0}
        );
    }

    @ParameterizedTest
    @MethodSource
    @SuppressWarnings("squid:S00107") // we do not mind too many parameters in test method
    void readIntTest(String string, int pos, int minChars, int maxChars, StringParser.SignHandling signHandling,
                     boolean exception, int result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(() -> parser.readInt(minChars, maxChars, signHandling));
        } else {
            assertThat(parser.readInt(minChars, maxChars, signHandling)).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> readIntTest2() {
        return Stream.of(
                new Object[]{"abcdefg", 6, 2, MANDATORY, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, INCLUDED, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, EXTEND, true, 0, 0}
                , new Object[]{"abcdefg", 6, 2, NONE, true, 0, 0}
                , new Object[]{"ab+1cdefg", 2, 2, MANDATORY, false, 1, 4}
                , new Object[]{"ab+1cdefg", 2, 2, INCLUDED, false, 1, 4}
                , new Object[]{"ab+1cdefg", 2, 2, EXTEND, true, 0, 0}
                , new Object[]{"ab+1cdefg", 2, 2, NONE, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, MANDATORY, true, 0, 0}
                , new Object[]{"ab12cdefg", 2, 2, INCLUDED, false, 12, 4}
                , new Object[]{"ab12cdefg", 2, 2, EXTEND, false, 12, 4}
                , new Object[]{"ab12cdefg", 2, 2, NONE, false, 12, 4}
                , new Object[]{"ab-12345cdefg", 2, 4, MANDATORY, false, -123, 6}
                , new Object[]{"ab-12345cdefg", 2, 4, INCLUDED, false, -123, 6}
                , new Object[]{"ab-12345cdefg", 2, 4, EXTEND, false, -1234, 7}
                , new Object[]{"ab-12345cdefg", 2, 4, NONE, true, 0, 0}
                , new Object[]{"ab1234567890123456", 2, 15, INCLUDED, true, 0, 0}
                , new Object[]{"ab-1234567890123456", 2, 15, INCLUDED, true, 0, 0}
                , new Object[]{"ab+1234567890123456", 2, 15, INCLUDED, true, 0, 0}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readIntTest2(String string, int pos, int chars, StringParser.SignHandling signHandling,
                     boolean exception, int result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception) {
            assertThatThrownBy(() -> parser.readInt(chars, signHandling));
        } else {
            assertThat(parser.readInt(chars, signHandling)).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> readString0Test() {
        return Stream.of(
                new Object[]{"abcdefg", 8, null, 8, StringIndexOutOfBoundsException.class}
                , new Object[]{"abcdefg", 7, "", 7, null}
                , new Object[]{"abcdefg", 5, "fg", 7, null}
                , new Object[]{"abcdefg", 3, "defg", 7, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readString0Test(String string, int pos, @Nullable String result, int endPos, @Nullable Class<?> exception) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception != null) {
            assertThatThrownBy(parser::readString).isInstanceOf(exception);
        } else {
            assertThat(parser.readString()).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> readStringTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, 2, null, 8, StringIndexOutOfBoundsException.class}
                , new Object[]{"abcdefg", 5, 2, "fg", 7, null}
                , new Object[]{"abcdefg", 3, 2, "de", 5, null}
                , new Object[]{"abcdefg", 5, -1, null, 5, InvalidParameterException.class}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readStringTest(String string, int pos, int chars, @Nullable String result, int endPos,
                        @Nullable Class<?> exception) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        if (exception != null) {
            assertThatThrownBy(() -> parser.readString(chars)).isInstanceOf(exception);
        } else {
            assertThat(parser.readString(chars)).isEqualTo(result);
            assertThat(parser.getPos()).isEqualTo(endPos);
        }
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> onTextTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, "fg", false, 6}
                , new Object[]{"abcdefg", 5, "fg", true, 7}
                , new Object[]{"abcdeFg", 5, "fg", false, 5}
                , new Object[]{"abcdefg", 2, "cd", true, 4}
                , new Object[]{"abcdefg", 2, "cD", false, 2}
                , new Object[]{"abcdefg", 2, "fg", false, 2}
        );
    }

    @ParameterizedTest
    @MethodSource
    void onTextTest(String string, int pos, String text, boolean result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        assertThat(parser.onText(text)).isEqualTo(result);
        assertThat(parser.getPos()).isEqualTo(endPos);
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> isOnTextTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, "fg", false, 6}
                , new Object[]{"abcdefg", 5, "fg", true, 5}
                , new Object[]{"abcdeFg", 5, "fg", false, 5}
                , new Object[]{"abcdefg", 2, "cd", true, 2}
                , new Object[]{"abcdefg", 2, "Cd", false, 2}
                , new Object[]{"abcdefg", 2, "fg", false, 2}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isOnTextTest(String string, int pos, String text, boolean result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        assertThat(parser.isOnText(text)).isEqualTo(result);
        assertThat(parser.getPos()).isEqualTo(endPos);
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> onTextIgnoreCaseTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, "fg", false, 6}
                , new Object[]{"abcdefg", 5, "fg", true, 7}
                , new Object[]{"abcdeFg", 5, "fg", true, 7}
                , new Object[]{"abcdefg", 2, "cd", true, 4}
                , new Object[]{"abcdefg", 2, "cD", true, 4}
                , new Object[]{"abcdefg", 2, "fg", false, 2}
        );
    }

    @ParameterizedTest
    @MethodSource
    void onTextIgnoreCaseTest(String string, int pos, String text, boolean result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        assertThat(parser.onTextIgnoreCase(text)).isEqualTo(result);
        assertThat(parser.getPos()).isEqualTo(endPos);
    }

    @Nonnull
    @SuppressWarnings("squid:S1192") // we do not care about duplicate strings in test data
    static Stream<Object[]> isOnTextIgnoreCaseTest() {
        return Stream.of(
                new Object[]{"abcdefg", 6, "fg", false, 6}
                , new Object[]{"abcdefg", 5, "fg", true, 5}
                , new Object[]{"abcdefG", 5, "fg", true, 5}
                , new Object[]{"abcdefg", 2, "cd", true, 2}
                , new Object[]{"abcdefg", 2, "Cd", true, 2}
                , new Object[]{"abcdefg", 2, "fg", false, 2}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isOnTextIgnoreCaseTest(String string, int pos, String text, boolean result, int endPos) {
        var parser = new StringParser(string);
        parser.setPos(pos);
        assertThat(parser.isOnTextIgnoreCase(text)).isEqualTo(result);
        assertThat(parser.getPos()).isEqualTo(endPos);
    }
}