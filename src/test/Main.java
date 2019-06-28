package test;

public class Main {

    public static void main(String[] args) throws InterruptedException {
//	    ArrayList<Integer> arrayList = new ArrayList<Integer>();
//	    for (int i = 1; i < 7; i++) {
//		    arrayList.add(i);
//	    }
//	    System.out.println(arrayList);
        int n = 37;
        n >>>= 1;
        // 由于"java"字符串已经在虚拟机运行时出现，所以这里不会加入到常量池中，常量池已经存在一个"java"实例
        String str2 = new StringBuilder("ja").append("va").toString();
        // str2在堆上分配，而str2.intern()是常量池中的字符串，所以false
//        System.out.println(str2.intern() == str2);
        // "计算机"，"软件"都是虚拟机运行之前未出现的字符会存储到常量池中
		String str1 = new StringBuilder("计算机").append("软件").toString();
		// true
//        System.out.println(str1.intern() == str1);
//        String str3 = new StringBuilder("ja").append(t"va").toString();
//        System.out.println(str3.intern() == str3);
//        String str4 = new StringBuilder("j3").append("va1234").toString();
//        System.out.println(str4.intern() == str4);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("主线程结束");
    }
}
