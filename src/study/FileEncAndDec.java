package study;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileEncAndDec {
    private static final int numOfEncAndDec = 0x99; //加密解密秘钥
    private static int dataOfFile = 0; //文件字节内容
    public static void main(String[] args) {

        File srcFile = new File("C:\\Users\\86176\\Desktop\\1.txt"); //初始文件
        File encFile = new File("C:\\Users\\86176\\Desktop\\ency.txt"); //加密文件

        try {
            EncFile(srcFile, encFile); //加密操作
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void EncFile(File srcFile, File encFile) throws Exception {
        if(!srcFile.exists()){
            System.out.println("source file not exixt");
            return;
        }

        if(!encFile.exists()){
            System.out.println("encrypt file created");
            encFile.createNewFile();
        }
        InputStream fis  = new FileInputStream(srcFile);
        OutputStream fos = new FileOutputStream(encFile);

        while ((dataOfFile = fis.read()) > -1) {
            fos.write(dataOfFile^numOfEncAndDec);
        }

        fis.close();
        fos.flush();
        fos.close();
    }
}