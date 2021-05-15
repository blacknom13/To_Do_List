import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ToDoList {
    static final String[] AVAILABLE_STATUS = new String[] { "new", "in_progress", "done" };

    static final String DEF_CONFIGS = "configs.txt";
    static HashMap<Integer, Entery> tasks = new HashMap<>();
    static ArrayList<Integer> taskIDs = new ArrayList<>();
    static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    static final String STOP_EDITING_PATTERN = "*n*";
    static final String CURRENT_SAVE_LOCATION = "*d*";
    static String defaultFileName = loadDefaultConfigs();

    public static String loadDefaultConfigs() {
        try {
            File f = new File(DEF_CONFIGS);
            Scanner x = new Scanner(f);
            String def = "";
            if (x.hasNextLine()) {
                def = x.nextLine();
            } else {
                System.out.println("Default configs not found!");
            }
            x.close();
            return def;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Default configs not found!");
            return "";
        }
    }

    public static void writeDefaultConfigs(String path) {
        try {
            File f = new File(DEF_CONFIGS);
            boolean exists = true;
            String def = "";
            Scanner s;
            if (f.exists()) {
                s= new Scanner(f);
                if (s.hasNextLine())
                    def = s.nextLine();
                if (!(new File(def)).exists())
                    exists = false;
                s.close();
            }
            else
                exists=false;
            if (!exists) {
                BufferedWriter w = new BufferedWriter(new FileWriter(f));
                w.write(path);
                w.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void loadList(String xml) {
        try {
            File xmlText;
            if (!xml.contains(".xml"))
                xml+=".xml";
            xmlText =new File(xml);
            if (xmlText.exists()) {
                System.out.println("File loaded successfully! Type 'help' to see the available commands.");
            } else {
                if (!defaultFileName.isEmpty()) {
                    xmlText = new File(defaultFileName);
                    System.out
                            .println("File hasn't been found! Default ToDoList will be loaded instead " + defaultFileName);
                }
            }
            Entery temp;
            DocumentBuilderFactory docBldFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = docBldFac.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlText);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Task");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element n = (Element) nodeList.item(i);
                temp = new Entery();
                temp.setId(Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue().trim()));
                temp.setCaption(n.getAttributes().getNamedItem("caption").getNodeValue().trim());
                temp.setDescription(n.getElementsByTagName("Description").item(0).getTextContent().trim());
                temp.setPriority(Integer.parseInt(n.getElementsByTagName("Priority").item(0).getTextContent().trim()));
                temp.setDeadLine(FORMAT.parse(n.getElementsByTagName("DeadLine").item(0).getTextContent().trim()));
                temp.setStatus(n.getElementsByTagName("Status").item(0).getTextContent().trim());
                if (temp.getStatus().equals("done"))
                    temp.setComplete(FORMAT.parse(n.getElementsByTagName("Completed").item(0).getTextContent().trim()));
                else
                    temp.setComplete(null);
                taskIDs.add(temp.getId());
                tasks.put(temp.getId(), temp);
            }

        } catch (ParserConfigurationException | DOMException | ParseException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Default file not found!");
        }
    }

    public static int intValidation(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Date dateValidation(String value) {
        try {
            return FORMAT.parse(value);
        } catch (ParseException e1) {
            return null;
        }
    }

    static public int alterTask(int id, boolean remove, String caption, String description, String priority,
                                String deadL) {
        Entery temp;
        int parsedPrior;
        Date parsedDeadL;
        if (remove) {
            if (!tasks.isEmpty() && tasks.containsKey(id)) {
                tasks.remove(id);
                int idRemove = Collections.binarySearch(taskIDs, id);
                taskIDs.remove(idRemove);
                System.out.println("Task " + id + " has been removed successfully!");
                return id;
            } else {
                System.out.println("There is no task with such id");
                return -1;
            }
        }
        parsedPrior = intValidation(priority);
        if (parsedPrior == -1) {
            System.out.println("Incorrect priority parameter! Has to be integer");
            return -1;
        }
        parsedDeadL = dateValidation(deadL);
        if (parsedDeadL == null) {
            System.out.println("Incorrect date format! Must be " + FORMAT.toPattern());
            return -1;
        }
        if (id == -1) {
            temp = new Entery();
            if (!tasks.isEmpty())
                id = taskIDs.get(taskIDs.size() - 1) + 1;
            else
                id = 1;
            taskIDs.add(id);
            temp.setStatus("new");
        } else {
            if (tasks.isEmpty() || !tasks.containsKey(id)) {
                System.out.println("There is no task with such id");
                return -1;
            } else
                temp = tasks.get(id);
        }
        temp.setId(id);
        temp.setCaption(caption);
        temp.setDescription(description);
        temp.setPriority(parsedPrior);
        temp.setDeadLine(parsedDeadL);
        if (id == -1)
            temp.setComplete(null);
        tasks.put(id, temp);
        System.out.println(tasks.size());
        return id;
    }

    static public boolean paramNumber(String[] commandParams, int correctNumber) {
        if (commandParams.length < correctNumber) {
            System.out.println("Incorrect number of parameters! Few parameters.");
            return false;
        } else if (commandParams.length > correctNumber) {
            System.out.println("Incorrect number of parameters! Too many parameters.");
            return false;
        } else
            return true;
    }

    static public boolean editValidation(String[] commandParams) {
        for (int i = 0; i < commandParams.length; i++) {
            if (commandParams[i].contains(STOP_EDITING_PATTERN)) {
                System.out.println("Editing aborted by user");
                return false;
            }
        }
        return true;
    }

    static public int IDValidation(String id) {
        try {
            int parsedId = intValidation(id);
            if (parsedId < 0) {
                System.out.println("Bad formated ID! Must be a positive integer");
                return -1;
            } else
                return parsedId;
        } catch (NumberFormatException e) {
            System.out.println("Bad formated ID! Must be an integer");
            return -1;
        }
    }

    static public void saveChanges(Collection<Entery> list, String name) {
        String outputFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<ToDoList>\n";
        for (Entery i : list) {
            String task = "\t<Task id=\"" + i.getId() + "\" caption=\"" + i.getCaption() + "\">\n"
                    + "\t\t<Description> " + i.getDescription() + " </Description>\n" + "\t\t<Priority> "
                    + i.getPriority() + " </Priority>\n" + "\t\t<DeadLine> " + FORMAT.format(i.getDeadLine())
                    + " </DeadLine>\n" + "\t\t<Status> " + i.getStatus() + " </Status>\n";
            if (i.getComplete() == null)
                task += "\t\t<Completed> " + i.getComplete() + " </Completed>\n";
            else
                task += "\t\t<Completed> " + FORMAT.format(i.getComplete()) + " </Completed>\n";
            task += "\t</Task>\n";
            outputFile += task;
        }
        outputFile += "</ToDoList>";
        // System.out.println(outputFile);
        name += ".xml";
        File f = new File(name);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(outputFile);
            System.out.println("Changes saved.");
            writeDefaultConfigs(name);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("No such a file!");
        }
    }

    static public void listTasksTable(String filter) {
        String openning = "*********************************************************************************\n"
                + "*********************************************************************************\n" + "|";
        String list = "";
        String[] fieldNames = Entery.getFieldNames();
        for (int i = 0; i < fieldNames.length; i++) {
            list += "| " + fieldNames[i] + " |";
        }
        list += "|\n";
        if (filter.isEmpty())
            for (Entery i : tasks.values()) {
                list += "*********************************************************************************\n" + "|| "
                        + i.getId() + " || " + i.getCaption() + " || " + i.getDescription() + " || " + i.getPriority()
                        + " || " + FORMAT.format(i.getDeadLine()) + " || " + i.getStatus() + " || ";
                if (i.getComplete() == null) {
                    list += " null ||\n";
                } else
                    list += FORMAT.format(i.getComplete()) + " ||\n";
            }
        else {
            for (Entery i : tasks.values()) {
                if (i.getStatus().equals(filter)) {
                    list += "*********************************************************************************\n"
                            + "|| " + i.getId() + " || " + i.getCaption() + " || " + i.getDescription() + " || "
                            + i.getPriority() + " || " + FORMAT.format(i.getDeadLine()) + " || " + i.getStatus()
                            + " || ";
                    if (i.getComplete() == null) {
                        list += " null ||\n";
                    } else
                        list += FORMAT.format(i.getComplete()) + " ||\n";
                }
            }
        }
        if (list.isEmpty())
            System.out.println("No task matches the given filter!");
        else
            System.out.println(openning + list);
    }

    static public void listTasks(String filter) {
        String openning = "*********************************************************************************\n";
        String list = "";
        if (filter.isEmpty())
            for (Entery i : tasks.values()) {
                list += i.toString()
                        + "*********************************************************************************\n";
            }
        else {
            for (Entery i : tasks.values()) {
                if (i.getStatus().equals(filter)) {
                    list += i.toString()
                            + "*********************************************************************************\n";
                }
            }
        }
        if (list.isEmpty())
            System.out.println("No task matches the given filter!");
        else
            System.out.println(openning + list);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String fileName;
        String[] commandParams;
        int taskState;
        int id;
        System.out.println("Welcome to ToDoList program! Please enter the ToDoList file name.");
        fileName = s.nextLine().trim();
        loadList(fileName);
        while (true) {
            commandParams = s.nextLine().trim().toLowerCase().split(" ");
            switch (commandParams[0]) {
                case "":
                    System.out.println("no commands found!");
                    break;
                case "help":
                    System.out.println("Available commands:\n"
                            + "**********************************************************************************\n"
                            + "<list>: lists all the tasks sharing the same attribute.\n\n"
                            + "Enter keyword <list> then the attribute <-s> and the status <task status>;\n"
                            + "which is one of three: new, in_progress, done.\n"
                            + "Enter keyword <list> with no attributes; lists all the tasks in the xml file.\n\n"
                            + "_Example: list -s done; lists the completed tasks.\n"
                            + "**********************************************************************************\n"
                            + "<new>: creates a new task. The status is automatically set to (new).\n\n"
                            + "Enter keyword <new> then, in a new line enter the following:\n "
                            + "<caption>,<description>,<priority>,<dead line>;\n\n"
                            + "_Example: new \\n Buy grocery, Go to the store to buy stuff, 100, 2020-03-13\n"
                            + "**********************************************************************************\n"
                            + "<edit>: edits a task. Requires task's id.\n\n"
                            + "Enter keyword <edit> and the task id <id> then, in a new line enter the following:\n"
                            + "<caption>,<description>,<priority>,<dead line>;\n\n"
                            + "_Example: edit 2 \\n Buy grocery, Go to the store to buy stuff, 100, 2020-03-13\n"
                            + "**********************************************************************************\n"
                            + "<complete>: marks a task as done.\n\n" + "Enter keyword <complete> and the task id <id>.\n\n"
                            + "-Example: complete 4\n"
                            + "**********************************************************************************\n"
                            + "<remove>: removes a task from the list. Requires task\'s id.\n\n"
                            + "_Enter keyword <remove> and the task id <id>;\n\n" + "Example: remove 2\n"
                            + "**********************************************************************************\n"
                            + "<exit>: exits the program.\n\n" + "Enter keyword <exit>;\n\n" + "_Example: exit\n");
                    break;
                case "list":
                    boolean incorrect = true;
                    if (commandParams.length == 1) {
                        listTasks("");
                    } else if (paramNumber(commandParams, 3) && commandParams[1].equals("-s")) {
                        for (int i = 0; i < AVAILABLE_STATUS.length; i++)
                            if (commandParams[2].equals(AVAILABLE_STATUS[i])) {
                                listTasks(AVAILABLE_STATUS[i]);
                                incorrect = false;
                                break;
                            }
                        if (incorrect) {
                            String statuses = "";
                            for (int i = 0; i < AVAILABLE_STATUS.length; i++) {
                                statuses += " " + AVAILABLE_STATUS[i] + ",";
                            }
                            statuses = statuses.substring(0, statuses.length() - 1);
                            System.out.println("Incorrect status parameter! Can be one of the following:" + statuses);
                        }
                    } else if (!commandParams[1].equals("-s")) {
                        System.out.println("Incorrect list command! Try <help> for more details.");
                    }
                    break;
                case "new":
                    if (paramNumber(commandParams, 1)) {
                        System.out.println("Please enter the values in order:\n"
                                + "caption, description, priority, dead line(format " + FORMAT.toPattern() + ")");
                        commandParams = s.nextLine().split(",");
                        if (paramNumber(commandParams, 4)) {
                            taskState = alterTask(-1, false, commandParams[0].trim(), commandParams[1].trim(),
                                    commandParams[2].trim(), commandParams[3].trim());
                            if (taskState != -1)
                                System.out.println("Task successfully added and written under ID: " + taskState);
                            else
                                System.out.println("Task wasn't created! Something went wrong.");
                        }
                    }
                    break;
                case "edit":
                    if (paramNumber(commandParams, 2)) {
                        id = IDValidation(commandParams[1]);
                        if (id >= 0) {
                            if (!tasks.isEmpty() && tasks.containsKey(id)) {
                                System.out.println("Please enter the values in order:\n"
                                        + "caption, description, priority, dead line(format " + FORMAT.toPattern() + ")\n"
                                        + "Enter *n* to cancel.");
                                commandParams = s.nextLine().split(",");
                                if (editValidation(commandParams) && paramNumber(commandParams, 4)) {
                                    taskState = alterTask(id, false, commandParams[0].trim(), commandParams[1].trim(),
                                            commandParams[2].trim(), commandParams[3].trim());
                                    if (taskState != -1)
                                        System.out.println("Task successfully edited and written under ID: " + taskState);
                                    else
                                        System.out.println("Task wasn't edited!");
                                }
                            } else
                                System.out.println("There is no task with such id!");
                        }
                    }
                    break;
                case "remove":
                    if (paramNumber(commandParams, 2)) {
                        id = IDValidation(commandParams[1]);
                        if (id >= 0) {
                            taskState = alterTask(id, false, "", "", "", "");
                            if (taskState != -1)
                                System.out.println("Task successfully removed! ID: " + taskState);
                            else
                                System.out.println("Task wasn't edited!");
                        }
                    }
                    break;
                case "complete":
                    if (paramNumber(commandParams, 2)) {
                        id = IDValidation(commandParams[1]);
                        if (id > 0) {
                            if (tasks.containsKey(id)) {
                                Entery temp = tasks.get(id);
                                if (temp.getStatus().equals("done"))
                                    System.out.println(
                                            "The task is already completed in " + FORMAT.format(temp.getComplete()));
                                else {
                                    temp.setComplete(new Date(System.currentTimeMillis()));
                                    temp.setStatus("done");
                                    System.out.println("Status changed successfully! Task id:" + id + " marked as done.");
                                }
                            } else
                                System.out.println("Incorrect ID! There is no task with such id:" + id);
                        } else
                            System.out.println("Incorrect ID! The ID is negative or not a number.");
                    }
                    break;
                case "exit":
                    if (paramNumber(commandParams, 1)) {
                        String choice = "";
                        while (!choice.equals("y") && !choice.equals("n")) {
                            System.out.println("Save changes? Y/N");
                            choice = s.nextLine().trim().toLowerCase();
                        }
                        if (choice.equals("y")) {
                            System.out.println("Enter a ToDoList file name, or type " + CURRENT_SAVE_LOCATION
                                    + " to save in the current list.");
                            choice = s.nextLine().trim();
                            if (!choice.equals(CURRENT_SAVE_LOCATION))
                                fileName = choice;
                            saveChanges(tasks.values(), fileName);
                            s.close();
                        } else {
                            System.out.println("Exited without saving.");
                        }
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("Incorrect command! Type <help> to see the list of commands.");
                    break;
            }
        }
    }
}