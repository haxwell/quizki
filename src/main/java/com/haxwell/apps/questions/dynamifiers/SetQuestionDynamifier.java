package com.haxwell.apps.questions.dynamifiers;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;

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
				csv = c.getId() + "";
				
				session.setAttribute(q.getId() + "_QuestionTypeSetDynamification", csv);				
			}
			
			q.setDynamicData("choiceIdsToBeAnswered", csv);
		}		
	}
}
