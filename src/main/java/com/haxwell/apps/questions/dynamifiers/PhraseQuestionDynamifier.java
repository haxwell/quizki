package com.haxwell.apps.questions.dynamifiers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class PhraseQuestionDynamifier extends AbstractDynamifier {

	public void dynamify(AbstractEntity ae, HttpServletRequest request) {
		if (ae instanceof Question) {
			Question q = (Question)ae;
			
			HttpSession session = request.getSession();
			
			String dynamification = (String)session.getAttribute(q.getId() + "_QuestionTypePhraseDynamification");
			
			if (dynamification == null) {
				
				String text = q.getText();
				
				int numfields = StringUtil.getCountOfDynamicFields(text);
				
				dynamification = (RandomIntegerUtil.getRandomInteger(numfields)+1) + "";				
				
				session.setAttribute(q.getId() + "_QuestionTypePhraseDynamification", dynamification);				
			}
			
			q.setDynamicData("dynamicFieldToBeBlankedOut", dynamification);
		}		
	}
}
