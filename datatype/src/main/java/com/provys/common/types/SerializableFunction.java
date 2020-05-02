package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Extension of Function that is serializable.
 *
 * @param <T> is source type
 * @param <R> is type of result
 */
@Immutable
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {

}
