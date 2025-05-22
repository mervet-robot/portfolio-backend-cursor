package yool.ma.responsableservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.responsableservice.ennum.Role;
import yool.ma.responsableservice.model.Profile;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserUsername(String username);
    List<Profile> findByUserRole(Role role);
}
