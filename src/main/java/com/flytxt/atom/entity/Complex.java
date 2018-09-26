package com.flytxt.atom.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.avro.Schema;

//import org.apache.avro.Schema;

import com.flytxt.atom.SerializationException;

/**
 * @author athul
 *
 */
public class Complex {

	public Boolean[] boolArray = new Boolean[50];
	public Long[] longArray = new Long[50];
	private static final Map<Byte, Schema> schemas = new HashMap<>();

	static {
		schemas.put((byte) 1, new Schema.Parser().parse(
				"{\"type\":\"record\",\"name\":\"Complex\",\"namespace\":\"com.flytxt.atom.entity\",\"fields\":[{\"name\":\"longArray\",\"type\":[{\"type\":\"array\",\"items\":[\"long\",\"null\"],\"java-class\":\"[Ljava.lang.Long;\"},\"null\"]},{\"name\":\"boolArray\",\"type\":[{\"type\":\"array\",\"items\":[\"boolean\",\"null\"],\"java-class\":\"[Ljava.lang.Boolean;\"},\"null\"]}]}"));
	}

	public Complex(boolean setValues) {
		Random random = new Random();
		long randomLong = random.nextLong();

		for (int i = 0; i < 50; i++) {
			this.longArray[i] = random.nextInt() % 2 == 0 ? randomLong += 20 : null;
			boolArray[i] = (i % (random.nextInt(50) + 1)) % 2 == 0 ? random.nextBoolean() : null;
		}
	}

	public Complex() {

	}

	public Compressed compress() {

		Compressed compressed = new Compressed();

		for (int i = 0; i < 50; i++) {
			int theChosenOne = i / 4, position = i % 4;
			if (this.boolArray[i] == null) {
				compressed.boolArray[theChosenOne] |= (2 << (6 - (2 * position)));
			} else if (this.boolArray[i]) {
				compressed.boolArray[theChosenOne] |= (3 << (6 - (2 * position)));
			}
		}

		boolean isLongBaseSet = false;
		int valuesPresent = 0;
		for (int i = 0; i < 50; i++) {
			if (this.longArray[i] != null) {
				int theChosenOne = i / 8, position = i % 8;
				compressed.longArrayValuePresent[theChosenOne] |= (1 << (7 - position)); // setting the corresponding
																							// bit
				if (isLongBaseSet) {
					long difference = this.longArray[i] - compressed.longBase;
					if (difference != (int) difference) { // difference is LONG
						compressed.diffValuesLong[valuesPresent] = difference;
						compressed.longArrayValueType[theChosenOne] |= (1 << (7 - position)); // setting the
																								// corresponding bit
					} else { // difference is INT
						compressed.diffValuesInt[valuesPresent] = (int) difference;
					}
				} else {
					compressed.longBase = this.longArray[i];
					isLongBaseSet = true;
				}
				valuesPresent += 1;
			}
		}
		return compressed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++)
			sb.append(boolArray[i] + ",");
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n");

		for (int i = 0; i < 50; i++)
			sb.append(longArray[i] + ",");
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	public static void main(String[] args) throws IOException, SerializationException {

		Complex testObj = new Complex(true);
		System.out.println(testObj);

		long t1 = System.currentTimeMillis();
		Compressed compressed = testObj.compress();

		byte[] serialized = compressed.serialize();
		Compressed deserialized = Compressed.deserialize(serialized);

		Complex expanded = deserialized.expand();
		long t2 = System.currentTimeMillis() - t1;
		System.out.println(expanded);
		System.out.println("Time taken:" + t2);

	}
}
