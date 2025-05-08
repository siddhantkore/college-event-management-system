package in.adcet.event_management.service;

import java.util.List;

import in.adcet.event_management.DTO.ResultDTO;
import in.adcet.event_management.dao.ResultDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultService {
	
	private ResultDAO resultDAO = new ResultDAO();
	
	public void addResult (String runnerupUsername, String WinnerUsername,String eventName ) {
		try {
			resultDAO.addAEventResult(WinnerUsername, runnerupUsername, eventName);
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	public List<ResultDTO> getAllResults() {
		List<ResultDTO> resultsList=null;
		try {
			resultsList = resultDAO.getResults();
			
			if(resultsList==null) {
				throw new Exception();
			}
		} catch (Exception e) {
			log.error("Can't get results",e);
		}
		
		return resultsList;
	}
	
}
