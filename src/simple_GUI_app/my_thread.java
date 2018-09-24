package simple_GUI_app;

public class my_thread extends Thread {
    private int a = 0;
    private int b = 1;
    private int sum = 0;
    private int count;
    String[] str;

    public my_thread(int var1)
    {
        this.count = var1;
    }

    public void run() {
        for(int var1 = 1; var1 <= this.count; ++var1) {
            this.sum = this.a + this.b;
            this.a = this.b;
            this.b = this.sum;
            System.out.println("\n" + this.sum);
        }
    }
}