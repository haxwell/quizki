package com.haxwell.apps.questions.dynamifiers;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Sets the dynamic data on the question and the session.
 * 
 * @author jjames
 */
public class SetQuestionDynamifier extends AbstractDynamifier {

	public void dynamify(AbstractEntity ae, HttpServletRequest request) {
		if (ae instanceof Question) {
			Question q = (Question)ae;
			
			HttpSession session = request.getSession();
			
			String csv = (String)session.getAttribute(q.getId() + "_QuestionTypeSetDynamification");
			
			if (csv == null) {
				
				Set<Choice> set = q.getChoices();
				
				int randomIndex = RandomIntegerUtil.getRandomInteger(set.size());

				Iterator<Choice> iterator = set.iterator();
				
				Choice c = null;
				while (randomIndex-- >= 0 && iterator.hasNext()) c = iterator.next();
				
				// For now, we're only doing one value, but I can see in the future that there will be multiple values..
				int fieldNum = getDynamifiedTextFieldNumber(c);
				
				csv = c.getId() + ";" + fieldNum;
				
				session.setAttribute(q.getId() + "_QuestionTypeSetDynamification", csv);				
			}
			
			q.setDynamicData("choiceIdsToBeAnswered", csv);
		}		
	}

	private int getDynamifiedTextFieldNumber(Choice c) {
		int rtn = -1;
		String text = c.getText();
		
		int occurrances = StringUtil.getNumberOfOccurrances("[[", text);
		
		if (occurrances > 0 && StringUtil.getNumberOfOccurrances("]]", text) == occurrances) {
			if (occurrances ==  1) {
				rtn = 1;
			} else {
				rtn = RandomIntegerUtil.getRandomInteger(occurrances) + 1;
			}
		}
		
		return rtn;
	}
}
