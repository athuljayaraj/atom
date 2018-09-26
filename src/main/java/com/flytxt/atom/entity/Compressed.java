package com.flytxt.atom.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.flytxt.atom.SerializationException;
import com.flytxt.atom.util.ByteArrayUtil;

/**
 * @author athul
 *
 */
public class Compressed {

	public byte boolArray[] = new byte[13];

	public long longBase;
	public byte longArrayValuePresent[] = new byte[7];
	public byte longArrayValueType[] = new byte[7];
	public long diffValuesLong[] = new long[50];
	public int diffValuesInt[] = new int[50];

	public Complex expand() {

		Complex complex = new Complex();
		for (int i = 0; i < 50; i++) {
			int theChosenOne = i / 4, position = i % 4;
			int value = (this.boolArray[theChosenOne] >> (2 * (3 - position))) & 3; // get 2 bits from each position
			switch (value) {
			case 0:
				complex.boolArray[i] = false;
				break;
			case 3:
				complex.boolArray[i] = true;
				break;
			default:
				complex.boolArray[i] = null;
				break;
			}
		}

		long baseValue = this.longBase;
		int valuesPresent = 0;
		for (int i = 0; i < 50; i++) {
			int theChosenOne = i / 8, position = i % 8;
			boolean valuePresent = ((this.longArrayValuePresent[theChosenOne] >> (7 - position)) & 1) == 1 ? true
					: false;
			if (valuePresent) {
				boolean isDiffLong = ((this.longArrayValueType[theChosenOne] >> (7 - position)) & 1) == 1 ? true
						: false;
				if (isDiffLong) {
					complex.longArray[i] = this.diffValuesLong[valuesPresent] + baseValue;
				} else {
					complex.longArray[i] = this.diffValuesInt[valuesPresent] + baseValue;
				}
				valuesPresent++;
			}
		}
		return complex;
	}

	public byte[] serialize() throws IOException, SerializationException {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			stream.write(boolArray); // writing boolean array to stream
			stream.write(ByteArrayUtil.toByteArray(longBase));
			stream.write(longArrayValuePresent);
			stream.write(longArrayValueType);

			// writing long-int to stream
			int valuesPresent = 0;
			for (int i = 0; i < 50; i++) {
				int theChosenOne = i / 8, position = theChosenOne % 8;
				if (((longArrayValuePresent[theChosenOne] >> position) & 1) == 1) {
					if (((longArrayValueType[theChosenOne] >> position) & 1) == 1) {
						stream.write(ByteArrayUtil.toByteArray(diffValuesLong[valuesPresent++]));
					} else {
						stream.write(ByteArrayUtil.toByteArray(diffValuesInt[valuesPresent++]));
					}
				}
			}
			stream.flush();
			return stream.toByteArray();
		} catch (Exception E) {
			throw new SerializationException();
		}

	}

	public static Compressed deserialize(byte[] data) {
		Compressed compressed = new Compressed();
		int bytePosition;
		for (bytePosition = 0; bytePosition < 13; bytePosition++) {
			compressed.boolArray[bytePosition] = data[bytePosition];
		}
		compressed.longBase = ByteArrayUtil.toLong(data, bytePosition, 8);
		bytePosition += 8;
		for (int i = 0; i < 7; i++, bytePosition++) {
			compressed.longArrayValuePresent[i] = data[bytePosition];
			compressed.longArrayValueType[i] = data[bytePosition + 7];
		}
		bytePosition += 7;

		int valuesPresent = 0;
		for (int i = 0; i < 50; i++) {
			int theChosenOne = i / 8, position = i % 8;
			if (((compressed.longArrayValuePresent[theChosenOne] >> position) & 1) == 1) {
				if (((compressed.longArrayValueType[theChosenOne] >> position) & 1) == 1) {
					compressed.diffValuesLong[valuesPresent++] = ByteArrayUtil.toLong(data, bytePosition, 8);
					bytePosition += 8;
				} else {
					compressed.diffValuesInt[valuesPresent++] = ByteArrayUtil.toInt(data, bytePosition, 4);
					bytePosition += 4;
				}
			}
		}
		return compressed;
	}

}
