package sem.datenhaltung.semmodel.impl;

import sem.datenhaltung.semmodel.entities.Konto;
import sem.datenhaltung.semmodel.services.ICRUDKonto;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CRUD Klasse die die ICRUDKonto Schnitstelle implementiert.
 * Bietet Methoden fuer den Zugriff auf die Kontos aus der Datenbank.
 */
public class CRUDKonto extends DBCRUDTeamplate<Konto> implements ICRUDKonto {
    private static final String TABLE_NAME = "konto";
    private static final String COLUMN_KID = "kid";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORT = "passwort";
    private static final String COLUMN_IMAPHOST = "imaphost";
    private static final String COLUMN_SMTPHOST = "smtphost";
    private static final String COLUMN_PORT = "port";
    private static final String COLUMN_SIGNATUR = "signatur";

    private static final String SQL_INSERT_KONTO = "INSERT INTO konto (UserName, " +
            "IMAPhost, PassWort, Port, SMTPhost, signatur)" + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_KONTO = "UPDATE konto SET userName = ?, passwort = ?, imaphost = ?, smtphost = ?," +
            " port = ?, signatur = ? WHERE kid = ?";

    @Override
    protected Konto makeObject(ResultSet rs) throws SQLException{
        Konto konto = new Konto();
        konto.setKid(rs.getInt(COLUMN_KID));
        konto.setUserName(rs.getString(COLUMN_USERNAME));
        konto.setIMAPhost(rs.getString(COLUMN_IMAPHOST));
        konto.setPassWort(rs.getString(COLUMN_PASSWORT));
        konto.setPort(rs.getInt(COLUMN_PORT));
        konto.setSMTPhost(rs.getString(COLUMN_SMTPHOST));
        konto.setSignatur(rs.getString(COLUMN_SIGNATUR));
        return konto;
    }

    @Override
    public int createKonto(Konto konto){
        konto.setPassWort(encryptPassword(konto.getPassWort()));
        try {
            int id = insertAndReturnKey(SQL_INSERT_KONTO, konto.getUserName(),
                    konto.getIMAPhost(), konto.getPassWort(), konto.getPort(), konto.getSMTPhost(), konto.getSignatur());
            konto.setKid(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteKonto(int kid){
        int ret = 0;
        try {
            ret = updateOrDelete(String.format(SQL_DELETE_FROM_WHERE, TABLE_NAME, COLUMN_KID),kid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    @Override
    public boolean updateKonto(Konto konto){
        int ret;
        try {
            ret = updateOrDelete(SQL_UPDATE_KONTO, konto.getUserName(), konto.getPassWort(),
                    konto.getIMAPhost(), konto.getSMTPhost(),
                    konto.getPort(), konto.getSignatur(), konto.getKid());
            return 1 == ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Konto getKontoById(int kid){
        ArrayList<Konto> kontos;
        try {
            kontos = query(String.format(SQL_SELECT_FROM_WHERE, TABLE_NAME, COLUMN_KID), kid);
            if(kontos.size() > 0){
                kontos.get(0).setPassWort(decryptPassword(kontos.get(0).getPassWort()));
                return kontos.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Konto> getAlleKonten(){
        try {
            ArrayList<Konto> kontos =  query(String.format(SQL_SELECT_FROM, TABLE_NAME));
            for(Konto k : kontos){
                k.setPassWort(decryptPassword(k.getPassWort()));
            }
            return kontos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    /**
     * Methode zum verschluesseln eines PlainText passwortes
     * @param userPWD
     * @return
     */
    private String encryptPassword(String userPWD){
        String encryptedPassword = "";
        try {
            //Key generieren
            SecretKeySpec secretKeySpec = generateCrypKey();

            // Verschluesseln
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(userPWD.getBytes());

            // bytes zu Base64-String konvertieren (dient der Lesbarkeit)
            BASE64Encoder myEncoder = new BASE64Encoder();
            encryptedPassword = myEncoder.encode(encrypted);
        }
        catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException
                | InvalidKeyException e) {
            System.out.println("Beim Verschlüssen wurde eine Exception geworfen: " + e.getMessage() + "\n");
            e.printStackTrace();

        }
        return encryptedPassword;
    }


    /**
     * Methode zum entschliesseln eines Plaintextpasswortes
     * @param userPWD
     * @return
     */
    private String decryptPassword(String userPWD){
        String decryptedPassword = "";
        try {
            //Key generieren
            SecretKeySpec secretKeySpec = generateCrypKey();

            // BASE64 String zu Byte-Array konvertieren
            BASE64Decoder myDecoder2 = new BASE64Decoder();
            byte[] crypted2 = myDecoder2.decodeBuffer(userPWD);

            // Entschluesseln
            Cipher cipher2 = null;
            cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherData2 = new byte[0];
            cipherData2 = cipher2.doFinal(crypted2);
            decryptedPassword = new String(cipherData2);

        }
        catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException | IOException e) {
            System.out.println("Beim Entschlüssen wurde eine Exception geworfen: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        return decryptedPassword;
    }


    /**
     * Hilfstmethode zum ent und verschluesseln von passwoertern
     * @return
     */
    private SecretKeySpec generateCrypKey(){
        //Key generieren, Den Wert letzendlich aus der DB holen
        String cryptKey = "123456789";
        SecretKeySpec secretKeySpec = null;
        byte[] key = new byte[0];
        try {
            key = cryptKey.getBytes("UTF-8");

            MessageDigest sha = null;
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println("Beim Generieren des Schlüssels wurde eine Exception geworfen: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        return secretKeySpec;
    }
}
