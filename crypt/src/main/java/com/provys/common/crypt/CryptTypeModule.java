package com.provys.common.crypt;

import com.provys.common.types.DefaultTypeConverter;
import com.provys.common.types.TypeConverter;
import com.provys.common.types.TypeModule;
import com.provys.common.types.TypeName;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Register Provys compatible types, registered in this module - DtEncryptedString.
 */
public final class CryptTypeModule implements TypeModule {

  /**
   * Get list of types / names registered by this module.
   *
   * @return list of type / name pairs registered by this module.
   */
  @Override
  public Collection<TypeName<? extends Serializable>> getTypes() {
    return List.of(new TypeName<>(DtEncryptedString.class, "PASSWORD"));
  }

  /**
   * Get list of converters registered by this module.
   *
   * @return list of converters registered by this module
   */
  @Override
  public Collection<TypeConverter<?, ?>> getConverters() {
    return List.of(
        new DefaultTypeConverter<>(DtEncryptedString.class, String.class, true,
            DtEncryptedString::getValue)
        , new DefaultTypeConverter<>(String.class, DtEncryptedString.class, true,
            DtEncryptedString::valueOf)
    );
  }
}
