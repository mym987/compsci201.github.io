package src;

import java.io.*;
import java.util.*;

public class TreeSum {
	public static void main(String[] args) throws IOException{
		TreeSum t = new TreeSum();
		Scanner in = new Scanner(new BufferedInputStream(new FileInputStream("test.in")));
		String s = in.useDelimiter("\\Z").next().replace("(", " ( ").replace(")", " ) ");
		in.close();
		s = t.run(s);
		PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("test.out")));
		out.print(s);
		out.flush();
		out.close();
	}
	private class Node{
		int val;
		boolean isLeaf = true;
		Node(int val){
			this.val = val;
		}
	}
	private String run(String in){
		StringBuilder sb = new StringBuilder();
		Scanner s = new Scanner(in);
		while(s.hasNext()){
			int sum = Integer.parseInt(s.next());
			Set<Integer> set = new HashSet<>();
			Stack<Node> stack = new Stack<>();
			s.next();
			String next = s.next();
			if(next.equals(")")){
				sb.append("no\n");
				continue;
			}
			stack.push(new Node(Integer.parseInt(next)));
			while(!stack.isEmpty()){
				String t = s.next();
				if(t.equals("(")){
					String t1 = s.next();
					if(!t1.equals(")")){
						stack.peek().isLeaf = false;
						stack.push(new Node(stack.peek().val+Integer.parseInt(t1)));
					}
				}else if(t.equals(")")){
					Node n = stack.pop();
					if(n.isLeaf)
						set.add(n.val);
				}
			}
			sb.append(set.contains(sum)?"yes":"no").append('\n');
		}
		s.close();
		return sb.toString();
	}
}
