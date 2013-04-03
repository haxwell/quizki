package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.Constants;

/**
 * Encapsulates the information necessary to pull a subset of data from a larger set.
 * 
 * An instance of this class should be present for each set of data that may need to be paginated.
 *  At present, thats just Exams and Questions.
 * 
 * @author johnathanj
 */
public class PaginationData {

	public final int FIRST_PAGE = 0;

	int beginIndex = -1;
	int endIndex = -1;
	int pageSize = Constants.DEFAULT_PAGINATION_PAGE_SIZE;
	int pageNumber = this.FIRST_PAGE;
	long totalItemCount = -1;
	
	public void initialize() {
		beginIndex = -1;
		endIndex = -1;
		pageNumber = this.FIRST_PAGE;
		totalItemCount = -1;
		
		// purposely do not set pageSize because we want to keep that value as it was.
	}

	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public long getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(long totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
}
