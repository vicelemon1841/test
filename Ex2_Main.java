package pm;

public class Ex2_Main {

	public static void main(String[] args) {
		// 원하는 객체 생성
		Ex2_Test<Integer> t1 = new Ex2_Test<Integer>();
		t1.setValue(10000);
		
		System.out.printf("t1.getValue():%d\n", t1.getValue());
		
		// 원하는 객체를 하나 더 생성
		Ex2_Test<String> t2 = new Ex2_Test<String>();
		t2.setValue("10000");// 허용할 수 없다.
		
		Ex2_Test<Double> t3 = new Ex2_Test<>();
		t3.setValue(100.0);
		
		System.out.printf("t2.getValue():%s\n", t2.getValue());
		System.out.printf("t3.getValue():%f\n", t3.getValue());

	}

}
