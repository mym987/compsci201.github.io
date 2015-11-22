package src;

import java.io.*;
import java.util.*;

public class LevelOrder {
	public static void main(String[] args) throws IOException{
		LevelOrder l = new LevelOrder();
		Scanner in = new Scanner(new BufferedInputStream(new FileInputStream("test.in")));
		String s = in.useDelimiter("\\Z").next();
		in.close();
		s = l.run(s);
		PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("test.out")));
		out.print(s);
		out.flush();
		out.close();
	}
	Map<String,Integer> map = new TreeMap<>((a,b)->{
		if(a.length()!=b.length())
			return a.length()-b.length();
		else
			return a.compareTo(b);
	});
	boolean flag = false;
	private String run(String in){
		int idx = 0;
		StringBuilder sb = new StringBuilder();
		while(true){
			int start = in.indexOf('(',idx)+1,end = in.indexOf(')',idx);
			idx = end+1;
			if(start==-1||end==-1)
				break;
			String[] t = in.substring(start, end).split(",",-1);
			if(t.length<2){
				sb.append(validate());
				map.clear();
				flag = false;
			}else{
				flag = flag || map.containsKey(t[1]);
				map.put(t[1], Integer.parseInt(t[0]));
			}
		}
		return sb.toString();
	}
	private String validate(){
		if(flag)
			return "not complete\n";
		for(String k:map.keySet()){
			if(!k.equals("") && !map.containsKey(k.substring(0, k.length()-1)))
				return "not complete\n";
		}
		StringBuilder sb = new StringBuilder();
		map.forEach((k,v)->sb.append(" "+v));
		return sb.append("\n").toString();
	}
}
