package com.example.campusdetectionv2.support;
import java.util.Random;


public class RandomNum {

	public static String operate() { 
		int random[] = new int[16];

		for(int i=0; i<12; i++)
			random[i]= (new Random().nextInt(10))%2;
		//闅忔満鏁扮粍鐨勫悗鍥涗綅鍥哄畾涓?001
		random[12] = 1;
		random[13] = 0;
		random[14] = 0;
		random[15] = 1;


		String str="";
		//杞崲涓簊tring褰㈠紡
		for(int i=0;i<random.length;i++){
			str+=String.valueOf(random[i]);
		}


		return str;
	}

}

