package net.mattbenson.network.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.stream.Stream;

public class EncryptionUtils {
	public EncryptionUtils() {}
	
	public static String encryptPacket(String key, String message) {
		int[] output = new int[message.length()];
		
		for(int i = 0; i < message.length(); i++)
			output[i] = (Integer.valueOf(message.charAt(i)) ^ Integer.valueOf(key.charAt(i % (key.length() - 1)))) + '0';
		
		return Arrays.toString(output);
	}
	
	public static byte[] encryptPacketToBytes(String key, String message) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
				
		for(int i = 0; i < message.length(); i++) {
			try {
				dos.writeInt((Integer.valueOf(message.charAt(i)) ^ Integer.valueOf(key.charAt(i % (key.length() - 1)))) + '0');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return baos.toByteArray();
	}
	
	public static String decryptPacket(String key, String message) {
		int[] theMessage = Stream.of(message.replace("[", "").replace("]", "").split(", ")).mapToInt(Integer::parseInt).toArray();
		char[] outputMessage = new char[theMessage.length];
		
		for(int i = 0; i < theMessage.length; i++) {
			outputMessage[i] = (char) ((theMessage[i] - 48) ^ (int) key.charAt(i % (key.length() - 1)));
		}
		
		return new String(outputMessage);
	}
	
	public static String decryptPacketFromBytes(String key, byte[] message) {
		IntBuffer intBuf = ByteBuffer.wrap(message).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] theMessage = new int[intBuf.remaining()];
		intBuf.get(theMessage);
		
		char[] outputMessage = new char[theMessage.length];
		
		for(int i = 0; i < theMessage.length; i++) {
			outputMessage[i] = (char) ((theMessage[i] - 48) ^ (int) key.charAt(i % (key.length() - 1)));
		}
		
		return new String(outputMessage);
	}
}
