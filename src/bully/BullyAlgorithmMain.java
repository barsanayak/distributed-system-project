package bully;

import java.util.Scanner;

public class BullyAlgorithmMain {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("***Bully Algorithm Demo initiated***");
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the number of processes that would participate in leader election:");
		int processes = in.nextInt();
		System.out.print(" The processes participating: ");
		StringBuffer strbuf = new StringBuffer();
		
		for(int i=1;i<=processes ;i++) {
			strbuf.append("P"+i + " ");
		}
		System.out.println(strbuf);
		System.out.println("The co-ordinator:(process with highest process id): P" + processes);
		
		MyThread[] t = new MyThread[processes];

		for (int i = 0; i < processes; i++)
			t[i] = new MyThread(new Process(i+1), processes);
		
		Election.initialElection(t);

		for (int i = 0; i < processes; i++)
			new Thread(t[i]).start();
	}
}
