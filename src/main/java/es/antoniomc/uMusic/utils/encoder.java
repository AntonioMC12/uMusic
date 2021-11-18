package es.antoniomc.uMusic.utils;

import org.mindrot.jbcrypt.BCrypt;

public class encoder {

	/**
	 * Método que recibe una contraseña y la encripta para su posterior guardado en 
	 * la base de datos
	 * 
	 * @param texto contraseña
	 * @return String con la encriptación
	 */
	public static String encrypt(String texto) {
		return BCrypt.hashpw(texto, BCrypt.gensalt());
	}

	/**
	 * Método que nos comprueba dos String, uno encriptado y otro sin encriptar
	 * para saber si son iguales.
	 * 
	 * @param plainPassword contraseña sin encriptar
	 * @param hashedPassword contraseña encriptada
	 * @return true si coinciden, false sino.
	 */
	public static boolean matchPass(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
