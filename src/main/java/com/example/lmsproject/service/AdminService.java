package com.example.lmsproject.service;

import com.example.lmsproject.auth.PWEncoder;
import com.example.lmsproject.entity.Admin;
import com.example.lmsproject.entity.Department;
import com.example.lmsproject.entity.Lecturer;
import com.example.lmsproject.repository.AdminRepo;
import com.example.lmsproject.repository.DepartmentRepository;
import com.example.lmsproject.repository.LecturerRepo;
import com.example.lmsproject.utils.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

@Service
public class AdminService implements UserService<Admin> {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private LecturerRepo lecturerRepo;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Admin create(Admin admin){

        String lastUserID = adminRepo.getLastUserID();
        if(lastUserID != null){
            admin.setUserId(Utils.nextId(lastUserID));
        } else {
            admin.setUserId("USER-0000001");
        }

        admin.setRole("ADMIN");
        admin.setAdminStatus(true);
        admin.setPassword(PWEncoder.encode(admin.getPassword()));

        return adminRepo.save(admin);
    }

    public boolean delete(String userName){

        Admin admin = adminRepo.findByUsername(userName);

        if(admin == null){
            return false;
        }

        adminRepo.delete(admin);
        return true;

    }

    public Admin changePassword(String userName, String password){
        Admin admin = adminRepo.findByUsername(userName);
        admin.setPassword(PWEncoder.encode(password));
        return adminRepo.save(admin);
    }

    public Admin update(Admin admin, String userName){

        Admin adminToUpdate = adminRepo.findByUsername(userName);

        if(adminToUpdate != null){
            adminToUpdate.setEmail(admin.getEmail());
            adminToUpdate.setPhone(admin.getPhone());
            adminToUpdate.setFirstName(admin.getFirstName());
            adminToUpdate.setLastName(admin.getLastName());
            adminToUpdate.setDateOfBirth(admin.getDateOfBirth());
            return adminRepo.save(adminToUpdate);
        }

        return adminRepo.save(admin);
    }

    public Map<String, String> get(String userName){
        Admin admin = adminRepo.findByUsername(userName);

        Map<String , String> adminMap = new HashMap<>();
        adminMap.put("userName", admin.getUsername());
        adminMap.put("email", admin.getEmail());
        adminMap.put("phone", admin.getPhone());

        return adminMap;
    }

    public boolean attachLecturerToDepartment(String lecturerId, String departmentID){

        Lecturer lecturer = lecturerRepo.findByLecturerID(lecturerId);
        Department department = departmentRepository.getReferenceById(100L); // Replace with String departmnt ID

        lecturer.setDepartment(department);

        return true;

    }

    public JSONArray getAllAdmins() {

        List<Admin> allAdmins = adminRepo.findByRole("ADMIN");

        JSONArray jsonArray = new JSONArray();

        for(Admin admin : allAdmins){

            JSONObject adminJson = new JSONObject();

            adminJson.put("userId", admin.getUserId());
            adminJson.put("username", admin.getUsername());
            adminJson.put("firstName", admin.getFirstName());
            adminJson.put("lastName", admin.getLastName());
            adminJson.put("email", admin.getEmail());
            adminJson.put("phone", admin.getPhone());
            adminJson.put("role", admin.getRole());
            adminJson.put("adminStatus", admin.isAdminStatus());

            jsonArray.put(adminJson);
        }

        return jsonArray;

    }

    public JSONArray getAllDepartments() {

        List<Department> allDepartments = departmentRepository.findAll();

        JSONArray jsonArray = new JSONArray();

        for(Department department : allDepartments){

            JSONObject departmentJson = new JSONObject();

            departmentJson.put("id", department.getId());
            departmentJson.put("name", department.getName());
            departmentJson.put("description", department.getDescription());

            jsonArray.put(departmentJson);
        }

        return jsonArray;

    }


    public JSONArray getAllCourses() {


        return new JSONArray();
    }

    public JSONArray getAllStudents() {


        return new JSONArray();
    }

}
