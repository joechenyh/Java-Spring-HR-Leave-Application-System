package sg.edu.iss.sa50.t8.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.BlockedLeaves;

@Service
public interface ParsingService {
	List<BlockedLeaves> parse(String url) throws Exception;
}
