package jenny.learn.springboot.security.util;

import cn.hutool.core.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsaUtils.class);
    private static final String PUBLIC_KEY_FILE_NAME = "publicKey.cer";
    private static final String PRIVATE_KEY_FILE_NAME = "privateKey.cer";

    public static void main(String[] args) throws Exception {
        String text = "Hello Jenny.";
        String cipher = encryptByPublicKey(text);
        LOGGER.info("cipher: {}", cipher);
        String plain = decryptByPrivateKey(cipher);
        LOGGER.info("plain: {}", plain);
    }

    /**
     * 生成密钥文件（公钥、私钥）
     */
    public static void generateKeyFiles() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024, new SecureRandom());
            // 生成密钥对
            KeyPair keyPair = keyGen.generateKeyPair();
            byte[] publicKey = keyPair.getPublic().getEncoded();
            byte[] privateKey = keyPair.getPrivate().getEncoded();
            // 生成公钥文件
            FileOutputStream publicKeyFileOutputStream = new FileOutputStream(PUBLIC_KEY_FILE_NAME);
            publicKeyFileOutputStream.write(publicKey);
            LOGGER.info("## public key file generate successfully. ##");
            // 生成私钥文件
            FileOutputStream privateKeyFileOutputStream = new FileOutputStream(PRIVATE_KEY_FILE_NAME);
            privateKeyFileOutputStream.write(privateKey);
            LOGGER.info("## private key file generate successfully. ##");
        } catch (Exception e) {
            LOGGER.error("generate key files error. ", e);
        }
    }

    /**
     * 获取公钥
     * @return 公钥
     */
    public static PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            FileInputStream fis = new FileInputStream(PUBLIC_KEY_FILE_NAME);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }
            byte[] keyBytes = out.toByteArray();
            LOGGER.info("public key: {}", new String(Base64.encode(keyBytes)));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(spec);
        } catch (Exception e) {
            LOGGER.error("get public key error.", e);
        }
        return publicKey;
    }

    /**
     * 获取私钥
     * @return 私钥
     */
    public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            FileInputStream fis = new FileInputStream(PRIVATE_KEY_FILE_NAME);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }
            byte[] keyBytes = out.toByteArray();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(spec);
        } catch (Exception e) {
            LOGGER.error("get private key error.", e);
        }
        return privateKey;
    }

    /**
     * 公钥加密
     *
     * @param text 待加密的文本
     * @return 密文
     */
    public static String encryptByPublicKey(String text) throws Exception {
        PublicKey publicKey = getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encode(result);
    }

    /**
     * 公钥解密
     *
     * @param text 待解密的信息
     * @return 明文
     */
    public static String decryptByPublicKey(String text) throws Exception {
        PublicKey publicKey = getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decode(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param text 待加密的信息
     * @return 密文
     */
    public static String encryptByPrivateKey(String text) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encode(result);
    }

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 明文
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decode(text));
        return new String(result);
    }
}
