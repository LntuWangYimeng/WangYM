package study;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

public class DESS {
	public static final String content = "1234"  ;
    public DESS() {
    }
    //����
    public static void main(String args[]) {
		FileWriter writer;
		try {
			writer = new FileWriter("C:\\Users\\86176\\Desktop\\1.txt");
			writer.write(content);
			writer.flush();
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}

		System.out.println("����֮ǰ��" + content + "  ���ĳ���:" + content.length());

		// ������Կ
		 String password = "95880288";
	        byte[] result = DESS.encrypt(content.getBytes(),password);
		// ����
	
		System.out.println("���ܺ�����ݣ�" + new String(result) + "\n���ĳ��ȣ�" + result.length);
		
		File f = new File("D:\\b.txt");
        FileWriter fw = null;
		try {
			if(!f.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(new String(result),0,new String(result).length()-1);
			out.close();
	}catch(IOException e) {
		e.printStackTrace();
	}

		// ����
		byte[] decryResult;
		try {
			decryResult = DESS.decrypt(result, password);
			System.out.println("���ܺ�����ݣ�" + new String(decryResult));
			File f2 = new File("D:\\c.txt");
			if(!f2.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f2);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(new String(decryResult),0,new String(decryResult).length()-1);
			out.close();
		} catch (Exception e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}



	}
    /**
     * ����
     * @param datasource byte[]
     * @param password String
     * @return byte[]
     */
    public static  byte[] encrypt(byte[] datasource, String password) {            
        try{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        //����һ���ܳ׹�����Ȼ��������DESKeySpecת����
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher����ʵ����ɼ��ܲ���
        Cipher cipher = Cipher.getInstance("DES");
        //���ܳ׳�ʼ��Cipher����,ENCRYPT_MODE���ڽ� Cipher ��ʼ��Ϊ����ģʽ�ĳ���
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        //���ڣ���ȡ���ݲ�����
        //��ʽִ�м��ܲ���
        return cipher.doFinal(datasource); //�������ֲ������ܻ�������ݣ����߽���һ���ಿ�ֲ���
        }catch(Throwable e){
                e.printStackTrace();
        }
        return null;
}
    /**
     * ����
     * @param src byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
            // DES�㷨Ҫ����һ�������ε������Դ
            SecureRandom random = new SecureRandom();
            // ����һ��DESKeySpec����
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // ����һ���ܳ׹���
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");//����ʵ��ָ��ת���� Cipher ����
            // ��DESKeySpec����ת����SecretKey����
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher����ʵ����ɽ��ܲ���
            Cipher cipher = Cipher.getInstance("DES");
            // ���ܳ׳�ʼ��Cipher����
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // ������ʼ���ܲ���
            return cipher.doFinal(src);
        }
}
