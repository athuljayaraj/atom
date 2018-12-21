/**
 * 
 */
package com.flytxt.atom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flytxt.atom.entity.Complex;

/**
 * @author athul
 *
 */
public class PerformanceTest {

	public static Logger logger = LoggerFactory.getLogger(PerformanceTest.class);

	/**
	 * @throws IOException
	 * 
	 */
	public static void serializer() throws IOException {

		Complex complex = new Complex(true);
		// Instantiating the Schema.Parser class.
		Schema schema = new Schema.Parser().parse(
				"{\"type\":\"record\",\"name\":\"Complex\",\"namespace\":\"com.flytxt.atom.entity\",\"fields\":[{\"name\":\"longArray\",\"type\":[{\"type\":\"array\",\"items\":[\"long\",\"null\"],\"java-class\":\"[Ljava.lang.Long;\"},\"null\"]},{\"name\":\"boolArray\",\"type\":[{\"type\":\"array\",\"items\":[\"boolean\",\"null\"],\"java-class\":\"[Ljava.lang.Boolean;\"},\"null\"]}]}");
		// Instantiating the GenericRecord class.
		byte[] serealizeAvroHttpRequestJSON = serealizeAvroHttpRequestJSON(complex, schema);

		System.out.println(serealizeAvroHttpRequestJSON.length);
	}

	public static byte[] serealizeAvroHttpRequestJSON(Complex request, Schema schema) {

		DatumWriter<Complex> writer = new GenericDatumWriter<>(schema);
		byte[] data = new byte[0];
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Encoder jsonEncoder = null;
		try {
			jsonEncoder = EncoderFactory.get().jsonEncoder(schema, stream);
			writer.write(request, jsonEncoder);
			jsonEncoder.flush();
			data = stream.toByteArray();
		} catch (IOException e) {
			logger.error("Serialization error:" + e.getMessage());
		}
		return data;
	}

	public static void main(String[] args) throws IOException {
		serializer();
	}
}
