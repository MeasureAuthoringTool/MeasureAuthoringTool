package mat.server.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncryptDecryptToken {

	@Autowired
	StandardPBEStringEncryptor standardPBEStringEncryptor;
	
	public String decryptKey(String token) {
		return standardPBEStringEncryptor.decrypt(token);
	}

	public String encryptKey(String token) {
		return standardPBEStringEncryptor.encrypt(token);
	}
	
}
