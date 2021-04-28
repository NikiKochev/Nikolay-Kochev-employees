import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Demo {
    public static final String PATH_NAME = "test.csv";
    public static final int EMP_ID = 0;
    public static final int PROJECT_ID = 1;
    public static final int START_DATE = 2;
    public static final int END_DATE = 3;

    public static void main(String[] args) {
        try {
        ArrayList<Entry> entries = readFile();
        HashSet<Employee> employees = createEmployees(entries);
        findEmployeesWithMostWorkingDaysTogether(employees);

        } catch (FileNotFoundException | BadFileException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void findEmployeesWithMostWorkingDaysTogether(HashSet<Employee> employees) {
        int mostWorkingDaysTogether = 0;
        String employeeOneId = null;
        String employeeTwoId = null;
        for(Employee current :employees){
            for(Employee next : employees){
                if(!current.equals(next)) {
                    int currentWorkingDays = current.workingTimeTogether(next);
                    if(currentWorkingDays > mostWorkingDaysTogether ){
                        mostWorkingDaysTogether = currentWorkingDays;
                        employeeOneId = current.getId();
                        employeeTwoId = next.getId();
                    }
                }
            }
        }
        printResult(employeeOneId, employeeTwoId,mostWorkingDaysTogether);
    }

    private static void printResult(String employeeOneId, String employeeTwoId, int mostWorkingDaysTogether) {
        if(mostWorkingDaysTogether > 0){
            System.out.println(employeeOneId + " | " + employeeTwoId + " | " + mostWorkingDaysTogether);
        }
        else {
            System.out.println("No two people who have worked on a project");
        }
    }

    private static ArrayList<Entry> readFile() throws FileNotFoundException, BadFileException {
        ArrayList<Entry> entries = new ArrayList<>();
        File f  =new File(PATH_NAME);
        Scanner sc =new Scanner(f);
        while (sc.hasNextLine()){
            String row = sc.nextLine();
            String[] entry = row.split(",");
            incorrectData(entry, row);
            Entry e= new Entry(entry[EMP_ID],entry[PROJECT_ID],entry[START_DATE],entry[END_DATE]);
            entries.add(e);
        }
        return entries;
    }

    private static void incorrectData (String [] data, String row) throws BadFileException {
        if(data.length < 4){
            throw new BadFileException("insufficient data on this line - "+ row);
        }
        for (int i = 0; i < data.length; i++) {
            if(data[i].trim().isEmpty() ){
                throw new BadFileException("Incorrect data on this line - " + row);
            }
        }
    }

    private static HashSet<Employee> createEmployees(ArrayList<Entry> entries){
        HashSet<Employee> employees = new HashSet<>();
        HashMap<String, Employee> emp = new HashMap<>();
        for (Entry e : entries){
            if(!emp.containsKey(e.getEmpID())){
                emp.put(e.getEmpID(),new Employee(e.getEmpID()));
            }
            Employee employee = emp.get(e.getEmpID());
            employee.addProjects(e.getProjectID(),e.getStartDate(), e.getEndDate());
            employees.addAll(emp.values());
        }
        return employees;
    }

}
