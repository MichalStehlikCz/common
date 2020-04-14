package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import com.provys.common.datatype.DtDate;
import com.provys.common.datatype.DtDateTime;
import com.provys.common.datatype.DtUid;
import com.provys.common.exception.InternalException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Default map uses service loader to retrieve registered maps from all available libraries. This
 * allows to extend types, available in this package with additional types as needed. Note that if
 * you load two libraries providing different mappings for given string, you will get warning in log
 * and only one mapping will be used, such situation should be avoided.
 * Mapping of {@code Object.class} to value ANY is hardcoded.
 */
@Immutable
public final class TypeMapImpl implements TypeMap {

  private static final Logger LOG = LogManager.getLogger(TypeMapImpl.class);

  private static final String ANY = "ANY";
  private static final TypeMapImpl DEFAULT;

  static {
    Stream<TypeName<? extends Serializable>> builtInStream = Stream.of(
        new TypeName<>(Boolean.class, "BOOLEAN"),
        new TypeName<>(Integer.class, "INTEGER"),
        new TypeName<>(Double.class, "NUMBER"),
        new TypeName<>(String.class, "STRING"),
        new TypeName<>(DtUid.class, "UID"),
        new TypeName<>(DtDate.class, "DATE"),
        new TypeName<>(DtDateTime.class, "DATETIME"),
        new TypeName<>(Byte.class, "BYTE"),
        new TypeName<>(BigInteger.class, "BIGINTEGER"),
        new TypeName<>(BigDecimal.class, "BIGDECIMAL"));
    var loaderStream = ServiceLoader.load(TypeModule.class).stream().map(Provider::get)
        .map(TypeModule::getTypes).flatMap(Collection::stream);
    var types = Stream.concat(builtInStream, loaderStream).collect(Collectors.toUnmodifiableList());
    DEFAULT = new TypeMapImpl(types);
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

  /**
   * Create type map based on supplied collection of type names.
   *
   * @param types are types and their names registered in this type map
   */
  public TypeMapImpl(Collection<TypeName<? extends Serializable>> types) {
    this.namesByType = types.stream()
        .collect(
            Collectors.toUnmodifiableMap(TypeName::getType, TypeName::getName, new NameMerger()));
    this.typesByName = types.stream()
        .collect(
            Collectors.toUnmodifiableMap(TypeName::getName, TypeName::getType, new ClassMerger()));
  }

  @Override
  public Class<?> getType(String name) {
    if (name.equals(ANY)) {
      return Object.class;
    }
    var result = typesByName.get(name);
    if (result == null) {
      throw new InternalException("No type mapping found for " + name);
    }
    return result;
  }

  @Override
  public String getName(Class<?> type) {
    if (type == Object.class) {
      return ANY;
    }
    var result = namesByType.get(type);
    if (result == null) {
      throw new InternalException("No type name found for class " + type);
    }
    return result;
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

    SerializationProxy() {
    }

    // we create TypeMapImpl from TypeName entries, ensuring immutability; we do not want to keep
    // list of type names around just to suppress warnings on serialization...
    @SuppressWarnings("Immutable")
    SerializationProxy(TypeMapImpl value) {
      this.types = value.namesByType.entrySet().stream()
          .map(entry -> new TypeName<>(entry.getKey(), entry.getValue()))
          .collect(Collectors.toList());
    }

    private Object readResolve() throws InvalidObjectException {
      if (types == null) {
        throw new InvalidObjectException("Types not read during TypeMapImpl deserialization");
      }
      return new TypeMapImpl(types);
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
    return Objects.equals(typesByName, that.typesByName)
        && Objects.equals(namesByType, that.namesByType);
  }

  @Override
  public int hashCode() {
    int result = typesByName != null ? typesByName.hashCode() : 0;
    result = 31 * result + (namesByType != null ? namesByType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DefaultTypeMap{"
        + "typesByName=" + typesByName
        + ", namesByType=" + namesByType
        + '}';
  }
}
