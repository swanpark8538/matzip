package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EvidenceFileRowMapper implements RowMapper<EvidenceFile>{

	@Override
	public EvidenceFile mapRow(ResultSet rs, int rowNum) throws SQLException {
		EvidenceFile evidenceFile = new EvidenceFile();
		evidenceFile.setFilename(rs.getString("filename"));
		evidenceFile.setFilepath(rs.getString("filepath"));
		evidenceFile.setFileNo(rs.getInt("file_no"));
		evidenceFile.setStoreNo(rs.getInt("store_no"));
		return evidenceFile;
	}

}
