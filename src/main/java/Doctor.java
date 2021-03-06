import java.util.List;
import org.sql2o.*;

public class Doctor {
  private String name;
  private String specialty;
  private int id;

  public Doctor(String name, String specialty) {
    this.name = name;
    this.specialty = specialty;
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO Doctors(name, specialty) VALUES (:name, :specialty)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("specialty", this.specialty)
      .executeUpdate()
      .getKey();
    }
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return this.id;
  }

  public String getSpecialty() {
    return specialty;
  }

  public static List<Doctor> all() {
    String sql = "SELECT id, name FROM doctors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  public static List<Doctor> getDoctorsBySpecialty(String specialty) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors WHERE specialty=:specialty";
      return con.createQuery(sql)
        .addParameter("specialty", specialty)
        .executeAndFetch(Doctor.class);
    }
  }

  // public int getPatientCount() {
  //   try (Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT COUNT (name) AS count FROM patients WHERE doctor_id=:id";
  //     return con.createQuery(sql)
  //       .addParameter("id", this.id)
  //       .executeAndFetch();
  //   }
  // }

  public static List<Doctor> returnDoctorsAlphabetically() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors ORDER BY name";
      return con.createQuery(sql)
        .executeAndFetch(Doctor.class);
    }
  }

  public List<Patient> getPatients() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM patients WHERE doctor_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Patient.class);
    }
  }

  public static Doctor find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors WHERE id=:id";
      Doctor doctor = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Doctor.class);
      return doctor;
    }
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
             this.getId() == newDoctor.getId();
    }
  }



}
