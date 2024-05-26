package db.models;

public class DepartmentModel {
    public int departmentId;
    public String departmentName;

    public DepartmentModel(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
}
