package com.provys.common.types;

import java.util.Collection;

/**
 * Allows registration type / name mappings, supported by given library.
 */
public interface TypeModule {

  /**
   * Get list of types / names registered by this module.
   *
   * @return list of type / name pairs registered by this module.
   */
  Collection<TypeName> getTypes();
}
