package com.provys.common.datatype;

import com.google.errorprone.annotations.Immutable;
import com.provys.common.exception.InternalException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Immutable envelope around binary data. Given that it is backed by byte array (kept in memory), it
 * should not be used for too big binary files. On the other hand its advantage is immutability of
 * data - unlike streams that are stateful and thus not serializable.
 */
@Immutable
public final class DtBinaryData implements Serializable {

  private static final long serialVersionUID = -7205192074604076761L;

  @SuppressWarnings("Immutable") // data are never accessed directly, copy supplied by getter
  private final byte[] data;

  /**
   * Create binary data (byte array) as clone of supplied byte array.
   *
   * @param data is byte array containing data
   */
  public DtBinaryData(byte[] data) {
    this.data = Arrays.copyOf(data, data.length);
  }

  /**
   * Create binary data (byte array) from supplied input stream.
   *
   * @param inputStream is stream containing binary data to be stored in this object. Stream is read
   *                    and closed by this object
   */
  public DtBinaryData(InputStream inputStream) throws IOException {
    this.data = inputStream.readAllBytes();
    inputStream.close();
  }

  /**
   * Create binary data (byte array) from supplied Blob.
   *
   * @param blob containing data to be represented by this object
   */
  public DtBinaryData(Blob blob) {
    try {
      if (blob.length() > Integer.MAX_VALUE) {
        throw new InternalException("Cannot read BLOB bigger than 2 GB to memory");
      }
      this.data = blob.getBytes(1, Integer.MAX_VALUE);
    } catch (SQLException e) {
      throw new InternalException("Failed to read BLOB", e);
    }
  }

  /**
   * Get data as byte array. Clones data to ensure immutability of this object.
   *
   * @return data as byte array
   */
  public byte[] getData() {
    return Arrays.copyOf(data, data.length);
  }

  /**
   * Get input stream accessing data held by this object.
   *
   * @return input stream with data
   */
  public ByteArrayInputStream getInputStream() {
    return new ByteArrayInputStream(data);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DtBinaryData that = (DtBinaryData) o;
    return Arrays.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(data);
  }

  @Override
  public String toString() {
    return "DtBinaryData{"
        + "length=" + data.length
        + ", data=" + Arrays.toString(Arrays.copyOf(data, 30))
        + '}';
  }
}
