package com.hai.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class VideoUtils {
	
	
	//ffmpeg.exe的位置
//	public static final String FFMPEG_PATH= "E:\\ffmpeg\\bin\\ffmpeg.exe";
	public static final String FFMPEG_PATH= "C:\\ffmpeg\\bin\\ffmpeg.exe";
	
	/**
	 * @Description video与bgm的合成方法
	 * @param videoSource
	 * @param videoObject
	 * @param veidoSeconds
	 * @throws Exception
	 */
	public static void convertor(String videoSource,String bgmAbsolutePath,String videoObject,double veidoSeconds) throws Exception{
		List<String> command = new ArrayList<String>();
		command.add(FFMPEG_PATH);
		command.add("-i");
		command.add(videoSource);
		command.add("-i");
		command.add(bgmAbsolutePath);
		command.add("-t");
		command.add(""+veidoSeconds);
		command.add("-y");
		System.out.println("#######################");
		System.out.println(videoObject);
		System.out.println("#######################");
		int lastposition=videoObject.lastIndexOf(".");
		System.out.println("截取的位置:"+lastposition);
		String[] sufix = videoObject.split("\\.");
		String str=videoObject.substring(0, lastposition);
		System.out.println("截取到的前缀:"+str);
		System.out.println(lastposition);
		System.out.println(sufix[sufix.length-1]);
		String newName=str+"new."+sufix[sufix.length-1];
		System.out.println("合成文件的新名字:"+newName);
		
		//改变保存在数据库的路径
		videoSource=newName;
		
		
		command.add(newName);
		
		for(String s:command) {
			System.out.print(s);
		}
		
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process=builder.start();
		InputStream input = process.getErrorStream();
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		while((br.readLine())!=null) {}
		if(br!=null) {
			br.close();
		}
		if(reader!=null) {
			reader.close();
		}
		if(input!=null) {
			input.close();
		}
	}
	
	//ffmpeg.exe -ss 00:00:01 -y -i spring.mp4 -vframes 1 new.jpg
	public static void fetchCover(String sourceVideo,String coverPath) {
		List<String> command = new ArrayList<String>();
		command.add(FFMPEG_PATH);
		command.add("-ss");
		command.add("00:00:01");
		command.add("-i");
		command.add(sourceVideo);
		command.add("-y");
		command.add("-vframes");
		command.add("1");
		command.add(coverPath);
		
		System.out.println("#############");
		for(String s:command) {
			System.out.print(s);
		}
		System.out.println("#############");
		
		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			Process process=builder.start();
			InputStream in=process.getErrorStream();
			InputStreamReader reader =new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			while((br.readLine())!=null) {}
			
			if(br!=null) {
				br.close();
			}
			if(reader!=null) {
				reader.close();
			}
			if(in!=null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
