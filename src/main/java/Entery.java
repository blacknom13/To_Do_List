import java.text.SimpleDateFormat;
import java.util.Date;

public class Entery {
    private int id;
    private String caption;
    private String description;
    private int priority;
    private Date deadLine;
    private String status;
    private Date complete;
    private static final String[] FIELD_NAMES = new String[] { "id", "caption", "Description", "Priority", "DeadLine", "Status",
            "Completed" };

    static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");


    Entery(){
    }

    Entery(int id,String caption,String description, int priority, Date deadL, String status)
    {
        this.id=id;
        this.caption=caption;
        this.description=description;
        this.priority=priority;
        this.deadLine=deadL;
        this.status=status;
        this.complete=null;
    }

    Entery(int id,String caption,String description, int priority, Date deadL, String status, Date complete)
    {
        this.id=id;
        this.caption=caption;
        this.description=description;
        this.priority=priority;
        this.deadLine=deadL;
        this.status=status;
        this.complete=complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getComplete() {
        return complete;
    }

    public void setComplete(Date complete) {
        this.complete = complete;
    }


    public static String[] getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String x="";
        x+="Task "+FIELD_NAMES[0]+": "+this.getId()
                +"\t"+FIELD_NAMES[1]+": "+this.getCaption()+"\n"
                +"\t"+FIELD_NAMES[2]+": "+this.getDescription()+"\n"
                +"\t"+FIELD_NAMES[3]+": "+this.getPriority()+"\n"
                +"\t"+FIELD_NAMES[4]+": "+format.format(this.getDeadLine())+"\n"
                +"\t"+FIELD_NAMES[5]+": "+this.getStatus()+"\n";
        if (this.getComplete()==null) {
            x+="\t"+FIELD_NAMES[6]+": null\n";
        }
        else {
            x+="\t"+FIELD_NAMES[6]+": "+format.format(this.getComplete())+"\n";
        }
        return x;
    }
}
