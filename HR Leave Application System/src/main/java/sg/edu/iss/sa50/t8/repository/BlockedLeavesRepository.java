package sg.edu.iss.sa50.t8.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.sa50.t8.model.BlockedLeaves;

public interface BlockedLeavesRepository extends JpaRepository<BlockedLeaves, Integer> {

	void deleteAll();
	
	@Query(value="SELECT date FROM blocked_leaves",nativeQuery = true)
	List<Date> findAllDates();
	
}
