package sg.edu.iss.sa50.t8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.BlockedLeaves;
import sg.edu.iss.sa50.t8.repository.BlockedLeavesRepository;

@Service
public class BlockedLeavesService {
	
	@Autowired
	BlockedLeavesRepository blrepo;
	
	public void Clear() {
		blrepo.deleteAll();
	}

	public void save(BlockedLeaves x) {
		// TODO Auto-generated method stub
		blrepo.save(x);
	}
}
