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
    //测试
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

		System.out.println("加密之前：" + content + "  明文长度:" + content.length());

		// 生成密钥
		 String password = "95880288";
	        byte[] result = DESS.encrypt(content.getBytes(),password);
		// 加密
	
		System.out.println("加密后的内容：" + new String(result) + "\n密文长度：" + result.length);
		
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

		// 解密
		byte[] decryResult;
		try {
			decryResult = DESS.decrypt(result, password);
			System.out.println("解密后的内容：" + new String(decryResult));
			File f2 = new File("D:\\c.txt");
			if(!f2.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f2);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(new String(decryResult),0,new String(decryResult).length()-1);
			out.close();
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}



	}
    /**
     * 加密
     * @param datasource byte[]
     * @param password String
     * @return byte[]
     */
    public static  byte[] encrypt(byte[] datasource, String password) {            
        try{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        //现在，获取数据并加密
        //正式执行加密操作
        return cipher.doFinal(datasource); //按单部分操作加密或解密数据，或者结束一个多部分操作
        }catch(Throwable e){
                e.printStackTrace();
        }
        return null;
}
    /**
     * 解密
     * @param src byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");//返回实现指定转换的 Cipher 对象
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(src);
        }
}
