package study;
import java.util.Scanner;

public class Exp1 {
    public static void main(String[] args) {
        char[] plainText = new char[128];
        plainText = inputPlaintext();
        System.out.println("输入明文进行二进制转换结果：");
        System.out.println(plainText);


        char[] key = new char[8];
        key = inputKey();
        System.out.println("输入密钥进行二进制转换结果：");
        System.out.println(key);

        //ip置换表
        int[] ipTable = {58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7
        };
        char[] ip_plainText = new char[64];
        char[] l0 = new char[32];
        char[] r0 = new char[32];
        ip_plainText = initialPermutation(ipTable, plainText, l0, r0);  //IP置换，获得l0，r0
        System.out.println("明文进行IP转换结果：");
        System.out.println(ip_plainText);
        System.out.printf("获取L0：");
        System.out.println(l0);
        System.out.printf("获取R0：");
        System.out.println(r0);
        //pc1置换表
        int[] pcTable1 = {57, 49, 41, 33, 25, 17, 9,
                1, 58, 50, 42, 34, 26, 18,
                10, 2, 59, 51, 43, 35, 27,
                19, 11, 3, 60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15,
                7, 62, 54, 46, 38, 30, 22,
                14, 6, 61, 53, 45, 37, 29,
                21, 13, 5, 28, 20, 12, 4};

        char[] pc_Key = new char[56];
        char[] c0 = new char[28];
        char[] d0 = new char[28];
        pc_Key = initialPermutation1(pcTable1, key, c0, d0);
        System.out.println("密钥进行PC1转换结果：");
        System.out.println(pc_Key);
        System.out.printf("获取C0：");
        System.out.println(c0);
        System.out.printf("获取D0：");
        System.out.println(d0);

        //pc2置换表
        int[] pcTable2 = {14, 17, 11, 24, 1, 5,
                3, 28, 15, 6, 21, 10,
                23, 19, 12, 4, 26, 8,
                16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55,
                30, 40, 51, 45, 33, 48,
                44, 49, 39, 56, 34, 53,
                46, 42, 50, 36, 29, 32};
        char[][] k16 = new char[17][48];  //0不用
        moveCD(k16, c0, d0, pcTable2);

        char[] cipertext = new char[64];
        cipertext = functionRound(l0, r0, k16);
        System.out.print("加密密文:");
        transToascii(cipertext);

        char[] cipertext_left = new char[32];
        char[] cipertext_right = new char[32];
        //对密文进行ip置换，获得l0，r0
        initialPermutation(ipTable, cipertext, cipertext_left, cipertext_right);

        char[][] reverse_k16 = new char[17][48];
        for (int i = 1; i < 17; i++) {
            reverse_k16[i] = k16[17 - i];
        }
        char[] decipertext = new char[64];
        decipertext = functionRound(cipertext_left, cipertext_right, reverse_k16);
        System.out.print("解密密文:");
        transToascii(decipertext);

    }
    //输入明文处理
    public static char[] inputPlaintext() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要加密的明文");
        //输入要加密的明文
        String pT = sc.nextLine();
        while (true) {
            while (true) {
                if (pT.length() != 8) {
                    //判断输入长度是否正确
                    System.out.println("输入明文的长度不是八位,请重新输入");
                    pT = sc.nextLine();
                } else
                    break;
            }

            int flag = 0;
            for (int i = 0; i < 8; i++) {
                char c = pT.charAt(i);
                //判断是否能转成ascii码
                if (c > 128 || c < 0) {
                    System.out.println("输入内容不合法，请重新输入");
                    pT = sc.nextLine();
                    break;
                }
                flag++;
            }
            if (flag == 8) {
                System.out.println("输入明文合法");
                break;
            }
        }

        char[] plainText = new char[8];
        char[] hex_PT = new char[64];
        for (int i = 0; i < 8; i++) {
            plainText[i] = pT.charAt(i);
            //将每个字符转换为2进制
            String hexPlainText = Integer.toBinaryString(plainText[i]);
            for (int j = 8 * i + 7; j >= 8 * i; ) {
                for (int k = hexPlainText.length() - 1; k >= 0; k--) {
                    //将转换后的2进制赋值给字符数组
                    hex_PT[j] = hexPlainText.charAt(k);
                    j--;
                }
                //当2进制不足八位时，用0补齐位数
                while (j >= 8 * i) {
                    hex_PT[j] = '0';
                    j--;
                }
            }
        }
        return hex_PT;
    }
    //输入密钥处理
    public static char[] inputKey() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入密钥");
        //输入密钥
        String k = sc.nextLine();
        while (true) {
            while (true) {
                if (k.length() != 8) {
                    //判断输入长度是否正确
                    System.out.println("输入密钥的长度不是八位,请重新输入");
                    k = sc.nextLine();
                } else
                    break;
            }

            int flag = 0;
            for (int i = 0; i < 8; i++) {
                char c = k.charAt(i);
                //判断是否能转成ascii码
                if (c > 128 || c < 0) {
                    System.out.println("输入内容不合法，请重新输入");
                    k = sc.nextLine();
                    break;
                }
                flag++;
            }
            if (flag == 8) {
                System.out.println("输入密钥合法");
                break;
            }
        }

        char[] key = new char[8];
        char[] hex_K = new char[64];
        for (int i = 0; i < 8; i++) {
            key[i] = k.charAt(i);
            //将每个字符转换为2进制
            String hexKey = Integer.toBinaryString(key[i]);
            for (int j = 8 * i + 7; j >= 8 * i; ) {
                for (int n = hexKey.length() - 1; n >= 0; n--) {
                    //将转换后的2进制赋值给字符数组
                    hex_K[j] = hexKey.charAt(n);
                    j--;
                }
                //当2进制不足八位时，用0补齐位数
                while (j >= 8 * i) {
                    hex_K[j] = '0';
                    j--;
                }
            }
        }
        return hex_K;
    }

    //ip置换函数
    public static char[] initialPermutation(int[] ip_Table, char[] plainText, char[] l0, char[] r0) {
        char[] p_Ip = new char[64];
        for (int i = 0; i < 64; i++) {
            //将ip表中位置作为坐标进行置换
            p_Ip[i] = plainText[ip_Table[i] - 1];
        }
        for (int i = 0; i < 32; i++) {
            l0[i] = p_Ip[i];
        }
        for (int i = 32; i < 64; i++) {
            r0[i - 32] = p_Ip[i];
        }
        return p_Ip;
    }

    //PC1置换函数
    public static char[] initialPermutation1(int[] pc_Table1, char[] key, char[] c0, char[] d0) {
        char[] k_Pc = new char[56];
        for (int i = 0; i < 56; i++) {
            //将ip表中位置作为坐标进行置换
            k_Pc[i] = key[pc_Table1[i] - 1];
        }
        for (int i = 0; i < 28; i++) {
            c0[i] = k_Pc[i];
        }
        for (int i = 28; i < 56; i++) {
            d0[i - 28] = k_Pc[i];
        }
        return k_Pc;
    }

    //移动cd并进行pc置换获得子k
    public static void moveCD(char[][] k16, char[] C0, char[] D0, int[] pc_Table2) {
        char[] temp_k = new char[56];
        int i = 0;
        while (i < 16) {
            i++;
          //当第1、2、9、16轮时，只位移一位
            if (i == 1 || i == 2 || i == 9 || i == 16) {
                char temp_cfirst = C0[0];
                System.arraycopy(C0, 1, C0, 0, 27);
                C0[27] = temp_cfirst;

                char temp_dfirst = D0[0];
                System.arraycopy(D0, 1, D0, 0, 27);
                D0[27] = temp_dfirst;

                //将移位后的c，d复制到暂时的k中
                System.arraycopy(C0, 0, temp_k, 0, 28);
                System.arraycopy(D0, 0, temp_k, 28, 28);
                for (int n = 0; n < 48; n++) {
                    k16[i][n] = temp_k[pc_Table2[n] - 1];
                }
            } else {
                char temp_cfirst = C0[0];
                char temp_csecond = C0[1];
                System.arraycopy(C0, 2, C0, 0, 26);
                C0[26] = temp_cfirst;
                C0[27] = temp_csecond;

                char temp_dfirst = D0[0];
                char temp_dsecond = D0[1];
                System.arraycopy(D0, 2, D0, 0, 26);
                D0[26] = temp_dfirst;
                D0[27] = temp_dsecond;

                //将移位后的c，d复制到暂时的k中
                System.arraycopy(C0, 0, temp_k, 0, 28);
                System.arraycopy(D0, 0, temp_k, 28, 28);

                for (int n = 0; n < 48; n++) {
                    k16[i][n] = temp_k[pc_Table2[n] - 1];
                }
            }
        }
    }
    //进行十六轮计算
    public static char[] functionRound(char[] l0, char[] r0, char[][] k16) {
        //位选择函数E
        int[] e = {32, 1, 2, 3, 4, 5,
                4, 5, 6, 7, 8, 9,
                8, 9, 10, 11, 12, 13,
                12, 13, 14, 15, 16, 17,
                16, 17, 18, 19, 20, 21,
                20, 21, 22, 23, 24, 25,
                24, 25, 26, 27, 28, 29,
                28, 29, 30, 31, 32, 1};
        //S盒
        int[][] s1 = {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};
        int[][] s2 = {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};
        int[][] s3 = {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};
        int[][] s4 = {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};
        int[][] s5 = {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};
        int[][] s6 = {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};
        int[][] s7 = {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};
        int[][] s8 = {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

        int i = 0; //循环16次
        char[] feorresult = new char[48];
        while (i <= 15) {
            char[] temp_r = new char[48];
            i++;
            for (int n = 0; n < 48; n++) {
                temp_r[n] = r0[e[n] - 1];
            }
          //r部分和k进行异或操作
            for (int n = 0; n < 48; n++) {
                if (temp_r[n] == k16[i][n])
                    feorresult[n] = '0';
                else
                    feorresult[n] = '1';
            }
            int[] sresult = new int[8];  //存储经过s盒获得的十进制结果
            int[] hang = new int[8];   //存储八个盒子的行数
            int[] lie = new int[8];     //存储八个盒子的列数
            for (int n = 0; n < 48; n = n + 6) {
                int one_hang = 0;
                int one_lie = 0;
                if (feorresult[n] == '1')
                    one_hang = one_hang + 2;
                if (feorresult[n + 5] == '1')
                    one_hang = one_hang + 1;

                if (feorresult[n + 1] == '1')
                    one_lie = one_lie + 8;
                if (feorresult[n + 2] == '1')
                    one_lie = one_lie + 4;
                if (feorresult[n + 3] == '1')
                    one_lie = one_lie + 2;
                if (feorresult[n + 4] == '1')
                    one_lie = one_lie + 1;
                hang[n / 6] = one_hang;
                lie[n / 6] = one_lie;
            }

            sresult[0] = s1[hang[0]][lie[0]];
            sresult[1] = s2[hang[1]][lie[1]];
            sresult[2] = s3[hang[2]][lie[2]];
            sresult[3] = s4[hang[3]][lie[3]];
            sresult[4] = s5[hang[4]][lie[4]];
            sresult[5] = s6[hang[5]][lie[5]];
            sresult[6] = s7[hang[6]][lie[6]];
            sresult[7] = s8[hang[7]][lie[7]];

            char[] sresult_tobinary = new char[32];
            for (int n = 0; n < 8; n++) {
                String binary_result = Integer.toBinaryString(sresult[n]);
                for (int j = n * 4 + 3; j >= n * 4; ) {
                    for (int f = binary_result.length() - 1; f >= 0; f--) {
                        sresult_tobinary[j] = binary_result.charAt(f);
                        j--;
                    }
                    while (j >= 4 * n) {
                        sresult_tobinary[j] = '0';
                        j--;
                    }
                }
            }

            int[] p_Table = {16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25};
            char[] p_Result = new char[32];
            char[] round_Result = new char[32];
            for (int n = 0; n < 32; n++) {
                //进行p置换
                p_Result[n] = sresult_tobinary[p_Table[n] - 1];
                if (p_Result[n] == l0[n])
                    round_Result[n] = '0';
                else
                    round_Result[n] = '1';
            }
            //交换结果进行下一轮计算
            l0 = r0;
            r0 = round_Result;

        }

        char[] mergeRL = new char[64];
        System.arraycopy(r0, 0, mergeRL, 0, 32);
        System.arraycopy(l0, 0, mergeRL, 32, 32);

        int[] ip_1Table = {40, 8, 48, 16, 56, 24, 64, 32,
                39, 7, 47, 15, 55, 23, 63, 31,
                38, 6, 46, 14, 54, 22, 62, 30,
                37, 5, 45, 13, 53, 21, 61, 29,
                36, 4, 44, 12, 52, 20, 60, 28,
                35, 3, 43, 11, 51, 19, 59, 27,
                34, 2, 42, 10, 50, 18, 58, 26,
                33, 1, 41, 9, 49, 17, 57, 25};
        char[] cipertext = new char[64];
        for (int n = 0; n < 64; n++) {
            cipertext[n] = mergeRL[ip_1Table[n] - 1];
        }
        return cipertext;
    }
    //将结果转换为ascii码
    public static void transToascii(char[] text) {
        char[][] plainText = new char[8][8];
        char[] finalText = new char[8];
        int[] temp_Integer = new int[8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(text, i * 8, plainText[i], 0, 8);
            if (plainText[i][0] == '1')
                temp_Integer[i] = temp_Integer[i] + 128;
            if (plainText[i][1] == '1')
                temp_Integer[i] = temp_Integer[i] + 64;
            if (plainText[i][2] == '1')
                temp_Integer[i] = temp_Integer[i] + 32;
            if (plainText[i][3] == '1')
                temp_Integer[i] = temp_Integer[i] + 16;
            if (plainText[i][4] == '1')
                temp_Integer[i] = temp_Integer[i] + 8;
            if (plainText[i][5] == '1')
                temp_Integer[i] = temp_Integer[i] + 4;
            if (plainText[i][6] == '1')
                temp_Integer[i] = temp_Integer[i] + 2;
            if (plainText[i][7] == '1')
                temp_Integer[i] = temp_Integer[i] + 1;
        }
        for (int i = 0; i < 8; i++) {
            finalText[i] = (char) temp_Integer[i];
        }
        System.out.println(finalText);
    }
}
