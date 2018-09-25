package Lab1Part3;

public class test {
    public static void main(String[] args)
    {
        String cmd = "pwd && ls -ll && grep";
        System.out.println("before: " +cmd);

        //String splitCmd;
        cmd=cmd.replaceAll("&&",";");

        System.out.println("after: " + cmd);

        String[] splitCmd=cmd.split(";");
        System.out.println("after splitting: ");
        for(int i=0;i<splitCmd.length;++i)
        {
            splitCmd[i]=splitCmd[i].trim();
            System.out.println(splitCmd[i]);
        }

        String lnxcmd = cmd.replaceAll(" && ",";");
        String[] cmds = lnxcmd.split(";");
        String finalCmd = "";
        for(int i = 0; i < cmds.length; i++)
            finalCmd = finalCmd + cmds[i].trim() + ";";

        System.out.println("lnxcmd: " + finalCmd);


        String[] buff=null;
        for(int i=1;i<=5;++i)
            buff[i]="abc";

        for(int i=1;i<=5;++i)
            System.out.println(buff[i] + "\n");
    }
}