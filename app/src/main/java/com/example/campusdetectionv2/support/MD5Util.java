package com.example.campusdetectionv2.support;
/**
 *  php中的md5算法与java中得不一样！�?
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;


public class MD5Util {  
	private static MD5Util instance=null;
	private static MessageDigest md5 =null;
	private MD5Util(){};
	static	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static MD5Util getInstance()
	{
		try
		{
			instance=new MD5Util();
			md5=MessageDigest.getInstance("MD5"); 
		}catch(Exception e)
		{
			return null;
		}
		return instance;
	}
	public String getStringHash(String source)
	{
		String hash=null;
		try
		{
			ByteArrayInputStream in=new ByteArrayInputStream(source.getBytes());
			hash=getStreamHash(in);
			in.close();
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		return hash;
	}
	public String getFileHash(String file)
	{
		String hash=null;
		try
		{
			FileInputStream in=new FileInputStream(file);
			hash=getStreamHash(in);
			in.close();
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		return hash;
	}
	public String getStreamHash(InputStream stream)
	{
		String hash=null;
		byte[] buffer = new byte[1024];
		BufferedInputStream in=null;
		try
		{
			in=new BufferedInputStream(stream);
			int numRead = 0;  
			while ((numRead = in.read(buffer)) > 0) 
			{  
				md5.update(buffer, 0, numRead);  
			}  
			in.close(); 
			hash=toHexString(md5.digest());  
		}catch (Exception e) 
		{
			if(in!=null)try{in.close();}catch (Exception ex) {ex.printStackTrace();}
			e.printStackTrace();
		}
		return hash;
	}
	private String toHexString(byte[] b) {  
        StringBuilder sb = new StringBuilder(b.length * 2);  
        for (int i = 0; i < b.length; i++) {  
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);  
            sb.append(hexChar[b[i] & 0x0f]);  
        }  
        return sb.toString();  
    }  
    private char[] hexChar = {'0', '1', '2', '3',  
        '4', '5', '6', '7',  
        '8', '9', 'a', 'b',  
        'c', 'd', 'e', 'f'};
	/** 
	 * 默认的密码字符串组合，用来将字节转换�?16 进制表示的字�?apache校验下载的文件的正确性用的就是默认的这个组合 
	 */  
//	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
//		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  

//	protected static MessageDigest messagedigest = null;  
//	static {  
//		try {  
//			messagedigest = MessageDigest.getInstance("MD5");  
//		} catch (NoSuchAlgorithmException nsaex) {  
//			System.err.println(MD5Util.class.getName()  
//					+ "初始化失败，MessageDigest不支持MD5Util�?);  
//			nsaex.printStackTrace();  
//		}  
//	}  

	/** 
	 * 生成字符串的md5校验�?
	 *  
	 * @param s 
	 * @return 
	 */  
//	public static String getMD5String(String s) {  
//		return getMD5String(s.getBytes());  
//	}  

	/** 
	 * 判断字符串的md5校验码是否与�?��已知的md5码相匹配 
	 *  
	 * @param password 要校验的字符�?
	 * @param md5PwdStr 已知的md5校验�?
	 * @return 
	 */  
//	public static boolean checkPassword(String password, String md5PwdStr) {  
//		String s = getMD5String(password);  
//		return s.equals(md5PwdStr);  
//	}  

	//该函数用于将两个字节数组合并为一
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
		return byte_3;  
	}  

	/** 
	 * 生成文件的md5校验�?
	 *  
	 * @param file 
	 * @return 
	 * @throws IOException 
	 */  
//	public static String getFileMD5String(File file,String randomNum) throws IOException {         
//		InputStream fis;  
//		byte[] ret = new byte[4096];//实验输出�?
//		byte[] random = new byte[1024];//随机数的字节数组
//		byte[] combine = new byte[4096];//随机数和文件合并后的字节数组
//		random = randomNum.getBytes();
//		fis = new FileInputStream(file); 
//		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);  
//		byte[] buffer = new byte[1024];//文件的字节数�? 
//		int numRead = 0;  
//
//		while ((numRead = fis.read(buffer)) > 0) { 
//			//System.out.println(numRead);
//			combine = byteMerger(Arrays.copyOf(buffer, numRead),random);
//			numRead = combine.length;
//			//System.out.println(numRead);
//			messagedigest.update(combine, 0, numRead); //只会将前numread个字节读�?
//			out.write(combine,0,numRead);
//		}
//
//		fis.read(combine);
//		fis.close();  
//
//		ret = out.toByteArray();
//		String s = new String(ret);
//		out.close();
//		//System.out.println("1:"+s);
//		return bufferToHex(messagedigest.digest());  
//	}  

	/** 
	 * 获得指定文件的byte数组 
	 */  
	public static byte[] getBytes(String filePath){  
		byte[] buffer = null;  
		try {  
			File file = new File(filePath);  
			FileInputStream fis = new FileInputStream(file);  
			ByteArrayOutputStream bos = new ByteArrayOutputStream(10000);  
			byte[] b = new byte[10000];  
			int n;  
			while ((n = fis.read(b)) != -1) {  
				bos.write(b, 0, n);  
			}  
			fis.close();  
			bos.close();  
			buffer = bos.toByteArray();  
			//System.out.println(buffer);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		return buffer;  
	}  


	/** 
	 * 根据byte数组，生成文�?
	 */  
	public static void getFile(byte[] bfile, String filePath,String fileName) {  
		BufferedOutputStream bos = null;  
		FileOutputStream fos = null;  
		File file = null;  
		try {  
			File dir = new File(filePath);  
			if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
				dir.mkdirs();  
			}  
			file = new File(filePath+"/"+fileName);  
			fos = new FileOutputStream(file);  
			bos = new BufferedOutputStream(fos);  
			bos.write(bfile);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			if (bos != null) {  
				try {  
					bos.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			}  
			if (fos != null) {  
				try {  
					fos.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			}  
		}  
	}  

	public static int byte2int(byte[] res) {   
		// �?��byte数据左移24位变�?x??000000，再右移8位变�?x00??0000   

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位�?  
				| ((res[2] << 24) >>> 8) | (res[3] << 24);   
		return targets;   
	}

	public static byte[] int2byte(int res) {  
		byte[] targets = new byte[4];  

		targets[0] = (byte) (res & 0xff);// �?���?  
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低�?  
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高�?  
		targets[3] = (byte) (res >>> 24);// �?���?无符号右移�?   
		return targets;   
	}   

	//�?个字节数组合并成�?��
	public static void three2one(String outputpath,String name,String inputpath,byte[] byte_2,byte[] byte_3){
		//return byteMerger(byte_1,byteMerger(byte_2,byte_3));
		byte[] byte_4 = new byte[1024];
		int length = byteMerger(byte_2,byte_3).length;
		byte_4 = byteMerger(byteMerger(byte_2,byte_3),int2byte(length));
		//getFile(byteMerger(getBytes(inputpath),byteMerger(byte_2,byte_3)),outputpath,name);
		getFile(byteMerger(getBytes(inputpath),byte_4),outputpath,name);

	}

	//输入�?��大字节数组，将图片按路径复原，再返回两个string
	public static String[] one2three(String path,String name,String inputpath){
		byte[] picture = null;
		byte[] combine = null;
		combine = getBytes(inputpath);
		int len = byte2int(Arrays.copyOfRange(combine, (combine.length)-4,combine.length));
		String[] output = new String[3];
		picture = Arrays.copyOf(combine, (combine.length-len-4));
		getFile(picture,path,name);
		String s = new String(Arrays.copyOfRange(combine, (combine.length)-len-4,(combine.length-4)-len+32));
		String k = new String(Arrays.copyOfRange(combine, (combine.length-4)-len+32,combine.length-4));
		output[0] = s;
		output[1] = k;
		return output;
	}
	/**
	 * 图片加密算法
	 * @param picPath
	 * @param is 公钥文件的文件流（其实没必要从文件读，公钥以字符串形式存储即可）
	 * @return
	 */
	public static String encrypt(String picPath, InputStream is){
		//获得assets文件夹内的文件路�?
		String outputPath = picPath.substring(0,picPath.lastIndexOf("/"));//利用传入的图片路径截取输出路�?
		RandomNum randomnum = new RandomNum();
		String number = randomnum.operate();// 随机�?
		byte fileByte[] = getBytes(picPath);
		byte numberByte[] = number.getBytes();
		byte[] finalByte = new byte[fileByte.length+numberByte.length];
		System.arraycopy(fileByte,0,finalByte,0,fileByte.length);
		System.arraycopy(numberByte,0,finalByte,fileByte.length,numberByte.length);
		getFile(finalByte, ".", outputPath + "/" + "file.JPEG");
		System.out.println("picpath " + picPath);
		String md5="";
		md5 = getInstance().getFileHash(outputPath + "/" + "file.JPEG");
		File file = new File(outputPath + "/" + "file.JPEG");
		file.delete();
		System.out.println("from md5: + " + md5);
		RSAUtil RSAencrypt = new RSAUtil();
		String miwen = RSAencrypt.encrypt(number,is);

		
		String picEncryptedName = formatter.format(System.currentTimeMillis())
				+ ".JPEG";//加密后的图片输出文件�?
	
		byte bytes[]=new byte[512];
		

		//加密后图片的输出路径，加密后图片的输出文件名，原图片的地�?��MD5，密�?
		three2one(outputPath,picEncryptedName,picPath,md5.getBytes(),miwen.getBytes());
		return outputPath + "/" + picEncryptedName;//,加密后的文件绝对路径,更新drr中的�?
	}
//	public static void main(String[] args) throws IOException {  
//
//		RandomNum randomnum = new RandomNum();
//
//		String[] output = new String[3];
//
//		File file = new File("newfile.JPEG"); 
//		String number = randomnum.operate();
//		System.out.println("生成的随机数是："+ number);
//		RSAUtil RSAencrypt = new RSAUtil();
//		String miwen = RSAencrypt.encrypt("publickey.txt",number);
//		System.out.println("随机数进行RSA加密后的密文 �? +miwen);//其中的路径是公钥存的地方
//		System.out.println("对随机数进行RSA解密结果如下�?);
//		System.out.println(RSAencrypt.decrypt("privatekey.txt",miwen));//其中路径为秘钥存的地�?
//		String md5 = getFileMD5String(file,number);  
//		System.out.println("md5:" + md5); 
//
//
//		//参数：输出路径，输出名称，输入的图片路径，输入的MD5的字节数组，输入的密文的字节数组
//		//输出合成图片存的地址
//		three2one(".","10.jpg","1.jpg",md5.getBytes(),miwen.getBytes());
//
//		//参数：剥离出的原图转存地�?��剥离出来的原图片转存名称，输入的合成图片地址
//		output = one2three(".","2.jpg","10.jpg");//分开
//		System.out.println("在服务器端拆�?��结果�?);
//		System.out.println("MD5: " + output[0] + "  RSA:" + output[1]);
//
//	}  
}  

