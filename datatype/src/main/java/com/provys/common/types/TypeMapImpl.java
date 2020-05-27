package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import com.provys.common.datatype.DbBoolean;
import com.provys.common.datatype.DtBinaryData;
import com.provys.common.datatype.DtDate;
import com.provys.common.datatype.DtDateTime;
import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

/**
 * Default map uses service loader to retrieve registered maps from all available libraries. This
 * allows to extend types, available in this package with additional types as needed. Note that if
 * you load two libraries providing different mappings for given string, you will get warning in log
 * and only one mapping will be used, such situation should be avoided. Mapping of {@code
 * Object.class} to value ANY is hardcoded.
 */
@SuppressWarnings("CyclicClassDependency") // Dependency between class and its serialization proxy
@Immutable
public final class TypeMapImpl implements TypeMap {

  private static final Logger LOG = LogManager.getLogger(TypeMapImpl.class);

  private static final String ANY_NAME = "ANY";
  private static final TypeMapImpl DEFAULT;

  static {
    Stream<TypeName<? extends Serializable>> builtInNameStream = Stream.of(
        new TypeName<>(Boolean.class, "BOOLEAN"),
        new TypeName<>(Integer.class, "INTEGER"),
        new TypeName<>(Double.class, "NUMBER"),
        new TypeName<>(String.class, "STRING"),
        new TypeName<>(DtUid.class, "UID"),
        new TypeName<>(DtDate.class, "DATE"),
        new TypeName<>(DtDateTime.class, "DATETIME"),
        new TypeName<>(Byte.class, "BYTE"),
        new TypeName<>(BigInteger.class, "BIGINTEGER"),
        new TypeName<>(BigDecimal.class, "BIGDECIMAL"),
        new TypeName<>(DtBinaryData.class, "BLOB"));
    Stream<TypeName<? extends Serializable>> loaderNameStream = ServiceLoader.load(TypeModule.class)
        .stream().map(Provider::get)
        .map(TypeModule::getTypes).flatMap(Collection::stream);
    List<TypeName<? extends Serializable>> names = Stream
        .concat(builtInNameStream, loaderNameStream)
        .collect(Collectors.toUnmodifiableList());
    Stream<TypeConverter<?, ?>> builtInConverterStream = Stream.of(
        new DefaultTypeConverter<>(Byte.class, Integer.class, true, Number::intValue),
        new DefaultTypeConverter<>(Short.class, Integer.class, true, Number::intValue),
        new DefaultTypeConverter<>(Long.class, Integer.class, false, Math::toIntExact),
        new DefaultTypeConverter<>(Float.class, Integer.class, false,
            TypeConversionUtil::floatToIntExact),
        new DefaultTypeConverter<>(Double.class, Integer.class, false,
            TypeConversionUtil::doubleToIntExact),
        new DefaultTypeConverter<>(BigInteger.class, Integer.class, false,
            BigInteger::intValueExact),
        new DefaultTypeConverter<>(BigDecimal.class, Integer.class, false,
            BigDecimal::intValueExact),
        new DefaultTypeConverter<>(Byte.class, Double.class, true, Number::doubleValue),
        new DefaultTypeConverter<>(Short.class, Double.class, true, Number::doubleValue),
        new DefaultTypeConverter<>(Integer.class, Double.class, true, Number::doubleValue),
        new DefaultTypeConverter<>(Long.class, Double.class, false, Number::doubleValue),
        new DefaultTypeConverter<>(Float.class, Double.class, true, Number::doubleValue),
        new DefaultTypeConverter<>(BigInteger.class, Double.class, false, BigInteger::doubleValue),
        new DefaultTypeConverter<>(BigDecimal.class, Double.class, false, BigDecimal::doubleValue),
        new DefaultTypeConverter<>(Byte.class, BigInteger.class, true,
            (SerializableFunction<Byte, BigInteger>) BigInteger::valueOf),
        new DefaultTypeConverter<>(Short.class, BigInteger.class, true,
            (SerializableFunction<Short, BigInteger>) BigInteger::valueOf),
        new DefaultTypeConverter<>(Integer.class, BigInteger.class, true,
            (SerializableFunction<Integer, BigInteger>) BigInteger::valueOf),
        new DefaultTypeConverter<>(Long.class, BigInteger.class, true, BigInteger::valueOf),
        new DefaultTypeConverter<>(Float.class, BigInteger.class, false,
            TypeConversionUtil::floatToBigIntegerExact),
        new DefaultTypeConverter<>(Double.class, BigInteger.class, false,
            TypeConversionUtil::doubleToBigIntegerExact),
        new DefaultTypeConverter<>(BigDecimal.class, BigInteger.class, false,
            BigDecimal::toBigIntegerExact),
        new DefaultTypeConverter<>(Byte.class, BigDecimal.class, true,
            (SerializableFunction<Byte, BigDecimal>) BigDecimal::valueOf),
        new DefaultTypeConverter<>(Short.class, BigDecimal.class, true,
            (SerializableFunction<Short, BigDecimal>) BigDecimal::valueOf),
        new DefaultTypeConverter<>(Integer.class, BigDecimal.class, true,
            (SerializableFunction<Integer, BigDecimal>) BigDecimal::valueOf),
        new DefaultTypeConverter<>(Long.class, BigDecimal.class, true, BigDecimal::valueOf),
        new DefaultTypeConverter<>(Float.class, BigDecimal.class, true, BigDecimal::valueOf),
        new DefaultTypeConverter<>(Double.class, BigDecimal.class, true, BigDecimal::valueOf),
        new DefaultTypeConverter<>(BigInteger.class, BigDecimal.class, true, BigDecimal::new),
        new DefaultTypeConverter<>(DbBoolean.class, Boolean.class, true, DbBoolean::value),
        new DefaultTypeConverter<>(DtDateTime.class, DtDate.class, true, DtDateTime::getDate),
        new DefaultTypeConverter<>(LocalDate.class, DtDate.class, true, DtDate::ofLocalDate),
        new DefaultTypeConverter<>(LocalDateTime.class, DtDate.class, true,
            value -> DtDate.ofLocalDate(value.toLocalDate())),
        new DefaultTypeConverter<>(DtDate.class, DtDateTime.class, false, DtDateTime::ofDate),
        new DefaultTypeConverter<>(LocalDate.class, DtDateTime.class, false,
            value -> DtDateTime.ofDate(DtDate.ofLocalDate(value))),
        new DefaultTypeConverter<>(LocalDateTime.class, DtDateTime.class, true,
            DtDateTime::ofLocalDateTime)
    );
    var loaderConverterStream = ServiceLoader.load(TypeModule.class).stream().map(Provider::get)
        .map(TypeModule::getConverters).flatMap(Collection::stream);
    var converters = Stream
        .concat(builtInConverterStream, loaderConverterStream)
        .collect(Collectors.toUnmodifiableList());
    DEFAULT = new TypeMapImpl(names, converters);
  }

  /**
   * Type map, retrieved from type modules, registered by service loader.
   *
   * @return default type map
   */
  public static TypeMapImpl getDefault() {
    return DEFAULT;
  }

  @SuppressWarnings("Immutable") // product of toUnmodifiableMap, String and Class are immutable
  private final Map<String, Class<? extends Serializable>> typesByName;
  @SuppressWarnings("Immutable") // product of toUnmodifiableMap, String and Class are immutable
  private final Map<Class<? extends Serializable>, String> namesByType;
  @SuppressWarnings("Immutable") // also unmodifiable map of unmodifiable entries
  private final Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> converters;

  /**
   * Reports duplicate mappings of given class as warnings to log.
   */
  private static final class NameMerger implements BinaryOperator<String> {

    @Override
    public String apply(String first, String second) {
      if (!first.equals(second)) {
        LOG.warn("Duplicate mapping - names {} and {} represent the same class", first, second);
      }
      return first;
    }
  }

  /**
   * Reports duplicate mappings of given name as warnings to log.
   */
  private static final class ClassMerger implements BinaryOperator<Class<? extends Serializable>> {

    @Override
    public Class<? extends Serializable> apply(Class<? extends Serializable> first,
        Class<? extends Serializable> second) {
      if (first != second) {
        LOG.warn("Duplicate mapping - classes {} and {} coorespond to same name", first, second);
      }
      return first;
    }
  }

  private static Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> collectConvertors(
      Iterable<? extends TypeConverter<?, ?>> converters) {
    var intermediate = new HashMap<Class<?>, HashMap<Class<?>, TypeConverter<?, ?>>>(20);
    // collect converters
    for (var converter : converters) {
      var mapForSource = intermediate.computeIfAbsent(converter.getSourceType(),
          key -> new HashMap<>(2));
      var old = mapForSource.get(converter.getTargetType());
      if ((old == null) || (old.getPriority() < converter.getPriority())) {
        mapForSource.put(converter.getTargetType(), converter);
      } else if (old.getPriority() == converter.getPriority()) {
        LOG.warn("Converter priority conflict: {} vs. {}", old, converter);
      }
    }
    // and convert everything to unmodifiable types
    return intermediate.entrySet().stream()
        .map(entry -> Map.entry(entry.getKey(), Map.copyOf(entry.getValue())))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Create type map based on supplied collection of type names.
   *
   * @param types are types and their names registered in this type map
   * @param converters are type converters to be registered in this type map
   */
  public TypeMapImpl(Collection<TypeName<? extends Serializable>> types,
      Iterable<? extends TypeConverter<?, ?>> converters) {
    this.namesByType = types.stream()
        .filter(TypeName::isDefaultForType) // only default name registered
        .collect(
            Collectors.toUnmodifiableMap(TypeName::getType, TypeName::getName, new NameMerger()));
    this.typesByName = types.stream()
        .collect(
            Collectors.toUnmodifiableMap(TypeName::getName, TypeName::getType, new ClassMerger()));
    this.converters = collectConvertors(converters);
  }

  @Override
  public Class<? extends Serializable> getType(String name) {
    var result = typesByName.get(name);
    if (result == null) {
      throw new InternalException("No type mapping found for " + name);
    }
    return result;
  }

  @Override
  public Class<?> getExtendedType(String name) {
    return name.equals(getAnyName()) ? getAnyType() : getType(name);
  }

  @Override
  public void validateType(Class<?> type) {
    if (namesByType.get(type) == null) {
      throw new InternalException("Type " + type + " is not supported value type");
    }
  }

  private <S, T> Optional<TypeConverter<S, T>> getConverter(Class<S> sourceType,
      Class<T> targetType) {
    var mapBySource = converters.get(sourceType);
    if (mapBySource == null) {
      return Optional.empty();
    }
    @SuppressWarnings("unchecked") // we know type parameters are ok from the eay we constructed map
        var result = (TypeConverter<S, T>) mapBySource.get(targetType);
    return Optional.ofNullable(result);
  }

  @Override
  public boolean isAssignableFrom(Class<?> targetType, Class<?> sourceType) {
    if (targetType.isAssignableFrom(sourceType)) {
      return true;
    }
    return getConverter(sourceType, targetType)
        .map(TypeConverter::isAssignableFrom)
        .orElse(false);
  }

  private <S, T> @NonNull T convertInt(Class<T> targetType, @NonNull S value) {
    // we do not care about parametrized types... thus this suppression is ok
    @SuppressWarnings("unchecked")
    Class<S> sourceType = (Class<S>) value.getClass();
    return getConverter(sourceType, targetType)
        .map(converter -> converter.convert(value))
        .orElseThrow(() -> new InternalException(
            "Conversion from " + sourceType + " to " + targetType + " not supported"));
  }

  @Override
  public <T> @PolyNull T convert(Class<T> targetType, @PolyNull Object value) {
    /* null is null... */
    if (value == null) {
      return null;
    }
    /* if value is instance of targetType, we can use it as it is */
    if (targetType.isInstance(value)) {
      return targetType.cast(value);
    }
    /* otherwise we need conversion using registered convertors */
    return convertInt(targetType, value);
  }

  @Override
  public String getName(Class<?> type) {
    var result = namesByType.get(type);
    if (result == null) {
      throw new InternalException("No type name found for class " + type);
    }
    return result;
  }

  @Override
  public String getExtendedName(Class<?> type) {
    return (type == Object.class) ? ANY_NAME : getName(type);
  }

  /**
   * Retrieve type, corresponding to any value.
   *
   * @return Object.class as type, corresponding to any value
   */
  @Override
  public Class<Object> getAnyType() {
    return Object.class;
  }

  /**
   * Retrieve name, representing any value.
   *
   * @return name, representing any value (Object.class)
   */
  @Override
  public String getAnyName() {
    return ANY_NAME;
  }

  /**
   * Supports serialization via SerializationProxy.
   *
   * @return proxy, corresponding to this TypeMapImpl
   */
  private Object writeReplace() {
    return new SerializationProxy(this);
  }

  /**
   * Should be serialized via proxy, thus no direct deserialization should occur.
   *
   * @param stream is stream from which object is to be read
   * @throws InvalidObjectException always
   */
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Use Serialization Proxy instead.");
  }

  private static final class SerializationProxy implements Serializable {

    private static final long serialVersionUID = 4943844360741067489L;
    private @Nullable List<TypeName<?>> types;
    private @Nullable List<TypeConverter<?, ?>> converters;

    SerializationProxy() {
    }

    // we create TypeMapImpl from TypeName entries, ensuring immutability; we do not want to keep
    // list of type names around just to suppress warnings on serialization...
    @SuppressWarnings("Immutable")
    SerializationProxy(TypeMapImpl value) {
      this.types = value.typesByName.entrySet().stream()
          .map(entry -> new TypeName<>(entry.getValue(), entry.getKey(),
              Objects.equals(value.namesByType.get(entry.getValue()), entry.getKey())))
          .collect(Collectors.toList());
      this.converters = value.converters
          .values()
          .stream()
          .map(Map::values)
          .flatMap(Collection::stream)
          .collect(Collectors.toList());
    }

    private Object readResolve() throws InvalidObjectException {
      if (types == null) {
        throw new InvalidObjectException("Types not read during TypeMapImpl deserialization");
      }
      if (converters == null) {
        throw new InvalidObjectException("Converters not read during TypeMapImpl deserialization");
      }
      return new TypeMapImpl(types, converters);
    }
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeMapImpl that = (TypeMapImpl) o;
    return typesByName.equals(that.typesByName)
        && namesByType.equals(that.namesByType)
        && converters.equals(that.converters);
  }

  @Override
  public int hashCode() {
    int result = typesByName.hashCode();
    result = 31 * result + namesByType.hashCode();
    result = 31 * result + converters.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "DefaultTypeMap{"
        + "typesByName=" + typesByName
        + ", namesByType=" + namesByType
        + ", converters=" + converters
        + '}';
  }
}
