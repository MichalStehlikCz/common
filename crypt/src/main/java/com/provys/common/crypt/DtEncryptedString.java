package com.provys.common.crypt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.errorprone.annotations.Immutable;
import com.provys.common.exception.InternalException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Immutable data type, corresponding to PROVYS's PASSWORD domain. Works with IIS$ encrypted
 * Strings, allows their encryption and decryption.
 */
@Immutable
@SuppressWarnings("CyclicClassDependency") // dependency between class and its converter is ok
@JsonSerialize(converter = DtEncryptedStringToStringConverter.class)
@JsonDeserialize(converter = DtEncryptedStringFromStringConverter.class)
public final class DtEncryptedString implements Serializable {

  private static final Key IIS_KEY = new SecretKeySpec(DatatypeConverter.parseHexBinary(
          "DEA682B7BAB78F443B438508AE58BC11C1182122DE5F01804ABE9C9494E018FF"),
      "AES");
  private static final AlgorithmParameterSpec IV_SPEC = new IvParameterSpec(
      DatatypeConverter.parseHexBinary("71A4A512BF922435D7F0C98DC9B42E4B"));
  private static final long serialVersionUID = 61808702334184564L;

  /**
   * Static factory method, producing instance of encrypted string. If string starts with IIS$, it
   * is considered encrypted and not modified. Otherwise, it is encrypted using IIS encryption
   * algorithm compatible with server
   *
   * @param value is supplied string value this encrypted value should be based on
   * @return encrypted wrapper of supplied value
   */
  public static DtEncryptedString valueOf(String value) {
    return new DtEncryptedString(value);
  }

  private final String iisValue;

  private static Cipher getCipher() {
    try {
      return Cipher.getInstance("AES/CBC/NoPadding");
    } catch (NoSuchAlgorithmException e) {
      throw new InternalException("Algorithm AES/CBC/NoPadding not recognized by JDK", e);
    } catch (NoSuchPaddingException e) {
      throw new InternalException("Padding NoPadding not recognized by JDK", e);
    }
  }

  private static String encrypt(String value) {
    if (value.startsWith("IIS$")) {
      return value;
    }
    byte[] utf8Value = value.getBytes(StandardCharsets.UTF_8);
    byte[] data = new byte[((utf8Value.length + 8 + 15) / 16 * 16)]; // round up to nearest 16
    data[0] = 'I';
    data[1] = 1;
    System.arraycopy(utf8Value, 0, data, 8, utf8Value.length);
    Cipher cipher = getCipher();
    try {
      cipher.init(Cipher.ENCRYPT_MODE, IIS_KEY, IV_SPEC);
    } catch (InvalidKeyException e) {
      throw new InternalException("Key rejected", e);
    } catch (InvalidAlgorithmParameterException e) {
      throw new InternalException("Algorithm parameter rejected", e);
    }
    try {
      return "IIS$" + "00000000" +
          Base64.getEncoder().encodeToString(cipher.doFinal(data));
    } catch (IllegalBlockSizeException e) {
      throw new InternalException("Illegal block size reported", e);
    } catch (BadPaddingException e) {
      throw new InternalException("Bad padding reported", e);
    }
  }

  private static String decrypt(String iisValue) {
    if (!iisValue.startsWith("IIS$")) {
      throw new InternalException("Cannot decrypt supplied string - not IIS$ value");
    }
    byte[] rawValue = Base64.getDecoder().decode(iisValue.substring(12));
    Cipher cipher = getCipher();
    try {
      cipher.init(Cipher.DECRYPT_MODE, IIS_KEY, IV_SPEC);
    } catch (InvalidKeyException e) {
      throw new InternalException("Key rejected", e);
    } catch (InvalidAlgorithmParameterException e) {
      throw new InternalException("Algorithm parameter rejected", e);
    }
    byte[] decrypted;
    try {
      decrypted = cipher.doFinal(rawValue);
    } catch (IllegalBlockSizeException e) {
      throw new InternalException("Illegal block size reported", e);
    } catch (BadPaddingException e) {
      throw new InternalException("Bad padding reported", e);
    }
    // we use zero padded string encryption
    int size = 8;
    while ((size < decrypted.length) && (decrypted[size] != 0)) {
      size++;
    }
    return new String(decrypted, 8, size-8, StandardCharsets.UTF_8);
  }

  private DtEncryptedString(String value) {
    this.iisValue = encrypt(value);
  }

  /**
   * Encrypted value of this encrypted string.
   *
   * @return value of field iisValue
   */
  public String getIisValue() {
    return iisValue;
  }

  /**
   * Decrypted value of this encrypted string.
   *
   * @return decrypted value
   */
  public String getValue() {
    return decrypt(iisValue);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DtEncryptedString that = (DtEncryptedString) o;
    return iisValue.equals(that.iisValue);
  }

  @Override
  public int hashCode() {
    return iisValue.hashCode();
  }

  @Override
  public String toString() {
    return "DtEncryptedString{"
        + "iisValue='" + getIisValue() + '\''
        + '}';
  }
}
