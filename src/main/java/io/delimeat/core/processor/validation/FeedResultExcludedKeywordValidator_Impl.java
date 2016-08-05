package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.ValidationException;
import io.delimeat.core.show.Show;
import io.delimeat.util.DelimeatUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedResultExcludedKeywordValidator_Impl implements FeedResultValidator {


    @Override
    public void validate(List<FeedResult> results, Show show, Config config)
            throws ValidationException {
      if(DelimeatUtils.isEmpty(config.getExcludedKeywords()) == true){
        return;
      }
      
		String regex = "(";
      Iterator<String> it = config.getExcludedKeywords().iterator();
		while(it.hasNext()){
        regex += it.next();
        if(it.hasNext()){
          regex += "|";
        }
      }
     	regex += ")";
      
      final Pattern pattern = Pattern.compile(regex.toLowerCase());
      Matcher matcher;
      for(final FeedResult result: results){
        	matcher = pattern.matcher(result.getTitle().toLowerCase());
        	if(matcher.find()){
           	result.getFeedResultRejections().add(FeedResultRejection.EXCLUDED_KEYWORD);
         }
      }

    }
}
