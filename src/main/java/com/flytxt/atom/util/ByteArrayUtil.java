package com.flytxt.atom.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ByteArrayUtil {

	public static String byteToBits(byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	}

	protected static void checkBytesNotNull(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("Byte array was null");
		}
	}

	protected static void checkBytes(byte[] bytes, int offset, int length, int expectedLength) {
		try {
			checkOffsetLength(bytes.length, offset, length);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Byte array was null");
		}
		if (length != expectedLength) {
			throw new IllegalArgumentException(
					"Unexpected length of byte array [expected=" + expectedLength + ", actual=" + length + "]");
		}
	}

	static protected void checkOffsetLength(int bytesLength, int offset, int length) {
		// offset cannot be negative
		if (offset < 0 || length < 0) {
			throw new IllegalArgumentException(
					"The byte[] offset or length parameter cannot be negative offset:" + offset + " length: " + length);
		}
		// is it a valid offset? Must be < bytes.length if non-zero
		// if its zero, then the check below will validate if it would cause
		// a read past the length of the byte array
		if (offset != 0 && offset >= bytesLength) {
			throw new IllegalArgumentException("The byte[] offset (" + offset
					+ ") must be < the length of the byte[] length (" + bytesLength + ")");
		}
		if (offset + length > bytesLength) {
			throw new IllegalArgumentException("The offset+length (" + (offset + length)
					+ ") would read past the end of the byte[] (length=" + bytesLength + ")");
		}
	}

	public static byte[] toByteArray(byte value) {
		return new byte[] { value };
	}

	public static byte[] toByteArray(short value) {
		byte[] buf = new byte[2];
		buf[1] = (byte) (value & 0xFF);
		buf[0] = (byte) ((value >>> 8) & 0xFF);
		return buf;
	}

	public static byte[] toByteArray(int value) {
		byte[] buf = new byte[4];
		buf[3] = (byte) (value & 0xFF);
		buf[2] = (byte) ((value >>> 8) & 0xFF);
		buf[1] = (byte) ((value >>> 16) & 0xFF);
		buf[0] = (byte) ((value >>> 24) & 0xFF);
		return buf;
	}

	public static byte[] toByteArray(long value) {
		byte[] buf = new byte[8];
		buf[7] = (byte) (value & 0xFF);
		buf[6] = (byte) ((value >>> 8) & 0xFF);
		buf[5] = (byte) ((value >>> 16) & 0xFF);
		buf[4] = (byte) ((value >>> 24) & 0xFF);
		buf[3] = (byte) ((value >>> 32) & 0xFF);
		buf[2] = (byte) ((value >>> 40) & 0xFF);
		buf[1] = (byte) ((value >>> 48) & 0xFF);
		buf[0] = (byte) ((value >>> 56) & 0xFF);
		return buf;
	}

	public static byte[] toByteArray(BigDecimal bDecimal) {
		BigInteger bInt = bDecimal.unscaledValue();
		byte[] data = bInt.toByteArray();
		byte[] scale = toByteArray(bDecimal.scale());
		byte[] length = toByteArray(data.length);
		byte[] result = new byte[data.length + scale.length + length.length];
		System.arraycopy(length, 0, result, 0, length.length);
		System.arraycopy(scale, 0, result, length.length, scale.length);
		System.arraycopy(data, 0, result, length.length + scale.length, data.length);
		return result;
	}

	public static byte[] toByteArray(Double dValue) {
		long lValue = Double.doubleToLongBits(dValue);
		return toByteArray(lValue);
	}

	public static byte[] toByteArray(long[] longArray) {
		int arraySize = longArray.length * 8;
		if (arraySize == 0) {
			return null;
		}
		arraySize += 4;
		byte[] data = new byte[arraySize];
		System.arraycopy(toByteArray(longArray.length), 0, data, 0, 4);
		int index = 4;
		for (int i = 0; i < longArray.length; i++) {
			System.arraycopy(toByteArray(longArray[i]), 0, data, index, 8);
			index += 8;
		}
		return data;
	}

	public static byte[] toByteArray(Long[] longArray) {
		int arraySize = longArray.length * 8;
		if (arraySize == 0) {
			return null;
		}
		arraySize += 4;
		byte[] data = new byte[arraySize];
		System.arraycopy(toByteArray(longArray.length), 0, data, 0, 4);
		int index = 4;
		for (int i = 0; i < longArray.length; i++) {
			System.arraycopy(toByteArray(longArray[i]), 0, data, index, 8);
			index += 8;
		}
		return data;
	}

	public static byte[] toByteArray(double[] doubleArray) {
		int arraySize = doubleArray.length * 8;
		if (arraySize == 0) {
			return null;
		}
		arraySize += 4;
		byte[] data = new byte[arraySize];
		System.arraycopy(toByteArray(doubleArray.length), 0, data, 0, 4);
		int index = 4;
		for (int i = 0; i < doubleArray.length; i++) {
			System.arraycopy(toByteArray(doubleArray[i]), 0, data, index, 8);
			index += 8;
		}
		return data;
	}

	public static byte[] toByteArray(boolean data) {
		return new byte[] { (byte) (data ? 0x01 : 0x00) };
	}

	public static byte toByte(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toByte(bytes, 0, bytes.length);
	}

	public static byte toByte(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 1);
		return bytes[offset];
	}

	public static short toUnsignedByte(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toUnsignedByte(bytes, 0, bytes.length);
	}

	public static boolean toBoolean(byte[] bytes) {
		checkBytesNotNull(bytes);
		return bytes[0] != 0x00;
	}

	public static boolean toBoolean(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 1);
		return bytes[offset] != 0x00;
	}

	public static short toUnsignedByte(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 1);
		short v = 0;
		v |= bytes[offset] & 0xFF;
		return v;
	}

	public static short toUnsignedByte(byte bytE) {
		short v = 0;
		v |= bytE & 0xFF;
		return v;
	}

	public static short toShort(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toShort(bytes, 0, 2);
	}

	public static short toShort(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 2);
		short v = 0;
		v |= bytes[offset] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 1] & 0xFF;
		return v;
	}

	public static int toUnsignedShort(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toUnsignedShort(bytes, 0, 2);
	}

	public static int toUnsignedShort(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 2);
		int v = 0;
		v |= bytes[offset] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 1] & 0xFF;
		return v;
	}

	public static int toInt(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toInt(bytes, 0, 4);
	}

	public static int toInt(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 4);
		int v = 0;
		v |= bytes[offset] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 1] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 2] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 3] & 0xFF;
		return v;
	}

	public static long toUnsignedInt(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toUnsignedInt(bytes, 0, 4);
	}

	public static long toUnsignedInt(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 4);
		long v = 0;
		v |= bytes[offset] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 1] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 2] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 3] & 0xFF;
		return v;
	}

	public static long toLong(byte[] bytes) {
		checkBytesNotNull(bytes);
		return toLong(bytes, 0, 8);
	}

	public static long toLong(byte[] bytes, int offset, int length) {
		checkBytes(bytes, offset, length, 8);
		long v = 0;
		v |= bytes[offset] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 1] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 2] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 3] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 4] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 5] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 6] & 0xFF;
		v <<= 8;
		v |= bytes[offset + 7] & 0xFF;
		return v;
	}

	public static double toDouble(byte[] bytes) {
		return toDouble(bytes, 0, 8);
	}

	public static double toDouble(byte[] bytes, int offset, int length) {
		long l = ByteArrayUtil.toLong(bytes, offset, length);
		return Double.longBitsToDouble(l);
	}

	public static Long[] toLongArray(byte[] bytes) {
		int size = toInt(bytes, 0, 4), offset = 0;
		Long[] longArray = new Long[size];
		long v;
		offset += 4;
		for (int i = 0; i < size; i += 1, offset += 8) {
			v = 0;
			v |= bytes[offset] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 1] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 2] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 3] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 4] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 5] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 6] & 0xFF;
			v <<= 8;
			v |= bytes[offset + 7] & 0xFF;
			longArray[i] = v;
		}

		return longArray;
	}

	public static BigDecimal toBigDecimal(byte[] bytes, int offset, int length) {
		int scale = toInt(bytes, offset, 4);
		offset += 4;
		byte[] bigIntBytes = new byte[length];
		System.arraycopy(bytes, offset, bigIntBytes, 0, length);
		BigInteger bigInt = new BigInteger(bigIntBytes);
		return new BigDecimal(bigInt, scale);
	}

	public static BigDecimal toBigDecimal(byte[] bytes) {
		int length = toInt(bytes, 0, 4);
		return toBigDecimal(bytes, 4, length);
	}

	public static int nearestPowerOfTwo(int number) {
		number -= 1;
		for (int i = 0; i < 8; i++) {
			number = number | (number >> i);
		}
		return number + 1;
	}

	public static int cOctetStringLength(byte[] bytes, int offset, int length) {
		int octetStringOffset = offset;
		for (int i = 0; i < length; i += 1) {
			if (bytes[octetStringOffset] == 0) {
				break;
			}
			octetStringOffset += 1;
		}
		return (octetStringOffset + 1 - offset);
	}

	public static String getOctetString(byte[] octetBytes) {
		return new String(octetBytes, 0, octetBytes.length - 1);
	}

}