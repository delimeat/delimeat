package io.delimeat.core.processor.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;
import io.delimeat.util.DelimeatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedResultDailyValidator_Impl implements FeedResultValidator {

	private static final String YEAR_REGEX = "\\d{4}(?=[\\s\\.]\\d{2}[\\s\\.]\\d{2})";
	private static final String MONTH_REGEX = "(?<=\\d{4}[\\s\\.])\\d{2}(?=[\\s\\.]\\d{2})";
	private static final String DAY_REGEX = "(?<=\\d{4}[\\s\\.]\\d{2}[\\s\\.])\\d{2}";
  
  	private String dateFormat = "yyyy-MM-dd";
	
  	public void setDateFormat(String dateFormat){
     	this.dateFormat = dateFormat;
   }
  
  	public String getDateFormat(){
     	return dateFormat;
   }
  
	@Override
	public void validate(List<FeedResult> results, Show show) throws ValidationException {
		final Date airDate;
		if(show.getNextEpisode()!=null){
			airDate = show.getNextEpisode().getAirDate();
		}else{
			airDate = null;
		}
		final Pattern yearSelectPattern = Pattern.compile(YEAR_REGEX);
		final Pattern monthSelectPattern = Pattern.compile(MONTH_REGEX);
		final Pattern daySelectPattern = Pattern.compile(DAY_REGEX);
     	final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Matcher yearMatcher;
		Matcher monthMatcher;
		Matcher dayMatcher;
		String title;
		for(FeedResult result: results){
			title = result.getTitle();
			if( DelimeatUtils.isNotEmpty(title) && airDate != null ){
				yearMatcher = yearSelectPattern.matcher(title);
				monthMatcher = monthSelectPattern.matcher(title);
				dayMatcher = daySelectPattern.matcher(title);
				if(yearMatcher.find() && monthMatcher.find() &&  dayMatcher.find()){
					String year = yearMatcher.group();
					String month = monthMatcher.group();
					String day = dayMatcher.group();
					try{
						Date resultAirDate = sdf.parse(year+"-"+month+"-"+day);
						if(airDate.getTime() == resultAirDate.getTime()){
							continue;
						}
					}catch(ParseException ex){
						//do nothing
					}
				}
			}
			result.getFeedResultRejections().add(FeedResultRejection.INVALID_DAILY_RESULT);
		}

	}

}
