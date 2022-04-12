package com.example.demo.mapper;

        import com.example.demo.bean.UserBean;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<UserBean, String> {

    UserBean findBySchoolnumberAndPassword(String schoolnumber, String password);
    UserBean findBySchoolnumber(String schoolnumber);
    UserBean findByIdentitynumber(String identitynumber);
    UserBean findByUsertype(String usertype);
    UserBean findFirstByUsertypeAndName(String usertype, String name);

    
}