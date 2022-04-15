package com.example.demo.mapper;

        import com.example.demo.bean.User;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<User, String> {

    User findBySchoolnumberAndPassword(String schoolnumber, String password);
    User findBySchoolnumber(String schoolnumber);
    User findByIdentitynumber(String identitynumber);
    User findByUsertype(String usertype);
    User findFirstByUsertypeAndName(String usertype, String name);

    
}