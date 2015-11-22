package src;

import java.io.*;
import java.util.*;

public class Parent {
	public static void main(String[] args) throws IOException {
		Parent p = new Parent();
		Scanner in = new Scanner(new BufferedInputStream(new FileInputStream("test.in")));
		String s = p.run(in);
		in.close();
		PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("test.out")));
		out.print(s);
		out.flush();
		out.close();
	}

	Map<String, Integer> map = new HashMap<>();
	int n = 0;
	boolean mat[][] = new boolean[500][500];

	private String run(Scanner in) {
		StringBuilder sb = new StringBuilder();
		while (in.hasNextLine()) {
			// 0:Child 1:Parent
			String[] s = in.nextLine().split(" ");
			if (s[0].contains("no.child"))
				break;
			if (!map.containsKey(s[0]))
				map.put(s[0], n++);
			if (!map.containsKey(s[1]))
				map.put(s[1], n++);
			int a = map.get(s[0]), b = map.get(s[1]);
			mat[a][b] = true;
		}
		while (in.hasNextLine()) {
			String[] s = in.nextLine().split(" ");
			if (s[0].contains("no.child"))
				break;
			if(!map.containsKey(s[0])||!map.containsKey(s[1])){
				sb.append("no relation\n");
			}else
			sb.append(query(map.get(s[0]),map.get(s[1]))).append('\n');
		}
		return sb.toString();
	}

	Integer [] myPath = null;
	Set<Integer> searched = new HashSet<>();
	
	private String query(int p1, int p2) {
		myPath = null;
		searched.clear();
		search(new LinkedList<>(),p1,p2);
		if(myPath == null)
			return "no relation";
		int p = 0; int c = 0;
		for(int i=1;i<myPath.length;i++){
			if(mat[myPath[i-1]][myPath[i]])
				c++;
			else
				p++;		
		}
		if(p>0 && c==0){
			StringBuilder sb = new StringBuilder();
			for(int i=p;i>2;i--)
				sb.append("great ");
			if(p>1)
				sb.append("grand ");
			sb.append("parent");
			return sb.toString();
		}
		else if(c>0 && p==0){
			StringBuilder sb = new StringBuilder();
			for(int i=c;i>2;i--)
				sb.append("great ");
			if(c>1)
				sb.append("grand ");
			sb.append("child");
			return sb.toString();
		} else if(c==1 && p==1)
			return "sibling";
		else if(c==p)
			return (p-1)+" cousin";
		else
			return (Math.min(p, c)-1)+" cousin removed "+Math.abs(p-c);
	}
	
	private void search(List<Integer> path, int source, int target){
		path.add(source);
		searched.add(source);
		if(source==target){
			myPath = path.toArray(new Integer[path.size()]);
			return;
		}
		for(int i=0;i<n;i++){
			if(searched.contains(i))
				continue;
			if(mat[source][i])
				search(path,i,target);
			if(mat[i][source])
				search(path,i,target);
		}
		path.remove(path.size()-1);
	}

}
