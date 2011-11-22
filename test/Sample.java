import demo.*;

public class Sample {

  public void test(Sample a, Object o, Sample b) {
    return;
  }
  
  public static void main(String[] args) {
    
    // Create superclass
    Super sup = new Super();
    
    // Create subclass
    Sub sub = new Sub(2);
    
    // Call printMessage
    sup.printMessage();
    
    // Call printMessage from superclass
    sub.printMessage();
    
    // Test toString methods
    System.out.println(sup);
    System.out.println(sub);

    // Print out the initial value of superclass's x
    int v = sup.getX();
    System.out.println(v);
    
    // Change the value of superclass's x
    sup.setX(5);
    v = sup.getX();
    
    // Check if v equals 5
    if (v < 5) {
      System.out.println("v < 5");
    } else if (v == 5) {
      System.out.println("v = 5");
    } else {
      System.out.println("v > 5");
    }

    // Change the value of subclass's x to 20
    sub.setXplusTen(10);
    int w = sub.getX();
    System.out.println(w);
    
    // If w is greater than 10, decrement it until w = 10
    while (w > 10)
      w--;
    System.out.print("w = ");
    System.out.println(w);

    // Print out 0-9 on the same line
    for (int i = 0; i < 10; i++)
      System.out.print(i);
    System.out.println();

    System.out.println("test " + sup.toString());

    Sample sam = new Sample();
    sam.test(sam, sam, sam);

    if (sam.equals(sam))
      System.out.println("TRUE");

    int a[] = new int[5];
    a[1] = 1;

    String b[] = new String[3];
    b[0] = "HEY";

  }

}
