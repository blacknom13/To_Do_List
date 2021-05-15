import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DummyTasksGenerator {

    static final String[] AVAILABLE_STATUS = new String[] { "new", "in_progress", "done" };
    static ArrayList<Entery> dummyTasks;
    static SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    static public void generateTasks(int num) {
        dummyTasks=new ArrayList<>();
        int id=1;
        String cap;
        String des;
        int prior;
        Date deadLine;
        String status;
        Date complete;
        Entery e;
        Random r=new Random(System.currentTimeMillis());

        for (int i=0;i<num;i++) {
            e=new Entery();
            cap="";
            des="";
            int x=i+1;
            while (x>0) {
                cap+=String.valueOf(Character.toChars('a'+(x-1)%26));
                //System.out.println('a'+(x-1)%26);
                if (x/26<2)
                    x/=27;
                else
                    x=(x-1)/26;
            }
            des=cap;
            prior=r.nextInt(101);
            status=AVAILABLE_STATUS[r.nextInt(3)];
            //deadLine=r.nextLong();
            long dLine=r.nextInt(500000000)+500000000;
            dLine=System.currentTimeMillis()-dLine;
            deadLine=new Date(dLine);
            if (status.equals("done")) {
                dLine=r.nextInt(100000000)+100000000;
                dLine=System.currentTimeMillis()-dLine;
                complete=new Date(dLine);
            }
            else
                complete=null;
            e.setId(id);
            e.setCaption(cap);
            e.setDescription(des);
            e.setPriority(prior);
            e.setDeadLine(deadLine);
            e.setStatus(status);
            e.setComplete(complete);
            dummyTasks.add(e);
            id++;

        }
    }


    public static void main (String [] args) {
        generateTasks(800);
//		for (int i=0;i<dummyTasks.size();i++) {
//			System.out.println(dummyTasks.get(i).toString());
//		}
        ToDoList.saveChanges(dummyTasks,"dummyList");
    }

}
