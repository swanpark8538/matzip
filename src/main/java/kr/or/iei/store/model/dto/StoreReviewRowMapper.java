package kr.or.iei.store.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreReviewRowMapper implements RowMapper<StoreReview> {

	@Override
	public StoreReview mapRow(ResultSet rs, int rowNum) throws SQLException {
		StoreReview review = new StoreReview();
		review.setReviewNo(rs.getInt("review_no"));
		review.setStoreNo(rs.getInt("store_no"));
		review.setReviewWriter(rs.getString("review_writer"));
		review.setReviewPhoto(rs.getString("review_photo"));
		review.setReviewStar(rs.getInt("review_star"));
		review.setReviewContent(rs.getString("review_content"));
		review.setRegDate(rs.getString("reg_date"));
		return review;
	}

}
