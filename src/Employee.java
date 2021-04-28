import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class Employee {

    public class WorkingDiapason {

        private LocalDate startDate;
        private LocalDate endDate;

        public WorkingDiapason (LocalDate startDate, LocalDate endDate){
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    private String id;
    private TreeMap<String, ArrayList<WorkingDiapason>> projects;

    public Employee(String id){
        this.id = id;
        projects = new TreeMap<>();
    }

    public void addProjects(String projectId, LocalDate startDate, LocalDate endDate){
        if(!projects.containsKey(projectId)){
            projects.put(projectId, new ArrayList<>());
        }
        projects.get(projectId).add(new WorkingDiapason(startDate,endDate));
    }

    public int workingTimeTogether(Employee otherEmployee) {
        int workingDaysTogether = 0;
        for(String projectId : this.projects.keySet()){
            ArrayList <WorkingDiapason>otherEmployeeDiapason = otherEmployee.workOnProject(projectId);
            if(otherEmployeeDiapason != null){
                for(WorkingDiapason w : this.projects.get(projectId)){
                    workingDaysTogether = this.daysTogether(otherEmployeeDiapason, w);
                }
            }
        }
        return workingDaysTogether;
    }

    private int daysTogether(ArrayList<WorkingDiapason> otherEmployeeDiapason, WorkingDiapason holder) {
        int workingDaysForProject = 0;
        for (WorkingDiapason w : otherEmployeeDiapason){
            if(!w.startDate.isBefore(holder.startDate) && !w.startDate.isAfter(holder.endDate)){
                workingDaysForProject += (int) ChronoUnit.DAYS.between(w.startDate, holder.endDate);
            }
        }
        return workingDaysForProject;
    }

    private ArrayList <WorkingDiapason> workOnProject(String projectId) {
        if(this.projects.containsKey(projectId)) {
            return this.projects.get(projectId);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }
}
